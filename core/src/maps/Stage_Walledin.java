package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Walledin extends Stage {
	
	public Stage_Walledin(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/storm.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/wall.tmx");
	}
	
	public int[] getSides() {
		return new int[]{11 * GlobalRepo.TILE, 30 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 10 * GlobalRepo.TILE;
	}

	public Vector2 getSpawnPoint() {
		return new Vector2(28 * GlobalRepo.TILE, 23 * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(18 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
}
