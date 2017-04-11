package moves;

import main.GlobalRepo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import entities.Fighter;
import timers.DurationTimer;
import timers.Timer;

public class Move {
	final Fighter user;
	final Timer duration;
	final EventList eventList = new EventList();
	private boolean helpless = false, continueOnLanding = false, noTurn = false;
	float armor = 0;
	Animation animation = null;
	int addedFrames = 0;

	Move(Fighter user, int dur){
		this.user = user;
		duration = new DurationTimer(dur);
	}

	public void update(){
		duration.countUp();
		eventList.update(duration.getCounter());
	}

	public boolean done(){ return duration.timeUp(); }
	public int getDuration() { return duration.getEndTime(); }
	public boolean causesHelpless() { return helpless; }
	public void setHelpless() { helpless = true; }
	public boolean continuesOnLanding() { return continueOnLanding; }
	public boolean isNoTurn() { return noTurn; }
	public void dontTurn() { noTurn = true; }
	public void setContinueOnLanding() { continueOnLanding = true; }
	public Animation getAnimation() { return animation; }
	public int getFrame() { return duration.getCounter() + addedFrames; }
	public float getArmor() { return armor; }
	public void setArmor(float armor) { this.armor = armor; }

	public void setAnimation(String string, int cols, int speed) {
		animation = GlobalRepo.makeAnimation(string, cols, 1, speed, PlayMode.LOOP);
	}

	public void addFrame() {
		duration.setEndTime(duration.getEndTime() + 1);
		addedFrames--;
	}

}
