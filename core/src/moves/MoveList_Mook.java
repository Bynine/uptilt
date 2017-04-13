package moves;

import main.SFX;
import entities.Fighter;

public class MoveList_Mook extends MoveList {

	public MoveList_Mook(Fighter user) {
		super(user);
	}
	
	public Move nWeak() {
		Move m = new Move(user, 16);
		Hitbox h1 = new Hitbox(user, 0, 2, 2, Hitbox.SAMURAIANGLE, 12, 6, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 6, 7);
		return m;
	}
	
	public Move uWeak() {
		Move m = new Move(user, 24);
		Hitbox h1 = new Hitbox(user, 2, 2.5f, 5, Hitbox.SAMURAIANGLE, 4, 16, 16, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 11, 12);
		return m;
	}
	
	public Move sWeak() {
		Move m = new Move(user, 24);
		Hitbox h1 = new Hitbox(user, 1, 2.5f, 4, Hitbox.SAMURAIANGLE, 16, 6, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 11, 12);
		return m;
	}
	
	public Move dWeak() {
		Move m = new Move(user, 24);
		Hitbox h1 = new Hitbox(user, 2, 0.5f, 3, Hitbox.SAMURAIANGLE, 12, -4, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 11, 12);
		return m;
	}
	
	public Move slide() {
		Move m = new Move(user, 72);
		Hitbox h1 = new Hitbox(user, 2, 3, 7, 40, 16, -6, 18, new SFX.LightHit());
		m.eventList.addVelocityChange(user, 11, 8, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(h1, 11, 12);
		return m;
	}
	
	public Move nAir() {
		Move m = new Move(user, 32);
		Hitbox h1 = new Hitbox(user, 1, 1, 4, Hitbox.SAMURAIANGLE, 0, 0, 16, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 20);
		return m;
	}
	
	public Move fAir() {
		Move m = new Move(user, 32);
		Hitbox h1 = new Hitbox(user, 1, 1, 4, Hitbox.SAMURAIANGLE, 16, 0, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 20);
		return m;
	}
	
	public Move bAir() {
		Move m = new Move(user, 32);
		Hitbox h1 = new Hitbox(user, 1, 1, 4, Hitbox.SAMURAIANGLE, -16, 0, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 20);
		return m;
	}
	
	public Move uAir() {
		Move m = new Move(user, 32);
		Hitbox h1 = new Hitbox(user, 1, 1, 4, 90, 0, 16, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 20);
		return m;
	}
	
	public Move dAir() {
		Move m = new Move(user, 32);
		Hitbox h1 = new Hitbox(user, 1, 1, 4, 270, 0, -16, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 20);
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 32);
		m.eventList.addVelocityChange(user, 6, Action.ChangeVelocity.noChange, 12);
		return m;
	}
	
	public Move sCharge() {
		return new Move(user, 0);
	}
	
	public Move uCharge() {
		return new Move(user, 0);
	}
	
	public Move dCharge() {
		return new Move(user, 0);
	}
	
	public Move nSpecial() {
		return new Move(user, 0);
	}
	
	public Move sSpecial() {
		return new Move(user, 0);
	}
	
	public Move dSpecial() {
		return new Move(user, 0);
	}

	public Move fThrow() {
		return new Move(user, 0);
	}

	public Move bThrow() {
		return new Move(user, 0);
	}
	
	public Move uThrow() {
		return new Move(user, 0);
	}
	
	public Move dThrow() {
		return new Move(user, 0);
	}
	
	public Move fAirThrow() {
		return new Move(user, 0);
	}
	
	public Move bAirThrow() {
		return new Move(user, 0);
	}
	
	public Move uAirThrow() {
		return new Move(user, 0);
	}
	
	public Move dAirThrow() {
		return new Move(user, 0);
	}
	
	public Move grab() {
		return new Move(user, 0);
	}
	
	public Move dashGrab() {
		return new Move(user, 0);
	}
	
	public Move airGrab() {
		return new Move(user, 0);
	}
	
	public Move rollForward() {
		return new Move(user, 0);
	}
	
	public Move rollBack() {
		return new Move(user, 0);
	}

	public Move airDodge() {
		return new Move(user, 0);
	}
	
	public Move dodge() {
		return new Move(user, 0);
	}

	public Move land() {
		return new Move(user, 0);
	}

	public Move skid() {
		return new Move(user, 0);
	}

	public Move taunt() {
		return new Move(user, 0);
	}

}
