package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Room_Height extends Stage {

	public Room_Height() {
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/storm.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/height.tmx");
	}

	public int[] getSides() {
		return new int[]{10 * GlobalRepo.TILE, 23 * GlobalRepo.TILE};
	}

	@Override
	public float getFloor() {
		return 5 * GlobalRepo.TILE;
	}

	@Override
	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) return new Vector2(11 * GlobalRepo.TILE, 26 * GlobalRepo.TILE);
		else return new Vector2(22 * GlobalRepo.TILE, 26 * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(14 * GlobalRepo.TILE, 5 * GlobalRepo.TILE);
	}

}
