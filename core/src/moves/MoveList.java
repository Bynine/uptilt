package moves;

import entities.Fighter;

public abstract class MoveList {

	/* grounded normals */
	public abstract Move nWeak(Fighter user);
	public abstract Move uWeak(Fighter user);
	public abstract Move sWeak(Fighter user);
	public abstract Move dWeak(Fighter user);
	public abstract Move slide(Fighter user);
	// upper and lower dash attacks?
	
	/* charge attacks */
	public abstract Move sCharge(Fighter user);
	public Move fCharge(Fighter user){
		Move m = sCharge(user);
		if (user.direct() != 1) user.flip();
		return m;
	}
	public Move bCharge(Fighter user){
		Move m = sCharge(user);
		if (user.direct() != -1) user.flip();
		return m;
	}
	public abstract Move uCharge(Fighter user);
	public abstract Move dCharge(Fighter user);
	
	/* aerial normals */
	public abstract Move nAir(Fighter user);
	public abstract Move fAir(Fighter user);
	public abstract Move bAir(Fighter user);
	public abstract Move uAir(Fighter user);
	public abstract Move dAir(Fighter user);
	
	/* specials */
	public abstract Move nSpecial(Fighter user);
	public abstract Move sSpecial(Fighter user);
	public abstract Move uSpecial(Fighter user);
	public abstract Move dSpecial(Fighter user);
	
	// throws
	public abstract Move fThrow(Fighter user);
	public abstract Move bThrow(Fighter user);
	public abstract Move uThrow(Fighter user);
	public abstract Move dThrow(Fighter user);
	public abstract Move fAirThrow(Fighter user);
	public abstract Move bAirThrow(Fighter user);
	public abstract Move uAirThrow(Fighter user);
	public abstract Move dAirThrow(Fighter user);
	
	// grabs
	public abstract Move grab(Fighter user);
	public abstract Move dashGrab(Fighter user);
	public abstract Move airGrab(Fighter user);
	
}
