package burlap.domain.singleagent.frostbite;

import burlap.domain.singleagent.frostbite.state.FrostbitePlatform;
import burlap.domain.singleagent.frostbite.state.FrostbiteState;
import burlap.oomdp.core.oo.OODomain;
import burlap.oomdp.core.oo.propositional.PropositionalFunction;
import burlap.oomdp.core.oo.state.OOState;
import burlap.oomdp.core.state.State;
import burlap.oomdp.singleagent.GroundedAction;
import burlap.oomdp.singleagent.RewardFunction;

import java.util.List;

/**
 * @author Phillipe Morere
 */
public class FrostbiteRF implements RewardFunction{

	public double goalReward = 1000.0;
	public double lostReward = -1000.0;
	public double activatedPlatformReward = 10.0;
	public double defaultReward = -1.0;
	private PropositionalFunction onIce;
	private PropositionalFunction inWater;
	private PropositionalFunction iglooBuilt;

	public FrostbiteRF(OODomain domain) {
		this.inWater = domain.getPropFunction(FrostbiteDomain.PF_IN_WATER);
		this.onIce = domain.getPropFunction(FrostbiteDomain.PF_ON_ICE);
		this.iglooBuilt = domain.getPropFunction(FrostbiteDomain.PF_IGLOO_BUILT);
	}

	@Override
	public double reward(State s, GroundedAction a, State sprime) {
		if (inWater.somePFGroundingIsTrue((OOState)sprime))
			return lostReward;
		if (iglooBuilt.somePFGroundingIsTrue((OOState)sprime) && onIce.somePFGroundingIsTrue((OOState)s))
			return goalReward;
		if (numberPlatformsActive((FrostbiteState)s) != numberPlatformsActive((FrostbiteState)sprime))
			return activatedPlatformReward;
		return defaultReward;
	}

	private int numberPlatformsActive(FrostbiteState s) {
		List<FrostbitePlatform> platforms = s.platforms;
		int nb = 0;
		for (FrostbitePlatform platform : platforms)
			if (platform.activated)
				nb++;
		return nb;
	}

}
