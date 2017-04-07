package moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import entities.Fighter;
import entities.Projectile;
import main.MapHandler;

public abstract class Action {

	abstract void performAction();

	public static class MakeHitbox extends Action{
		ActionCircle ac;
		
		MakeHitbox(ActionCircle ac){
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
		public static final float noChange = -999f;
		
		ChangeVelocity(Fighter user, float velX, float velY){
			this.user = user;
			this.velX = velX;
			this.velY = velY;
		}
		
		void performAction(){
			if (velX != noChange) user.getVelocity().x = velX * user.direct();
			if (velY != noChange) user.getVelocity().y = velY;
		}
	}
	
	public static class SFX extends Action{
		final Sound sfx;

		SFX(String sfxUrl){
			 sfx = Gdx.audio.newSound(Gdx.files.internal(sfxUrl));
		}
		
		void performAction() {
			sfx.play(0.01f);
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
	
}
