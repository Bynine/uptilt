package entities;

import com.badlogic.gdx.graphics.Color;

public class AlloyMook extends Mook {

	public AlloyMook(float posX, float posY, int team) {
		super(posX, posY, team);
		gravity = -0.75f;
		weight = 160;
		armor = 0.5f;
		walkSpeed = 2f;
		runSpeed = 4f;
		doubleJumpStrength = 11f;
		jumpAcc = 0.82f;
		jumpSquatTimer.setEndTime(8);
	}
	
	public Color getColor() { return new Color(0.3f, 0.5f, 0.6f, 1); }

}
