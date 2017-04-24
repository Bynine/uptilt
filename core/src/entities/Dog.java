package entities;

import main.GlobalRepo;
import movelists.M_Dog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Dog extends Fighter{
	
	private TextureRegion stand = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/dog/stand.png")));
	private TextureRegion dash = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/dog/dash.png")));
	private TextureRegion crouch = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/dog/crouch.png")));
	private TextureRegion jumpSquat = new TextureRegion(new Texture(Gdx.files.internal("sprites/fighters/dog/jumpsquat.png")));
	private Animation walk = GlobalRepo.makeAnimation("sprites/fighters/dog/walk.png", 2, 1, 12, PlayMode.LOOP);
	private Animation run = GlobalRepo.makeAnimation("sprites/fighters/dog/run.png", 2, 1, 8, PlayMode.LOOP);

	public Dog(float posX, float posY, int team) {
		super(posX, posY, team);
		runAcc = 1.5f;
		runSpeed = 7.5f;
		walkAcc = 0.75f;
		walkSpeed = 3.2f;
		friction = 0.92f;
		fallSpeed = -6f;
		airFriction = 0.96f;
		weight = 85;
		dashStrength = -1;
		moveList = new M_Dog(this);
	}
	
	public Rectangle getNormalHurtBox(){
		return GlobalRepo.makeHurtBoxDynamic(this, 0.9f, 0.8f);
	}
	
	TextureRegion getJumpFrame(float deltaTime) { return stand; }
	TextureRegion getStandFrame(float deltaTime) { return stand; }
	TextureRegion getWalkFrame(float deltaTime) { return walk.getKeyFrame(deltaTime); }
	TextureRegion getRunFrame(float deltaTime) { return run.getKeyFrame(deltaTime); }
	TextureRegion getWallSlideFrame(float deltaTime) { return stand; }
	TextureRegion getHelplessFrame(float deltaTime) { return stand; }
	TextureRegion getGrabFrame(float deltaTime) { return stand; }
	TextureRegion getFallFrame(float deltaTime) { return stand; }
	TextureRegion getAscendFrame(float deltaTime) { return stand; }
	TextureRegion getCrouchFrame(float deltaTime) { return crouch; }
	TextureRegion getDashFrame(float deltaTime) { return dash; }
	TextureRegion getDodgeFrame(float deltaTime) { return stand; }
	TextureRegion getJumpSquatFrame(float deltaTime) { return jumpSquat; }
	TextureRegion getTumbleFrame(float deltaTime) { return stand; }
	TextureRegion getHitstunFrame(float deltaTime) { return stand; }
	TextureRegion getFallenFrame(float deltaTime) { return stand; }

}
