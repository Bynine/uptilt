package moves;

import entities.Fighter;

public abstract class MoveList {
	
	final Fighter user;
	
	MoveList(Fighter user){
		this.user = user;
	}

	/* grounded normals */
	public abstract Move nWeak();
	public abstract Move uWeak();
	public abstract Move sWeak();
	public abstract Move dWeak();
	public abstract Move slide();

	/* charge attacks */
	public abstract Move sCharge();
	public Move fCharge(){
		return setSideCharge(1);
	}
	public Move bCharge(){
		return setSideCharge(-1);
	}
	private Move setSideCharge(int dir){
		Move m = sCharge();
		if (user.isGrounded() && user.direct() != dir) user.flip();
		return m;
	}
	public abstract Move uCharge();
	public abstract Move dCharge();

	/* aerial normals */
	public abstract Move nAir();
	public abstract Move fAir();
	public abstract Move bAir();
	public abstract Move uAir();
	public abstract Move dAir();

	/* specials */
	public abstract Move nSpecial();
	public abstract Move sSpecial();
	public abstract Move uSpecial();
	public abstract Move dSpecial();

	/* throws */
	public abstract Move fThrow();
	public abstract Move bThrow();
	public abstract Move uThrow();
	public abstract Move dThrow();
	public abstract Move fAirThrow();
	public abstract Move bAirThrow();
	public abstract Move uAirThrow();
	public abstract Move dAirThrow();

	/* grabs */
	public abstract Move grab();
	public abstract Move dashGrab();
	public abstract Move airGrab();
	public Move airBackGrab(){
		user.flip();
		return airGrab();
	}
	
	/* guard */
	public abstract Move rollForward();
	public abstract Move rollBack();
	public abstract Move airDodge();

	/* misc */
	public abstract Move land();
	public abstract Move skid();
	public abstract Move taunt();

	/* Move Selection */

	public Move selectNormalMove(){
		if (user.isGrounded()) {
			if (user.isDashing()) return slide();
			else if (user.holdUp()) return uWeak();
			else if (user.holdDown()) return dWeak();
			else if (user.holdForward()) return sWeak();
			else return nWeak();
		}
		else return selectAerial();
	}

	public Move selectSpecialMove(){
		if (user.holdUp()) return uSpecial();
		else if (user.holdDown()) return dSpecial();
		else if (user.holdForward()) return sSpecial();
		else if (user.holdBack()) {
			user.flip();
			return sSpecial();
		}
		else return nSpecial();
	}

	public Move selectThrow(){
		if (user.isGrounded()){
			if (user.holdUp()) return uThrow();
			else if (user.holdDown()) return dThrow();
			else if (user.holdForward()) return fThrow();
			else if (user.holdBack()) return bThrow();
		}
		else{
			if (user.holdUp()) return uAirThrow();
			else if (user.holdDown()) return dAirThrow();
			else if (user.holdForward()) return fAirThrow();
			else if (user.holdBack()) return bAirThrow();
		}
		return null;
	}

	public Move selectGrab(){
		if (!user.isGrounded()){
			if (user.holdBack()) return airBackGrab();
			else return airGrab();
		}
		else if (user.isDashing()) return dashGrab();
		else return grab();
	}

	public Move selectCharge() {
		if (user.isGrounded()){
			if (user.holdUp()) return uCharge();
			else if (user.holdDown()) return dCharge();
			else if (user.holdBack()) {
				user.flip();
				return sCharge();
			}
			else return sCharge();
		}
		else return selectAerial();
	}
	
	public Move selectBlock() {
		if (user.isGrounded() && !user.isDashing()){
			if (user.holdForward()) return rollForward();
			else if (user.holdBack()) return rollBack();
		}
		else if (!user.isGrounded()) return airDodge();
		return null;
	}
	
	public Move selectTaunt() {
		return taunt();
	}

	private Move selectAerial(){
		if (user.holdUp()) return uAir();
		else if (user.holdDown()) return dAir();
		else if (user.holdForward()) return fAir();
		else if (user.holdBack()) return bAir();
		else return nAir();
	}

}
