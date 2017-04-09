package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Room_Flat extends Room {

	public Room_Flat(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		startPosition.x = 35;
		startPosition.y = 11;
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}

	public int[] getSides() {
		return new int[]{30 * GlobalRepo.TILE, 70 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 10 * GlobalRepo.TILE;
	}
}
