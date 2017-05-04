package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Grabbox;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Fighter;
import entities.Hittable.HitstunType;
import entities.Projectile;
import entities.Entity.Direction;

public class M_Frog extends M_Kicker {

	public M_Frog(Fighter user) {
		super(user);
	}

	public Move nWeak() {
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/frog/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 1.5f, 0.5f, 4, 74, 18, 6, 17, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 3, 7);
		return m;
	}

	public Move uWeak() {
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/frog/uweak.png", 3, 9);
		Hitbox knee = new Hitbox(user, 4.2f, 0, 3, 95, 14, -4, 18, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 3, 2, 7, 85, 18, 34, 14, new SFX.MidHit());
		m.eventList.addActionCircle(knee, 5, 8);
		m.eventList.addActionCircle(kick, 18, 20);
		return m;
	}

	public Move dWeak() {
		Move m = new Move(user, 17);
		m.setAnimation("sprites/fighters/frog/dweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 3, 0.8f, 6, 76, 15, -5, 15, new SFX.LightHit());
		Hitbox outer = new Hitbox(user, 3, 0.8f, 7, 84, 30, -4, 12, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.eventList.addActionCircle(inner, 6, 9);
		m.eventList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move sWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/frog/sweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 2, 2.4f, 10, Hitbox.SAMURAI, 20, 4, 14, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 2, 2.4f, 10, Hitbox.SAMURAI, 34, 4, 12, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.eventList.addActionCircle(inner, 6, 9);
		m.eventList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/scharge.png", 6, 3);
		Hitbox early = new Hitbox(user, 4, 4, 10, 40, 24, 4, 18, new SFX.HeavyHit());
		Hitbox late = new Hitbox(user,  2, 2, 7, 110, 24, 4, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 4, 24, 8, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 3, 10);
		m.eventList.addActionCircle(late, 11, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move sCharge() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/scharge.png", 6, 5);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		int bkb = 6, kbg = 4, dam = 20, ang = 40;
		Hitbox h1 = new Hitbox(user, bkb, kbg, dam, ang, 10, 4, 16, new SFX.MeatyHit(), c);
		Hitbox h2 = new Hitbox(user, bkb, kbg, dam, ang, 26, 4, 8, new SFX.MeatyHit(), c);
		Hitbox h3 = new Hitbox(user, bkb, kbg, dam, ang, 36, 4, 8, new SFX.MeatyHit(), c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 12, 18);
		m.eventList.addActionCircle(h2, 12, 18);
		m.eventList.addActionCircle(h3, 12, 18);
		m.eventList.addVelocityChangeCharge(user, 12, 8, Action.ChangeVelocity.noChange, c);
		return m;
	}

	public Move uCharge() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/ucharge.png", 5, 8);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 7, 	4, 18, 90, 26,-12, 18, new SFX.HeavyHit(), 	 c);
		Hitbox h2 = new Hitbox(user, 6, 	4, 16, 87, 22, 21, 16, new SFX.HeavyHit(), 	 c);
		Hitbox h3 = new Hitbox(user, 5, 	3, 14, 84,  0, 26, 16, new SFX.MidHit(), 	 c);
		Hitbox h4 = new Hitbox(user, 4, 	2, 13, 70,-22, 21, 16, new SFX.MidHit(), 	 c);
		Hitbox h5 = new Hitbox(user, 3, 	1, 10, 30,-26,-16, 16, new SFX.LightHit(),	 c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 8, 10);
		m.eventList.addActionCircle(h2, 11, 12);
		m.eventList.addActionCircle(h3, 13, 14);
		m.eventList.addActionCircle(h4, 15, 16);
		m.eventList.addActionCircle(h5, 16, 18);
		return m;
	}

	public Move dCharge() {
		Move m = new Move(user, 42);
		m.setAnimation("sprites/fighters/frog/dcharge.png", 4, 8);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		m.eventList.addCharge(user, c);
		Hitbox front1 = new Hitbox(user, 4.0f, 4.0f, 13, 40, 16, 16, 14, new SFX.HeavyHit(),  c);
		Hitbox front2 = new Hitbox(user, 4.0f, 3.0f, 13, 40, 36, 16, 10, new SFX.HeavyHit(),  c);
		Hitbox back1 = new Hitbox(user,  4.0f, 4.0f, 14, 40,-16, 16, 14, new SFX.HeavyHit(),  c);
		Hitbox back2 = new Hitbox(user,  4.0f, 3.0f, 13, 40,-36, 16, 10, new SFX.HeavyHit(),  c);
		m.eventList.addActionCircle(front1, 16, 24);
		m.eventList.addActionCircle(front2, 16, 24);
		m.eventList.addActionCircle(back1, 16, 24);
		m.eventList.addActionCircle(back2, 16, 24);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/nair.png", 1, 1);
		Hitbox earlyBody = new Hitbox(user, 4, 1.5f, 11, 75, -10, 0, 20, new SFX.MidHit());
		Hitbox earlyFoot = new Hitbox(user, 4, 1.4f, 12, 90, 24, -6, 12, new SFX.MidHit());
		Hitbox lateBody = new Hitbox(user, 2, 1.1f, 8, 80, -10, 0, 16, new SFX.LightHit());
		Hitbox lateFoot = new Hitbox(user, 2, 1.1f, 9, 90, 24, -6, 8, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(earlyBody, earlyFoot, lateBody, lateFoot));
		m.eventList.addActionCircle(earlyBody, 3, 6);
		m.eventList.addActionCircle(earlyFoot, 3, 6);
		m.eventList.addActionCircle(lateBody, 7, 25);
		m.eventList.addActionCircle(lateFoot, 7, 25);
		return m;
	}

	public Move uAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/uair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3, 2.2f, 8, 90, 0, 24, 18, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 10, 14);
		return m;
	}

	public Move dAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/dair.png", 3, 10);
		Hitbox early1 = new Hitbox(user, 1.5f, 2.4f, 12, 270, 0, 0, 15, new SFX.MeatyHit());
		Hitbox early2 = new Hitbox(user, 1.5f, 2.6f, 13, 270, 0, -24, 19, new SFX.MeatyHit());
		Hitbox late1 = new Hitbox(user, 1, 2f, 9, 270, 0, 0, 12, new SFX.MidHit());
		Hitbox late2 = new Hitbox(user, 1, 2f, 10, 270, 0, -24, 16, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 11, 12);
		m.eventList.addActionCircle(early2, 11, 12);
		m.eventList.addActionCircle(late1, 13, 17);
		m.eventList.addActionCircle(late2, 13, 17);
		return m;
	}

	public Move fAir() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/frog/fair.png", 1, 1);
		Hitbox early = new Hitbox(user, 5, 2.4f, 14, 30, 18, -2, 20, new SFX.MeatyHit());
		Hitbox late = new Hitbox(user, 2, 1, 5, 100, 24, -2, 16, new SFX.LightHit());
		early.setProperty(Hitbox.Property.ELECTRIC);
		early.setHitstunType(HitstunType.SUPER);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addActionCircle(early, 3, 4);
		m.eventList.addActionCircle(late, 5, 18);
		return m;
	}

	public Move bAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/bair.png", 1, 1);
		Hitbox early1 = new Hitbox(user, 3, 2.5f, 12, 	40, -24, 4, 16, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 3, 2.5f, 12, 	40,  -8, 4, 20, new SFX.MidHit());
		Hitbox late1 =  new Hitbox(user, 2, 1, 8, 		75, -22, 2, 12, new SFX.LightHit());
		Hitbox late2 =  new Hitbox(user, 2, 1, 8, 		75, -6, 2, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 6, 11);
		m.eventList.addActionCircle(early2, 6, 11);
		m.eventList.addActionCircle(late1, 12, 24);
		m.eventList.addActionCircle(late2, 12, 24);
		return m;
	}

	/* SPECIAL ATTACKS */

	public Move uSpecial() {
		if (user.isGrounded()) return groundedUSpecial();
		else return aerialUSpecial();
	}

	private Move groundedUSpecial(){
		Move m = new Move(user, 10);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/frog/crouch.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3, 3, 12, Hitbox.SAMURAI, 0, -24, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 8, 10);
		m.eventList.addVelocityChange(user, 10, Action.ChangeVelocity.noChange, 20);
		return m;
	}

	private Move aerialUSpecial(){
		Move m = new Move(user, 30);
		m.setHelpless();
		Hitbox h1 = new Hitbox(user, 6, 3, 20, 270, 0, -48, 16, new SFX.MeatyHit());
		m.setAnimation("sprites/fighters/frog/fjump.png", 1, 1);
		m.eventList.addVelocityChange(user, 5, Action.ChangeVelocity.noChange, 17);
		m.eventList.addActionCircle(h1, 4, 6);
		return m;
	}

	public Move dSpecial() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/dspecial.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, user.getVelocity().y - 4);
		m.eventList.addArmor(m, 5, 35, 4);
		m.eventList.addVelocityChange(user, 20, -5, Action.ChangeVelocity.noChange);
		m.eventList.addProjectile(user, Projectile.Rocket.class, 20);
		return m;
	}

	public Move sSpecial() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/frog/sspecial.png", 4, 12);
		Hitbox earlyfoot = new Hitbox(user, 6, 3, 20, Hitbox.SAMURAI, 24, -8, 12, new SFX.HeavyHit());
		Hitbox earlybody = new Hitbox(user, 6, 2, 16, Hitbox.SAMURAI, 10, -4, 20, new SFX.HeavyHit());
		Hitbox latebody = new Hitbox(user, 	3, 1, 12, 80, 12, -4, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(earlyfoot, earlybody, latebody));
		
		if (user.isGrounded()) m.eventList.addVelocityChange(user, 10, Action.ChangeVelocity.noChange, 8);
		else m.eventList.addVelocityChange(user, 12, Action.ChangeVelocity.noChange, 6);
		m.eventList.addConstantVelocity(user, 12, 14, 14, Action.ChangeVelocity.noChange);
		
		m.eventList.addActionCircle(earlyfoot, 13, 16);
		m.eventList.addActionCircle(earlybody, 13, 24);
		m.eventList.addActionCircle(latebody, 25, 32);
		return m;
	}

	public Move nSpecial() {
		Move m = new Move(user, 75);
		m.setAnimation("sprites/fighters/frog/taunt.png", 7, 9);
		m.setContinueOnLanding();
		Hitbox inner = new Hitbox(user, 15, 5, 32, Hitbox.SAMURAI, 15, 0, 30, new SFX.HeavyHit());
		Hitbox middl = new Hitbox(user,  9, 5, 24, Hitbox.SAMURAI, 30, 0, 60, new SFX.HeavyHit());
		Hitbox outer = new Hitbox(user,  7, 2, 16, Hitbox.SAMURAI, 45, 0, 90, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, middl, outer));
		m.eventList.addArmor(m, 25, 50, 6);
		m.eventList.addArmor(m, 51, 60, 999);
		m.eventList.addSound(new SFX.Explode(), 50);
		m.eventList.addActionCircle(inner, 50, 56);
		m.eventList.addActionCircle(middl, 50, 54);
		m.eventList.addActionCircle(outer, 50, 52);
		return m;
	}

	/* THROWS */

	public Move fThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/sweak.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 5, 1.1f, 14, 30, 8, 0, 30, new SFX.MeatyHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/bthrow.png", 3, 8);
		m.dontTurn();
		Hitbox thro = new Hitbox(user, 5.5f, 0, 2, 160, 8, 0, 30, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 5, 1.1f, 12, 30, -24, 0, 30, new SFX.MeatyHit());
		kick.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(thro, 0, 4);
		m.eventList.addActionCircle(kick, 8, 16);
		return m;
	}

	public Move uThrow(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 6, 1.4f, 12, 90, 8, 0, 30, new SFX.MidHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/frog/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox down = new Hitbox(user, 3, 0, 4, 270, 8, 0, 30, new SFX.LightHit());
		Hitbox up = new Hitbox(user, 3, 1.2f, 8, 82, 8, -8, 30, new SFX.MidHit());
		m.eventList.addActionCircle(down, 0, 1);
		m.eventList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/fair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 4, 2.8f, 14, 30, 16, 0, 30, new SFX.MidHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 4, 2.4f, 14, 150, 24, -12, 20, new SFX.MidHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addVelocityChange(user, 5, 4, 4);
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/frog/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 7, 0.1f, 13, 90, 8, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 6);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/dair.png", 3, 8);
		Hitbox down = new Hitbox(user, 3, 3, 15, 280, 8, 0, 30, new SFX.HeavyHit());
		down.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(down, 0, 4);
		m.eventList.addVelocityChange(user, 0, -4, 6);
		return m;
	}

	/* GRABS */

	public Move grab() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/grab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 18, 12, 14);
		m.eventList.addActionCircle(g1, 5, 10);
		return m;
	}

	public Move dashGrab() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/frog/dashgrab.png", 1, 1);
		m.eventList.addVelocityChange(user, 4, 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 18, 12, 14);
		m.eventList.addActionCircle(g1, 5, 10);
		return m;
	}

	public Move airGrab() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/airgrab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 14, 0, 24);
		m.eventList.addActionCircle(g1, 6, 10);
		return m;
	}

	/* DODGES */

	protected float boost = 10.6f;
	public Move airDodge(){
		Move m = new Move(user, 20);
		m.setHelpless();
		m.dontTurn();
		boolean airDodgeBack = (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT) || (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT);
		if (airDodgeBack) m.setAnimation("sprites/fighters/frog/airdodgeb.png", 1, 1);
		else m.setAnimation("sprites/fighters/frog/airdodgef.png", 1, 1);
		m.eventList.addInvincible(user, 2, 18);
		m.eventList.addConstantVelocity(user, 0, 10, user.direct() * user.getStickX() * boost, -user.getStickY() * boost);
		m.eventList.addConstantVelocity(user, 11, 20, 0, 0);
		return m;
	}

	public Move dodge(){
		Move m = new Move(user, 24);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/dodge.png", 1, 1);
		m.eventList.addInvincible(user, 1, 20);
		return m;
	}

	public Move getUpAttack() {
		Move m = new Move(user, 30);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/dweak.png", 1, 1);
		m.eventList.addInvincible(user, 1, 10);
		Hitbox h1 = new Hitbox(user, 5, 0, 8, 50, 16, 0, 20, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 2, 8);
		return m;
	}

	private final int rollLength = 32;
	private final float rollHeight = 4.2f;
	private final float rollSpeed = -8f;
	private final int rollInvinc = 24;
	public Move rollForward(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		user.flip();
		m.setAnimation("sprites/fighters/frog/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}

	public Move rollBack(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}

	/* MISC */

	public Move land(){
		Move m = new Move(user, 4);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/land.png", 1, 1);
		return m;
	}

	public Move skid(){
		Move m = new Move(user, 16);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/skid.png", 1, 1);
		return m;
	}

	public Move taunt(){
		Move m = new Move(user, 56);
		m.setAnimation("sprites/fighters/frog/taunt.png", 7, 8);
		return m;
	}

}
