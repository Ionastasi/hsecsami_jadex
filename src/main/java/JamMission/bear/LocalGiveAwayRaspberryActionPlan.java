package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;
import jadex.bdiv3.annotation.PlanReason;

import jammision.bear.BearBDI.GiveAwayRaspberryAction;
import jammission.world.IEnvironment;

import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;

@Plan
public class LocalGiveAwayRaspberryActionPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanReason
	protected GiveAwayRaspberryAction goal;

	@PlanBody
	public IFuture<Void> body() {
		IEnvironment environment = capa.getEnvironment();

		boolean	success	= environment.giveAwayRaspberry(capa.getMyBasket());
		if(!success)
			return new Future<Void>(new PlanFailureException());
		else {
			capa.setMyBasket(0);
			return IFuture.DONE;
		}
	}
}
