package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Bomb extends Basic {

	public Bomb(float posX, float posY, int team) {
		super(posX, posY, team);
		setPalette(new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/palettes/basic/bomb.glsl")));
	}

}
