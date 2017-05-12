package maps;

import main.GlobalRepo;
import main.UptiltEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Stage_Flat extends Stage {

	public Stage_Flat(){
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/debug.mp3"));
		setup();
	}

	public TiledMap getMap() {
		return tmxMapLoader.load("maps/flat.tmx");
	}

	public int[] getSides() {
		return new int[]{15 * GlobalRepo.TILE, 34 * GlobalRepo.TILE};
	}

	public float getFloor() {
		return 5 * GlobalRepo.TILE;
	}

	public Vector2 getSpawnPoint() {
		if (Math.random() < 0.5) 
			return new Vector2(19 * GlobalRepo.TILE, 16 * GlobalRepo.TILE);
		else 
			return new Vector2(30 * GlobalRepo.TILE, 16 * GlobalRepo.TILE);
	}

	public Vector2 getStartPosition() {
		return new Vector2(25 * GlobalRepo.TILE, getFloor());
	}

	float windStrength = 0.5f;
	int directionTiming = 800;
	public float getWind(){
		int gx = (UptiltEngine.getDeltaTime() % directionTiming);
		if (gx > 000 && gx < 100) return  windStrength;
		if (gx > 400 && gx < 500) return -windStrength;
		else return 0;
	}
}
