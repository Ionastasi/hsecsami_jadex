package jammission.bear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Body;
import jadex.bdiv3.annotation.Deliberation;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalContextCondition;
import jadex.bdiv3.annotation.GoalCreationCondition;
import jadex.bdiv3.annotation.GoalDropCondition;
import jadex.bdiv3.annotation.GoalInhibit;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Plans;
import jadex.bdiv3.annotation.Trigger;

import jammission.world.Bear;
import jammission.world.Bush;
import jammission.world.Environment;
import jammission.world.IEnvironment;
import jammission.world.Location;
import jammission.world.LocationObject;
import jammission.world.MapPoint;
import jammission.world.Vision;

import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.model.MProcessableElement.ExcludeMode;
import jadex.bdiv3.runtime.IPlan;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.CheckNotNull;
import jadex.commons.SUtil;
import jadex.commons.Tuple2;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.Description;

@Agent
@Plans({
    @Plan(trigger=@Trigger(goals={BearBDI.MaintainGiveAwayRaspberry.class, BearBDI.PerformStayHome.class}), body=@Body(GoHomePlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.AchieveGoToBush.class), body=@Body(GoToBushPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.AchieveTakeRaspberry.class), body=@Body(TakeRaspberryPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.AchieveHarvest.class), body=@Body(HarvestRaspberryPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.AchieveMoveTo.class), body=@Body(MoveToLocationPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.PerformRandomWalk.class), body=@Body(RandomWalkPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.PerformMemorizeBushes.class), body=@Body(MemorizeBushesPlan.class))
    @Plan(trigger=@Trigger(goals=BearBDI.PerformMemorizePositions.class), body=@Body(MemorizePositionsPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.QueryBush.class), body=@Body(ExploreMapPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.GetVisionAction.class), priority=1, body=@Body(LocalGetVisionActionPlan.class))
    @Plan(trigger=@Trigger(goals=BearBDI.TakeRaspberryAction.class), priority=1, body=@Body(LocalTakeRaspberrryActionPlan.class)),
    @Plan(trigger=@Trigger(goals=BearBDI.GiveAwayRaspberryAction.class), priority=1, body=@Body(LocalGiveAwayRaspberryActionPlan.class)),
})
@Description("<h1>Bear Agent</h1>")
public class BearBDI {
    @Agent
    protected IInternalAccess agent;

    //----------Beliefs----------

    @Belief
    protected IEnvironment environment = Environment.getInstance();

    @Belief
    protected Lair homeLair = new Lair(); // ???

    @Belief
    protected Set<Bush> bushes = new HashSet<Bush>();

    /** The raster for memorizing positions. */
    @Belief
    protected Tuple2<Integer, Integer> raster = new Tuple2<Integer, Integer>(Integer.valueOf(10), Integer.valueOf(10));

    @Belief
    protected Set<MapPoint> visited_positions = new HashSet<MapPoint>(Arrays.asList(
        MapPoint.getMapPointRaster(raster.getFirstEntity().intValue(),
        raster.getSecondEntity().intValue(), 1, 1)));

    @Belief
    protected boolean daytime;

    @Belief
    protected Location my_location = new Location(0.2, 0.2);

    @Belief
    protected double my_speed = 3;

    @Belief
    protected double my_vision = 0.1;

    @Belief
    protected int my_basket = 0;

    protected int basketMaxVolume = 10;


    //----------Goals----------

    @Goal(deliberation=@Deliberation(inhibits={PerformLookForWaste.class, AchieveCleanup.class, PerformPatrol.class}))
    public class MaintainGiveAwayRaspberry {
        @GoalMaintainCondition//(beliefs="my_basket")
        public boolean checkMaintain() {
            return my_chargestate == basketMaxVolume;
        }

        @GoalTargetCondition//(beliefs="my_basket")
        public boolean checkTarget() {
            return my_chargestate == 0;
        }
    }


