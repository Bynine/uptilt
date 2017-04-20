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
import entities.Entity.Direction;
import entities.Fighter;
import entities.Projectile;

public class Kicker extends MoveList{
	
	public Kicker(Fighter user) {
		super(user);
	}

	/* WEAK ATTACKS */

	public Move nWeak() {
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/kicker/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 1.5f, 0.5f, 4, 74, 18, 6, 17, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 3, 7);
		return m;
	}

	public Move uWeak() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/kicker/uweak.png", 5, 6);
		Hitbox knee = new Hitbox(user, 4.2f, 0, 3, 95, 14, -4, 18, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 3, 2, 7, 85, 18, 34, 14, new SFX.MidHit());
		m.eventList.addActionCircle(knee, 5, 8);
		m.eventList.addActionCircle(kick, 18, 20);
		return m;
	}

	public Move dWeak() {
		Move m = new Move(user, 17);
		m.setAnimation("sprites/fighters/kicker/dweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 3, 0.8f, 6, 76, 15, -5, 15, new SFX.LightHit());
		Hitbox outer = new Hitbox(user, 3, 0.8f, 7, 84, 30, -4, 12, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.eventList.addActionCircle(inner, 6, 9);
		m.eventList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move sWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/kicker/sweak.png", 1, 1);
		Hitbox inner = new Hitbox(user, 2, 2.4f, 10, Hitbox.SAMURAIANGLE, 20, 4, 14, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 2, 2.4f, 10, Hitbox.SAMURAIANGLE, 34, 4, 12, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, outer));
		m.eventList.addActionCircle(inner, 6, 9);
		m.eventList.addActionCircle(outer, 6, 9);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/nair.png", 1, 1);
		Hitbox early = new Hitbox(user, 4, 3, 10, 65, 16, -4, 20, new SFX.HeavyHit());
		Hitbox late = new Hitbox(user, 2, 2, 7, 90, 24, -4, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 4, 12, 12, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 3, 10);
		m.eventList.addActionCircle(late, 11, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move sCharge() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/kicker/scharge.png", 6, 5);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 5, 3f, 15, 60, 10, 4, 12, new SFX.HeavyHit(), c);
		Hitbox h2 = new Hitbox(user, 6, 3.6f, 18, 52, 26, 4, 8, new SFX.HeavyHit(), c);
		Hitbox h3 = new Hitbox(user, 6.5f, 4.2f, 22, 42, 36, 4, 8, new SFX.HomeRun(), c);
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
		m.setAnimation("sprites/fighters/kicker/ucharge.png", 4, 8);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 5, 5f, 17, 90, 26,-16, 16, new SFX.HeavyHit(),  c);
		Hitbox h2 = new Hitbox(user, 5, 4.5f, 16, 87, 20, 16, 16, new SFX.HeavyHit(),  c);
		Hitbox h3 = new Hitbox(user, 4.5f, 4, 15, 84,  0, 26, 16, new SFX.MidHit(),  c);
		Hitbox h4 = new Hitbox(user, 4, 3.5f, 14, 70,-20, 16, 16, new SFX.MidHit(),  c);
		Hitbox h5 = new Hitbox(user, 3.5f, 3, 13, 30,-26,-16, 16, new SFX.LightHit(),  c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 8, 9);
		m.eventList.addActionCircle(h2, 10, 11);
		m.eventList.addActionCircle(h3, 12, 13);
		m.eventList.addActionCircle(h4, 14, 16);
		m.eventList.addActionCircle(h5, 16, 18);
		return m;
	}

	public Move dCharge() {
		Move m = new Move(user, 42);
		m.setAnimation("sprites/fighters/kicker/dcharge.png", 4, 8);
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		m.eventList.addCharge(user, c);
		Hitbox front1 = new Hitbox(user, 4, 4.5f, 14, 30, 20, 16, 8, new SFX.HeavyHit(),  c);
		Hitbox front2 = new Hitbox(user, 4, 3, 12, 30, 36, 16, 10, new SFX.HeavyHit(),  c);
		Hitbox back1 = new Hitbox(user, 4, 4.5f, 14, 30, -20, 16, 8, new SFX.HeavyHit(),  c);
		Hitbox back2 = new Hitbox(user, 4, 3, 12, 30, -36, 16, 10, new SFX.HeavyHit(),  c);
		m.eventList.addActionCircle(front1, 16, 24);
		m.eventList.addActionCircle(front2, 16, 24);
		m.eventList.addActionCircle(back1, 16, 24);
		m.eventList.addActionCircle(back2, 16, 24);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/nair.png", 1, 1);
		Hitbox earlyBody = new Hitbox(user, 2, 2.1f, 11, 60, -10, 0, 20, new SFX.MidHit());
		Hitbox earlyFoot = new Hitbox(user, 2, 2.2f, 12, 60, 24, -6, 12, new SFX.MidHit());
		Hitbox lateBody = new Hitbox(user, 1, 1.3f, 8, 60, -10, 0, 16, new SFX.LightHit());
		Hitbox lateFoot = new Hitbox(user, 1, 1.3f, 9, 60, 24, -6, 8, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(earlyBody, earlyFoot, lateBody, lateFoot));
		m.eventList.addActionCircle(earlyBody, 3, 6);
		m.eventList.addActionCircle(earlyFoot, 3, 6);
		m.eventList.addActionCircle(lateBody, 7, 25);
		m.eventList.addActionCircle(lateFoot, 7, 25);
		return m;
	}

	public Move uAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/uair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 2, 0, 3, 90, 0, 20, 15, new SFX.LightHit());
		Hitbox h2 = new Hitbox(user, 2, 3.2f, 8, 90, 0, 24, 18, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 7, 10);
		m.eventList.addActionCircle(h2, 12, 15);
		return m;
	}

	public Move dAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/dair.png", 3, 10);
		Hitbox h1 = new Hitbox(user, 1, 2.4f, 11, 270, 0, 0, 14, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 1, 2.6f, 14, 270, 0, -24, 18, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.eventList.addActionCircle(h1, 11, 17);
		m.eventList.addActionCircle(h2, 11, 17);
		return m;
	}

	public Move fAir() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/kicker/fair.png", 1, 1);
		Hitbox early = new Hitbox(user, 5, 2.4f, 14, 30, 24, 0, 16, new SFX.HeavyHit());
		Hitbox late = new Hitbox(user, 1, 1, 5, Hitbox.SAMURAIANGLE, 27, 0, 10, new SFX.LightHit());
		early.setProperty(Hitbox.Property.ELECTRIC);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addActionCircle(early, 8, 9);
		m.eventList.addActionCircle(late, 10, 20);
		return m;
	}

	public Move bAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/bair.png", 1, 1);
		Hitbox early1 = new Hitbox(user, 3, 2.5f, 12, 40, -24, 4, 16, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 3, 2.5f, 12, 40, -10, 4, 20, new SFX.MidHit());
		Hitbox late1 =  new Hitbox(user, 2, 1, 8, Hitbox.SAMURAIANGLE, -26, 6, 12, new SFX.LightHit());
		Hitbox late2 =  new Hitbox(user, 2, 1, 8, Hitbox.SAMURAIANGLE, -12, 6, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 6, 11);
		m.eventList.addActionCircle(early2, 6, 11);
		m.eventList.addActionCircle(late1, 12, 22);
		m.eventList.addActionCircle(late2, 12, 22);
		return m;
	}

	/* SPECIAL ATTACKS */

	private final int fire = 16;
	public Move uSpecial() {
		Move m = new Move(user, 30);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/kicker/uspecial.png", 1, 1);
		Hitbox upward = new Hitbox(user, 5, 2, 10, 90, 0, 20, 16, new SFX.MidHit());
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 7f);
		m.eventList.addProjectile(user, Projectile.ShotgunBlast.class, fire - 1);
		m.eventList.addVelocityChange(user, fire, Action.ChangeVelocity.noChange, 16);
		m.eventList.addActionCircle(upward, fire, fire + 20);
		m.setHelpless();
		return m;
	}

	public Move dSpecial() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/kicker/dspecial.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, user.getVelocity().y - 4);
		m.eventList.addArmor(m, 5, 25, 5);
		m.eventList.addVelocityChange(user, 20, -5, Action.ChangeVelocity.noChange);
		m.eventList.addProjectile(user, Projectile.Rocket.class, 20);
		return m;
	}

	public Move sSpecial() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/kicker/sspecial.png", 4, 12);
		Hitbox early = new Hitbox(user, 6, 2.5f, 13, Hitbox.SAMURAIANGLE, 10, -4, 20, new SFX.HeavyHit());
		Hitbox late = new Hitbox(user, 3, 1, 9, 80, 12, -4, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		if (user.isGrounded()) m.eventList.addVelocityChange(user, 10, Action.ChangeVelocity.noChange, 8);
		else m.eventList.addVelocityChange(user, 12, Action.ChangeVelocity.noChange, 6);
		m.eventList.addConstantVelocity(user, 12, 14, 13, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 13, 20);
		m.eventList.addActionCircle(late, 21, 32);
		return m;
	}

	public Move nSpecial() {
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/nspecial.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addProjectile(user, Projectile.Spiker.class, 12);
		return m;
	}

	/* THROWS */

	public Move fThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/sweak.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 4.8f, 1.1f, 14, 50, 8, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bThrow(){
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/kicker/bthrow.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 5, 0.25f, 9, 100, 8, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move uThrow(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 4, 2, 10, 90, 8, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/kicker/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox down = new Hitbox(user, 3, 0, 4, 270, 8, 0, 30, new SFX.LightHit());
		Hitbox up = new Hitbox(user, 3, 1.2f, 6, 82, 8, -8, 30, new SFX.MidHit());
		m.eventList.addActionCircle(down, 0, 1);
		m.eventList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/fair.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3, 2.8f, 14, 30, 16, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 3, 2.4f, 14, 150, 24, -12, 20, new SFX.MidHit());
		m.eventList.addVelocityChange(user, 5, 4, 4);
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/kicker/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 4, 1.5f, 13, 90, 8, 0, 30, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 0, 4);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 4);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/dair.png", 3, 8);
		Hitbox down = new Hitbox(user, 3, 3, 15, 280, 8, 0, 30, new SFX.HeavyHit());
		m.eventList.addActionCircle(down, 0, 4);
		m.eventList.addVelocityChange(user, 0, -4, 6);
		return m;
	}

	/* GRABS */

	public Move grab() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/grab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 18, 12, 14);
		m.eventList.addActionCircle(g1, 6, 10);
		return m;
	}

	public Move dashGrab() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/kicker/dashgrab.png", 1, 1);
		m.eventList.addVelocityChange(user, 4, 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 18, 12, 14);
		m.eventList.addActionCircle(g1, 6, 10);
		return m;
	}

	public Move airGrab() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/kicker/airgrab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 14, 0, 24);
		m.eventList.addActionCircle(g1, 7, 10);
		return m;
	}
	
	/* DODGES */
	
	private final float boost = 10.6f;
	public Move airDodge(){
		Move m = new Move(user, 20);
		m.setHelpless();
		m.dontTurn();
		boolean airDodgeBack = (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT) || (user.getStickX() < 0 && user.getDirection() == Direction.RIGHT);
		if (airDodgeBack) m.setAnimation("sprites/fighters/kicker/airdodgeb.png", 1, 1);
		else m.setAnimation("sprites/fighters/kicker/airdodgef.png", 1, 1);
		m.eventList.addInvincible(user, 2, 18);
		m.eventList.addConstantVelocity(user, 0, 10, user.direct() * user.getStickX() * boost, -user.getStickY() * boost);
		m.eventList.addConstantVelocity(user, 11, 20, 0, 0);
		return m;
	}
	
	public Move dodge(){
		Move m = new Move(user, 24);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/dodge.png", 1, 1);
		m.eventList.addInvincible(user, 1, 20);
		return m;
	}
	
	private final int rollLength = 27;
	private final float rollSpeed = -8;
	private final int rollInvinc = 21;
	public Move rollForward(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		user.flip();
		m.setAnimation("sprites/fighters/kicker/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, 4);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}
	
	public Move rollBack(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, 4);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}
	
	/* MISC */
	
	public Move land(){
		Move m = new Move(user, 4);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/land.png", 1, 1);
		return m;
	}
	
	public Move skid(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/skid.png", 1, 1);
		return m;
	}
	
	public Move taunt(){
		Move m = new Move(user, 56);
		m.setAnimation("sprites/fighters/kicker/taunt.png", 7, 8);
		return m;
	}

}
