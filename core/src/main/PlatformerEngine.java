package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Room;
import timers.Timer;
import entities.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector2;

public class PlatformerEngine extends ApplicationAdapter {

	private static float volume = 0f;
	private static final Timer hitlagTimer = new Timer(0, false);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitlagTimer));
	private static int deltaTime = 0;
	private static Fighter player1;
	
	@Override public void create () {
		player1 = new Fast(0, 0, new InputHandler());
		new InputHandler().begin(player1);
		GraphicsHandler.begin();
		MapHandler.begin(player1);
	}

	@Override public void render () {
		deltaTime++;
		updateTimers();
		player1.getInputHandler().updatePlayer(player1);
		
		MapHandler.activeRoom.update();
		if (outOfHitlag()) MapHandler.updateEntities();
		GraphicsHandler.updateGraphics();
		GraphicsHandler.updateCamera(player1);
	}

	private void updateTimers(){
		for (Timer t: timerList) t.countUp();
	}
	
	public static void causeHitlag(int length){
		hitlagTimer.setEndTime(length);
		hitlagTimer.restart();
	}

	public static void changeRoom (Room room, Vector2 position) {
		deltaTime = 0;
		player1.setPosition(position);
		MapHandler.updateRoomMap(room);
		GraphicsHandler.updateRoomGraphics(player1);
	}
	
	static float getVolume(){ return volume; }
	static int getDeltaTime(){ return deltaTime; }
	static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }

}