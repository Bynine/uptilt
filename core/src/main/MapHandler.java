package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import maps.Stage;
import maps.Stage_Standard;
import moves.ActionCircle;
import moves.Grabbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity;
import entities.FallingEnemy;
import entities.Fighter;
import entities.Graphic;
import entities.Hittable;

public class MapHandler {

	static Stage activeRoom;
	static TiledMap activeMap;
	private static final List<Rectangle> rectangleList = new ArrayList<>();
	static int mapWidth;
	static int mapHeight; 
	private static final float musicVolume = 0.25f;

	static void begin(){
		activeRoom = new Stage_Standard();
		activeMap = activeRoom.getMap();
		UptiltEngine.changeRoom(activeRoom);
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
		activeRoom.getMusic().setVolume(musicVolume);
		activeRoom.getMusic().play();
		Iterator<Entity> entityIter = activeRoom.getEntityList().iterator();
		while (entityIter.hasNext()) {
			Entity en = entityIter.next();
			boolean shouldUpdate = UptiltEngine.outOfHitlag() || en instanceof Graphic;
			if (shouldUpdate) en.update(rectangleList, activeRoom.getEntityList(), UptiltEngine.getDeltaTime()); 
			Rectangle boundary = new Rectangle(0, 0, mapWidth, mapHeight);
			Rectangle cameraBoundary = GraphicsHandler.getCameraBoundary();
			boolean toRemove = en.toRemove() || en.isOOB(boundary);
			if (UptiltEngine.getChallenge().isInCombat()) toRemove = toRemove || (en instanceof Fighter && en.isOOB(cameraBoundary));
			if (toRemove) {
				if (en instanceof Fighter){
					Fighter fi = ((Fighter) en);
					if (kill(fi)) {
						if (fi.getPosition().y > cameraBoundary.y + cameraBoundary.height) activeRoom.addEntity(
								new FallingEnemy(fi.getPosition().x, cameraBoundary.y + cameraBoundary.height));
						if (fi.getTeam() == GlobalRepo.BADTEAM) UptiltEngine.getChallenge().score(20);
						entityIter.remove();
					}
				}
				else entityIter.remove();
			}
		}
	}

	private static boolean kill(Fighter fi){
		int mod = 6;
		addEntity(new Graphic.Die(
				(fi.getCenter().x * (mod-1) + GraphicsHandler.getCameraPos().x) / mod, 
				(fi.getCenter().y * (mod-1) + GraphicsHandler.getCameraPos().y) / mod
		));
		new SFX.Die().play();
		if (fi.getLives() > 1) {
			fi.respawn();
			return false;
		}
		if (UptiltEngine.getPlayers().size() > 1){
			for (Fighter player: UptiltEngine.getPlayers()){
				if (player.getLives() > 1 && player.getTeam() == GlobalRepo.GOODTEAM){
					fi.respawn();
					fi.setLives(1);
					player.setLives(player.getLives() - 1);
					return false;
				}
			}
		}
		fi.setLives(0);
		return true;
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

	public static void updateRoomMap(Stage room) {
		activeRoom.getMusic().stop();
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
		UptiltEngine.changeRoom(activeRoom); 
	}

	public static void addEntity(Entity e){ activeRoom.addEntity(e); }
	public static ActionCircle addActionCircle(ActionCircle ac){ 
		return activeRoom.addActionCircle(ac); 
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
