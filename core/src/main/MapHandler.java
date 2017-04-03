package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maps.Level;
import maps.Level_Test;
import maps.Room;
import moves.ActionCircle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity;
import entities.Fighter;
import entities.Graphic;

public class MapHandler {

	static Room activeRoom;
	static TiledMap activeMap;
	static Level activeLevel; 
	private static final List<Rectangle> rectangleList = new ArrayList<>();
	static int mapWidth;
	static int mapHeight; 
	static Fighter player;

	static void begin(Fighter playern){
		player = playern;
		activeLevel = new Level_Test();
		activeRoom = activeLevel.getRoom(0);
		activeMap = activeRoom.getMap();
		PlatformerEngine.changeRoom(activeRoom, activeRoom.getStartPosition());
		activeRoom.getMusic().setVolume(PlatformerEngine.getVolume());
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	static void updateEntities(){
		Iterator<Entity> entityIter = activeRoom.getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity en = entityIter.next();
			boolean shouldUpdate = PlatformerEngine.outOfHitlag() || en instanceof Graphic;
			if (shouldUpdate) { 
				en.update(rectangleList, activeRoom.getEntityList(), PlatformerEngine.getDeltaTime()); 
				if (en instanceof Fighter) fighterHitboxInteract(en);
			}
			if ( en.isOOB(mapWidth, mapHeight) || en.toRemove() ) {
				if (en instanceof Fighter){
					Fighter fi = (Fighter) en;
					fi.respawn();
				}
				else entityIter.remove();
			}
		}
		removeActionCircles();
		if (player.isOOB(mapWidth, mapHeight)) resetRoom();
	}

	private static void removeActionCircles(){
		Iterator<ActionCircle> actionCircleIter = activeRoom.getActionCircleList().iterator();
		while (actionCircleIter.hasNext()) {
			ActionCircle ac = actionCircleIter.next();
			if (ac.toRemove()) actionCircleIter.remove();
		}
	}

	private static void fighterHitboxInteract(Entity en){
		Fighter fi = (Fighter) en;
		for (ActionCircle ac: activeRoom.getActionCircleList()) ac.hitTarget(fi);
	}

	public static void updateRoomMap(Room room) {
		activeRoom = room;
		activeMap = activeRoom.getMap();
		activeRoom.initEntities(player);
		rectangleList.clear();
		activeRoom.setup();
		rectangleList.addAll(activeRoom.getRectangleList());
		mapWidth  = activeMap.getProperties().get("width",  Integer.class)*GlobalRepo.TILE;
		mapHeight = activeMap.getProperties().get("height", Integer.class)*GlobalRepo.TILE;
	}

	static void resetRoom() {
		PlatformerEngine.changeRoom(activeRoom, activeRoom.getStartPosition()); 
	}

	public static void addEntity(Entity e){ activeRoom.addEntity(e); }
	public static void addActionCircle(ActionCircle ac){ activeRoom.addActionCircle(ac); }

}
