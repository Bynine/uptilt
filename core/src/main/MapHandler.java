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
import entities.Hittable;

public class MapHandler {

	static Room activeRoom;
	static TiledMap activeMap;
	static Level activeLevel; 
	private static final List<Rectangle> rectangleList = new ArrayList<>();
	static int mapWidth;
	static int mapHeight; 

	static void begin(int startMap){
		activeLevel = new Level_Stages();
		activeRoom = activeLevel.getRoom(startMap);
		activeMap = activeRoom.getMap();
		UptiltEngine.changeRoom(activeRoom, activeRoom.getStartPosition());
		activeRoom.getMusic().setVolume(UptiltEngine.getVolume());
		Gdx.gl.glClearColor(0, 0, 0, 1);
	}

	static void updateInputs(){
		for (Entity en: activeRoom.getEntityList()){
			if (en instanceof Fighter){
				Fighter fi = (Fighter) en;
				fi.getInputHandler().update();
			}
		}
	}

	static void updateEntities(){
		Iterator<Entity> entityIter = activeRoom.getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity en = entityIter.next();
			boolean shouldUpdate = UptiltEngine.outOfHitlag() || en instanceof Graphic;
			if (shouldUpdate) en.update(rectangleList, activeRoom.getEntityList(), UptiltEngine.getDeltaTime()); 
			if ( en.toRemove(mapWidth, mapHeight) ) {
				if (en instanceof Fighter){
					if (kill ((Fighter) en)) entityIter.remove();
				}
				else entityIter.remove();
			}
		}
	}
	
	private static boolean kill(Fighter fi){
		new SFX.Die().play();
		if (fi.getLives() > 1) {
			fi.respawn();
			return false;
		}
		else {
			fi.setLives(0);
			return true;
		}
	}

	static void updateActionCircleInteractions(){
		for (Entity en: activeRoom.getEntityList()){
			if (en instanceof Hittable) {
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
		for (ActionCircle ac: activeRoom.getActionCircleList()) ac.hitTarget((Hittable) en);
	}

	public static void updateRoomMap(Room room) {
		activeRoom = room;
		activeMap = activeRoom.getMap();
		activeRoom.initEntities();
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

	public static List<Entity> getEntities() {
		if (activeRoom == null) return new ArrayList<Entity>();
		else return activeRoom.getEntityList();
	}
	
	public static float getRoomWind(){
		if (activeRoom == null) return 0;
		else return activeRoom.getWind();
	}
	
	public static float getRoomGravity(){
		if (activeRoom == null) return 1;
		else return activeRoom.getGravity();
	}
	
	public static List<ActionCircle> getActionCircles(){
		if (activeRoom == null) return new ArrayList<ActionCircle>();
		else return activeRoom.getActionCircleList();
	}

}
