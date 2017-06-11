package moves;

import main.GlobalRepo;
import moves.Effect.Charge;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;
import entities.Fighter;
import entities.Entity.Direction;
import timers.DurationTimer;
import timers.Timer;

public class Move {
	final Fighter user;
	final Timer duration;
	public final EventList eventList = new EventList();
	private boolean helpless = false, continueOnLanding = false, noTurn = false, connected = false, stopsInAir = false;
	float armor = 0;
	private Animation animation = null;
	private int addedFrames = 0;
	private Rectangle hurtBox = null;
	private float hurtBoxDispX = 0, hurtBoxDispY = 0;

	public Move(Fighter user, int dur){
		this.user = user;
		duration = new DurationTimer(dur);
	}

	public void update(){
		duration.countUp();
		eventList.update(duration.getCounter(), user.isInHitstun());
		for (ActionCircle ac: eventList.acList){
			if (ac.hitFighterList.size() > 0) connected = true;
		}
		if (null != user && null != hurtBox){
			hurtBox.x = user.getPosition().x + hurtBoxDispX;
			if (user.getDirection() == Direction.LEFT) hurtBox.x += hurtBox.width/2;
			hurtBox.y = user.getPosition().y + hurtBoxDispY;
		}
	}

	public boolean done(){ return duration.timeUp(); }
	public void setDone() { duration.end();}
	public int getDuration() { return duration.getEndTime(); }
	public boolean causesHelpless() { return helpless; }
	public void setHelpless() { helpless = true; }
	public void setStopsInAir() { stopsInAir = true;  }
	public boolean stopsInAir() { return stopsInAir; }
	public boolean continuesOnLanding() { return continueOnLanding; }
	public boolean isNoTurn() { return noTurn; }
	public void dontTurn() { noTurn = true; }
	public void setContinueOnLanding() { continueOnLanding = true; }
	public Animation getAnimation() { return animation; }
	public int getFrame() { return duration.getCounter() + addedFrames; }
	public float getArmor() { return armor; }
	public void setArmor(float armor) { this.armor = armor; }
	public boolean connected() { return connected; }
	public Rectangle getHurtBox() { return hurtBox; }
	public boolean isCharging() {
		for (Effect e: eventList.effectList){
			if (e instanceof Charge) if (e.isActive(duration.getCounter())) return true;
		}
		return false;
	}
	public void setHurtBox(Rectangle hb) { 
		hurtBox = hb; 
		hurtBoxDispX = hb.x - user.getImage().getBoundingRectangle().x;
		hurtBoxDispY = hb.y - user.getImage().getBoundingRectangle().y;
	}

	public void setAnimation(String string, int cols, int speed) {
		animation = GlobalRepo.makeAnimation(string, cols, 1, speed, PlayMode.LOOP);
	}

	public void addFrame() {
		duration.setEndTime(duration.getEndTime() + 1);
		addedFrames--;
	}

}
