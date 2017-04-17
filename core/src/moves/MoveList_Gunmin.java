package moves;

import java.util.Arrays;

import main.SFX;
import entities.Fighter;
import entities.Graphic;
import entities.Projectile;

public class MoveList_Gunmin extends MoveList {

	public MoveList_Gunmin(Fighter user) {
		super(user);
	}

	public Move nWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/nweak.png", 4, 6);
		Hitbox h1 = new Hitbox(user, 2, 1, 2, Hitbox.SAMURAIANGLE, 24, 6, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 12, 18);
		return m;
	}
	
	public Move uWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/fire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.LaserUp.class, 12);
		return m;
	}
	
	public Move sWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/fire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.Laser.class, 12);
		return m;
	}
	
	public Move dWeak() {
		Move m = new Move(user, 60);
		m.setAnimation("sprites/fighters/laser/crouchfire.png", 1, 1);
		m.eventList.addGraphic(user, 2, 42, new Graphic.LaserCharge(user, user.getPosition().x,  user.getPosition().y));
		m.eventList.addVelocityChange(user, 42, -4, 0);
		m.eventList.addProjectile(user, Projectile.ChargeLaser.class, 42);
		return m;
	}
	
	public Move slide() {
		Move m = new Move(user, 45);
		m.causesHelpless();
		m.setAnimation("sprites/fighters/laser/fire.png", 1, 1);
		m.eventList.addGraphic(user, 2, 42, new Graphic.LaserCharge(user, user.getPosition().x,  user.getPosition().y));
		m.eventList.addVelocityChange(user, 42, -5, 3);
		m.eventList.addProjectile(user, Projectile.ChargeLaser.class, 42);
		return m;
	}
	
	public Move nAir() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/laser/nair.png", 8, 4);
		Hitbox h1 = new Hitbox(user, 1, 1, 1, 20, -16, -4, 10, new SFX.LightHit());
		Hitbox h2 = new Hitbox(user, 1, 1, 1, 160, 16, -4, 10, new SFX.LightHit());
		new ActionCircleGroup(Arrays.asList(h1, h2));
		h1.setRefresh(4);
		h2.setRefresh(4);
		m.eventList.addActionCircle(h1, 12, 24);
		m.eventList.addActionCircle(h2, 12, 24);
		return m;
	}
	
	public Move fAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/airfire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.Laser.class, 12);
		return m;
	}
	
	public Move bAir() {
		Move m = new Move(user, 28);
		m.setAnimation("sprites/fighters/laser/bair.png", 4, 7);
		Hitbox h1 = new Hitbox(user, 0.5f, 2f, 4, Hitbox.SAMURAIANGLE, -30, 0, 16, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 14, 20);
		return m;
	}
	
	public Move uAir() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/airfire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.LaserUp.class, 12);
		return m;
	}
	
	public Move dAir() {
		Move m = new Move(user, 45);
		m.setAnimation("sprites/fighters/laser/airfire.png", 1, 1);
		m.eventList.addGraphic(user, 2, 42, new Graphic.LaserCharge(user, user.getPosition().x,  user.getPosition().y));
		m.eventList.addVelocityChange(user, 42, -4, 0);
		m.eventList.addProjectile(user, Projectile.ChargeLaser.class, 42);
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 80);
		m.setHelpless();
		m.eventList.addGraphic(user, 6, 70, new Graphic.UFO(user, user.getPosition().x, user.getPosition().y));
		m.eventList.addConstantVelocity(user, 6, 70, Action.ChangeVelocity.noChange, 4);
		return m;
	}

	public Move land() {
		return new Move(user, 6);
	}

	public Move skid() {
		return new Move(user, 8);
	}

	public Move taunt() {
		return new Move(user, 60);
	}
	
	/* UNUSED */
	
	public Move sCharge() {
		return new Move(user, 0);
	}
	
	public Move uCharge() {
		return new Move(user, 0);
	}
	
	public Move dCharge() {
		return new Move(user, 0);
	}
	
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
	
	public Move rollForward() {
		return new Move(user, 0);
	}
	
	public Move rollBack() {
		return new Move(user, 0);
	}

	public Move airDodge() {
		return new Move(user, 0);
	}
	
	public Move dodge() {
		return new Move(user, 0);
	}
	
}