package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Room_Flat extends Room {

	public Room_Flat(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		startPosition.x = 25;
		startPosition.y = 12;
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}
}
