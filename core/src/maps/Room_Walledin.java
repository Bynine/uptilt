package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Room_Walledin extends Room {
	
	public Room_Walledin(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/walledin.tmx");
	}
	
	public int[] getSides() {
		return new int[]{17 * GlobalRepo.TILE, 40 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 11 * GlobalRepo.TILE;
	}

	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.33) return new Vector2(19 * GlobalRepo.TILE, 25 * GlobalRepo.TILE);
		else if (Math.random() < 0.5) return new Vector2(29 * GlobalRepo.TILE, 26 * GlobalRepo.TILE);
		else return new Vector2(38 * GlobalRepo.TILE, 25 * GlobalRepo.TILE);
	}

	@Override
	public Vector2 getStartPosition() {
		return new Vector2(29 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
}
