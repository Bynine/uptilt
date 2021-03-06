package movelists;

import entities.Fighter;
import entities.Projectile;

public class M_HyperSpeedy extends M_Speedy {

	public M_HyperSpeedy(Fighter user) {
		super(user);
		slideSpeed = 14;
		slideDur = 40;
		sChargeSpeed = 12;
	}
	
	protected Class<? extends Projectile> getAcidSpit(){
		return Projectile.HyperAcidSpit.class;
	}

}
