package entities;

import main.GlobalRepo;
import movelists.M_Frog;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Frog extends Kicker {

	public Frog(float posX, float posY, int team) {
		super(posX, posY, team);
		weight = 120;
		runAcc = 2.2f;
		runSpeed = 9.2f;
		airSpeed = 3.4f;
		airAcc = 0.29f;
		gravity = -0.48f;
		jumpAcc = 1.05f;
		dashStrength = -1f;
		friction = 0.83f;
		jumpSquatTimer.setEndTime(5);
		moveList = new M_Frog(this);
		footStoolKB.set(0, -1);
		footStoolDuration = 40;
	}
	
	public Rectangle getNormalHurtBox(){
		return GlobalRepo.makeHurtBoxDynamic(this, 0.85f, 0.9f);
	}
	
	public Color getColor() { return new Color(0.4f, 1.0f, 0.5f, 1); }

}
