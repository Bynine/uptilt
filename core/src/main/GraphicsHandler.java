package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.badlogic.gdx.math.Rectangle;
import entities.Entity;
import entities.Fighter;

public class GraphicsHandler {

	private static SpriteBatch batch;
	private static final OrthographicCamera cam = new OrthographicCamera();
	private static final OrthographicCamera parallaxCam = new OrthographicCamera();
	private static final int camAdjustmentSpeed = 8;
	private static OrthogonalTiledMapRenderer renderer;
	private static final float screenAdjust = 2f;
	private static final ShapeRenderer debugRenderer = new ShapeRenderer();
	private static BitmapFont font = new BitmapFont();

	public static final int SCREENWIDTH  = (int) ((48 * GlobalRepo.TILE));
	public static final int SCREENHEIGHT = (int) ((24 * GlobalRepo.TILE));
	private static final float ZOOM2X = 1/2f;
	@SuppressWarnings("unused") private static final float ZOOM1X = 1/1f;
	static float ZOOM = ZOOM2X;

	public static void begin() {
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		cam.zoom = ZOOM;
		parallaxCam.setToOrtho(false, SCREENWIDTH, SCREENHEIGHT);
		parallaxCam.zoom = ZOOM;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nes.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 8;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	public static java.util.function.Predicate<? super Fighter> isDead() {
		return p -> p.getStocks() <= 0;
	}

	static void updateCamera(){
		float posX = 0;
		float posY = 0;
		List<Fighter> livePlayers = new ArrayList<Fighter>();
		livePlayers.addAll(UptiltEngine.getPlayers());
		livePlayers.removeIf(isDead());
		for (Fighter player: livePlayers){
			posX += player.getPosition().x / livePlayers.size();
			posY += player.getPosition().y / livePlayers.size();
		}
		cam.position.x = (cam.position.x*(camAdjustmentSpeed-1) + posX)/camAdjustmentSpeed;
		cam.position.y = (cam.position.y*(camAdjustmentSpeed-1) + GlobalRepo.TILE * 2 + posY)/camAdjustmentSpeed;

		cam.position.x = MathUtils.round(MathUtils.clamp(cam.position.x, screenBoundary(SCREENWIDTH), MapHandler.mapWidth - screenBoundary(SCREENWIDTH)));
		cam.position.y = MathUtils.round(MathUtils.clamp(cam.position.y, screenBoundary(SCREENHEIGHT), MapHandler.mapHeight - screenBoundary(SCREENHEIGHT)));

		parallaxCam.position.x = cam.position.x/2 + SCREENWIDTH/4;
		parallaxCam.position.y = cam.position.y;

		if (!UptiltEngine.outOfHitlag()) shakeScreen();

		cam.update();
		parallaxCam.update();
	}

	private static float screenBoundary(float dimension){
		return dimension/(screenAdjust/ZOOM) + GlobalRepo.TILE * 3;
	}

	static void updateGraphics(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		int[] arr;
		renderer.setView(parallaxCam);

		arr = new int[]{0, 1};  // render back
		renderer.render(arr);

		renderer.setView(cam);
		int numLayers = MapHandler.activeMap.getLayers().getCount() - 2;  // render tiles
		arr = new int[numLayers];
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = i + 2;
		}
		renderer.render(arr);

		batch.begin();  // render entities
		for (Entity e: MapHandler.activeRoom.getEntityList()) renderEntity(e);
		batch.end();
		font.setColor(1, 1, 1, 1);

		float stockLocationMod = 1/4.5f;
		float lineHeight = 6;
		batch.begin();
		for (Fighter player: UptiltEngine.getPlayers()){
			font.draw(batch, "stocks: " + player.getStocks(), 
					cam.position.x - SCREENWIDTH * stockLocationMod, cam.position.y - SCREENHEIGHT * stockLocationMod + lineHeight);
			lineHeight *= -1/2;
		}
		if (UptiltEngine.isPaused()) font.draw(batch, "PAUSED", cam.position.x, cam.position.y);
		batch.end();

		arr = new int[]{numLayers-1};  // render foreground
		renderer.render(arr);

		if (UptiltEngine.debugToggle) debugRender();
	}

