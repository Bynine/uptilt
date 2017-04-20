package entities;

import com.badlogic.gdx.graphics.Color;

public class F_AlloyMook extends F_Mook {

	public F_AlloyMook(float posX, float posY, int team) {
		super(posX, posY, team);
		gravity = -0.75f;
		weight = 140;
		armor = 0.2f;
		walkSpeed = 2f;
		runSpeed = 4f;
		doubleJumpStrength = 11f;
		jumpAcc = 0.82f;
	}
	
	public Color getColor() { return new Color(0.3f, 0.5f, 0.6f, 1); }

}
