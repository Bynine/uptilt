package com.pack.desktop;

import main.GraphicsHandler;
import main.UptiltEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Up Tilt";
		cfg.width = GraphicsHandler.SCREENWIDTH;
		cfg.height= GraphicsHandler.SCREENHEIGHT;
		new LwjglApplication(new UptiltEngine(), cfg);
	}
}