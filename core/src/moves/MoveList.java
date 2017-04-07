package moves;

import entities.Fighter;

public abstract class MoveList {

	/* grounded normals */
	public abstract Move nWeak(Fighter user);
	public abstract Move uWeak(Fighter user);
	public abstract Move sWeak(Fighter user);
	public abstract Move dWeak(Fighter user);
	public abstract Move slide(Fighter user);

	/* charge attacks */
	public abstract Move sCharge(Fighter user);
	public Move fCharge(Fighter user){
		return setSideCharge(user, 1);
	}
	public Move bCharge(Fighter user){
		return setSideCharge(user, -1);
	}
	private Move setSideCharge(Fighter user, int dir){
		Move m = sCharge(user);
		if (user.isGrounded() && user.direct() != dir) user.flip();
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

	/* throws */
	public abstract Move fThrow(Fighter user);
	public abstract Move bThrow(Fighter user);
	public abstract Move uThrow(Fighter user);
	public abstract Move dThrow(Fighter user);
	public abstract Move fAirThrow(Fighter user);
	public abstract Move bAirThrow(Fighter user);
	public abstract Move uAirThrow(Fighter user);
	public abstract Move dAirThrow(Fighter user);

	/* grabs */
	public abstract Move grab(Fighter user);
	public abstract Move dashGrab(Fighter user);
	public abstract Move airGrab(Fighter user);
	public Move airBackGrab(Fighter user){
		user.flip();
		return airGrab(user);
	}

	/* misc */
	public abstract Move land(Fighter user);
	public abstract Move skid(Fighter user);
	private final float boost = 7.2f;
	public Move boost(Fighter user){
		Move m = new Move(user, 10);
		m.setHelpless();
		m.dontTurn();
		m.eventList.addConstantVelocity(user, 0, 7, user.direct() * user.getStickX() * boost, -user.getStickY() * boost);
		m.eventList.addVelocityChange(user, 8, 0, 0);
		return m;
	}

	/* Move Selection */

	public Move selectNormalMove(Fighter user){
		if (user.isGrounded()) {
			if (user.isDashing()) return slide(user);
			else if (user.holdUp()) return uWeak(user);
			else if (user.holdDown()) return dWeak(user);
			else if (user.holdForward()) return sWeak(user);
			else return nWeak(user);
		}
		else return selectAerial(user);
	}

	public Move selectSpecialMove(Fighter user){
		if (user.holdUp()) return uSpecial(user);
		else if (user.holdDown()) return dSpecial(user);
		else if (user.holdForward()) return sSpecial(user);
		else if (user.holdBack()) {
			user.flip();
			return sSpecial(user);
		}
		else return nSpecial(user);
	}

	public Move selectThrow(Fighter user){
		if (user.isGrounded()){
			if (user.holdUp()) return uThrow(user);
			else if (user.holdDown()) return dThrow(user);
			else if (user.holdForward()) return fThrow(user);
			else if (user.holdBack()) return bThrow(user);
		}
		else{
			if (user.holdUp()) return uAirThrow(user);
			else if (user.holdDown()) return dAirThrow(user);
			else if (user.holdForward()) return fAirThrow(user);
			else if (user.holdBack()) return bAirThrow(user);
		}
		return null;
	}

	public Move selectGrab(Fighter user){
		if (!user.isGrounded()){
			if (user.holdBack()) return airBackGrab(user);
			else return airGrab(user);
		}
		else if (user.isDashing()) return dashGrab(user);
		else return grab(user);
	}

	public Move selectCharge(Fighter user) {
		if (user.isGrounded()){
			if (user.holdUp()) return uCharge(user);
			else if (user.holdDown()) return dCharge(user);
			else if (user.holdBack()) {
				user.flip();
				return sCharge(user);
			}
			else return sCharge(user);
		}
		else return selectAerial(user);
	}

	private Move selectAerial(Fighter user){
		if (user.holdUp()) return uAir(user);
		else if (user.holdDown()) return dAir(user);
		else if (user.holdForward()) return fAir(user);
		else if (user.holdBack()) return bAir(user);
		else return nAir(user);
	}

}
