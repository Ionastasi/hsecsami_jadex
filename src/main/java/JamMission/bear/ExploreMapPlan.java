package jammission.bear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;

import jammission.bear.BearBDI.AchieveMoveTo;
import jammission.world.MapPoint;

import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;



/**
 *  Plan to explore the map by going to the seldom visited positions.
 *  Uses the absolute quantity to go to positions that are not yet
 *  explored.
 */
@Plan
public class ExploreMapPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	/** Random number generator. */
	protected Random	rnd	= new Random();

	public ExploreMapPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		final Future<Void> ret = new Future<Void>();

		List<MapPoint> mps = getMinQuantity();
		MapPoint mp = (MapPoint)mps.get(0);
		int cnt	= 1;
		for( ; cnt<mps.size(); cnt++) {
			MapPoint mp2 = (MapPoint)mps.get(cnt);
			if(mp.getSeen()!=mp2.getSeen())
				break;
		}
		mp	= (MapPoint)mps.get(rnd.nextInt(cnt));
		IFuture<AchieveMoveTo> fut = rplan.dispatchSubgoal(capa.new AchieveMoveTo(mp.getLocation()));
		fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.AchieveMoveTo, Void>(ret) {
			public void customResultAvailable(AchieveMoveTo amt) {
				ret.setResult(null);
			}
		});

		return ret;
	}

	protected List<MapPoint> getMinQuantity() {
		Set<MapPoint> locs = capa.getVisitedPositions();
		List<MapPoint> ret = new ArrayList<MapPoint>(locs);
		Collections.sort(ret, new Comparator<MapPoint>() {
			public int compare(MapPoint o1, MapPoint o2) {
				return o1.getQuantity()-o2.getQuantity();
			}
		});
		return ret;
	}
}
