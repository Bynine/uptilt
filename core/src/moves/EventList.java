package moves;

import java.util.ArrayList;
import java.util.List;

import moves.Effect.Charge;
import entities.Fighter;
import entities.Projectile;

public class EventList {

	List<Action> actionList = new ArrayList<Action>();
	List<Effect> effectList = new ArrayList<Effect>();
	List<Integer> actionStartTimes = new ArrayList<Integer>();

	void update(int time) {
		time = time - 1;
		for (Effect e: effectList){
			if (e.getStart() <= time && e.getEnd() >= time) e.performAction();
		}
		while (!actionList.isEmpty() && actionStartTimes.contains(time)) {
			Action a = actionList.remove(0);
			actionStartTimes.remove(new Integer(time));
			a.performAction();
		}
	}

	void addActionCircle(ActionCircle ac, int start, int finish){
		ac.setDuration(finish - start);
		actionList.add(new Action.MakeHitbox(ac));
		actionStartTimes.add(start);
	}

	void addVelocityChange(Fighter user, int start, float velX, float velY) {
		actionList.add(new Action.ChangeVelocity(user, velX, velY));
		actionStartTimes.add(start);
	}
	
	void addSound(String sfxUrl, int start){
		actionList.add(new Action.SFX(sfxUrl));
		actionStartTimes.add(start);
	}
	
	<T extends Projectile> void addProjectile(Fighter user, Class<T> proj, int start){
		actionList.add(new Action.MakeProjectile<>(user, proj));
		actionStartTimes.add(start);
	}
	
	/* effects */

	void addConstantVelocity(Fighter user, int start, int end, float velX, float velY) {
		effectList.add(new Effect.ConstantVelocity(user, velX, velY, start, end));
		//actionStartTimes.add(start);
	}

	void addCharge(Fighter user, Charge c) {
		effectList.add(c);
	}

}
