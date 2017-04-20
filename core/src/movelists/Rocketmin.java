package movelists;

import entities.Fighter;
import entities.Projectile;

public class Rocketmin extends Gunmin {

	public Rocketmin(Fighter user) {
		super(user);
	}
	
	protected Class<? extends Projectile> getChargeLaserClass(){
		return Projectile.Rocket.class;
	}

}