    @Goal(retry=false)
    public class AchieveGoToBush {
        protected Bush bush;

        public AchievePickupWaste(Bush bush) {
            this.bush = bush;
        }

        public Bush getBush() {
            return bush;
        }
    }

    @Goal//(excludemode=ExcludeMode.Never)
    public class AchieveTakeRaspberry {
        protected Bush bush;

        public AchieveDropWaste(Bush bush) {
            this.bush = bush;
        }

        public boolean checkContext() {
            return bush.isEmpty();
        }

        public Bush getBush() {
            return bush;
        }
    }

    @Goal(excludemode=ExcludeMode.Never, unique=true, deliberation=@Deliberation(inhibits={PerformLookForWaste.class, AchieveHarvest.class}))
    public class AchieveHarvest {
        protected Bush bush;

        @GoalCreationCondition(beliefs="bushes")
        public AchieveHarvest(@CheckNotNull Bush bush) {
            this.bush = bush;
        }

        @GoalContextCondition//(beliefs="daytime")
        public boolean checkContext() {
            return isDaytime();
        }

        @GoalDropCondition(beliefs={"bushes"})
        public boolean checkDrop() {
            return !getBushes().contains(bush) || my_basket >= basketMaxVolume;
        }

        /**
         *  Inhibit other achieve harvest goals that
         *  are farer away from the bear.
         */
        @GoalInhibit(AchieveHarvest.class)
        protected boolean inhibitAchieveHarvest(AchieveHarvest other) {
            if bush.equals(other.getBush()) {
                return false;  // smth strange happend, but ok
            }
            double d1 = getMyLocation().getDistance(bush.getLocation());
            double d2 = getMyLocation().getDistance(other.getBush().getLocation());
            return d1 <= d2;
        }

        public Waste getBush() {
            return bush;
        }

        // hashcode and equals implementation for unique flag

        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((bush == null) ? 0 : bush.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            boolean ret = false;
            if(obj instanceof AchieveHarvest) {
                AchieveHarvest other = (AchieveHarvest)obj;
                ret = getOuterType().equals(other.getOuterType()) && SUtil.equals(bush, other.getBush());
            }
            return ret;
        }

        private BearBDI getOuterType() {
            return BearBDI.this;
        }

        public String toString() {
            return "AchieveHarvest: "+getBush();
        }
    }

    @Goal
    public class AchieveMoveTo {
        protected Location location;

        public AchieveMoveTo(Location location) {
            this.location = location;
        }

        @GoalTargetCondition//(beliefs="my_location")
        public boolean checkTarget() {
            return my_location.isNear(location);
        }

        public Location getLocation() {
            return location;
        }
    }

    @Goal(excludemode=ExcludeMode.Never, orsuccess=false)
    public class PerformRandomWalk {
        @GoalContextCondition//(beliefs="daytime")
        public boolean checkContext() {
            return daytime;
        }
    }

    @Goal(excludemode=ExcludeMode.Never, orsuccess=false)
    public class PerformStayHome {
        @GoalContextCondition//(beliefs="daytime")
        public boolean checkContext() {
            return !daytime;
        }
    }

    @Goal(excludemode=ExcludeMode.Never, orsuccess=false)
    public class PerformMemorizeBushes {}

    @Goal(excludemode=ExcludeMode.Never, orsuccess=false, retrydelay=300)
    public class PerformMemorizePositions {}

    @Goal(excludemode=ExcludeMode.Never)
    public class QueryBush {
        protected Bush bush;

        @GoalTargetCondition(beliefs="bushes")
        public boolean checkTarget() {
            bush = getNearestNonEmptyBush();
            return bush != null;
        }

        protected Bush getNearestNonEmptyBush() {
            Bush ret = null;
            for(Bush b: bushes) {
                if(!b.isEmpty()) {
                    if(ret==null) {
                        ret = b;
                    } else if (getMyLocation().getDistance(b.getLocation())
                               < getMyLocation().getDistance(ret.getLocation())) {
                        ret = b;
                    }
                }
            }
            return ret;
        }

