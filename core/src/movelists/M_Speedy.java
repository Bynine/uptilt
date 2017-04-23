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

public class M_Speedy extends MoveList {

	public M_Speedy(Fighter user) {
		super(user);
	}
	
	public Move nWeak() {
		Move m = new Move(user, 14);
		m.setAnimation("sprites/fighters/speedy/nweak.png", 2, 7);
		Hitbox h1 = new Hitbox(user, 2, 0.5f, 3, Hitbox.SAMURAIANGLE, 38, 2, 12, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 7, 8);
		return m;
	}
	
	public Move uWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/uweak.png", 3, 8);
		Hitbox h1 = new Hitbox(user, 3, 1.2f, 8, 83, 16, 32, 14, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 3, 1.2f, 8, 97, -16, 32, 14, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		m.eventList.addActionCircle(h1, 8, 11);
		m.eventList.addActionCircle(h2, 8, 11);
		return m;
	}
	
	public Move sWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/nweak.png", 2, 4);
		Hitbox bite1 = new Hitbox(user, 3, 0, 2, 90, 26, -4, 12, new SFX.MidHit());
		Hitbox bite2 = new Hitbox(user, 2, 0, 2, 90, 26, 12, 12, new SFX.MidHit());
		Hitbox bite3 = new Hitbox(user, 1, 2, 5, Hitbox.SAMURAIANGLE, 26, 4, 12, new SFX.MidHit());
		m.eventList.addActionCircle(bite1, 9, 11);
		m.eventList.addActionCircle(bite2, 14, 16);
		m.eventList.addActionCircle(bite3, 21, 23);
		return m;
	}
	
	public Move dWeak() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/speedy/dweak.png", 2, 10);
		Hitbox h1 = new Hitbox(user, 3, 1f, 5, Hitbox.SAMURAIANGLE, 30, 0, 12, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 10, 12);
		return m;
	}
	
	public Move slide() {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 7);
		Hitbox h1 = new Hitbox(user, 5, 2, 7, 74, 20, 0, 24, new SFX.MidHit());
		m.eventList.addVelocityChange(user, 0, Action.ChangeVelocity.noChange, 4);
		m.eventList.addConstantVelocity(user, 10, 30, 14, -user.getGravity());
		m.eventList.addActionCircle(h1, 10, 30);
		return m;
	}
	
	int nDisp = 24;
	public Move nAir() {
		Move m = new Move(user, 20);
		m.setAnimation("sprites/fighters/speedy/nair.png", 5, 4);
		Hitbox h1 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAIANGLE, -nDisp, -nDisp, 12, new SFX.MidHit());
		Hitbox h2 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAIANGLE,  nDisp, -nDisp, 12, new SFX.MidHit());
		Hitbox h3 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAIANGLE, -nDisp,  nDisp, 12, new SFX.MidHit());
		Hitbox h4 = new Hitbox(user, 2, 2, 6, Hitbox.SAMURAIANGLE,  nDisp,  nDisp, 12, new SFX.MidHit());
		new ActionCircleGroup(Arrays.asList(h1, h2, h3, h4));
		m.eventList.addActionCircle(h1, 9, 12);
		m.eventList.addActionCircle(h2, 9, 12);
		m.eventList.addActionCircle(h3, 9, 12);
		m.eventList.addActionCircle(h4, 9, 12);
		return m;
	}
	
	public Move fAir() {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 4);
		Hitbox rotate = 	new Hitbox(user, 1, 0, 1, 80, 32, 0, 18, new SFX.LightHit());
		rotate.setRefresh(3);
		Hitbox finisher = 	new Hitbox(user, 4, 1.5f, 7, 35, 20, 0, 24, new SFX.MidHit());
		m.eventList.addConstantVelocity(user, 10, 30, 0, -user.getGravity());
		m.eventList.addActionCircle(rotate, 10, 25);
		m.eventList.addActionCircle(finisher, 26, 30);
		return m;
	}
	
	public Move bAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/wallslide.png", 3, 24);
		Hitbox early1 = new Hitbox(user, 1.5f, 2.1f, 11, 30, -20,  10, 14, new SFX.MidHit());
		Hitbox early2 = new Hitbox(user, 1.5f, 2.1f, 11, 30, -20, -10, 14, new SFX.MidHit());
		Hitbox late1 = new Hitbox(user, 1.2f, 0.5f, 6, Hitbox.SAMURAIANGLE, -18,  7, 10, new SFX.LightHit());
		Hitbox late2 = new Hitbox(user, 1.2f, 0.5f, 6, Hitbox.SAMURAIANGLE, -18, -7, 10, new SFX.LightHit());
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
	
	public Move sCharge() {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 2);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox rotate = 	new Hitbox(user, 6.5f, 0, 1, 30, 32, 0, 18, new SFX.LightHit(), c);
		rotate.setRefresh(3);
		Hitbox finisher = 	new Hitbox(user, 4, 1.5f, 7, 35, 20, 0, 24, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChangeCharge(user, 10, 6, Action.ChangeVelocity.noChange, c);
		m.eventList.addActionCircle(rotate, 10, 25);
		m.eventList.addActionCircle(finisher, 26, 30);
		return m;
	}
	
	public Move uCharge() {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 2);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox rotate = 	new Hitbox(user, 6.5f, 0, 1, 30, 32, 0, 18, new SFX.LightHit(), c);
		rotate.setRefresh(3);
		Hitbox finisher = 	new Hitbox(user, 4, 1.5f, 7, 35, 20, 0, 24, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChangeCharge(user, 10, 3, 6, c);
		m.eventList.addActionCircle(rotate, 10, 25);
		m.eventList.addActionCircle(finisher, 26, 30);
		return m;
	}
	
	public Move dCharge() {
		Move m = new Move(user, 40);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/speedy/fair.png", 2, 2);
		Effect.Charge c = new Charge(6, 36, 0.01f, user, m);
		Hitbox rotate = 	new Hitbox(user, 6.5f, 0, 1, 30, 32, 0, 18, new SFX.LightHit(), c);
		rotate.setRefresh(3);
		Hitbox finisher = 	new Hitbox(user, 4, 1.5f, 7, 35, 20, 0, 24, new SFX.MidHit(), c);
		m.eventList.addCharge(user, c);
		m.eventList.addVelocityChangeCharge(user, 10, 4, -4, c);
		m.eventList.addActionCircle(rotate, 10, 25);
		m.eventList.addActionCircle(finisher, 26, 30);
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 16);
		m.setHelpless();
		System.out.println("Speedy used up special");
		m.eventList.addVelocityChange(user, 1, Action.ChangeVelocity.noChange, 12);
		return m;
	}
	
	public Move rollForward() {
		Move m = new Move(user, 24);
		user.flip();
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, -7, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move rollBack() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/speedy/crouch.png", 1, 1);
		m.eventList.addConstantVelocity(user, 2, 20, -7, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move dodge() {
		Move m = new Move(user, 16);
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
		return new Move(user, 8);
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
