package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity;
import entities.Entity.Direction;

public class GlobalRepo {
	
	/* GLOBAL VARIABLES */
	
	public static final int TILE = 32;
	public static final int GOODTEAM = 0;
	public static final int BADTEAM = 1;
	
	/* GLOBAL METHODS */

	public static Animation makeAnimation(String address, int cols, int rows, float speed, PlayMode playMode){
		Texture sheet = new Texture(Gdx.files.internal(address));
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		TextureRegion[] frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		Animation animation = new Animation(speed, frames);
		animation.setPlayMode(playMode);
		return animation;
	}
	
	public static Rectangle makeHurtBox(Entity user, int width, int height){
		return makeHurtBoxHelper(user, width, height);
	}
	
	public static Rectangle makeHurtBoxDynamic(Entity user, float scaleX, float scaleY){
		Rectangle hurtBox = user.getImage().getBoundingRectangle();
		int newWidth = (int) (hurtBox.getWidth() * scaleX);
		int newHeight = (int) (hurtBox.getHeight() * scaleY);
		return makeHurtBoxHelper(user, newWidth, newHeight);
	}
	
	private static Rectangle makeHurtBoxHelper(Entity user, int width, int height){
		Rectangle hurtBox = user.getImage().getBoundingRectangle();
		float changeX = hurtBox.getWidth() - width;
		float changeY = hurtBox.getHeight() - height;
		hurtBox.setSize(width, height);
		float newX = hurtBox.getX() + changeX/2;
		float newY = hurtBox.getY() + changeY/2;
		hurtBox.setPosition(newX, newY);
		return hurtBox;
	}
	
	public static Rectangle makeHurtBoxInner(Entity user, int width, int height){
		Rectangle origBox = user.getImage().getBoundingRectangle();
		Rectangle hurtBox = makeHurtBoxHelper(user, width, height);
		float dispX = (origBox.width - width)/2;
		if (user.getDirection() == Direction.LEFT) hurtBox.x -= dispX;
		else hurtBox.x += dispX;
		return hurtBox;
	}
	
}
