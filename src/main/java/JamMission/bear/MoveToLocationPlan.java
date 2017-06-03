package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammission.bear.BearBDI.AchieveMoveTo;
import jammission.world.Location;

import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;


@Plan
public class MoveToLocationPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected AchieveMoveTo goal;

	public MoveToLocationPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		return moveToTarget();
	}

	@PlanBody
	protected IFuture<Void> moveToTarget() {
		final Future<Void> ret = new Future<Void>();

		Location target = goal.getLocation();
		Location myloc = capa.getMyLocation();

		if(!myloc.isNear(target)) {
			oneStepToTarget().addResultListener(new DelegationResultListener<Void>(ret) {
				public void customResultAvailable(Void result) {
					moveToTarget().addResultListener(new DelegationResultListener<Void>(ret));
				}
			});
		} else {
			ret.setResult(null);
		}

		return ret;
	}

	protected IFuture<Void> oneStepToTarget() {
		final Future<Void> ret = new Future<Void>();

		Location target = goal.getLocation();
		Location myloc = capa.getMyLocation();

		double speed = capa.getMySpeed();
		double d = myloc.getDistance(target);
		double r = speed*0.00004*100;//(newtime-time);
		double dx = target.getX()-myloc.getX();
		double dy = target.getY()-myloc.getY();

		double rx = r<d? r*dx/d: dx;
		double ry = r<d? r*dy/d: dy;
		capa.setMyLocation(new Location(myloc.getX()+rx, myloc.getY()+ry));

		// wait for 0.01 seconds
		rplan.waitFor(100).addResultListener(new DelegationResultListener<Void>(ret) {});
		return ret;
	}
