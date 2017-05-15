package main;

import inputs.InputHandlerController;
import inputs.InputHandlerKeyboard;
import inputs.InputHandlerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maps.Stage;
import timers.Timer;
import entities.*;
import challenges.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class UptiltEngine extends ApplicationAdapter {

	private static final Timer hitlagTimer = new Timer(0);
	private static final Timer waitTimer = new Timer(0);
	private static final List<Timer> timerList = new ArrayList<Timer>(Arrays.asList(hitlagTimer, waitTimer));
	private static final List<Fighter> playerList = new ArrayList<Fighter>();
	private static int deltaTime = 0;
	private static FPSLogger fpsLogger = new FPSLogger();
	private static boolean paused = false;
	private static Challenge challenge, defaultChallenge;
	private static GameState gameState = GameState.MENU;
	private static InputHandlerPlayer primaryInputHandler = null;

	/* DEBUG */
	public static boolean 	fpsLogToggle 	= false;
	public static boolean 	p2Toggle 		= false;
	public static boolean 	roundToggle 	= true;
	public static boolean 	debugToggle 	= false;
	public static boolean	musicToggle		= false;
	private static float	volume			= 1f;
	private static ShaderProgram p2Palette;

	public void create () {
		p2Palette = new ShaderProgram(Gdx.files.internal("shaders/vert.glsl"), Gdx.files.internal("shaders/spooky.glsl"));
		Fighter player1 = new Wasp(0, 0, 0);
		beginFighter(player1, 0);
		GraphicsHandler.begin();
		MapHandler.begin();
		
		Menu.begin();
		DebugMenu.begin();
		MainMenu.begin();
		
		challenge = new ChallengeAdventure(CombatGenerator.DIFF_EASY);
		defaultChallenge = new ChallengeAdventure(CombatGenerator.DIFF_TEST);

		if (p2Toggle){
			Fighter player2 = new Wasp(MapHandler.activeRoom.getStartPosition().x, MapHandler.activeRoom.getStartPosition().y, 0);
			beginFighter(player2, 1);
			MapHandler.addEntity(player2);
		}

	}
	
	private static void beginFighter(Fighter player, int cont){
		playerList.add(player);
		InputHandlerController ch = new InputHandlerController(player);
		if (!ch.setupController(cont)) startWithKeyboard(player);
		else {
			if (primaryInputHandler == null) primaryInputHandler = ch;
			player.setInputHandler(ch);
		}
	}

	private static void startWithKeyboard(Fighter player){
		InputHandlerKeyboard kh = new InputHandlerKeyboard(player);
		player.setInputHandler(kh);
		if (primaryInputHandler == null) primaryInputHandler = kh;
	}

	public void render () {
		deltaTime++;
		updateTimers();
		if (fpsLogToggle) fpsLogger.log();
		
		switch(gameState){
		case GAME:	updateGame();		break;
		case DEBUG: DebugMenu.update();	break;
		case MENU:	MainMenu.update();	break;
		}
	}
	
	private void updateGame(){
		challenge.update();
		if (waitTimer.timeUp()) MapHandler.updateInputs();
		if (!paused){
			MapHandler.activeRoom.update(deltaTime);
			MapHandler.updateActionCircleInteractions();
			if (outOfHitlag()) MapHandler.updateEntities();
		}
		GraphicsHandler.updateGraphics();
		ChallengeGraphicsHandler.update();
		GraphicsHandler.updateCamera();
	}

	private void updateTimers(){
		for (Timer t: timerList) t.countUp();
	}

	public static void causeHitlag(int length){
		hitlagTimer.setEndTime(length);
		hitlagTimer.restart();
	}
	
	public static void wait(int length){
		waitTimer.setEndTime(length);
		waitTimer.restart();
	}

	public static void pauseGame() {
		paused = !paused;
	}

	public static void changeRoom (Stage room) {
		deltaTime = 0;
		for (Fighter player: getPlayers()){
			player.setPosition(room.getStartPosition());
		}
		MapHandler.updateRoomMap(room);
		GraphicsHandler.updateRoomGraphics(getPlayers().get(0));
	}
	
	public static void startNewDebugGame(List<Fighter> newPlayers, int roomChoice, boolean debug){
		startNewChallenge(newPlayers, CombatGenerator.DIFF_HARD);
		MapHandler.begin();
		debugToggle = debug;
	}
	
	public static void startNewChallenge(List<Fighter> newPlayers, int difficulty){
		gameState = GameState.GAME;
		playerList.clear();
		paused = false;
		int i = 0;
		for (Fighter player: newPlayers) {
			beginFighter(player, i);
			if (i >= 1) player.setPalette(p2Palette);
			i++;
		}
		GraphicsHandler.begin();
		MapHandler.begin();
		challenge = new ChallengeAdventure(difficulty);
	}
	
	public static void startDebugMenu(){
		gameState = GameState.DEBUG;
	}
	
	public static void returnToMenu(){
		gameState = GameState.MENU;
	}
	
	public static Challenge getChallenge(){
		if (challenge == null) return defaultChallenge;
		else return challenge;
	}
	
	public enum GameState{
		GAME, DEBUG, MENU
	}

	public static float getVolume(){ return volume; }
	public static int getDeltaTime(){ return deltaTime; }
	public static boolean isPaused() { return paused; }
	public static List<Fighter> getPlayers(){ return playerList; }
	public static boolean outOfHitlag(){ return hitlagTimer.timeUp(); }
	public static boolean justOutOfHitlag() { return hitlagTimer.timeJustUp(); }
	public static GameState getGameState() { return gameState; }
	public static InputHandlerPlayer getPrimaryInputHandler() { return primaryInputHandler; }

}