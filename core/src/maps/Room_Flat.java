package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Room_Flat extends Room {

	public Room_Flat(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}

	public int[] getSides() {
		return new int[]{30 * GlobalRepo.TILE, 70 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 10 * GlobalRepo.TILE;
	}

	public Vector2 getSpawnPoint() {
		return new Vector2(50 * GlobalRepo.TILE, 30 * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(35 * GlobalRepo.TILE, 11 * GlobalRepo.TILE);
	}
}
