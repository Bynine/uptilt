package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Adventure extends Stage {

	public Stage_Adventure(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/debug.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/adventure.tmx");
	}

	public int[] getSides() {
		return new int[]{15 * GlobalRepo.TILE, 34 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 6 * GlobalRepo.TILE;
	}

	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) 
			return new Vector2(19 * GlobalRepo.TILE, 16 * GlobalRepo.TILE);
		else 
			return new Vector2(30 * GlobalRepo.TILE, 16 * GlobalRepo.TILE);
	}

	public Vector2 getStartPosition() {
		return new Vector2(2 * GlobalRepo.TILE, getFloor());
	}
}
