package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import main.GlobalRepo;
import movelists.M_HyperSpeedy;

public class HyperSpeedy extends Speedy {

	public HyperSpeedy(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 3.2f;
		runSpeed = 9.6f;
		walkAcc = 1.4f;
		walkSpeed = 6.3f;
		airAcc = 0.5f;
		airSpeed = 6.5f;
		friction = 0.8f;
		gravity = -0.53f;
		jumpAcc = 0.95f;
		dashStrength = 10f;
		doubleJumpStrength = 9.3f;
		fallSpeed = -8f;
		weight = 90;
		wallSlideSpeed = 5.5f;
		powerMod = 1.1f;
		moveList = new M_HyperSpeedy(this);
		
		wallSlide = GlobalRepo.makeAnimation("sprites/fighters/speedy/wallslide.png", 3, 1, 10, PlayMode.LOOP);
	}
	
	public Color getColor() { return new Color(0.8f, 1.0f, 0.0f, 1); }

}
