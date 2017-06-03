package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammission.bear.BearBDI.TakeRaspberryAction;
import jammission.world.IEnvironment;
import jammission.world.Waste;

import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;

@Plan
public class LocalTakeRaspberryActionPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected TakeRaspberryAction goal;

	@PlanBody
	public IFuture<Void> body() {
		IEnvironment environment = capa.getEnvironment();
		Bush bush = goal.getBush();
		int rasp = environment.harvestRaspberry(bush, capa.getMyBasket(), capa.getBasketMaxVolume());
		if(rasp == -1)
			return new Future<Void>(new PlanFailureException());
		else {
			capa.setMyBasket(rasp);
			return IFuture.DONE;
		}
	}
}
