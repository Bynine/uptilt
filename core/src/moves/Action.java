package moves;

import com.badlogic.gdx.math.Vector2;

import entities.Fighter;
import entities.Projectile;
import main.MapHandler;
import main.SFX;
import moves.Effect.Charge;

public abstract class Action {

	abstract void performAction();

	public static class MakeActionCircle extends Action{
		ActionCircle ac;
		
		MakeActionCircle(ActionCircle ac){
			this.ac = ac;
		}

		void performAction(){
			ac.checkGroup();
			if (ac.toRemove()) return;
			ac.updatePosition();
			MapHandler.addActionCircle(ac);
		}
	}
	
	public static class ChangeVelocity extends Action{
		float velX, velY;
		Fighter user;
		Charge charge = null;
		public static final float noChange = -999f;
		
		ChangeVelocity(Fighter user, float velX, float velY){
			this.user = user;
			this.velX = velX;
			this.velY = velY;
		}
		
		public ChangeVelocity(Fighter user, float velX, float velY, Charge c) {
			this(user, velX, velY);
			charge = c;
		}

		void performAction(){
			if (null != charge){
				if (velX != noChange) velX *= Math.pow(charge.getHeldCharge(), 2);
				if (velY != noChange) velY *= Math.pow(charge.getHeldCharge(), 2);
			}
			if (velX != noChange) user.getVelocity().x = velX * user.direct();
			if (velY != noChange) user.getVelocity().y = velY;
		}
	}
	
	public static class PlaySFX extends Action{
		final SFX sfx;
		
		PlaySFX(SFX sfx){
			this.sfx = sfx;
		}
		
		void performAction() {
			sfx.play();
		}
		
	}
	
	public static class MakeProjectile<T extends Projectile> extends Action{
		final Class<T> proj;
		final Fighter user;

		public MakeProjectile (Fighter user, Class<T> proj){
			this.proj = proj;
			this.user = user;
		}
		
		void performAction() {
			try { MapHandler.addEntity(proj.getConstructor(float.class, float.class, Fighter.class)
					.newInstance(user.getPosition().x, user.getPosition().y, user));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static class Invincible extends Action {
		final Fighter user;
		final int start, end;
		
		public Invincible(Fighter user, int start, int end){
			this.user = user;
			this.start = start;
			this.end = end;
		}
		
		void performAction() {
			user.setInvincible(end - start);
		}

	}
	
	public static class ChangeVelocityAngled extends Action {
		final Fighter user;
		final float velocity;
		
		ChangeVelocityAngled(Fighter user, float velocity){
			this.user = user;
			this.velocity = velocity;
		}

		void performAction() {
			Vector2 newVelocity = new Vector2(velocity, velocity);
			Vector2 angle = new Vector2(user.getStickX(), -user.getStickY());
			if (angle.angle() > 120 && angle.angle() < 270) newVelocity.setAngle(140);
			else if (angle.angle() < 60 || angle.angle() > 270) newVelocity.setAngle(40);
			else newVelocity.setAngle(90);
			user.getVelocity().set(newVelocity);
		}

	}
	
}
