package main;

import input.InputHandlerController;
import input.InputHandlerKeyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Room;
import maps.Round;
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
	private static boolean paused = false;
	Round round;

	/* DEBUG */
	FPSLogger fpsLogger = new FPSLogger();
	public static boolean fpsLogToggle = false;
	public static boolean p2Toggle = false;
	public static boolean roundToggle = true;
	public static boolean debugToggle = false;

	public void create () {
		player1 = new Kicker(0, 0, 0);
		InputHandlerController ch = new InputHandlerController(player1);
		if (!ch.setupController(0)) startWithKeyboard(player1);
		else player1.setInputHandler(ch);
		GraphicsHandler.begin();
		MapHandler.begin();

		if (p2Toggle){
			Fighter player2 = new Frog(600, 600, 0);
			startWithKeyboard(player2);
			MapHandler.addEntity(player2);
		}

		round = new Round(player1);
	}

	private void startWithKeyboard(Fighter player){
		InputHandlerKeyboard kh = new InputHandlerKeyboard(player);
		player.setInputHandler(kh);
	}

	public void render () {
		deltaTime++;
		updateTimers();
		if (roundToggle) round.update(deltaTime);
		if (fpsLogToggle) fpsLogger.log();

		MapHandler.updateInputs();
		if (!paused){
			MapHandler.activeRoom.update(deltaTime);
			MapHandler.updateActionCircleInteractions();
			if (outOfHitlag()) MapHandler.updateEntities();
		}
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

	public static void pauseGame() {
		paused = !paused;
	}

	public static void changeRoom (Room room, Vector2 position) {
		deltaTime = 0;
		player1.setPosition(position);
		MapHandler.updateRoomMap(room);
		GraphicsHandler.updateRoomGraphics(player1);
	}

	public static float getVolume(){ return volume; }
	public static int getDeltaTime(){ return deltaTime; }
	public static boolean isPaused() { return paused; }
	public static Fighter getPlayer(){ return player1; }
	public static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }

}