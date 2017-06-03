package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammission.bear.BearBDI.AchieveMoveTo;
import jammission.bear.BearBDI.AchieveTakeRaspberry;
import jammission.bear.BearBDI.TakeRaspberryAction;
import jammission.world.Bush;

import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;


@Plan
public class TakeRaspberryPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected AchieveTakeRaspberry goal;

	public TakeRaspberryPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		final Future<Void> ret = new Future<Void>();
		final Bush bush = goal.getBush();

		IFuture<AchieveMoveTo> fut = rplan.dispatchSubgoal(capa.new AchieveMoveTo(bush.getLocation()));
		fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.AchieveMoveTo, Void>(ret) {
			public void customResultAvailable(AchieveMoveTo amt) {
				IFuture<PickupWasteAction> fut = rplan.dispatchSubgoal(capa.new TakeRaspberryAction(bush));
				fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.TakeRaspberryAction, Void>(ret) {
					public void customResultAvailable(TakeRaspberryAction pwa) {
						capa.getBush().remove(bush);  // ?
						ret.setResult(null);
					}
				});
			}
		});

		return ret;
	}
}
