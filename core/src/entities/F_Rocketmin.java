package entities;

import com.badlogic.gdx.graphics.Color;

import movelists.Rocketmin;

public class F_Rocketmin extends F_Gunmin {

	public F_Rocketmin(float posX, float posY, int team) {
		super(posX, posY, team);
		moveList = new Rocketmin(this);
		weight = 94;
		friction = 0.97f;
	}
	
	public Color getColor() { return new Color(0.6f, 0.4f, 0.4f, 1); }

}
