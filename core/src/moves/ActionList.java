package moves;

import java.util.ArrayList;
import java.util.List;

import entities.Fighter;

public class ActionList {

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

	void addConstantVelocity(Fighter user, int start, int end, float velX, float velY) {
		effectList.add(new Effect.ConstantVelocity(user, velX, velY, start, end));
	}

}
