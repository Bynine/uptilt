package moves;

import entities.Fighter;
import entities.Projectile;

public class MoveList_Rocketmin extends MoveList_Gunmin {

	public MoveList_Rocketmin(Fighter user) {
		super(user);
	}
	
	protected Class<? extends Projectile> getChargeLaserClass(){
		return Projectile.Rocket.class;
	}

}
