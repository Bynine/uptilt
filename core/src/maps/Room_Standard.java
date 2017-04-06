package maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Room_Standard extends Room {
	
	public Room_Standard(Level superLevel){
		super(superLevel);
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/song of storm crow.mp3"));
		startPosition.x = 45;
		startPosition.y = 11;
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/standard.tmx");
	}
}
