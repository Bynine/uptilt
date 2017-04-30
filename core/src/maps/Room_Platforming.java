package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Room_Platforming extends Room {

	public Room_Platforming(Level superLevel) {
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/freeroam.tmx");
	}

	public int[] getSides() {
		return new int[]{2 * GlobalRepo.TILE, 100 * GlobalRepo.TILE};
	}

	@Override
	public float getFloor() {
		return 10 * GlobalRepo.TILE;
	}

	@Override
	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) return new Vector2(40 * GlobalRepo.TILE, 30 * GlobalRepo.TILE);
		else return new Vector2(70 * GlobalRepo.TILE, 30 * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(9 * GlobalRepo.TILE, 3 * GlobalRepo.TILE);
	}

}
