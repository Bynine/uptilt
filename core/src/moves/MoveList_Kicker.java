package moves;

import java.util.Arrays;

import moves.Effect.Charge;
import entities.Fighter;

public class MoveList_Kicker extends MoveList{

	/* WEAK ATTACKS */

	public Move nWeak(Fighter user) {
		Move m = new Move(user, 8);
		m.setAnimation("sprites/player/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 1.5f, 0.5f, 5, 74, 18, 6, 17);
		m.actionList.addActionCircle(h1, 3, 7);
		return m;
	}

	public Move uWeak(Fighter user) {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/player/uweak.png", 5, 6);
		Hitbox knee = new Hitbox(user, 4, 0, 3, 90, 24, 0, 12);
		Hitbox kick = new Hitbox(user, 3, 3, 7, 85, 20, 12, 24);
		m.actionList.addActionCircle(knee, 5, 8);
		m.actionList.addActionCircle(kick, 18, 20);
		return m;
	}

	public Move dWeak(Fighter user) {
		Move m = new Move(user, 17);
		m.setAnimation("sprites/player/dweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 3, 2, 6, 76, 20, -9, 14);
		Hitbox outer = new Hitbox(user, 3, 2, 7, 84, 34, -8, 12);
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.actionList.addActionCircle(inner, 6, 9);
		m.actionList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move sWeak(Fighter user) {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/player/sweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 3, 3, 10, Hitbox.SAMURAIANGLE, 20, 4, 14);
		Hitbox outer = new Hitbox(user, 3, 3, 10, Hitbox.SAMURAIANGLE, 34, 4, 12);
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.actionList.addActionCircle(inner, 6, 9);
		m.actionList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move slide(Fighter user) { 
		Move m = new Move(user, 24);
		m.setAnimation("sprites/player/nair.png", 1, 1);
		m.actionList.addConstantVelocity(user, 2, 12, user.direct() * 12, Action.ChangeVelocity.noChange);
		Hitbox early = new Hitbox(user, 3, 3, 10, 65, 16, -4, 20);
		Hitbox late = new Hitbox(user, 2, 2, 7, 90, 24, -4, 14);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.actionList.addActionCircle(early, 4, 8);
		m.actionList.addActionCircle(late, 9, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move sCharge(Fighter user) {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/player/scharge.png", 6, 5);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 3, 4, 15, 60, 16, 0, 8, c);
		Hitbox h2 = new Hitbox(user, 4, 5, 18, 52, 30, 0, 8, c);
		Hitbox h3 = new Hitbox(user, 6, 6, 22, 42, 40, 0, 10, c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3));
		m.actionList.addCharge(user, c);
		m.actionList.addActionCircle(h1, 12, 16);
		m.actionList.addActionCircle(h2, 12, 16);
		m.actionList.addActionCircle(h3, 12, 16);
		return m;
	}

	public Move uCharge(Fighter user) {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/player/ucharge.png", 4, 8);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 4, 5, 17, 90, 26, -16, 16, c);
		Hitbox h2 = new Hitbox(user, 4, 4, 16, 87, 20, 8, 16, c);
		Hitbox h3 = new Hitbox(user, 3, 4, 15, 84,  0,  26,16, c);
		Hitbox h4 = new Hitbox(user, 3, 3, 14, 70,-20, 8, 16, c);
		Hitbox h5 = new Hitbox(user, 2, 3, 13, 30,-26, -16, 16, c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.actionList.addCharge(user, c);
		m.actionList.addActionCircle(h1, 8, 9);
		m.actionList.addActionCircle(h2, 10, 11);
		m.actionList.addActionCircle(h3, 12, 13);
		m.actionList.addActionCircle(h4, 14, 16);
		m.actionList.addActionCircle(h5, 16, 18);
		return m;
	}

	public Move dCharge(Fighter user) {
		Move m = new Move(user, 40);
		Effect.Charge c = new Charge(3, 23, 0.02f, user, m);
		m.actionList.addCharge(user, c);
		Hitbox front1 = new Hitbox(user, 4, 5, 14, 30, 20, 8, 8, c);
		Hitbox front2 = new Hitbox(user, 3, 4, 12, 30, 30, 8, 10, c);
		Hitbox back1 = new Hitbox(user, 4, 5, 14, 30, -20, 8, 8, c);
		Hitbox back2 = new Hitbox(user, 3, 4, 12, 30, -30, 8, 10, c);
		m.actionList.addActionCircle(front1, 22, 25);
		m.actionList.addActionCircle(front2, 22, 25);
		m.actionList.addActionCircle(back1, 30, 33);
		m.actionList.addActionCircle(back2, 30, 33);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir(Fighter user) {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/player/nair.png", 1, 1);
		Hitbox earlyBody = new Hitbox(user, 2, 3, 11, 60, -10, 0, 20);
		Hitbox earlyFoot = new Hitbox(user, 2, 3, 12, 60, 24, -6, 12);
		Hitbox lateBody = new Hitbox(user, 1, 2, 8, 60, -10, 0, 16);
		Hitbox lateFoot = new Hitbox(user, 1, 2, 9, 60, 24, -6, 8);
		new ActionCircleGroup(Arrays.asList(earlyBody, earlyFoot, lateBody, lateFoot));
		m.actionList.addActionCircle(earlyBody, 3, 6);
		m.actionList.addActionCircle(earlyFoot, 3, 6);
		m.actionList.addActionCircle(lateBody, 7, 25);
		m.actionList.addActionCircle(lateFoot, 7, 25);
		return m;
	}

	public Move uAir(Fighter user) {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/player/uair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 2, 0, 3, 90, 0, 20, 15);
		Hitbox h2 = new Hitbox(user, 2, 5, 8, 90, 0, 24, 18);
		m.actionList.addActionCircle(h1, 7, 10);
		m.actionList.addActionCircle(h2, 12, 15);
		return m;
	}

	public Move dAir(Fighter user) {
		Move m = new Move(user, 30);
		Hitbox h1 = new Hitbox(user, 3, 3, 11, 270, 0, -16, 12);
		Hitbox h2 = new Hitbox(user, 3, 4, 14, 270, 0, -28, 16);
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.actionList.addActionCircle(h1, 10, 14);
		m.actionList.addActionCircle(h2, 10, 14);
		return m;
	}

	public Move fAir(Fighter user) {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/player/fair.png", 1, 1);
		Hitbox early = new Hitbox(user, 4, 5, 15, 30, 24, 0, 16);
		Hitbox late = new Hitbox(user, 1, 2, 5, Hitbox.SAMURAIANGLE, 27, 0, 10);
		early.setProperty(Hitbox.Property.ELECTRIC);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.actionList.addActionCircle(early, 8, 9);
		m.actionList.addActionCircle(late, 10, 20);
		return m;
	}

	public Move bAir(Fighter user) {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/player/bair.png", 1, 1);
		Hitbox early1 = new Hitbox(user, 3, 4, 12, 40, -30, 6, 16);
		Hitbox early2 = new Hitbox(user, 3, 4, 12, 40, -10, 6, 20);
		Hitbox late1 =  new Hitbox(user, 2, 2, 8, Hitbox.SAMURAIANGLE, -32, 6, 12);
		Hitbox late2 =  new Hitbox(user, 2, 2, 8, Hitbox.SAMURAIANGLE, -10, 6, 14);
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.actionList.addActionCircle(early1, 6, 11);
		m.actionList.addActionCircle(early2, 6, 11);
		m.actionList.addActionCircle(late1, 12, 22);
		m.actionList.addActionCircle(late2, 12, 22);
		return m;
	}

	/* SPECIAL ATTACKS */

	private final int fire = 16;
	public Move uSpecial(Fighter user) {
		Move m = new Move(user, 30);
		m.setContinueOnLanding();
		Hitbox gunshot = new Hitbox(user, 5, 6, 16, 270, 0, -28, 24);
		Hitbox upward = new Hitbox(user, 3, 4, 10, 90, 0, 20, 16);
		m.actionList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 7f);
		m.actionList.addActionCircle(gunshot, fire - 1, fire + 1);
		m.actionList.addVelocityChange(user, fire, Action.ChangeVelocity.noChange, 20);
		m.actionList.addActionCircle(upward, fire, fire + 12);
		m.setHelpless();
		return m;
	}

	public Move dSpecial(Fighter user) {
		Move m = new Move(user, 48);
		Hitbox h1;
		if (user.isGrounded()){
			m.actionList.addConstantVelocity(user, 8, 36, user.direct() * 9, 0.6f);
			h1 = new Hitbox(user, 5, 4, 14, Hitbox.SAMURAIANGLE, 24, -6, 18);
		}
		else {
			m.actionList.addConstantVelocity(user, 8, 36, user.direct() * 2, -12);
			h1 = new Hitbox(user, 5, 4, 14, 285, 18, -30, 18);
		}
		h1.setProperty(Hitbox.Property.ELECTRIC);
		m.actionList.addActionCircle(h1, 8, 32);
		return m;
	}

	public Move sSpecial(Fighter user) {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/player/nair.png", 1, 1);
		m.setContinueOnLanding();
		m.actionList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 8);
		m.actionList.addConstantVelocity(user, 2, 12, user.direct() * 12, Action.ChangeVelocity.noChange);
		Hitbox early = new Hitbox(user, 6, 3, 13, Hitbox.SAMURAIANGLE, 16, -4, 20);
		Hitbox late = new Hitbox(user, 3, 3, 9, 80, 24, -4, 14);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.actionList.addActionCircle(early, 4, 8);
		m.actionList.addActionCircle(late, 9, 32);
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
		m.setAnimation("sprites/player/sweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3, 5, 8, 50, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move bThrow(Fighter user){
		Move m = new Move(user, 12);
		Hitbox h1 = new Hitbox(user, 5, 1, 6, 100, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move uThrow(Fighter user){
		Move m = new Move(user, 14);
		m.setAnimation("sprites/player/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 4, 3, 6, 90, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move dThrow(Fighter user){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/player/dthrow.png", 1, 1);
		Hitbox down = new Hitbox(user, 3, 0, 1, 270, 16, 0, 20);
		Hitbox up = new Hitbox(user, 3, 1, 4, 82, 16, -8, 20);
		m.actionList.addActionCircle(down, 0, 1);
		m.actionList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(Fighter user){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/player/fair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5, 3, 8, 30, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move bAirThrow(Fighter user){
		Move m = new Move(user, 18);
		Hitbox h1 = new Hitbox(user, 3, 5, 8, 160, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move uAirThrow(Fighter user){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/player/uthrow.png", 1, 1);
		m.actionList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 4);
		Hitbox h1 = new Hitbox(user, 4, 3, 7, 90, 16, 0, 20);
		m.actionList.addActionCircle(h1, 0, 1);
		return m;
	}

	public Move dAirThrow(Fighter user){
		Move m = new Move(user, 24);
		Hitbox down = new Hitbox(user, 5, 5, 12, 270, 16, 0, 20);
		m.actionList.addActionCircle(down, 0, 1);
		return m;
	}

	/* GRABS */

	public Move grab(Fighter user) {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/player/grab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 20, 0, 18);
		m.actionList.addActionCircle(g1, 6, 10);
		return m;
	}

	public Move dashGrab(Fighter user) {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/player/dashgrab.png", 1, 1);
		m.actionList.addVelocityChange(user, 4, user.direct() * 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 20, 0, 18);
		m.actionList.addActionCircle(g1, 6, 10);
		return m;
	}

	public Move airGrab(Fighter user) {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/player/airgrab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 8, 0, 22);
		m.actionList.addActionCircle(g1, 7, 10);
		return m;
	}
	
	/* MISC */
	
	public Move land(Fighter user){
		Move m = new Move(user, 3);
		m.setAnimation("sprites/player/crouch.png", 1, 1);
		return m;
	}
	
	public Move skid(Fighter user){
		Move m = new Move(user, 16);
		m.setAnimation("sprites/player/skid.png", 1, 1);
		return m;
	}

}
