package entities;

import main.GlobalRepo;
import movelists.M_Grappler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Grappler extends Fighter {
	
	private TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/grappler/stand.png")));
	private TextureRegion crouch = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/grappler/crouch.png")));

	public Grappler(float posX, float posY, int team) {
		super(posX, posY, team);
		weight = 175;
		jumpAcc = 0.7f;
		doubleJumpStrength = 8f;
		runSpeed = 6f;
		armor = 0.5f;
		wallSlideSpeed = -2f;
		friction = 0.88f;
		footStoolKB.set(0, -4);
		footStoolDuration = 60;
		footStoolDamage = 10;
		moveList = new M_Grappler(this);
	}
	
	public Rectangle getNormalHurtBox(){
		return GlobalRepo.makeHurtBoxDynamic(this, 0.9f, 0.9f);
	}

	TextureRegion getJumpFrame(float deltaTime) { return stand; }
	TextureRegion getStandFrame(float deltaTime) { return stand; }
	TextureRegion getWalkFrame(float deltaTime) { return stand; }
	TextureRegion getRunFrame(float deltaTime) { return stand; }
	TextureRegion getWallSlideFrame(float deltaTime) { return stand; }
	TextureRegion getHelplessFrame(float deltaTime) { return stand; }
	TextureRegion getGrabFrame(float deltaTime) { return stand; }
	TextureRegion getFallFrame(float deltaTime) { return stand; }
	TextureRegion getAscendFrame(float deltaTime) { return stand; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouch; }
	TextureRegion getDashFrame(float deltaTime) { return stand; }
	TextureRegion getDodgeFrame(float deltaTime) { return stand; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return crouch; }
	TextureRegion getTumbleFrame(float deltaTime) { return stand; }
	TextureRegion getHitstunFrame(float deltaTime) { return stand; }
	TextureRegion getFallenFrame(float deltaTime) { return stand; }

}