        public Bush getBush() {
            return bush;
        }
    }

    @Goal
    public class GetVisionAction {
        protected Vision vision;

        public void setVision(Vision vision) {
            this.vision = vision;
        }

        public Vision getVision() {
            return vision;
        }
    }

    @Goal
    public class TakeRaspberryAction {
        protected Bush bush;

        public TakeRaspberryAction(Bush bush) {
            this.bush = bush;
        }

        public Bush getBush() {
            return bush;
        }
    }

    @Goal
    public class GiveAwayRaspberryAction {}


    //----------Plans----------

    @Plan(trigger=@Trigger(factchangeds={"environment", "my_location"}))
    protected IFuture<Void> updateVision(IPlan rplan) {
        final Future<Void> ret = new Future<Void>();

        // Create a representation of myself.
        final Cleaner cl = new Cleaner(getMyLocation(),
            getAgent().getComponentIdentifier().getLocalName(),
            getCarriedWaste(), getMyVision(),
            getMyChargestate());

        IFuture<GetVisionAction> fut = rplan.dispatchSubgoal(new GetVisionAction());
        fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.GetVisionAction, Void>(ret)
        {
            public void customResultAvailable(GetVisionAction gva)
            {
                Vision vi = gva.getVision();

                if(vi!=null)
                {
                    setDaytime(vi.isDaytime());

                    Waste[] ws = vi.getWastes();
                    Wastebin[] wbs = vi.getWastebins();
                    Chargingstation[] cs = vi.getStations();
                    Cleaner[] cls = vi.getCleaners();

                    // When an object is not seen any longer (not
                    // in actualvision, but in (near) beliefs), remove it.
        //          List known = (List)getExpression("query_in_vision_objects").execute();
                    List<LocationObject> known = getInVisionObjects();

                    for(int i=0; i<known.size(); i++)
                    {
                        Object object = known.get(i);
                        if(object instanceof Waste)
                        {
                            List tmp = SUtil.arrayToList(ws);
                            if(!tmp.contains(object))
                                getWastes().remove(object);
                        }
                        else if(object instanceof Wastebin)
                        {
                            List tmp = SUtil.arrayToList(wbs);
                            if(!tmp.contains(object))
                                getWastebins().remove(object);
                        }
                        else if(object instanceof Chargingstation)
                        {
                            List tmp = SUtil.arrayToList(cs);
                            if(!tmp.contains(object))
                                getChargingStations().remove(object);
                        }
                        else if(object instanceof Cleaner)
                        {
                            List tmp = SUtil.arrayToList(cls);
                            if(!tmp.contains(object))
                                getCleaners().remove(object);
                        }
                    }

                    // Add new or changed objects to beliefs.
                    for(int i=0; i<ws.length; i++)
                    {
                        if(!getWastes().contains(ws[i]))
                            getWastes().add(ws[i]);
                    }
                    for(int i=0; i<wbs.length; i++)
                    {
                        // Remove contained wastes from knowledge.
                        // Otherwise the agent might think that the waste is still
                        // somewhere (outside its vision) and then it creates lots of
                        // cleanup goals, that are instantly achieved because the
                        // target condition (waste in wastebin) holds.
                        Waste[] wastes  = wbs[i].getWastes();
                        for(int j=0; j<wastes.length; j++)
                        {
                            if(getWastes().contains(wastes[j]))
                                getWastes().remove(wastes[j]);
                        }

                        // Now its safe to add wastebin to beliefs.
                        if(getWastebins().contains(wbs[i]))
                        {
                            getWastebins().remove(wbs[i]);
                            getWastebins().add(wbs[i]);
        //                  bs.updateFact(wbs[i]);
        //                  Wastebin wb = (Wastebin)bs.getFact(wbs[i]);
        //                  wb.update(wbs[i]);
                        }
                        else
                        {
                            getWastebins().add(wbs[i]);
                        }
                        //getBeliefbase().getBeliefSet("wastebins").updateOrAddFact(wbs[i]);
                    }
                    for(int i=0; i<cs.length; i++)
                    {
                        if(getChargingStations().contains(cs[i]))
                        {
        //                      bs.updateFact(cs[i]);
        //                  Chargingstation stat = (Chargingstation)bs.getFact(cs[i]);
        //                  stat.update(cs[i]);
                            getChargingStations().remove(cs[i]);
                            getChargingStations().add(cs[i]);
                        }
                        else
                        {
                            getChargingStations().add(cs[i]);
                        }
                        //getBeliefbase().getBeliefSet("chargingstations").updateOrAddFact(cs[i]);
                    }
                    for(int i=0; i<cls.length; i++)
                    {
                        if(!cls[i].equals(cl))
                        {
                            if(getCleaners().contains(cls[i]))
                            {
        //                          bs.updateFact(cls[i]);
        //                      Cleaner clea = (Cleaner)bs.getFact(cls[i]);
        //                      clea.update(cls[i]);
                                getCleaners().remove(cls[i]);
                                getCleaners().add(cls[i]);
                            }
                            else
                            {
                                getCleaners().add(cls[i]);
                            }
                            //getBeliefbase().getBeliefSet("cleaners").updateOrAddFact(cls[i]);
                        }
                    }

                    //getBeliefbase().getBelief("???").setFact("allowed_to_move", new Boolean(true));
                }
                else
                {
        //          System.out.println("Error when updating vision! "+event.getGoal());
                }
                ret.setResult(null);
            }
        });

