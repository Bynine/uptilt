package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Ship extends Stage {

	public Stage_Ship(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/storm.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/ship.tmx");
	}

	public int[] getSides() {
		return new int[]{8 * GlobalRepo.TILE, 25 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 5 * GlobalRepo.TILE;
	}

	private int ceiling = 20;
	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) return new Vector2(10 * GlobalRepo.TILE, ceiling * GlobalRepo.TILE);
		else return new Vector2(21 * GlobalRepo.TILE, ceiling * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(24 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

}
