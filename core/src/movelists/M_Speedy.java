package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Effect;
import moves.Hitbox;
import moves.Move;
import moves.Effect.Charge;
import entities.Fighter;
import entities.Projectile;

public class M_Speedy extends MoveList {

	public M_Speedy(Fighter user) {
		super(user);
	}
	
	protected Class<? extends Projectile> getAcidSpit(){
		return Projectile.AcidSpit.class;
	}
	
	public Move nWeak() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/speedy/nweak.png", 2, 14);
		m.eventList.addProjectile(user, getAcidSpit(), 14);
		return m;
	}
	
	public Move uWeak() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/speedy/uweak.png", 3, 10);
		Hitbox h1 = new Hitbox(user, 3, 1.2f, 8, 83, 16, 32, 14, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 3, 1.2f, 8, 97, -16, 32, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.eventList.addActionCircle(h1, 10, 11);
		m.eventList.addActionCircle(h2, 10, 11);
		return m;
	}
	
	public Move sWeak() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/speedy/nweak.png", 2, 4);
		Hitbox bite1 = new Hitbox(user, 3, 0, 2, 90, 26, -4, 12, new SFX.MidHit());
		Hitbox bite2 = new Hitbox(user, 2, 0, 2, 90, 26, 12, 12, new SFX.MidHit());
		Hitbox bite3 = new Hitbox(user, 1, 2, 5, Hitbox.SAMURAI, 26, 4, 12, new SFX.MidHit());
		m.eventList.addActionCircle(bite1, 11, 12);
		m.eventList.addActionCircle(bite2, 15, 16);
		m.eventList.addActionCircle(bite3, 21, 22);
		return m;
	}
	
	public Move dWeak() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/speedy/dweak.png", 2, 14);
		Hitbox h1 = new Hitbox(user, 3.0f, 1.5f, 5, Hitbox.SAMURAI, 30, 0, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 14, 15);
		return m;
	}
	
	protected int slideSpeed = 11;
	protected int slideDur = 60;
	public Move slide() {
		Move m = new Move(user, slideDur);
		m.setContinueOnLanding();
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		Hitbox h1 = new Hitbox(user, 5.0f, 2.1f, 10, 74, 20, 0, 24, new SFX.MidHit());
		m.eventList.addConstantVelocity(user, 0, 21, slideSpeed, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(h1, 4, 21);
		return m;
	}
	
	public Move nAir() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/speedy/nair.png", 5, 4);
		int nDisp = 20;
		int nSize = 10;
		Hitbox h1 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAI, -nDisp, -nDisp, nSize, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAI,  nDisp, -nDisp, nSize, new SFX.MidHit());
		Hitbox h3 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAI, -nDisp,  nDisp, nSize, new SFX.MidHit());
		Hitbox h4 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAI,  nDisp,  nDisp, nSize, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4));
		m.eventList.addActionCircle(h1, 9, 12);
		m.eventList.addActionCircle(h2, 9, 12);
		m.eventList.addActionCircle(h3, 9, 12);
		m.eventList.addActionCircle(h4, 9, 12);
		return m;
	}
	
	public Move fAir() {
		Move m = new Move(user, 50);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 4);
		Hitbox rotate = 	new Hitbox(user, 1, 0, 1, 80, 32, 0, 18, new SFX.LightHit());
		rotate.setRefresh(3);
		Hitbox finisher = 	new Hitbox(user, 4, 1.5f, 7, 35, 20, 0, 24, new SFX.MidHit());
		m.eventList.addConstantVelocity(user, 10, 30, 0, -user.getGravity());
		m.eventList.addActionCircle(rotate, 20, 35);
		m.eventList.addActionCircle(finisher, 36, 40);
		return m;
	}
	
	public Move bAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/wallslide.png", 3, 24);
		Hitbox early1 = new Hitbox(user, 1.5f, 2.1f, 11, 30, -20,  10, 14, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 1.5f, 2.1f, 11, 30, -20, -10, 14, new SFX.MidHit());
		Hitbox late1 = new Hitbox(user, 1.2f, 0.5f, 6, Hitbox.SAMURAI, -18,  7, 10, new SFX.LightHit());
		Hitbox late2 = new Hitbox(user, 1.2f, 0.5f, 6, Hitbox.SAMURAI, -18, -7, 10, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(early1, early2, late1, late2));
		m.eventList.addActionCircle(early1, 9, 10);
		m.eventList.addActionCircle(early2, 9, 10);
		m.eventList.addActionCircle(late1, 11, 16);
		m.eventList.addActionCircle(late2, 11, 16);
		return m;
	}
	
	public Move uAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/uweak.png", 3, 8);
		Hitbox h1 = new Hitbox(user, 3, 1.2f, 8, 83, 16, 32, 14, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 3, 1.2f, 8, 97, -16, 32, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.eventList.addActionCircle(h1, 8, 10);
		m.eventList.addActionCircle(h2, 8, 10);
		return m;
	}
	
	public Move dAir() {
		Move m = new Move(user, 50);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/dair.png", 2, 4);
		Hitbox drill = new Hitbox(user, 2, 2, 3, 270, 0, -16, 18, new SFX.LightHit());
		drill.setRefresh(4);
		m.eventList.addConstantVelocity(user, 0, 9, 0, 0);
		m.eventList.addConstantVelocity(user, 10, 40, 0, -8);
		m.eventList.addActionCircle(drill, 10, 30);
		return m;
	}
	
	protected int sChargeSpeed = 10;
	public Move sCharge() {
		Move m = new Move(user, 50);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 7);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox h1 = new Hitbox(user, 5, 2, 7, 74, 20, 0, 24, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 4);
		m.eventList.addConstantVelocity(user, 4, 9, 0, -user.getGravity());
		m.eventList.addConstantVelocity(user, 10, 30, sChargeSpeed, -user.getGravity());
		m.eventList.addActionCircle(h1, 10, 30);
		return m;
	}
	
	public Move uCharge() {
		Move m = new Move(user, 56);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/ucharge.png", 7, 8);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox finisher = 	new Hitbox(user, 5.0f, 2.0f, 12, 80, 0, 0, 20, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChangeCharge(user, 14, 1, 7, c);
		m.eventList.addActionCircle(finisher, 14, 40);
		return m;
	}
	
	public Move dCharge() {
		Move m = new Move(user, 70);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/dcharge.png", 4, 5);
		Effect.Charge c = new Charge(3, 33, 0.01f, user, m);
		Hitbox outer1	= new Hitbox(user, 6.0f, 0.0f, 2,160,  34, 0, 16, new SFX.LightHit(), c);
		Hitbox outer2	= new Hitbox(user, 6.0f, 0.0f, 2,160, -34, 0, 16, new SFX.LightHit(), c);
		Hitbox inner	= new Hitbox(user,10.0f, 1.2f, 8, 90,	0, 0, 28, new SFX.MidHit(), c);
		outer1.setRefresh(5);
		outer2.setRefresh(5);
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(outer1, 10, 30);
		m.eventList.addActionCircle(outer2, 10, 30);
		m.eventList.addActionCircle(inner, 10, 30);
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 16);
		m.setHelpless();
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, 12);
		return m;
	}
	
	public Move rollForward() {
		Move m = new Move(user, 36);
		user.flip();
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, -7, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move rollBack() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, -7, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move dodge() {
		Move m = new Move(user, 26);
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		return m;
	}
	
	public Move getUpAttack() {
		return dodge();
	}

	public Move land() {
		Move m = new Move(user, 7);
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		return m;
	}

	public Move skid() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		return m;
	}

	public Move taunt() {
		return new Move(user, 60);
	}
	
	/* UNUSED */
	
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

	public Move airDodge() {
		return new Move(user, 0);
	}

}