        return ret;
    }


    //----------Methods----------

    protected List<LocationObject> getInVisionObjects() {
        List<LocationObject> ret = new ArrayList<LocationObject>();
        for(LocationObject o: getBushes()) {
            if(getMyLocation().isNear(o.getLocation(), getMyVision())) {
                ret.add(o);
            }
        }
        return ret;
    }

    /**
     *  The agent body.
     */
    @AgentBody
    public void body() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BearGui(agent.getExternalAccess());
            }
        });

        // ???
        agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new PerformRandomWalk());
        agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new MaintainGiveAwayRaspberry());
        agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new PerformMemorizePositions());
        agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new PerformMemorizeBushes());
    }

    public IEnvironment getEnvironment() {
        return environment;
    }

    public Set<Bush> getBushes() {
        return bushes;
    }

    public Lair getHomeLair() {
        return homeLair;
    }

    public Tuple2<Integer, Integer> getRaster() {
        return raster;
    }

    public Set<MapPoint> getVisitedPositions() {
        return visited_positions;
    }

    public boolean isDaytime() {
        return daytime;
    }

    public void setDaytime(boolean daytime) {
        this.daytime = daytime;
    }

    public Location getMyLocation() {
        return my_location;
    }

    public void setMyLocation(Location mylocation) {
        this.my_location = mylocation;
    }

    public double getMySpeed() {
        return my_speed;
    }

    public double getMyVision() {
        return my_vision;
    }

    public double getMyBasket() {
        return my_basket;
    }

    public void setMyBasket(int my_basket) {
        this.my_basket = my_basket;
    }

    public double getBasketMaxVolume() {
        return basketMaxVolume;
    }

    public void setBasketMaxVolume(int volume) {
        this.basketMaxVolume = volume;
    }

    public IInternalAccess getAgent() {
        return agent;
    }

    protected List<MapPoint> getMaxQuantity() {
        Set<MapPoint> locs = getVisitedPositions();
        List<MapPoint> ret = new ArrayList<MapPoint>(locs);
        Collections.sort(ret, new Comparator<MapPoint>() {
            public int compare(MapPoint o1, MapPoint o2) {
                return o2.getQuantity()-o1.getQuantity();
            }
        });
        return ret;
    }
}
