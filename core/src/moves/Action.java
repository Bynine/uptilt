package moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import entities.Fighter;
import main.MapHandler;

public abstract class Action {

	abstract void performAction();

	public static class MakeHitbox extends Action{
		ActionCircle ac;
		
		MakeHitbox(ActionCircle ac){
			this.ac = ac;
		}

		void performAction(){
			ac.checkConnected();
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
			if (velX != noChange) user.getVelocity().x = velX;
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
	
}
