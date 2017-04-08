package maps;

import main.GlobalRepo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Room_Standard extends Room {
	
	public Room_Standard(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		startPosition.x = 40;
		startPosition.y = 11;
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}
	
	public int[] getSides() {
		return new int[]{35 * GlobalRepo.TILE, 75 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 10 * GlobalRepo.TILE;
	}
}
