package entities;

import com.badlogic.gdx.graphics.Color;

import moves.MoveList_Rocketmin;

public class Rocketmin extends Gunmin {

	public Rocketmin(float posX, float posY, int team) {
		super(posX, posY, team);
		moveList = new MoveList_Rocketmin(this);
		weight = 94;
		friction = 0.97f;
	}
	
	public Color getColor() { return new Color(0.6f, 0.4f, 0.4f, 1); }

}
