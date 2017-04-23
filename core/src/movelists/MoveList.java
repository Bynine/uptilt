package movelists;

import moves.IDMove;
import moves.Move;
import entities.Fighter;

public abstract class MoveList {
	
	public static final int noStaleMove = -1;
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
	public abstract Move getUpAttack();
	public abstract Move dodge();

	/* misc */
	public abstract Move land();
	public abstract Move skid();
	public abstract Move taunt();

	/* Move Selection */

	public IDMove selectNormalMove(){
		if (user.isGrounded()) {
			if (user.isRunning()) return new IDMove(slide(), 0);
			else if (user.holdUp()) return new IDMove(uWeak(), 1);
			else if (user.holdDown()) return new IDMove(dWeak(), 2);
			else if (user.holdForward()) return new IDMove(sWeak(), 3);
			else return new IDMove(nWeak(), 4);
		}
		else return selectAerial();
	}

	public IDMove selectSpecialMove(){
		if (user.holdUp()) return new IDMove(uSpecial(), 10);
		else if (user.holdDown()) return new IDMove(dSpecial(), 11);
		else if (user.holdForward()) return new IDMove(sSpecial(), 12);
		else if (user.holdBack()) {
			user.flip();
			return new IDMove(sSpecial(), 12);
		}
		else return new IDMove(nSpecial(), 13);
	}

	public IDMove selectThrow(){
		if (user.isGrounded()){
			if (user.holdUp()) return new IDMove(uThrow(), 20);
			else if (user.holdDown()) return new IDMove(dThrow(), 21);
			else if (user.holdForward()) return new IDMove(fThrow(), 22);
			else if (user.holdBack()) return new IDMove(bThrow(), 23);
		}
		else{
			if (user.holdUp()) return new IDMove(uAirThrow(), 24);
			else if (user.holdDown()) return new IDMove(dAirThrow(), 25);
			else if (user.holdForward()) return new IDMove(fAirThrow(), 26);
			else if (user.holdBack()) return new IDMove(bAirThrow(), 27);
		}
		return null;
	}

	public IDMove selectGrab(){
		if (!user.isGrounded()){
			if (user.holdBack()) return new IDMove(airBackGrab(), 30);
			else return new IDMove(airGrab(), 31);
		}
		else if (user.isRunning()) return new IDMove(dashGrab(), 32);
		return new IDMove(grab(), 33);
	}

	public IDMove selectCharge() {
		if (user.isGrounded()){
			if (user.holdUp()) return new IDMove(uCharge(), 40);
			else if (user.holdDown()) return new IDMove(dCharge(), 41);
			else if (user.holdBack()) {
				user.flip();
				return new IDMove(sCharge(), 42);
			}
			else return new IDMove(sCharge(), 42);
		}
		else return selectAerial();
	}
	
	public IDMove selectBlock() {
		if (!user.isGrounded()) return new IDMove(airDodge(), noStaleMove);
		else return new IDMove(null, noStaleMove);
	}
	
	public IDMove selectTaunt() {
		return new IDMove(taunt(), noStaleMove);
	}

	private IDMove selectAerial(){
		if (user.holdUp()) return new IDMove(uAir(), 50);
		else if (user.holdDown()) return new IDMove(dAir(), 51);
		else if (user.holdForward()) return new IDMove(fAir(), 52);
		else if (user.holdBack()) return new IDMove(bAir(), 53);
		else return new IDMove(nAir(), 54);
	}

}
