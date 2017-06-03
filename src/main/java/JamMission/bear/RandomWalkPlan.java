package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;

import jammission.bear.BearBDI.AchieveMoveTo;
import jammission.world.Location;

import jadex.bdiv3.runtime.IPlan;
import jadex.commons.future.ExceptionDelegationResultListener;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;


@Plan
public class RandomWalkPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	public RandomWalkPlan() {}

	@PlanBody
	public IFuture<Void> body() {
		final Future<Void> ret = new Future<Void>();

		double x_dest = Math.random();
		double y_dest = Math.random();
		Location dest = new Location(x_dest, y_dest);

		IFuture<AchieveMoveTo> fut = rplan.dispatchSubgoal(capa.new AchieveMoveTo(dest));
		fut.addResultListener(new ExceptionDelegationResultListener<BearBDI.AchieveMoveTo, Void>(ret) {
			public void customResultAvailable(AchieveMoveTo amt) {
				ret.setResult(null);
			}
		});

		return ret;
	}
}
