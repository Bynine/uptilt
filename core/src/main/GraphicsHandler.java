package main;

import moves.ActionCircle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import entities.Entity;
import entities.Fighter;

public class GraphicsHandler {
	
	private static SpriteBatch batch;
	private static final OrthographicCamera cam = new OrthographicCamera();
	private static final int camAdjustmentSpeed = 8;
	private static final float ZOOM = 1/2f;
	private static OrthogonalTiledMapRenderer renderer;
	private static final float screenAdjust = 2f;
	private static final ShapeRenderer debugRenderer = new ShapeRenderer();
	private static BitmapFont font = new BitmapFont();
	private static boolean debug = false;
	
	public static final int SCREENWIDTH  = (int) ((45 * GlobalRepo.TILE)/ZOOM);
	public static final int SCREENHEIGHT = (int) ((26 * GlobalRepo.TILE)/ZOOM);
	
	public static void begin() {
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		cam.zoom = ZOOM;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 8;
		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
	static void updateCamera(Fighter player){
		cam.position.x = (cam.position.x*(camAdjustmentSpeed-1) + player.getPosition().x)/camAdjustmentSpeed;
		cam.position.y = (cam.position.y*(camAdjustmentSpeed-1) + GlobalRepo.TILE * 2 + player.getPosition().y)/camAdjustmentSpeed;
		
		cam.position.x = MathUtils.round(MathUtils.clamp(cam.position.x, screenBoundary(SCREENWIDTH), MapHandler.mapWidth - screenBoundary(SCREENWIDTH)));
		cam.position.y = MathUtils.round(MathUtils.clamp(cam.position.y, screenBoundary(SCREENHEIGHT), MapHandler.mapHeight - screenBoundary(SCREENHEIGHT)));
		
		if (!UptiltEngine.outOfHitlag()) shakeScreen();
		
		cam.update();
		renderer.setView(cam);
	}
	
	private static float screenBoundary(float dimension){
		return dimension/(screenAdjust/ZOOM) + GlobalRepo.TILE;
	}
	
	static void updateGraphics(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		int[] arr;

		arr = new int[]{0};  // render back
		renderer.render(arr);

		int numLayers = MapHandler.activeMap.getLayers().getCount() - 1;  // render tiles
		arr = new int[numLayers];
		for (int i = 0; i < arr.length; ++i) arr[i] = i+1;
		renderer.render(arr);

		batch.begin();  // render entities
		for (Entity e: MapHandler.activeRoom.getEntityList()){
			if (e instanceof Fighter) {
				Fighter fi = (Fighter) e;
				drawFighterPercentage(fi);
				if (!fi.hitstunTimer.timeUp()) batch.setColor(1, 0.25f, 0.25f, 1);
				else if (fi.isInvincible()) batch.setColor(0.25f, 0.75f, 1f, 1);
				else if (fi.isCharging()) batch.setColor(1, 1, 1, 1);
				
				if (fi.getTeam() == GlobalRepo.BAD) batch.setColor(batch.getColor().r, batch.getColor().g - 0.8f, batch.getColor().b, 1);
				if (fi.getTeam() == GlobalRepo.GOOD) batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b - 0.6f, 1);
			}
			batch.draw(e.getImage(), e.getPosition().x, e.getPosition().y);
			batch.setColor(1, 1, 1, 1);
		}
		batch.end();
		font.setColor(1, 1, 1, 1);

		arr = new int[]{numLayers-1};  // render foreground
		renderer.render(arr);
		
		if (!debug) return;
		Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeType.Filled);
		for (ActionCircle ac: MapHandler.activeRoom.getActionCircleList()){
			Circle c = ac.getArea();
			debugRenderer.setColor(ac.getColor());
			if (ac.toRemove()) debugRenderer.setColor(0.9f, 1, 1, 0.5f);
			debugRenderer.circle(c.x, c.y, c.radius);
		}
		debugRenderer.end();
	}

	private static void drawFighterPercentage(Entity e) {
		Fighter fi = (Fighter) e;
		float darken = fi.getPercentage()*0.0075f;
		font.setColor(1, 1 - darken, 1 - (darken*1.1f), 1);
		float xPos = fi.getPosition().x - 8 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getHurtBox().getHeight() + font.getLineHeight();
		font.draw(batch, (int)fi.getPercentage() + "%", xPos, yPos);
	}

	public static void updateRoomGraphics(Fighter player) {
		renderer = new OrthogonalTiledMapRenderer(MapHandler.activeMap, 1);
		cam.position.x = player.getPosition().x;
		cam.position.y = player.getPosition().y;
		cam.update();
		renderer.setView(cam);
	}
	
	private static void shakeScreen(){
		cam.position.x += shakeScreenHelper();
		cam.position.y += shakeScreenHelper();
	}
	
	private static double shakeScreenHelper() { 
		double posOrNeg = Math.signum(0.5 - Math.random());
		return posOrNeg * 5.0 * (0.9 + (Math.random()/10.0));
	}
	
}
