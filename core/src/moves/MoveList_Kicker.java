package moves;

import moves.Effect.Charge;
import entities.Fighter;

public class MoveList_Kicker extends MoveList{
	
	/* WEAK ATTACKS */

	public Move nWeak(Fighter user) {
		Move m = new Move(user, 8);
		Hitbox h1 = new Hitbox(user, 1.5f, 0.5f, 5, 74, 24, 0, 16);
		m.actionList.addActionCircle(h1, 3, 7);
		return m;
	}

	public Move uWeak(Fighter user) {
		Move m = new Move(user, 27);
		Hitbox knee = new Hitbox(user, 4, 0, 3, 90, 24, 0, 12);
		Hitbox kick = new Hitbox(user, 3, 3, 7, 85, 20, 12, 24);
		m.actionList.addActionCircle(knee, 4, 7);
		m.actionList.addActionCircle(kick, 18, 20);
		return m;
	}

	public Move dWeak(Fighter user) {
		Move m = new Move(user, 17);
		Hitbox inner = new Hitbox(user, 3, 2, 6, 76, 20, -12, 14);
		Hitbox outer = new Hitbox(user, 3, 2, 7, 84, 34, -12, 12);
		inner.connect(outer);
		m.actionList.addActionCircle(inner, 6, 9);
		m.actionList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move sWeak(Fighter user) {
		Move m = new Move(user, 20);
		Hitbox inner = new Hitbox(user, 3, 3, 10, Hitbox.SAMURAIANGLE, 20, 4, 14);
		Hitbox outer = new Hitbox(user, 3, 3, 10, Hitbox.SAMURAIANGLE, 34, 4, 12);
		inner.connect(outer);
		m.actionList.addActionCircle(inner, 6, 9);
		m.actionList.addActionCircle(outer, 6, 9);
		return m;
	}
	
	public Move slide(Fighter user) { 
		Move m = new Move(user, 30);
		m.actionList.addVelocityChange(user, 2, user.direct() * 12, Action.ChangeVelocity.noChange);
		Hitbox early = new Hitbox(user, 4, 6, 10, Hitbox.SAMURAIANGLE, 16, 0, 18);
		Hitbox late = new Hitbox(user, 3, 3, 6, 80, 24, 0, 12);
		late.connect(early);
		m.actionList.addActionCircle(early, 4, 8);
		m.actionList.addActionCircle(late, 9, 22);
		return m;
	}
	
	/* CHARGE ATTACKS */
	
	public Move sCharge(Fighter user) {
		Move m = new Move(user, 36);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 4, 4, 15, 45, 12, 0, 6, c);
		Hitbox h2 = new Hitbox(user, 5, 5, 18, 35, 20, 0, 6, c);
		Hitbox h3 = new Hitbox(user, 6, 6, 21, 25, 28, 0, 8, c);
		h1.connect(h2);
		h1.connect(h3);
		h2.connect(h3);
		m.actionList.addCharge(user, c);
		m.actionList.addActionCircle(h1, 12, 16);
		m.actionList.addActionCircle(h2, 12, 16);
		m.actionList.addActionCircle(h3, 12, 16);
		return m;
	}
	
