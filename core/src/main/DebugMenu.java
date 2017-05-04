package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import entities.*;

class DebugMenu {
	
	enum MenuChoice{
		CHARACTER, STAGES, PLAYERS, DEBUG
	}

	private static MenuOption<MenuChoice> choices = new MenuOption<MenuChoice>(Arrays.asList(
			MenuChoice.CHARACTER, MenuChoice.STAGES, MenuChoice.PLAYERS, MenuChoice.DEBUG
			));
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

	private static BitmapFont font = new BitmapFont();
	private static SpriteBatch batch;
	private static TextureRegion cursor = new TextureRegion(new Texture(Gdx.files.internal("sprites/graphics/iconwasp.png")));
	private static Music debugTheme = Gdx.audio.newMusic(Gdx.files.internal("music/debug.mp3"));

	static void begin(){
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = Color.GOLDENROD;
		font = generator.generateFont(parameter);
		generator.dispose();
		debugTheme.setVolume(0.03f);
	}

	static void update(){
		debugTheme.play();
		UptiltEngine.getPrimaryInputHandler().update();
		if (UptiltEngine.getPrimaryInputHandler().flickLeft())	handleCursor(-1);
		if (UptiltEngine.getPrimaryInputHandler().flickRight())	handleCursor(1);
		if (UptiltEngine.getPrimaryInputHandler().flickUp())	choices.moveCursor(-1);
		if (UptiltEngine.getPrimaryInputHandler().flickDown())	choices.moveCursor(1);
		if (UptiltEngine.getPrimaryInputHandler().taunt())		start();

		draw();
	}

	private static void handleCursor(int mov){
		switch (choices.selected()){
		case CHARACTER: characters.moveCursor(mov); break;
		case STAGES:	stages.moveCursor(mov);		break;
		case PLAYERS:	players.moveCursor(mov);	break;
		case DEBUG:		debug.moveCursor(mov);		break;
		}
	}

	private static void draw(){
		int posX = 400;
		int startY = 600;
		int dec = 60;
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0.15f, 0.22f, 0.25f, 1);
		batch.begin();

		font.draw(batch, "Press taunt to begin",						posX, startY);
		font.draw(batch, "CHARACTER: " + characters.selected().name,	posX, startY -= dec);
		font.draw(batch, "STAGE: " + stages.selected(),					posX, startY -= dec);
		font.draw(batch, "NUM PLAYERS: " + players.selected() + "",		posX, startY -= dec);
		font.draw(batch, "DEBUG ON: " + debug.selected() + "",			posX, startY -= dec);

		batch.draw(cursor, posX - 50, 600 - dec * (choices.cursorPos() + 1));
		batch.end();
	}

	static void start(){
		debugTheme.stop();
		List<Fighter> playerList = new ArrayList<Fighter>();
		for (int i = 0; i < players.selected(); ++i){
			Fighter player = new Dummy(0, 0, 0);
			try {
				player = characters.selected().type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(0, 0, 0);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			playerList.add(player);
		}
		UptiltEngine.startNewGame(playerList, stages.selected(), debug.selected());
	}

	static class PlayerType{
		final Class<? extends Fighter> type;
		final String name;

		PlayerType(Class<? extends Fighter> type, String name){
			this.type = type;
			this.name = name;
		}
	}

	static class MenuOption <T> {
		private int cursor = 0;
		private final List<T> choices;

		MenuOption(List<T> lst){
			this.choices = lst;
		}

		T selected(){
			return choices.get(cursor);
		}
		
		int cursorPos(){
			return cursor;
		}

		void moveCursor(int mov){
			if (cursor + mov > -1 && cursor + mov < choices.size()) {
				new SFX.LightHit().play(0.3f);
				cursor += mov;
			}
		}
	}

}
