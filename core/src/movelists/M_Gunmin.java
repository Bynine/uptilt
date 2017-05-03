package movelists;

import java.util.Arrays;

import main.SFX;
import moves.Action;
import moves.ActionCircleGroup;
import moves.Hitbox;
import moves.Move;
import entities.Fighter;
import entities.Graphic;
import entities.Projectile;

public class M_Gunmin extends MoveList {

	public M_Gunmin(Fighter user) {
		super(user);
	}
	
	protected Class<? extends Projectile> getChargeLaserClass(){
		return Projectile.ChargeLaser.class;
	}

	public Move nWeak() {
		Move m = new Move(user, 24);
		m.setAnimation("sprites/fighters/laser/nweak.png", 4, 6);
		Hitbox h1 = new Hitbox(user, 2, 1, 2, Hitbox.SAMURAI, 24, 6, 10, new SFX.LightHit());
		m.eventList.addActionCircle(h1, 12, 18);
		return m;
	}
	
	public Move uWeak() {
		Move m = new Move(user, 32);
		m.setAnimation("sprites/fighters/laser/fire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.LaserDiagonalF.class, 10);
		m.eventList.addProjectile(user, Projectile.LaserUp.class, 20);
		m.eventList.addProjectile(user, Projectile.LaserDiagonalB.class, 30);
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
		m.eventList.addVelocityChange(user, 42, -3, 0);
		m.eventList.addProjectile(user, getChargeLaserClass(), 42);
		return m;
	}
	
	public Move slide() {
		Move m = new Move(user, 45);
		m.setAnimation("sprites/fighters/laser/fire.png", 1, 1);
		m.eventList.addGraphic(user, 2, 42, new Graphic.LaserCharge(user, user.getPosition().x,  user.getPosition().y));
		m.eventList.addVelocityChange(user, 42, -3, 1);
		m.eventList.addProjectile(user, getChargeLaserClass(), 42);
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
		Hitbox h1 = new Hitbox(user, 0.5f, 2f, 4, Hitbox.SAMURAI, -30, 0, 16, new SFX.MidHit());
		m.eventList.addActionCircle(h1, 14, 20);
		return m;
	}
	
	public Move uAir() {
		Move m = new Move(user, 36);
		m.setAnimation("sprites/fighters/laser/airfire.png", 1, 1);
		m.eventList.addProjectile(user, Projectile.LaserDiagonalF.class, 8);
		m.eventList.addProjectile(user, Projectile.LaserUp.class, 16);
		m.eventList.addProjectile(user, Projectile.LaserDiagonalB.class, 24);
		return m;
	}
	
	public Move dAir() {
		Move m = new Move(user, 45);
		m.setContinueOnLanding();
		m.setAnimation("sprites/fighters/laser/airfire.png", 1, 1);
		m.eventList.addGraphic(user, 2, 42, new Graphic.LaserCharge(user, user.getPosition().x,  user.getPosition().y));
		m.eventList.addVelocityChange(user, 42, -2, 0);
		m.eventList.addProjectile(user, getChargeLaserClass(), 42);
		return m;
	}
	
	public Move uSpecial() {
		Move m = new Move(user, 80);
		m.setHelpless();
		m.eventList.addGraphic(user, 6, 77, new Graphic.UFO(user, user.getPosition().x, user.getPosition().y));
		m.eventList.addConstantVelocity(user, 6, 77, Action.ChangeVelocity.noChange, 5);
		return m;
	}
	
	public Move rollForward() {
		Move m = new Move(user, 48);
		user.flip();
		m.setAnimation("sprites/fighters/laser/sgetup.png", 2, 12);
		m.eventList.addConstantVelocity(user, 2, 34, -2, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move rollBack() {
		Move m = new Move(user, 48);
		m.setAnimation("sprites/fighters/laser/sgetup.png", 2, 12);
		m.eventList.addConstantVelocity(user, 2, 34, -2, Action.ChangeVelocity.noChange);
		return m;
	}
	
	public Move dodge() {
		Move m = new Move(user, 30);
		m.setAnimation("sprites/fighters/laser/ngetup.png", 1, 1);
		return m;
	}
	
	public Move getUpAttack() {
		return dodge();
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

	public Move airDodge() {
		return new Move(user, 0);
	}
	
}