	private static void renderEntity(Entity e){
		if (e instanceof Fighter) {
			Fighter fi = (Fighter) e;
			drawFighterPercentage(fi);
			if (isOffScreen(fi) && !fi.isInHitstun()) drawFighterIcon(fi);
			if (UptiltEngine.debugToggle) drawState(e);
			batch.setColor(fi.getColor());
			if (!fi.hitstunTimer.timeUp()) 
				batch.setColor(batch.getColor().r, batch.getColor().g - 0.5f, batch.getColor().g - 0.5f, 1);
			else if (fi.isInvincible()) 
				batch.setColor(batch.getColor().r - 0.5f, batch.getColor().g * 2, batch.getColor().g * 2, 1);
			else if (fi.isCharging()) 
				batch.setColor(batch.getColor().r + 0.2f, batch.getColor().g + 0.2f, batch.getColor().g + 0.2f, 1);

			if (fi.getTeam() == GlobalRepo.BADTEAM) batch.setColor(batch.getColor().r, batch.getColor().g - 0.4f, batch.getColor().b, 1);
			if (fi.getTeam() == GlobalRepo.GOODTEAM) batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b - 0.4f, 1);
		}
		batch.draw(e.getImage(), e.getPosition().x, e.getPosition().y);
		batch.setColor(1, 1, 1, 1);
	}

	private static boolean isOffScreen(Entity e){
		boolean left = e.getPosition().x + e.getImage().getWidth() < cameraBoundaries().get(0);
		boolean right = e.getPosition().x > cameraBoundaries().get(1);
		boolean top = e.getPosition().y + e.getImage().getHeight() < cameraBoundaries().get(2);
		boolean bottom = e.getPosition().y > cameraBoundaries().get(3);
		return left || right || top || bottom;
	}

	private final static float iconDimension = 32;
	private static void drawFighterIcon(Fighter fi){
		float iconX = MathUtils.clamp(fi.getPosition().x, cameraBoundaries().get(0), cameraBoundaries().get(1) - iconDimension);
		float iconY = MathUtils.clamp(fi.getPosition().y, cameraBoundaries().get(2), cameraBoundaries().get(3) - iconDimension);
		batch.draw(fi.getIcon(), iconX, iconY);
	}

	private static List<Float> cameraBoundaries(){
		return new ArrayList<Float>(Arrays.asList( 
				(cam.position.x - SCREENWIDTH/2*ZOOM),
				(cam.position.x + SCREENWIDTH/2*ZOOM),
				(cam.position.y - SCREENHEIGHT/2*ZOOM),
				(cam.position.y + SCREENHEIGHT/2*ZOOM)
				) );
	}

	private static void drawFighterPercentage(Entity e) {
		Fighter fi = (Fighter) e;
		float darken = fi.getPercentage()*0.0075f;
		font.setColor(1, 1 - darken, 1 - (darken*1.1f), 1);
		float xPos = fi.getPosition().x - 8 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight();
		font.draw(batch, (int)fi.getPercentage() + "%", xPos, yPos);
	}

	private static void drawState(Entity e) {
		Fighter fi = (Fighter) e;
		float xPos = fi.getPosition().x - 16 + fi.getImage().getWidth()/2;
		float yPos = fi.getPosition().y + fi.getImage().getHeight() + font.getLineHeight() * 2;
		font.draw(batch, fi.getState().toString(), xPos, yPos);
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

	public static void debugRender(){
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
		debugRenderer.setColor(0, 1, 0, 0.5f);
		for (Entity e: MapHandler.activeRoom.getEntityList()){
			Rectangle r = e.getHurtBox();
			if (e instanceof Fighter) {
				debugRenderer.rect(r.x, r.y, r.width, r.height);
			}
		}
		debugRenderer.end();
	}

}
