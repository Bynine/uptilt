package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maps.Level;
import maps.Level_Stages;
import maps.Room;
import moves.ActionCircle;
import moves.Grabbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
		activeLevel = new Level_Stages();
		activeRoom = activeLevel.getRoom(0);
		activeMap = activeRoom.getMap();
		UptiltEngine.changeRoom(activeRoom, activeRoom.getStartPosition());
		activeRoom.getMusic().setVolume(UptiltEngine.getVolume());
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	static void updateEntities(){
		Iterator<Entity> entityIter = activeRoom.getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity en = entityIter.next();
			boolean shouldUpdate = UptiltEngine.outOfHitlag() || en instanceof Graphic;
			if (shouldUpdate) en.update(rectangleList, activeRoom.getEntityList(), UptiltEngine.getDeltaTime()); 
			if ( en.isOOB(mapWidth, mapHeight) || en.toRemove() ) {
				if (en instanceof Fighter){
					Fighter fi = (Fighter) en;
					new SFX.Explode().play();
					if (fi.getStocks() > 1 || fi == UptiltEngine.getPlayer()) fi.respawn();
					else {
						fi.setStocks(0);
						entityIter.remove();
					}
				}
				else entityIter.remove();
			}
		}
		if (player.isOOB(mapWidth, mapHeight)) resetRoom();
	}

	static void updateActionCircleInteractions(){
		for (Entity en: activeRoom.getEntityList()){
			if (en instanceof Fighter) {
				fighterHitboxInteract(en);
				removeGrabboxes();
			}
		}
		removeActionCircles();
	}

	private static void removeActionCircles(){
		Iterator<ActionCircle> actionCircleIter = activeRoom.getActionCircleList().iterator();
		while (actionCircleIter.hasNext()) {
			ActionCircle ac = actionCircleIter.next();
			if (ac.toRemove()) actionCircleIter.remove();
		}
	}

	private static void removeGrabboxes(){
		Iterator<ActionCircle> actionCircleIter = activeRoom.getActionCircleList().iterator();
		while (actionCircleIter.hasNext()) {
			ActionCircle ac = actionCircleIter.next();
			if (ac.toRemove() && ac instanceof Grabbox) actionCircleIter.remove();
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

	public static void resetRoom() {
		UptiltEngine.changeRoom(activeRoom, activeRoom.getStartPosition()); 
	}

	public static void addEntity(Entity e){ activeRoom.addEntity(e); }
	public static ActionCircle addActionCircle(ActionCircle ac){ 
			return activeRoom.addActionCircle(ac); 
		}
	
	public static int[] getStageSides() { 
		if (activeRoom == null) return new int[]{0, 0};
		else return activeRoom.getSides();
	}
	public static float getStageFloor() {
		if (activeRoom == null) return 0;
		else return activeRoom.getFloor();
	}

	public static Vector2 getSpawnPoint() {
		if (activeRoom == null) return new Vector2(0, 0);
		else return activeRoom.getSpawnPoint();
	}

}
