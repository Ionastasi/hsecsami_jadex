package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammission.bear.BearBDI.AchieveGoToBush;
import jammission.bear.BearBDI.AchieveMoveTo;
import jammission.world.Location;
import jjammission.world.Bush;

import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;


@Plan
public class DropBushPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected AchieveGoToBush goal;

	public DropBushPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		final Future<Void> ret = new Future<Void>();

		final Bush bush = goal.getBush();
		Location location = bush.getLocation();

		IFuture<AchieveMoveTo> fut = rplan.dispatchSubgoal(capa.new AchieveMoveTo(location));
		fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.AchieveMoveTo, Void>(ret) {
			public void customResultAvailable(AchieveMoveTo amt) {
				ret.setResult(null);
			}
		});

		return ret;
	}
}
