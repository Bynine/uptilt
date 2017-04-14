package moves;

import java.util.ArrayList;
import java.util.List;

import main.SFX;
import moves.Effect.Charge;
import moves.Effect.GraphicEffect;
import entities.Fighter;
import entities.Graphic;
import entities.Projectile;

public class EventList {

	List<Action> actionList = new ArrayList<Action>();
	List<Effect> effectList = new ArrayList<Effect>();
	List<Integer> actionStartTimes = new ArrayList<Integer>();

	void update(int time, boolean hitstun) {
		time = time - 1;
		if (hitstun) return;
		for (Effect e: effectList){
			if (e.getStart() <= time && e.getEnd() >= time) e.performAction();
			if (e.getEnd() == time) e.finish();
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
	
	void addVelocityChangeCharge(Fighter user, int start, float velX, float velY, Charge c) {
		actionList.add(new Action.ChangeVelocity(user, velX, velY, c));
		actionStartTimes.add(start);
	}
	
	void addAngledVelocityChange(Fighter user, int start, float vel) {
		actionList.add(new Action.ChangeVelocityAngled(user, vel));
		actionStartTimes.add(start);
	}
	
	void addSound(SFX sfx, int start){
		actionList.add(new Action.PlaySFX(sfx));
		actionStartTimes.add(start);
	}
	
	<T extends Projectile> void addProjectile(Fighter user, Class<T> proj, int start){
		actionList.add(new Action.MakeProjectile<>(user, proj));
		actionStartTimes.add(start);
	}

	void addInvincible(Fighter user, int start, int end) {
		actionList.add(new Action.Invincible(user, start, end));
		actionStartTimes.add(start);
	}
	
	/* effects */

	void addConstantVelocity(Fighter user, int start, int end, float velX, float velY) {
		effectList.add(new Effect.ConstantVelocity(user, velX, velY, start, end));
	}

	void addCharge(Fighter user, Charge c) {
		effectList.add(c);
	}

	void addArmor(Move move, int start, int end, float armor) {
		effectList.add(new Effect.Armor(move, start, end, armor));
	}

	public void addGraphic(Fighter user, int start, int end, Graphic g) {
		effectList.add(new GraphicEffect(user, start, end, g));
	}

}
