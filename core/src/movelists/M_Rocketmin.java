package movelists;

import entities.Fighter;
import entities.Projectile;

public class M_Rocketmin extends M_Gunmin {

	public M_Rocketmin(Fighter user) {
		super(user);
	}
	
	protected Class<? extends Projectile> getChargeLaserClass(){
		return Projectile.SuperLaser.class;
	}

}
