package movelists;

import java.util.Arrays;

import main.GlobalRepo;
import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Effect.ConstantVelocity;
import moves.Grabbox;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Entity.Direction;
import entities.Fighter;
import entities.Hittable.HitstunType;
import entities.Projectile;

public class M_Wasp extends MoveList{
	
	public M_Wasp(Fighter user) {
		super(user);
	}

	/* WEAK ATTACKS */

	public Move nWeak() {
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/kicker/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3.1f, 0.6f, 5, 84, 18, 0, 17, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/kicker/uweak.png", 3, 7);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 60));
		Hitbox swing = new Hitbox(user, 3.0f, 1.0f, 4, 95, 6, 8, 22, new SFX.LightHit());
		Hitbox foot =  new Hitbox(user, 3.0f, 1.0f, 5, 85, 0, 34, 14, new SFX.MidHit());
		m.eventList.addActionCircle(swing, 6, 12);
		m.eventList.addActionCircle(foot, 8, 15);
		return m;
	}

	public Move dWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/kicker/dweak.png", 2, 10);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 30));
		Hitbox inner = new Hitbox(user, 4, 0.7f, 6, 82, -4, -8, 12, new SFX.MidHit());
		Hitbox midd =  new Hitbox(user, 4, 0.8f, 7, 88, 10,-11, 10, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 4, 0.9f, 8, 96, 27,-11, 10, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, midd, outer));
		m.eventList.addActionCircle(inner, 10, 19);
		m.eventList.addActionCircle(midd,  10, 19);
		m.eventList.addActionCircle(outer, 10, 19);
		return m;
	}

	public Move sWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/kicker/sweak.png", 3, 7);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 60));
		Hitbox vinner = new Hitbox(user, 2, 2.4f, 8, 70, 6, 4, 14, new SFX.MidHit());
		Hitbox inner = new Hitbox(user, 2, 2.4f, 8, 60, 20, 4, 14, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 2, 2.4f, 8, 60, 34, 4, 12, new SFX.MidHit());
		Hitbox vinner2 = new Hitbox(user, 1, 1.4f, 5, 70, 6, 4, 14, new SFX.LightHit());
		Hitbox inner2 = new Hitbox(user, 1, 1.4f, 5, 60, 20, 4, 14, new SFX.LightHit());
		Hitbox outer2 = new Hitbox(user, 1, 1.4f, 5, 60, 34, 4, 12, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(vinner, inner, outer, vinner2, inner2, outer2));
		m.eventList.addActionCircle(vinner, 7, 10);
		m.eventList.addActionCircle(inner, 7, 10);
		m.eventList.addActionCircle(outer, 7, 10);
		m.eventList.addActionCircle(vinner2, 10, 14);
		m.eventList.addActionCircle(inner2,  10, 14);
		m.eventList.addActionCircle(outer2,  10, 14);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 32);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/slideattack.png", 1, 1);
		Hitbox early = new Hitbox(user, 4.0f, 2.4f, 10, 65, 16, -4, 20, new SFX.MidHit());
		Hitbox late  = new Hitbox(user, 3.0f, 1.0f, 07, 90, 24, -4, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 4, 12, 12, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 3, 10);
		m.eventList.addActionCircle(late, 11, 20);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move sCharge() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/kicker/scharge.png", 6, 6);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 60));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = 	new Hitbox(user, 5.0f, 3.0f, 14, 60, 00, 4, 10, new SFX.HeavyHit(), c);
		Hitbox h2 = 	new Hitbox(user, 5.5f, 3.3f, 15, 52, 12, 4, 10, new SFX.HeavyHit(), c);
		Hitbox tipper1= new Hitbox(user, 6.0f, 3.6f, 18, 44, 24, 4, 11, new SFX.HomeRun(),  c);
		Hitbox tipper2= new Hitbox(user, 6.5f, 3.9f, 19, 38, 32, 4, 13, new SFX.HomeRun(),  c);
		Hitbox back = 	new Hitbox(user, 3.2f, 0.4f, 11, 80,-36,24, 10, new SFX.MidHit(), c);
		h1.setReflects();
		h2.setReflects();
		tipper1.setReflects();
		tipper2.setReflects();
		tipper1.setHitstunType(HitstunType.SUPER);
		tipper2.setHitstunType(HitstunType.SUPER);
		new ActionCircleGroup(Arrays.asList(h1, h2, tipper1, tipper2));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 13, 17);
		m.eventList.addActionCircle(h2, 13, 17);
		m.eventList.addActionCircle(tipper1, 12, 18);
		m.eventList.addActionCircle(tipper2, 12, 18);
		m.eventList.addActionCircle(back, 19, 25);
		return m;
	}

	public Move uCharge() {
		Move m = new Move(user, 35);
		m.setAnimation("sprites/fighters/kicker/ucharge.png", 5, 7);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 50));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		Hitbox h1 = new Hitbox(user, 5.5f, 4.8f, 17, 90, 22,-22, 26, new SFX.HeavyHit(), 	 c);
		Hitbox h2 = new Hitbox(user, 5.0f, 4.0f, 15, 87, 22, 21, 16, new SFX.HeavyHit(), 	 c);
		Hitbox h3 = new Hitbox(user, 4.5f, 3.0f, 13, 84,  0, 26, 16, new SFX.MidHit(), 	 c);
		Hitbox h4 = new Hitbox(user, 3.5f, 2.0f, 11, 70,-22, 21, 16, new SFX.MidHit(), 	 c);
		Hitbox h5 = new Hitbox(user, 2.0f, 1.0f,  9, 30,-26,-16, 16, new SFX.LightHit(),	 c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h5));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 9, 10);
		m.eventList.addActionCircle(h2, 11, 12);
		m.eventList.addActionCircle(h3, 13, 14);
		m.eventList.addActionCircle(h4, 15, 16);
		m.eventList.addActionCircle(h5, 16, 18);
		return m;
	}

	public Move dCharge() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/kicker/dcharge.png", 5, 6);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 30));
		Effect.Charge c = new Charge(3, 33, 0.02f, user, m);
		m.eventList.addCharge(user, c);
		Hitbox inner1 = new Hitbox(user, 5.0f, 1.0f, 12, 80,  20,  -7, 14, new SFX.MidHit(),		c);
		Hitbox inner2 = new Hitbox(user, 5.0f, 1.0f, 12, 80, -20,  -7, 14, new SFX.MidHit(),		c);
		Hitbox foot1  = new Hitbox(user, 4.0f, 2.5f, 14, 22,  46, -10, 10, new SFX.HeavyHit(),  	c);
		Hitbox foot2  = new Hitbox(user, 4.0f, 2.5f, 14, 22, -46, -10, 10, new SFX.HeavyHit(),  	c);
		new ActionCircleGroup(Arrays.asList(inner1, foot1));
		new ActionCircleGroup(Arrays.asList(inner2, foot2));
		m.eventList.addActionCircle(foot1, 6, 12);
		m.eventList.addActionCircle(inner1, 5, 12);
		m.eventList.addActionCircle(foot2, 18, 24);
		m.eventList.addActionCircle(inner2, 17, 24);
		return m;
	}

	/* AIR ATTACKS */

	public Move nAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/nair.png", 1, 1);
		Hitbox earlyBody = new Hitbox(user, 4, 1.5f, 10, 75, -10, 0, 20, new SFX.MidHit());
		Hitbox earlyFoot = new Hitbox(user, 4, 1.4f, 11, 90, 24, -6, 18, new SFX.MidHit());
		Hitbox lateBody = new Hitbox(user,  2, 1.1f, 7, 80, -10, 0, 16, new SFX.LightHit());
		Hitbox lateFoot = new Hitbox(user,  2, 1.1f, 8, 90, 24, -6, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(earlyBody, earlyFoot, lateBody, lateFoot));
		m.eventList.addActionCircle(earlyBody, 3, 6);
		m.eventList.addActionCircle(earlyFoot, 3, 6);
		m.eventList.addActionCircle(lateBody, 7, 22);
		m.eventList.addActionCircle(lateFoot, 7, 22);
		return m;
	}

	public Move uAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/uair.png", 3, 8);
		Hitbox h1top =	new Hitbox(user, 2.3f, 0.0f, 3, 90, 0, 20, 15, new SFX.LightHit());
		Hitbox h1bott =	new Hitbox(user, 2.5f, 0.0f, 3, 90, 0,  0, 16, new SFX.LightHit());
		Hitbox h2top = 	new Hitbox(user, 2.0f, 4.2f, 8, 90, 0, 24, 18, new SFX.MidHit());
		Hitbox h2bott =	new Hitbox(user, 2.0f, 4.2f, 8, 90, 0,  4, 20, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1top, h1bott));
		new ActionCircleGroup(Arrays.asList(h2top, h2bott));
		m.eventList.addActionCircle(h1top,  8, 13);
		m.eventList.addActionCircle(h1bott, 8, 13);
		m.eventList.addActionCircle(h2top,  15, 19);
		m.eventList.addActionCircle(h2bott, 15, 19);
		return m;
	}

	public Move dAir() {
		Move m = new Move(user, 24);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 60));
		m.setAnimation("sprites/fighters/kicker/dair.png", 3, 10);
		Hitbox sweep =	new Hitbox(user, 1.2f, 2.1f, 11, 320, 12,   0, 20, new SFX.MidHit());
		Hitbox foot =	new Hitbox(user, 2.4f, 2.6f, 14, 270,  3, -30, 17, new SFX.HeavyHit());
		new ActionCircleGroup(Arrays.asList(sweep, foot));
		m.eventList.addActionCircle(sweep, 10, 13);
		m.eventList.addActionCircle(foot, 11, 16);
		return m;
	}

	public Move fAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/fair.png", 3, 10);
		Hitbox early1 = new Hitbox(user, 3.4f, 2.7f, 13, 	48, 24, 0, 17, new SFX.HeavyHit());
		Hitbox early2 = new Hitbox(user, 3.4f, 2.7f, 13, 	48,  8, 0, 21, new SFX.HeavyHit());
		Hitbox late1 =  new Hitbox(user, 2.1f, 1.0f,  8, 	75, 22, 0, 15, new SFX.LightHit());
		Hitbox late2 =  new Hitbox(user, 2.1f, 1.0f,  8, 	75, 6,  0, 19, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 10, 15);
		m.eventList.addActionCircle(early2, 10, 15);
		m.eventList.addActionCircle(late1, 16, 23);
		m.eventList.addActionCircle(late2, 16, 23);
		return m;
	}

	public Move bAir() {
		Move m = new Move(user, 24);
		m.setHurtBox(GlobalRepo.makeHurtBoxOuter(user, 10, 60));
		m.setAnimation("sprites/fighters/kicker/bair.png", 2, 16);
		Hitbox early1 = new Hitbox(user, 2.8f, 2.9f, 12, 	72, -24,  1, 17, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 2.8f, 2.9f, 12, 	72,  -8, -6, 20, new SFX.MidHit());
		Hitbox late1 =  new Hitbox(user, 2.0f, 1.5f,  9, 	30, -22,  0, 12, new SFX.LightHit());
		Hitbox late2 =  new Hitbox(user, 2.0f, 1.5f,  9, 	30,  -6, -3, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 4, 12);
		m.eventList.addActionCircle(early2, 4, 12);
		m.eventList.addActionCircle(late1, 13, 24);
		m.eventList.addActionCircle(late2, 13, 24);
		return m;
	}

	/* SPECIAL ATTACKS */

	int start = 15;
	int end = start + 25;
	public Move uSpecial() {
		Move m = new Move(user, end + 1);
		m.setAnimation("sprites/fighters/kicker/uspecial.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5, 2, 10, 90, 0, 0, 24, new SFX.MidHit());
		h1.setMovesAheadMod(2);
		m.eventList.addConstantVelocity(user, 0, start, ConstantVelocity.noChange, 0);
		m.eventList.addUseSpecial(user, 11, -1);
		m.eventList.addConstantAngledVelocity(user, start, end, 12);
		m.eventList.addActionCircle(h1, start, end);
		m.eventList.addVelocityChange(user, end, 0, 0);
		m.setHelpless();
		return m;
	}

	public Move dSpecial() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/dspecial.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addArmor(m, 4, 28, 4);
		m.eventList.addUseSpecial(user, 11, -4);
		m.eventList.addVelocityChange(user, 16, -7, Action.ChangeVelocity.noChange);
		m.eventList.addProjectile(user, Projectile.Rocket.class, 12);
		return m;
	}

	public Move sSpecial() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/kicker/grab.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addUseSpecial(user, 9, -2);
		m.eventList.addProjectile(user, Projectile.Grenade.class, 10);
		return m;
	}

	public Move nSpecial() {
		Move m = new Move(user, 15);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 24, 60));
		m.setAnimation("sprites/fighters/kicker/nspecial.png", 1, 1);
		m.setContinueOnLanding();
		m.eventList.addUseSpecial(user, 5, -1);
		m.eventList.addProjectile(user, Projectile.Stunner.class, 6);
		return m;
	}

	/* THROWS */

	public Move fThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/fthrow.png", 4, 6);
		m.dontTurn();
		Hitbox thro  = new Hitbox(user, 3.2f, 0.0f,   1, 85, 8, 0, 20, new SFX.None());
		Hitbox swing1 = new Hitbox(user, 4.0f, 2.4f, 14, 30, 8, 0, 20, new SFX.MeatyHit());
		Hitbox swing2 = new Hitbox(user, 4.0f, 2.4f, 14, 30, 28, 0, 20, new SFX.MeatyHit());
		thro.setNoReverse();
		swing1.setNoReverse();
		swing2.setNoReverse();
		swing1.setHitstunType(Fighter.HitstunType.SUPER);
		swing2.setHitstunType(Fighter.HitstunType.SUPER);
		new ActionCircleGroup(Arrays.asList(swing1, swing2));
		m.eventList.addActionCircle(thro, 0, 1);
		m.eventList.addActionCircle(swing1, 12, 18);
		m.eventList.addActionCircle(swing2, 12, 18);
		return m;
	}

	public Move bThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/bthrow.png", 3, 8);
		m.dontTurn();
		Hitbox thro = new Hitbox(user, 5.5f, 0, 2, 160, 8, 0, 30, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 3, 2.4f, 12, 150, -24, 0, 30, new SFX.MeatyHit());
		thro.setNoReverse();
		kick.setNoReverse();
		kick.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(thro, 0, 4);
		m.eventList.addActionCircle(kick, 8, 16);
		return m;
	}

	public Move uThrow(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5, 1.6f, 12, 90, 8, 0, 30, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/kicker/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox down = new Hitbox(user, 3, 0, 4, 270, 8, 0, 30, new SFX.LightHit());
		Hitbox up = new Hitbox(user, 3, 1.2f, 8, 82, 8, -8, 30, new SFX.MidHit());
		down.setNoReverse();
		up.setNoReverse();
		m.eventList.addActionCircle(down, 0, 1);
		m.eventList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/fair.png", 3, 6);
		Hitbox h1 = new Hitbox(user, 3, 2.8f, 14, 30, 16, 0, 30, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/kicker/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 3, 2.4f, 14, 150, 24, -12, 20, new SFX.MidHit());
		h1.setNoReverse();
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addVelocityChange(user, 5, 4, 4);
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 8);
		m.setAnimation("sprites/fighters/kicker/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 7, 0.1f, 13, 90, 8, 0, 30, new SFX.MidHit());
		h1.setNoReverse();
		m.eventList.addActionCircle(h1, 0, 4);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 6);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/kicker/airgrab.png", 1, 1);
		Hitbox down = new Hitbox(user, 3.4f, 0, 5, 270, 8, 0, 30, new SFX.LightHit());
		down.setNoReverse();
		m.eventList.addActionCircle(down, 0, 4);
		m.eventList.addConstantVelocity(user, 0, 20, 0, 0);
		m.eventList.addUseSpecial(user, 19, -1);
		m.eventList.addProjectile(user, Projectile.DownwardGrenade.class, 20);
		m.eventList.addVelocityChange(user, 21, -3, 3);
		return m;
	}

	/* GRABS */

	public Move grab() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/kicker/grab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 18, 10, 16);
		m.eventList.addActionCircle(g1, 4, 8);
		return m;
	}

	public Move dashGrab() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/kicker/dashgrab.png", 1, 1);
		m.eventList.addVelocityChange(user, 4, 6, Action.ChangeVelocity.noChange);
		Grabbox g1 = new Grabbox(user, 18, 10, 16);
		m.eventList.addActionCircle(g1, 4, 8);
		return m;
	}

	public Move airGrab() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/kicker/airgrab.png", 1, 1);
		Grabbox g1 = new Grabbox(user, 14, 0, 24);
		m.eventList.addActionCircle(g1, 4, 8);
		return m;
	}
	
	/* DODGES */
	
	protected float boost = 10.6f;
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
	
	public Move getUpAttack() {
		Move m = new Move(user, 33);
		m.setAnimation("sprites/fighters/kicker/getupattack.png", 3, 11);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 30, 30));
		m.eventList.addInvincible(user, 0, 10);
		Hitbox front1 = new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI,  20, -8, 16, new SFX.LightHit());
		Hitbox front2 = new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI,  44, -8, 12, new SFX.LightHit());
		Hitbox back1 =  new Hitbox(user, 5.0f, 0.5f, 12, Hitbox.SAMURAI, -20, -8, 16, new SFX.LightHit());
		Hitbox back2 =  new Hitbox(user, 5.0f, 0.5f, 14, Hitbox.SAMURAI, -44, -8, 12, new SFX.LightHit());
		m.eventList.addActionCircle(front1, 11, 16);
		m.eventList.addActionCircle(front2, 11, 16);
		m.eventList.addActionCircle(back1, 22, 27);
		m.eventList.addActionCircle(back2, 22, 27);
		return m;
	}
	
	private final int rollLength = 27;
	private final float rollHeight = 3;
	private final float rollSpeed = -9;
	private final int rollInvinc = 21;
	public Move rollForward(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		user.flip();
		m.setAnimation("sprites/fighters/kicker/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}
	
	public Move rollBack(){
		Move m = new Move(user, rollLength);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/airdodgeb.png", 1, 1);
		m.eventList.addConstantVelocity(user, 1, 27, rollSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, rollHeight);
		m.eventList.addInvincible(user, 1, rollInvinc);
		return m;
	}
	
	/* MISC */
	
	public Move land(){
		Move m = new Move(user, 3);
		m.dontTurn();
		m.setAnimation("sprites/fighters/kicker/land.png", 1, 1);
		return m;
	}
	
	public Move skid(){
		Move m = new Move(user, 10);
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
