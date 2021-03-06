package maps;

import main.GlobalRepo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Adventure extends Stage {

	public Stage_Adventure(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/sand.mp3"));
		setup();
		name = "ADVENTURE";
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/adventure.tmx");
	}

	public Vector2 getStartPosition() {
		return new Vector2(2 * GlobalRepo.TILE, 6 * GlobalRepo.TILE);
	}
	
}
