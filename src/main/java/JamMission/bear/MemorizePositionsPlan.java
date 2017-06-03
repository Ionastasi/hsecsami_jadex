package jammission.bear;

import java.util.Set;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanAPI;
import jadex.bdiv3.annotation.PlanBody;
import jadex.bdiv3.annotation.PlanCapability;

import jammission.world.Location;
import jammission.world.MapPoint;

import jadex.bdiv3.runtime.IPlan;

@Plan
public class MemorizePositionsPlan  {
	@PlanCapability
	protected BearBDI capa;

	@PlanAPI
	protected IPlan rplan;

	/** The forget factor. */
	protected double forget;

	public MemorizePositionsPlan() {
		this.forget = 0.01;
	}

	@PlanBody
	public void body() {
		Location my_location = capa.getMyLocation();
		double	my_vision	= capa.getMyVision();
		Set<MapPoint> mps = capa.getVisitedPositions();
		for(MapPoint mp: mps) {
			if(my_location.isNear(mp.getLocation(), my_vision)) {
				mp.setQuantity(mp.getQuantity()+1);
				mp.setSeen(1);
			} else {
				double oldseen = mp.getSeen();
				double newseen = oldseen - oldseen*forget;
				mp.setSeen(newseen);
			}
		}
	}
}
