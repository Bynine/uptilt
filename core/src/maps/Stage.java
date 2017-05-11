package maps;

import java.util.ArrayList;
import java.util.List;

import main.UptiltEngine;
import moves.ActionCircle;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import entities.Entity;
import entities.Fighter;

public abstract class Stage {
	protected MapObjects mapCollision, mapEntities;
	protected final TmxMapLoader tmxMapLoader = new TmxMapLoader();
	protected final List<Rectangle> rectangleList = new ArrayList<>();
	protected final List<Entity> entityList = new ArrayList<>();
	protected final List<Entity> newEntityList = new ArrayList<>();
	protected final List<ActionCircle> actionCircleList = new ArrayList<>();
	protected Music roomMusic;
	protected float r, g, b = 1;
	protected float a = 0;
	protected float wind = 0;
	protected float gravity = 1;
	
	public static final float LOWGRAV = 0.66f;
	public static final float HIGRAV  = 1.3f;

	public Stage(){
		clearOut();
	}

	public void setup(){
		MapLayers layers = getMap().getLayers();
		mapCollision = layers.get(layers.getCount()-2).getObjects(); // gets the collision layer
		for(RectangleMapObject mapObject: mapCollision.getByType(RectangleMapObject.class)){		
			rectangleList.add(mapObject.getRectangle());
		}
		mapEntities = layers.get(layers.getCount()-1).getObjects();
		roomMusic.setLooping(true);
	}

	public void initEntities(){
		clearOut();
		for (Fighter player: UptiltEngine.getPlayers()){
			player.setPercentage(0);
			player.setPosition(getStartPosition());
			player.setRespawnPoint(getStartPosition());
			entityList.add(player);
		}
		for (MapObject m: mapEntities) entityList.add(new EntityLoader().loadEntity(m));
		if (UptiltEngine.musicToggle) roomMusic.play();
	}

	public void addEntity(Entity e){
		newEntityList.add(e);
	}

	public ActionCircle addActionCircle(ActionCircle ac) {
		actionCircleList.add(ac);
		return actionCircleList.get(actionCircleList.size() - 1);
	}

	public void update(int deltaTime){
		for (Entity e: newEntityList) entityList.add(e);
		newEntityList.clear();
		for (ActionCircle ac: actionCircleList) ac.update(deltaTime);
	}

	void clearOut(){
		rectangleList.clear();
		entityList.clear();
	}

	void setStartPosition(float x, float y){
		getStartPosition().x = x;
		getStartPosition().y = y;
	}

	public void removeEntity(Entity en){
		getEntityList().remove(en);
		getRectangleList().remove(en.getImage().getBoundingRectangle());
	}

	public abstract TiledMap getMap();
	public abstract int[] getSides();
	public abstract float getFloor();
	public abstract Vector2 getSpawnPoint();
	public abstract Vector2 getStartPosition();

	public List<Rectangle> getRectangleList(){ return rectangleList; }
	public List<Entity> getEntityList(){ return entityList; }
	public List<ActionCircle> getActionCircleList() { return actionCircleList; }
	public void stopMusic(){ roomMusic.stop(); }
	public Music getMusic(){ return roomMusic; }
	public float getR(){ return r; }
	public float getB(){ return b; }
	public float getG(){ return g; }
	public float getA(){ return a; }
	public float getWind() { return wind; }
	public float getGravity() { return gravity; }

}
