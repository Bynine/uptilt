package main;

import input.InputHandlerController;
import input.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Room;
import timers.Timer;
import entities.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;

public class UptiltEngine extends ApplicationAdapter {

	private static float volume = 1f;
	private static final Timer hitlagTimer = new Timer(0);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitlagTimer));
	private static int deltaTime = 0;
	private static Fighter player1;
	FPSLogger fpsLogger = new FPSLogger();

	@Override public void create () {
		player1 = new Kicker(0, 0, 0);
		InputHandlerController ch = new InputHandlerController(player1);
		if (!ch.setupController(0)) startWithKeyboard();
		else player1.setInputHandler(ch);
		GraphicsHandler.begin();
		MapHandler.begin(player1);
	}

	private void startWithKeyboard(){
		InputHandlerKeyboard kh = new InputHandlerKeyboard(player1);
		player1.setInputHandler(kh);
	}

	@Override public void render () {
		deltaTime++;
		updateTimers();
		//fpsLogger.log();

		MapHandler.activeRoom.update(deltaTime);
		MapHandler.updateActionCircleInteractions();
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
	public static Fighter getPlayer(){ return player1; }
	public static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }

}