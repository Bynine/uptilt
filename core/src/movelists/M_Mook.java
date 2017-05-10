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
import entities.Graphic;

public class M_Mook extends MoveList {

	public M_Mook(Fighter user) {
		super(user);
	}
	
	public Move nWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/mook/nweak.png", 2, 12);
		Hitbox h1 = new Hitbox(user, 0.5f, 1.0f, 4, Hitbox.SAMURAI, 16, 2, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 12, 14);
		return m;
	}
	
	private final int uWeakLength = 32;
	public Move uWeak() {
		Move m = new Move(user, uWeakLength);
		m.setAnimation("sprites/fighters/mook/uweak.png", 2, uWeakLength/2);
		Hitbox h1 = new Hitbox(user, 2.0f, 1.8f, 7, 80, 4, 16, 18, new SFX.LightHit());
		m.eventList.addActionCircle(h1, uWeakLength/2, (uWeakLength/2) + 3);
		m.eventList.addVelocityChange(user, uWeakLength/2, 0, 2);
		return m;
	}
	
	private final int sWeakLength = 32;
	public Move sWeak() {
		Move m = new Move(user, sWeakLength);
		m.setAnimation("sprites/fighters/mook/sweak.png", 2, sWeakLength/2);
		Hitbox h1 = new Hitbox(user, 1.0f, 2.1f, 6, Hitbox.SAMURAI, 26, 4, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, sWeakLength/2, (sWeakLength/2) + 3);
		return m;
	}
	
	private final int dWeakLength = 40;
	public Move dWeak() {
		Move m = new Move(user, dWeakLength);
		m.setAnimation("sprites/fighters/mook/dweak.png", 2, dWeakLength/2);
		Hitbox h1 = new Hitbox(user, 2.1f, 1.0f, 5, Hitbox.SAMURAI, 20, 0, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, dWeakLength/2, (dWeakLength/2) + 3);
		return m;
	}
	
	public Move slide() {
		Move m = new Move(user, 60);
		m.setContinueOnLanding();
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/mook/slide.png", 4, 15);
		Hitbox early = new Hitbox(user, 3, 2.5f, 9, 40, 16, -12, 24, new SFX.MidHit());
		Hitbox late = new Hitbox(user, 2, 2, 6, 40, 16, -12, 24, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(early, late));
		m.eventList.addConstantVelocity(user, 0, 14, 0, Action.ChangeVelocity.noChange);
		m.eventList.addVelocityChange(user, 14, 5, Action.ChangeVelocity.noChange);
		m.eventList.addConstantVelocity(user, 16, 30, 3, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(early, 16, 20);
		m.eventList.addActionCircle(late, 21, 30);
		return m;
	}
	
	public Move nAir() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/mook/nair.png", 4, 2);
		Hitbox h1 = new Hitbox(user, 1.6f, 1.6f, 6, Hitbox.SAMURAI, 0, 0, 24, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 8, 24);
		return m;
	}
	
	public Move fAir() {
		Move m = new Move(user, 40);
		m.setAnimation("sprites/fighters/mook/sweak.png", 2, 16);
		Hitbox h1 = new Hitbox(user, 1.5f, 2.6f, 10, Hitbox.SAMURAI, 24, 0, 16, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 16, 19);
		return m;
	}
	
	public Move bAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/mook/bair.png", 3, 10);
		Hitbox h1 = new Hitbox(user, 1.5f, 2.1f, 9, Hitbox.SAMURAI, -20, 0, 20, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 11, 15);
		return m;
	}
	
	public Move uAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/mook/uair.png", 3, 10);
		Hitbox h1 = new Hitbox(user, 1.5f, 2.1f, 9, 90, 4, 16, 20, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 11, 15);
		return m;
	}
	
	public Move dAir() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/mook/dair.png", 3, 10);
		Hitbox h1 = new Hitbox(user, 1.5f, 2.1f, 9, 270, 0, -20, 20, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 11, 15);
		return m;
	}
	
	int spinFrames = 14;
	public Move sCharge() {
		int sChargeFrameSpeed = 11;
		Move m = new Move(user, spinFrames * sChargeFrameSpeed);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/mook/scharge.png", spinFrames, sChargeFrameSpeed);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox h1 = new Hitbox(user, 3, 2.6f, 12, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h2 = new Hitbox(user, 2.5f, 2.4f, 11, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h3 = new Hitbox(user, 2f, 2.2f, 10, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h4 = new Hitbox(user, 1.5f, 2.0f, 9, Hitbox.SAMURAI, 26, 4, 16, new SFX.LightHit(), c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4));
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChangeCharge(user, (int) (sChargeFrameSpeed * 1.5), 6, Action.ChangeVelocity.noChange, c);
		m.eventList.addActionCircle(h1, sChargeFrameSpeed * 2, (int) (sChargeFrameSpeed * 2.5) );
		m.eventList.addActionCircle(h2, sChargeFrameSpeed * 4, (int) (sChargeFrameSpeed * 4.5) );
		m.eventList.addActionCircle(h3, sChargeFrameSpeed * 6, (int) (sChargeFrameSpeed * 6.5) );
		m.eventList.addActionCircle(h4, sChargeFrameSpeed * 8, (int) (sChargeFrameSpeed * 8.5) );
		return m;
	}
	
	public Move uCharge() {
		Move m = new Move(user, 60);
		m.setAnimation("sprites/fighters/mook/ucharge.png", 4, 15);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox h1 = new Hitbox(user, 3, 2.7f, 12, 77, 32, 8, 14, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, 30, 36);
		return m;
	}
	
	public Move dCharge() {
		int dChargeFrameSpeed = 11;
		Move m = new Move(user, spinFrames * dChargeFrameSpeed);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/mook/dcharge.png", spinFrames, dChargeFrameSpeed);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox h1 = new Hitbox(user, 2, 2.7f, 11, Hitbox.SAMURAI, 24, 4, 12, new SFX.MidHit(), c);
		Hitbox h1b = new Hitbox(user, 2, 2.7f, 11, Hitbox.SAMURAI, -24, 4, 12, new SFX.MidHit(), c);
		Hitbox h2 = new Hitbox(user, 1.5f, 2.6f, 10, Hitbox.SAMURAI, 24, 4, 12, new SFX.MidHit(), c);
		Hitbox h2b = new Hitbox(user, 1.5f, 2.6f, 10, Hitbox.SAMURAI, -24, 4, 12, new SFX.MidHit(), c);
		Hitbox h3 = new Hitbox(user, 1f, 2.2f, 10, Hitbox.SAMURAI, 24, 4, 12, new SFX.MidHit(), c);
		Hitbox h3b = new Hitbox(user, 1f, 2.2f, 10, Hitbox.SAMURAI, -24, 4, 12, new SFX.MidHit(), c);
		Hitbox h4 = new Hitbox(user, 0.5f, 2f, 9, Hitbox.SAMURAI, 24, 4, 12, new SFX.LightHit(), c);
		Hitbox h4b = new Hitbox(user, 0.5f, 2f, 9, Hitbox.SAMURAI, -24, 4, 12, new SFX.LightHit(), c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4, h1b, h2b, h3b, h4b));
		m.eventList.addCharge(user, c);
		m.eventList.addActionCircle(h1, dChargeFrameSpeed * 2, (int) (dChargeFrameSpeed * 2.5) );
		m.eventList.addActionCircle(h1b, dChargeFrameSpeed * 2, (int) (dChargeFrameSpeed * 2.5) );
		m.eventList.addActionCircle(h2, dChargeFrameSpeed * 4, (int) (dChargeFrameSpeed * 4.5) );
		m.eventList.addActionCircle(h2b, dChargeFrameSpeed * 4, (int) (dChargeFrameSpeed * 4.5) );
		m.eventList.addActionCircle(h3, dChargeFrameSpeed * 6, (int) (dChargeFrameSpeed * 6.5) );
		m.eventList.addActionCircle(h3b, dChargeFrameSpeed * 6, (int) (dChargeFrameSpeed * 6.5) );
		m.eventList.addActionCircle(h4, dChargeFrameSpeed * 8, (int) (dChargeFrameSpeed * 8.5) );
		m.eventList.addActionCircle(h4b, dChargeFrameSpeed * 8, (int) (dChargeFrameSpeed * 8.5) );
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 80);
		m.setHelpless();
		m.eventList.addGraphic(user, 6, 70, new Graphic.UFO(user, user.getPosition().x, user.getPosition().y));
		m.eventList.addConstantVelocity(user, 6, 70, Action.ChangeVelocity.noChange, 4);
		return m;
	}
	
	public Move rollForward() {
		Move m = new Move(user, 48);
		user.flip();
		m.setAnimation("sprites/fighters/mook/sgetup.png", 4, 12);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move rollBack() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/mook/sgetup.png", 4, 12);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/mook/ngetup.png", 1, 1);
		return m;
	}
	
	public Move getUpAttack() {
		return dodge();
	}

	public Move land() {
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/mook/crouch.png", 1, 1);
		return m;
	}

	public Move skid() {
		return new Move(user, 16);
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
