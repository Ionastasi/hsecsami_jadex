package jammission.bear;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;

import jammission.world.Bear;
import jammission.world.IEnvironment;
import jammission.world.Vision;

import jadex.bdiv3.runtime.IPlan;
import jadex.bdiv3.runtime.impl.RProcessableElement;


@Plan
public class LocalGetVisionActionPlan {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	@PlanBody
	public void body() {
		IEnvironment environment = capa.getEnvironment();
		Bear cl = new Bear(capa.getMyLocation(),
			               capa.getAgent().getComponentIdentifier().getLocalName(),
			               capa.getMyBasket(),
			               capa.getBasketMaxVolume(),
			               capa.getBushes(), //HashSet<Bush> вместо Bush[]
			               capa.getMyVision());

		Vision vision = (Vision)environment.getVision(cl).clone();

		BearBDI.GetVisionAction gva = (BearBDI.GetVisionAction)((RProcessableElement)rplan.getReason()).getPojoElement();
		gva.setVision(vision);
	}
}
