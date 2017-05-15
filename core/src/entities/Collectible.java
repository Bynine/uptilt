package entities;

import com.badlogic.gdx.graphics.Color;

import timers.*;
import main.GlobalRepo;
import main.SFX;
import main.UptiltEngine;

public abstract class Collectible extends Entity {
	
	private final Timer noTouchie = new DurationTimer(40);
	protected int bonus = 0;

	public Collectible(float posX, float posY) {
		super(posX, posY);
		timerList.add(noTouchie);
		airFriction = 0.99f;
	}

	void handleTouchHelper(Entity e){
		if (e instanceof Fighter){
			Fighter fi = (Fighter) e;
			if (isTouching(fi, 0) && fi.getTeam() == GlobalRepo.GOODTEAM && noTouchie.timeUp()) collect(fi);
		}
	}
	
	protected void collect(Fighter fi){
		new SFX.Collect().play();
		UptiltEngine.getChallenge().score(bonus);
		setRemove();
		collectHelper(fi);
	}

	void collectHelper(Fighter fi){
		/**/
	}
	
	public Color getColor(){
		if (noTouchie.timeUp()) return new Color(1, 1, 1, 1);
		else return new Color(1, 1, 1, 0.5f);
	}

}
