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
	List<ActionCircle> acList = new ArrayList<ActionCircle>();

	public void update(int time, boolean hitstun) {
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

	public void addActionCircle(ActionCircle ac, int start, int finish){
		ac.setDuration(finish - start);
		acList.add(ac);
		actionList.add(new Action.MakeActionCircle(ac));
		actionStartTimes.add(start);
	}

	public void addVelocityChange(Fighter user, int start, float velX, float velY) {
		actionList.add(new Action.ChangeVelocity(user, velX, velY));
		actionStartTimes.add(start);
	}
	
	public void addVelocityChangeCharge(Fighter user, int start, float velX, float velY, Charge c) {
		actionList.add(new Action.ChangeVelocity(user, velX, velY, c));
		actionStartTimes.add(start);
	}
	
	public void addSound(SFX sfx, int start){
		actionList.add(new Action.PlaySFX(sfx));
		actionStartTimes.add(start);
	}
	
	public <T extends Projectile> void addProjectile(Fighter user, Class<T> proj, int start){
		actionList.add(new Action.MakeProjectile<>(user, proj));
		actionStartTimes.add(start);
	}

	public void addInvincible(Fighter user, int start, int end) {
		actionList.add(new Action.Invincible(user, start, end));
		actionStartTimes.add(start);
	}
	
	/* effects */

	public void addConstantVelocity(Fighter user, int start, int end, float velX, float velY) {
		effectList.add(new Effect.ConstantVelocity(user, velX, velY, start, end));
	}

	public void addConstantAngledVelocity(Fighter user, int start, int end, float vel) {
		effectList.add(new Effect.ConstantVelocityAngled(user, start, end, vel));
	}

	public void addCharge(Fighter user, Charge c) {
		effectList.add(c);
	}

	public void addArmor(Move move, int start, int end, float armor) {
		effectList.add(new Effect.Armor(move, start, end, armor));
	}

	public void addGraphic(Fighter user, int start, int end, Graphic g) {
		effectList.add(new GraphicEffect(user, start, end, g));
	}

}
