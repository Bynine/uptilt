package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import entities.*;

class DebugMenu extends Menu{

	private static MenuOption<PlayerType> characters = new MenuOption<PlayerType>(Arrays.asList(
			new PlayerType(Kicker.class, "Wasp"), new PlayerType(Frog.class, "Frog"),
			new PlayerType(Grappler.class, "Teddy"), new PlayerType(Dog.class, "Dog"),
			new PlayerType(Mook.class, "Mook"), new PlayerType(AlloyMook.class, "AlloyMook"),
			new PlayerType(Gunmin.class, "Gunmin"), new PlayerType(Rocketmin.class, "Rocketmin"),
			new PlayerType(Speedy.class, "Speedy"), new PlayerType(Dummy.class, "Dummy")
			));
	private static MenuOption<Integer> stages = new MenuOption<Integer>(Arrays.asList(
			0, 1, 2, 3, 4
			));
	private static MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			1, 2, 4, 100
			));
	private static MenuOption<Boolean> debug = new MenuOption<Boolean>(Arrays.asList(
			false, true
			));
	private static MenuOption<String> choices = new MenuOption<String>(Arrays.asList("CHARACTER", "STAGES", "PLAYERS", "DEBUG"));
	private static List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, characters, stages, players, debug));

	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconwasp.png")));
	private static Music debugTheme = Gdx.audio.newMusic(Gdx.files.internal("music/debug.mp3"));

	static void begin(){
		debugTheme.setVolume(0.03f);
	}

	static void update(){
		Menu.update();
		debugTheme.play();
		if (UptiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1, options, choices);
		if (UptiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1,  options, choices);
		if (UptiltEngine.getPrimaryInputHandler().flickUp())	choices.moveCursor(-1);
		if (UptiltEngine.getPrimaryInputHandler().flickDown())	choices.moveCursor(1);
		if (UptiltEngine.getPrimaryInputHandler().taunt())		start();

		draw();
	}

	private static void draw(){
		int posX = 400;
		int startY = 600;
		int dec = 60;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.15f, 0.22f, 0.25f, 1);
		batch.begin();

		font.draw(batch, "Press taunt to begin",					posX, startY);
		font.draw(batch, "CHARACTER: " + characters.selected().name,posX, startY -= dec);
		font.draw(batch, "STAGE: " + stages.selected(),				posX, startY -= dec);
		font.draw(batch, "NUM PLAYERS: " + players.selected(),		posX, startY -= dec);
		font.draw(batch, "DEBUG ON: " + debug.selected(),			posX, startY -= dec);

		batch.draw(cursor, posX - 50, 600 - dec * (choices.cursorPos() + 1));
		batch.end();
	}

	static void start(){
		debugTheme.stop();
		ArrayList<PlayerType> newPlayers = new ArrayList<PlayerType>(Arrays.asList(characters.selected()));
		UptiltEngine.startNewDebugGame(makePlayers(players.selected(), newPlayers), stages.selected(), debug.selected());
	}

}
