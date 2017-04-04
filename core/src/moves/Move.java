package moves;

import entities.Fighter;
import timers.Timer;

public class Move {
	final Fighter user;
	final Timer duration;
	final EventList actionList = new EventList();
	boolean helpless = false;
	boolean special = false;

	Move(Fighter user, int dur){
		this.user = user;
		duration = new Timer(dur, true);
	}

	public void update(){
		duration.countUp();
		actionList.update(duration.getCounter());
	}

	public boolean done(){ return duration.timeUp(); }
	public boolean causesHelpless() { return helpless; }
	public int getDuration() { return duration.getEndTime(); }
	public boolean isSpecial() { return special; }
	// abstract Animation getAnimation();

}
