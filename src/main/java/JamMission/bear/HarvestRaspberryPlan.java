package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammission.bear.BearBDI.AchieveHarvest;
import jammission.bear.BearBDI.AchieveGoToBush;
import jammission.bear.BearBDI.AchieveTakeRaspberry;
import jammission.world.Bush;

import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.DelegationResultListener;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;


@Plan
public class HarvestRaspberryPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected AchieveHarvest goal;

	public HarvestRaspberryPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		final Future<Void> ret = new Future<Void>();

		Bush bush = goal.getBush();

		if (!capa.getMyLocation.isNear(bush.getLocation())) { // ?
			IFuture<AchieveGoToBush> fut = rplan.dispatchSubgoal(capa.new AchieveGoToBush(bush));
			fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.AchieveGoToBush, Void>(ret) {
				public void customResultAvailable(AchievGoToBush apw) {
					takeRasp().addResultListener(new DelegationResultListener<Void>(ret));
				}
			});
		} else {
			takeRasp().addResultListener(new DelegationResultListener<Void>(ret));
		}

		return ret;
	}

	protected IFuture<Void> takeRasp() {
		final Future<Void> ret = new Future<Void>();
		Bush bush = goal.getBush();

		IFuture<AchieveDropWaste> fut = rplan.dispatchSubgoal(capa.new AchieveTakeRaspberry(bush);
		fut.addResultListener(new IResultListener<BearBDI.AchieveTakeRaspberry>() {
			public void resultAvailable(AchieveTakeRaspberry result) {
				ret.setResult(null);
			}
			public void exceptionOccurred(Exception exception) {
				ret.setException(exception);
			}
		});

		return ret;
	}
}
