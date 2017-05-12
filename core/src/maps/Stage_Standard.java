package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Standard extends Stage {

	public Stage_Standard(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/alienattack.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}

	public int[] getSides() {
		return new int[]{12 * GlobalRepo.TILE, 31 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 5 * GlobalRepo.TILE;
	}

	private int ceiling = 20;
	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) return new Vector2(16 * GlobalRepo.TILE, ceiling * GlobalRepo.TILE);
		else return new Vector2(27 * GlobalRepo.TILE, ceiling * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(23 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}

}
