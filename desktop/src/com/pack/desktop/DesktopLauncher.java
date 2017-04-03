package com.pack.desktop;

import main.GraphicsHandler;
import main.PlatformerEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "UPTILT";
		cfg.width = GraphicsHandler.SCREENWIDTH;
		cfg.height= GraphicsHandler.SCREENHEIGHT;
		new LwjglApplication(new PlatformerEngine(), cfg);
	}
}