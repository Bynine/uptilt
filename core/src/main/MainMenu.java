package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import entities.*;

class MainMenu extends Menu {

	private static List<PlayerType> playableCharacters = new ArrayList<PlayerType>(Arrays.asList(
			new PlayerType(Wasp.class, "Wasp"), new PlayerType(Frog.class, "Frog")
			));
	private static MenuOption<PlayerType> p1Char = new MenuOption<PlayerType>(playableCharacters);
	private static MenuOption<PlayerType> p2Char = new MenuOption<PlayerType>(playableCharacters);
	private static MenuOption<Integer> players = new MenuOption<Integer>(Arrays.asList(
			1, 2
			));
	private static MenuOption<String> choices = new MenuOption<String>(Arrays.asList("P1CHAR", "P2CHAR", "PLAYERS"));
	private static List<MenuOption<?>> options = new ArrayList<MenuOption<?>>(Arrays.asList(choices, p1Char, p2Char, players));
	
	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconwasp.png")));

	enum MenuChoice{
		CHARACTER, PLAYERS
	}

	static void update() {
		Menu.update();
		if (UptiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1, options, choices);
		if (UptiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1, options,choices);
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
		Gdx.gl.glClearColor(0.1f, 0.04f, 0.1f, 1);
		batch.begin();

		font.draw(batch, "P1 CHAR: " + p1Char.selected().name,		posX, startY -= dec);
		if (players.selected() == 1) font.setColor(0.5f, 0.5f, 0.5f, 0.5f);
		font.draw(batch, "P2 CHAR: " + p2Char.selected().name,		posX, startY -= dec);
		font.setColor(Color.GOLDENROD);
		font.draw(batch, "NUM PLAYERS: " + players.selected(),		posX, startY -= dec);

		batch.draw(cursor, posX - 50, 600 - dec * (choices.cursorPos() + 1));
		batch.end();
	}

	private static void start(){
		ArrayList<PlayerType> newPlayers = new ArrayList<PlayerType>(Arrays.asList(p1Char.selected(), p2Char.selected()));
		UptiltEngine.startNewGame(makePlayers(players.selected(), newPlayers));
	}


}
