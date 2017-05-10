package movelists;

import java.util.Arrays;

import main.GlobalRepo;
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
//import entities.Projectile; HAHAHAHAHAHA
import entities.Entity.Direction;

public class M_Frog extends M_Kicker {

	public M_Frog(Fighter user) {
		super(user);
	}

	public Move nWeak() {
		Move m = new Move(user, 15);
		m.setHurtBox(GlobalRepo.makeHurtBox(user, 40, 60));
		m.setAnimation("sprites/fighters/frog/nweak.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 3.4f, 0.8f, 7, 64, 28, 6, 16, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 4, 11);
		return m;
	}

	public Move uWeak() {
		Move m = new Move(user, 27);
		m.setAnimation("sprites/fighters/frog/uweak.png", 3, 10);
		Hitbox sweep = new Hitbox(user, 4.0f, 0.0f, 5, 85, 16,  4, 17, new SFX.LightHit());
		Hitbox punch = new Hitbox(user, 3.0f, 3.0f, 8, 85, 16, 34, 15, new SFX.MidHit());
		Hitbox stale = new Hitbox(user, 2.0f, 1.0f, 4, 85, 16, 34, 12, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(punch, stale));
		m.eventList.addActionCircle(sweep, 9, 16);
		m.eventList.addActionCircle(punch, 9, 16);
		m.eventList.addActionCircle(stale,17, 23);
		return m;
	}

	public Move dWeak() {
		Move m = new Move(user, 28);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 40, 40));
		m.setAnimation("sprites/fighters/frog/dweak.png", 2, 10);
		Hitbox innerE = new Hitbox(user, 5.1f, 0.8f, 11, 72, 15, -5, 15, new SFX.MidHit());
		Hitbox outerE = new Hitbox(user, 5.1f, 0.8f, 11, 81, 34, -4, 14, new SFX.MidHit());
		Hitbox innerL = new Hitbox(user, 3.8f, 0.6f,  7, 72, 15, -5, 15, new SFX.LightHit());
		Hitbox outerL = new Hitbox(user, 3.8f, 0.6f,  7, 81, 34, -4, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(innerE, outerE, innerL, outerL));
		m.eventList.addActionCircle(innerE, 10, 15);
		m.eventList.addActionCircle(outerE, 10, 15);
		m.eventList.addActionCircle(innerL, 16, 20);
		m.eventList.addActionCircle(outerL, 16, 20);
		return m;
	}

	public Move sWeak() {
		Move m = new Move(user, 24);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 40, 60));
		m.setAnimation("sprites/fighters/frog/sweak.png", 2, 12);
		Hitbox vnner = new Hitbox(user, 3.0f, 2.3f, 10, Hitbox.SAMURAI,  0, 2, 16, new SFX.MidHit());
		Hitbox inner = new Hitbox(user, 3.1f, 2.4f, 10, Hitbox.SAMURAI, 16, 2, 12, new SFX.MidHit());
		Hitbox outer = new Hitbox(user, 3.2f, 2.5f, 11, Hitbox.SAMURAI, 34, 2, 12, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(vnner, inner, outer));
		m.eventList.addActionCircle(vnner, 11, 18);
		m.eventList.addActionCircle(inner, 11, 18);
		m.eventList.addActionCircle(outer, 11, 18);
		return m;
	}

	public Move slide() { 
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/nweak.png", 1, 1);
		Hitbox early = new Hitbox(user, 6.0f, 3.2f, 14, 40, 24, 4, 24, new SFX.HeavyHit());
		Hitbox late =  new Hitbox(user, 4.0f, 1.7f, 9, 110, 22, 4, 20, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 4, 24, 8, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 3, 16);
		m.eventList.addActionCircle(late, 17, 30);
		return m;
	}

	/* CHARGE ATTACKS */

	public Move sCharge() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/frog/scharge.png", 6, 5);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 50));
		Effect.Charge c = new Charge(3, 33, 0.025f, user, m);
		int bkb = 6, kbg = 4, dam = 21, ang = Hitbox.SAMURAI;
		Hitbox h1 = new Hitbox(user, bkb, kbg, dam, ang, 10, 4, 16, new SFX.MeatyHit(), c);
		Hitbox h2 = new Hitbox(user, bkb, kbg, dam, ang, 24, 4, 16, new SFX.MeatyHit(), c);
		Hitbox h3 = new Hitbox(user, bkb, kbg, dam, ang, 33, 4, 16, new SFX.MeatyHit(), c);
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
		m.setAnimation("sprites/fighters/frog/uweak.png", 3, 13);
		Effect.Charge c = new Charge(3, 33, 0.025f, user, m);
		Hitbox sweep = new Hitbox(user, 4.0f, 4.0f, 10, 85, 16,  4, 17, new SFX.MidHit(), c);
		Hitbox punch = new Hitbox(user, 5.0f, 5.0f, 16, 85, 16, 34, 15, new SFX.MeatyHit(), c);
		Hitbox stale = new Hitbox(user, 4.0f, 2.0f, 10, 85, 16, 34, 12, new SFX.MidHit(), c);
		new ActionCircleGroup(Arrays.asList(punch, stale));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(sweep, 13, 16);
		m.eventList.addActionCircle(punch, 13, 16);
		m.eventList.addActionCircle(stale, 17, 23);
		return m;
	}

	public Move dCharge() {
		Move m = new Move(user, 42);
		m.setAnimation("sprites/fighters/frog/dcharge.png", 4, 8);
		Effect.Charge c = new Charge(3, 33, 0.025f, user, m);
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
		Hitbox earlyBody =	new Hitbox(user, 4.0f, 1.5f, 11, 75, -10, 0, 20, new SFX.MidHit());
		Hitbox earlyFoot =	new Hitbox(user, 4.0f, 1.4f, 12, 90,  24, -6, 12, new SFX.MidHit());
		Hitbox lateBody =	new Hitbox(user, 2.0f, 1.1f,  8, 80, -10, 0, 16, new SFX.LightHit());
		Hitbox lateFoot =	new Hitbox(user, 2.0f, 1.1f,  9, 90,  24, -6, 8, new SFX.LightHit());
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
		Hitbox early1 = new Hitbox(user, 2.5f, 3.6f, 16, 270, 0,   2, 24, new SFX.MeatyHit());
		Hitbox early2 = new Hitbox(user, 2.5f, 3.6f, 16, 270, 0, -28, 20, new SFX.MeatyHit());
		Hitbox late1 =  new Hitbox(user, 1.0f, 2.0f, 10, 270, 0,   2, 20, new SFX.MidHit());
		Hitbox late2 =  new Hitbox(user, 1.0f, 2.0f, 10, 270, 0, -28, 16, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		early1.setHitstunType(HitstunType.SUPER);
		early2.setHitstunType(HitstunType.SUPER);
		m.eventList.addActionCircle(early1, 11, 15);
		m.eventList.addActionCircle(early2, 11, 15);
		m.eventList.addActionCircle(late1, 16, 24);
		m.eventList.addActionCircle(late2, 16, 24);
		return m;
	}

	public Move fAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/fair.png", 2, 13);
		m.setHurtBox(GlobalRepo.makeHurtBoxInner(user, 30, 50));
		Hitbox early = new Hitbox(user, 4.0f, 3.7f, 18, 30, 14, -2, 24, new SFX.MeatyHit());
		Hitbox late =  new Hitbox(user, 2.0f, 1.0f,  4,  0, 24, -2, 16, new SFX.LightHit());
		early.setProperty(Hitbox.Property.ELECTRIC);
		early.setHitstunType(HitstunType.SUPER);
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addActionCircle(early, 12, 17);
		m.eventList.addActionCircle(late, 18, 24);
		return m;
	}

	public Move bAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/frog/bair.png", 3, 10);
		m.setHurtBox(GlobalRepo.makeHurtBoxOuter(user, 40, 30));
		Hitbox early1 = new Hitbox(user, 3.2f, 2.8f, 14, 50, -24, 4, 18, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 3.2f, 2.8f, 14, 50,  -8, 4, 22, new SFX.MidHit());
		Hitbox late1 =  new Hitbox(user, 2.8f, 1.4f, 10, 75, -22, 2, 12, new SFX.LightHit());
		Hitbox late2 =  new Hitbox(user, 2.8f, 1.4f, 10, 75,  -6, 2, 14, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 10, 20);
		m.eventList.addActionCircle(early2, 10, 20);
		m.eventList.addActionCircle(late1,  20, 28);
		m.eventList.addActionCircle(late2,  20, 28);
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
		m.eventList.addUseSpecial(user, 7, -1);
		m.eventList.addActionCircle(h1, 8, 10);
		m.eventList.addVelocityChange(user, 10, Action.ChangeVelocity.noChange, 20);
		return m;
	}

	private Move aerialUSpecial(){
		Move m = new Move(user, 30);
		m.setHelpless();
		Hitbox h1 = new Hitbox(user, 6, 3, 20, 270, 0, -48, 16, new SFX.MeatyHit());
		m.setAnimation("sprites/fighters/frog/fjump.png", 1, 1);
		m.eventList.addUseSpecial(user, 4, -1);
		m.eventList.addVelocityChange(user, 5, Action.ChangeVelocity.noChange, 17);
		m.eventList.addActionCircle(h1, 4, 6);
		return m;
	}

	public Move dSpecial() {
		Move m = new Move(user, 40);
		return m;
	}

	public Move sSpecial() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/frog/sspecial.png", 4, 12);
		Hitbox earlyfoot = new Hitbox(user, 7.4f, 3.2f, 20, Hitbox.SAMURAI, 24, -8, 12, new SFX.HeavyHit());
		Hitbox earlybody = new Hitbox(user, 7.4f, 3.2f, 16, Hitbox.SAMURAI, 10, -4, 20, new SFX.HeavyHit());
		Hitbox latebody = new Hitbox(user, 	4.2f, 1.1f, 12, 80, 12, -4, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(earlyfoot, earlybody, latebody));
		
		m.eventList.addUseSpecial(user, 6, -2);
		if (user.isGrounded()) m.eventList.addVelocityChange(user, 10, Action.ChangeVelocity.noChange, 8);
		else m.eventList.addVelocityChange(user, 12, Action.ChangeVelocity.noChange, 6);
		m.eventList.addConstantVelocity(user, 12, 16, 14, Action.ChangeVelocity.noChange);
		
		m.eventList.addActionCircle(earlyfoot, 13, 16);
		m.eventList.addActionCircle(earlybody, 13, 24);
		m.eventList.addActionCircle(latebody, 25, 32);
		return m;
	}

	public Move nSpecial() {
		Move m = new Move(user, 75);
		m.setAnimation("sprites/fighters/frog/fair.png", 2, 50);
		m.setContinueOnLanding();
		Hitbox inner = new Hitbox(user, 15.0f, 6.0f, 32, Hitbox.SAMURAI, 15, 0, 30, new SFX.HeavyHit());
		Hitbox middl = new Hitbox(user, 11.0f, 4.5f, 24, Hitbox.SAMURAI, 30, 0, 60, new SFX.HeavyHit());
		Hitbox outer = new Hitbox(user,  7.0f, 2.0f, 16, Hitbox.SAMURAI, 45, 0, 90, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(inner, middl, outer));
		m.eventList.addArmor(m, 25, 50, 10);
		m.eventList.addUseSpecial(user, 25, -8);
		m.eventList.addArmor(m, 51, 60, 999);
		m.eventList.addSound(new SFX.Explode(), 50);
		m.eventList.addActionCircle(inner, 48, 56);
		m.eventList.addActionCircle(middl, 49, 54);
		m.eventList.addActionCircle(outer, 50, 52);
		return m;
	}

	/* THROWS */

	public Move fThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/sweak.png", 2, 9);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 5.3f, 1.5f, 18, 30, 8, 0, 30, new SFX.MeatyHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/bthrow.png", 3, 8);
		m.dontTurn();
		Hitbox thro = new Hitbox(user, 5.5f, 0.0f, 2, 160, 8, 0, 30, new SFX.LightHit());
		Hitbox kick = new Hitbox(user, 5.2f, 1.5f, 15, 30, -24, 0, 30, new SFX.MeatyHit());
		kick.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(thro, 0, 4);
		m.eventList.addActionCircle(kick, 8, 16);
		return m;
	}

	public Move uThrow(){
		Move m = new Move(user, 14);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/uthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 6, 1.4f, 16, 82, 8, 0, 30, new SFX.MidHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move dThrow(){
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/frog/dthrow.png", 1, 1);
		m.dontTurn();
		Hitbox down =	new Hitbox(user, 3.0f, 0.0f, 4, 290,  8,  0, 30, new SFX.LightHit());
		Hitbox up =		new Hitbox(user, 4.0f, 0.4f, 8,  64, 16, -8, 30, new SFX.MeatyHit());
		m.eventList.addActionCircle(down, 0, 1);
		m.eventList.addActionCircle(up, 4, 8);
		return m;
	}

	public Move fAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/fair.png", 2, 9);
		Hitbox h1 = new Hitbox(user, 4.8f, 4.8f, 21, 30, 16, 0, 30, new SFX.MeatyHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		return m;
	}

	public Move bAirThrow(){
		Move m = new Move(user, 18);
		m.setAnimation("sprites/fighters/frog/fjump.png", 1, 1);
		m.dontTurn();
		Hitbox h1 = new Hitbox(user, 5.1f, 2.4f, 14, 150, 24, -12, 20, new SFX.MidHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addVelocityChange(user, 5, 5, 5);
		m.eventList.addActionCircle(h1, 2, 5);
		return m;
	}

	public Move uAirThrow(){
		Move m = new Move(user, 14);
		m.setAnimation("sprites/fighters/frog/uairthrow.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5, 2.7f, 13, 80, 8, 0, 30, new SFX.MeatyHit());
		h1.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(h1, 0, 4);
		m.eventList.addConstantVelocity(user, 0, 10, 0, 0);
		return m;
	}

	public Move dAirThrow(){
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/frog/dair.png", 3, 8);
		Hitbox down = new Hitbox(user, 4.0f, 4.0f, 18, 270, 8, 0, 30, new SFX.MeatyHit());
		down.setHitstunType(Fighter.HitstunType.SUPER);
		m.eventList.addActionCircle(down, 0, 4);
		m.eventList.addVelocityChange(user, 0, 0, 6);
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
		m.setAnimation("sprites/fighters/frog/grab.png", 1, 1);
		m.eventList.addConstantVelocity(user, 4, 12, 8, Action.ChangeVelocity.noChange);
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
		Move m = new Move(user, 30);
		m.dontTurn();
		m.setAnimation("sprites/fighters/frog/dodge.png", 1, 1);
		m.eventList.addInvincible(user, 1, 24);
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