	public Move uCharge(Fighter user) {
		Move m = new Move(user, 32);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 4, 8, 18, 90, 16, 0, 10, c);
		Hitbox h2 = new Hitbox(user, 4, 6, 14, 90, 0,  16,10, c);
		Hitbox h3 = new Hitbox(user, 3, 3, 10, 30,-16,0, 10, c);
		h2.connect(h1);
		h3.connect(h2);
		m.actionList.addCharge(user, c);
		m.actionList.addActionCircle(h1, 16, 18);
		m.actionList.addActionCircle(h2, 19, 21);
		m.actionList.addActionCircle(h3, 22, 25);
		return m;
	}
	
	public Move dCharge(Fighter user) {
		Move m = new Move(user, 40);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		m.actionList.addCharge(user, c);
		Hitbox front = new Hitbox(user, 3, 4, 12, 30, 20, 0, 20, c);
		Hitbox back = new Hitbox(user, 3, 4, 12, 30, -20, 0, 20, c);
		m.actionList.addActionCircle(front, 26, 30);
		m.actionList.addActionCircle(back, 32, 36);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox early = new Hitbox(user, 3, 3, 7, 60, 0, 0, 20);
		Hitbox late = new Hitbox(user, 2, 2, 4, 50, 0, 0, 15);
		late.connect(early);
		m.actionList.addActionCircle(early, 5, 15);
		m.actionList.addActionCircle(late, 15, 25);
		return m;
	}

	public Move uAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox early = new Hitbox(user, 4, 3, 7, 90, 0, 12, 15);
		Hitbox late = new Hitbox(user, 3, 2, 5, 80, 0, 12, 12);
		late.connect(early);
		m.actionList.addActionCircle(early, 5, 15);
		m.actionList.addActionCircle(late, 15, 25);
		return m;
	}

	public Move dAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox h1 = new Hitbox(user, 3, 4, 12, 270, 0, -12, 15);
		m.actionList.addActionCircle(h1, 10, 14);
		return m;
	}

	public Move fAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox early = new Hitbox(user, 5, 6, 15, 30, 12, 0, 10);
		Hitbox late = new Hitbox(user, 3, 3, 5, 50, 12, 0, 5);
		early.setProperty(Hitbox.Property.ELECTRIC);
		late.connect(early);
		m.actionList.addActionCircle(early, 8, 10);
		m.actionList.addActionCircle(late, 11, 16);
		return m;
	}

	public Move bAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox early = new Hitbox(user, 3, 3, 8, 60, -12, 6, 30);
		Hitbox late = new Hitbox(user, 2, 2, 5, 50, -12, 12, 15);
		late.connect(early);
		m.actionList.addActionCircle(early, 5, 15);
		m.actionList.addActionCircle(late, 15, 25);
		return m;
	}

	/* SPECIAL ATTACKS */

	public Move uSpecial(Fighter user) {
		Move m = new Move(user, 30);
		m.setContinueOnLanding();
		Hitbox h1 = new Hitbox(user, 9, 0, 2, 90, 0, 0, 15);
		Hitbox h2 = new Hitbox(user, 7, 0, 2, 90, 0, 10, 10);
		Hitbox h3 = new Hitbox(user, 4, 4, 4, 80, 0, 12, 15);
		m.actionList.addVelocityChange(user, 0, 0, 1);
		m.actionList.addActionCircle(h1, 4, 12);
		m.actionList.addVelocityChange(user, 8, Action.ChangeVelocity.noChange, 16);
		m.actionList.addActionCircle(h2, 15, 25);
		m.actionList.addActionCircle(h3, 25, 28);
		m.helpless = true;
		return m;
	}

	public Move dSpecial(Fighter user) {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.actionList.addConstantVelocity(user, 8, 36, user.direct() * 2, -12);
		Hitbox h1 = new Hitbox(user, 4, 5, 14, 285, 12, -12, 20);
		h1.setProperty(Hitbox.Property.ELECTRIC);
		m.actionList.addActionCircle(h1, 8, 32);
		return m;
	}

	public Move sSpecial(Fighter user) {
		Move m = new Move(user, 30);
		m.setContinueOnLanding();
		Hitbox h1 = new Hitbox(user, 4, 3, 9, 70, 8, 0, 6);
		Hitbox h2 = new Hitbox(user, 5, 5, 15, 40, 14, 0, 6);
		Hitbox h3 = new Hitbox(user, 6, 6, 21, 20, 20, 0, 6);
		h1.connect(h2);
		h1.connect(h3);
		h2.connect(h3);
		m.actionList.addActionCircle(h1, 12, 16);
		m.actionList.addActionCircle(h2, 12, 16);
		m.actionList.addActionCircle(h3, 12, 16);
		return m;
	}

	public Move nSpecial(Fighter user) {
		Move m = new Move(user, 60);
		m.setContinueOnLanding();
		m.actionList.addVelocityChange(user, 32, user.direct() * 7, Action.ChangeVelocity.noChange);
		Hitbox h1 = new Hitbox(user, 7, 6, 25, 30, 10, 0, 25);
		m.actionList.addActionCircle(h1, 34, 42);
		//m.actionList.addSound("sfx/tada.mp3", 35);
		return m;
	}
	
	/* THROWS */

	public Move fThrow(Fighter user){
		Move m = new Move(user, 18);
		Hitbox h1 = new Hitbox(user, 4, 2, 5, 30, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move bThrow(Fighter user){
		Move m = new Move(user, 18);
		Hitbox h1 = new Hitbox(user, 2, 4, 5, 140, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move uThrow(Fighter user){
		Move m = new Move(user, 14);
		Hitbox h1 = new Hitbox(user, 4, 3, 5, 90, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move dThrow(Fighter user){
		Move m = new Move(user, 10);
		Hitbox down = new Hitbox(user, 3, 0, 1, 270, 16, 0, 20);
		Hitbox up = new Hitbox(user, 3, 1, 3, 82, 16, -8, 20);
		m.actionList.addActionCircle(down, 0, 1);
		m.actionList.addActionCircle(up, 4, 8);
		return m;
	}
	
	public Move fAirThrow(Fighter user){
		Move m = new Move(user, 18);
		Hitbox h1 = new Hitbox(user, 5, 3, 5, 30, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move bAirThrow(Fighter user){
		Move m = new Move(user, 18);
		Hitbox h1 = new Hitbox(user, 3, 5, 5, 140, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move uAirThrow(Fighter user){
		Move m = new Move(user, 14);
		Hitbox h1 = new Hitbox(user, 4, 3, 5, 90, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move dAirThrow(Fighter user){
		Move m = new Move(user, 10);
		m.helpless = true;
		Hitbox down = new Hitbox(user, 8, 0, 8, 270, 16, 0, 20);
		m.actionList.addActionCircle(down, 0, 1);
		return m;
	}
	
	/* GRABS */

	public Move grab(Fighter user) {
		Move m = new Move(user, 24);
		Grabbox g1 = new Grabbox(user, 20, 0, 18);
		m.actionList.addActionCircle(g1, 6, 10);
		return m;
	}
	
	public Move dashGrab(Fighter user) {
		Move m = new Move(user, 32);
		m.actionList.addVelocityChange(user, 4, user.direct() * 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 8, 0, 10);
		m.actionList.addActionCircle(g1, 6, 10);
		return m;
	}
	
	public Move airGrab(Fighter user) {
		Move m = new Move(user, 20);
		Grabbox g1 = new Grabbox(user, 8, 0, 15);
		m.actionList.addActionCircle(g1, 7, 10);
		return m;
	}

}
