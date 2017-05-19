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

	@Override
	public Move nWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/mook/nweak.png", 2, 12);
		Hitbox h1 = new Hitbox(user, 0.5f, 1.0f, 4, Hitbox.SAMURAI, 16, 2, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 12, 14);
		return m;
	}
	
	@Override
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

	@Override
	public Move nAir() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/mook/nair.png", 4, 2);
		Hitbox h1 = new Hitbox(user, 1.6f, 1.6f, 6, Hitbox.SAMURAI, 0, 0, 24, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 8, 24);
		return m;
	}

	@Override
	public Move sCharge() {
		int spinFrames = 14;
		int sChargeFrameSpeed = 11;
		Move m = new Move(user, spinFrames * sChargeFrameSpeed);
		m.setContinueOnLanding();
		m.setStopsInAir();
		m.setAnimation("sprites/fighters/mook/scharge.png", spinFrames, sChargeFrameSpeed);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox h1 = new Hitbox(user, 3, 2.6f, 12, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h2 = new Hitbox(user, 2.5f, 2.4f, 11, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h3 = new Hitbox(user, 2f, 2.2f, 10, Hitbox.SAMURAI, 26, 4, 16, new SFX.MidHit(), c);
		Hitbox h4 = new Hitbox(user, 1.5f, 2.0f, 9, Hitbox.SAMURAI, 26, 4, 16, new SFX.LightHit(), c);
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4));
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChange(user, (int) (sChargeFrameSpeed * 1.5), 6, Action.ChangeVelocity.noChange);
		m.eventList.addActionCircle(h1, sChargeFrameSpeed * 2, (int) (sChargeFrameSpeed * 2.5) );
		m.eventList.addActionCircle(h2, sChargeFrameSpeed * 4, (int) (sChargeFrameSpeed * 4.5) );
		m.eventList.addActionCircle(h3, sChargeFrameSpeed * 6, (int) (sChargeFrameSpeed * 6.5) );
		m.eventList.addActionCircle(h4, sChargeFrameSpeed * 8, (int) (sChargeFrameSpeed * 8.5) );
		return m;
	}

	@Override
	public Move nSpecial() {
		int length = 30;
		Move m = new Move(user, length);
		m.setAnimation("sprites/fighters/mook/sweak.png", 2, length/2);
		Hitbox h1 = new Hitbox(user, 1.0f, 2.1f, 6, Hitbox.SAMURAI, 26, 4, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, length/2, (length/2) + 3);
		return m;
	}

	@Override
	public Move uSpecial() {
		Move m = new Move(user, 80);
		m.setHelpless();
		m.eventList.addGraphic(user, 6, 70, new Graphic.UFO(user, user.getPosition().x, user.getPosition().y));
		m.eventList.addConstantVelocity(user, 6, 70, Action.ChangeVelocity.noChange, 4);
		return m;
	}

	@Override
	public Move rollForward() {
		Move m = new Move(user, 48);
		user.flip();
		m.setAnimation("sprites/fighters/mook/sgetup.png", 4, 12);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}
	
	@Override
	public Move rollBack() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/mook/sgetup.png", 4, 12);
		m.eventList.addConstantVelocity(user, 2, 20, -3, Action.ChangeVelocity.noChange);
		return m;
	}

	@Override
	public Move dodge() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/mook/ngetup.png", 1, 1);
		return m;
	}

	@Override
	public Move land() {
		Move m = new Move(user, 10);
		m.setAnimation("sprites/fighters/mook/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move skid() {
		Move m = new Move(user, 12);
		m.setAnimation("sprites/fighters/mook/crouch.png", 1, 1);
		return m;
	}

	@Override
	public Move taunt() {
		return new Move(user, 60);
	}

}
