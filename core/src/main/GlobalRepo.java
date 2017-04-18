package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class GlobalRepo {
	
	/* GLOBAL VARIABLES */
	
	public static final int TILE = 32;
	public static final int GOOD = 0;
	public static final int BAD = 1;
	
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
	
}
