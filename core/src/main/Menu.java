package main;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import entities.Dummy;
import entities.Fighter;

abstract class Menu {
	
	protected static BitmapFont font = new BitmapFont();
	protected static SpriteBatch batch;
	
	static void begin(){
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = Color.GOLDENROD;
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
	static void update(){
		UptiltEngine.getPrimaryInputHandler().update();
	}

	protected static void handleCursor(int mov, List<MenuOption<?>> options, MenuOption<String> choices){
		options.get(choices.cursorPos() + 1).moveCursor(mov);
	}
	
	protected static List<Fighter> makePlayers(int num, ArrayList<PlayerType> newPlayers){
		List<Fighter> playerList = new ArrayList<Fighter>();
		for (int i = 0; i < num; ++i){
			Fighter player = new Dummy(0, 0, 0);
			try {
				int j = i;
				if (j >= newPlayers.size()) j = newPlayers.size() - 1;
				player = newPlayers.get(j).type.getDeclaredConstructor(float.class, float.class, int.class).newInstance(0, 0, 0);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			playerList.add(player);
		}
		return playerList;
	}

	protected static class PlayerType{
		final Class<? extends Fighter> type;
		final String name;

		PlayerType(Class<? extends Fighter> type, String name){
			this.type = type;
			this.name = name;
		}
	}

	protected static class MenuOption <T> {
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
