package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public abstract class SFX {
	
	String url = null;
	
	public void play() {
		if (null != url) Gdx.audio.newSound(Gdx.files.internal(url)).play(UptiltEngine.getVolume());
	}
	
	public void playDirectional(float pan){
		if (null != url) {
			Sound sfx = Gdx.audio.newSound(Gdx.files.internal(url));
			sfx.setPan(sfx.play(), pan, UptiltEngine.getVolume());
		}
	}
	
	protected void setSFX(String url){
		this.url = "sfx" + url;
	}
	
	public static class None extends SFX{ }
	public static class LightHit extends SFX{ public LightHit(){ setSFX("/melee/softhit.wav"); } }
	public static class MidHit extends SFX{ public MidHit(){ setSFX("/melee/smack.wav"); } }
	public static class MeatyHit extends SFX{ public MeatyHit(){ setSFX("/melee/blow.wav"); } }
	public static class HeavyHit extends SFX{ public HeavyHit(){ setSFX("/melee/heavy2.wav"); } }
	public static class LaserFire extends SFX{ public LaserFire(){ setSFX("/melee/pew.wav"); } }
	public static class ChargeLaserFire extends SFX{ public ChargeLaserFire(){ setSFX("/melee/vwoop.wav"); } }
	public static class Explode extends SFX{ public Explode(){ setSFX("/melee/explode.wav"); } }
	public static class HomeRun extends SFX{ public HomeRun(){ setSFX("/melee/KRRIIIIING.wav"); } }
	public static class FootStool extends SFX{ public FootStool(){ setSFX("/footstool.wav"); } }
	public static class Ground extends SFX{ public Ground(){ setSFX("/land.mp3"); } }
	
}
