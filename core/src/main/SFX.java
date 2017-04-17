package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public abstract class SFX {
	
	Sound sfx = null;
	public void play() {
		if (null != sfx) sfx.play(UptiltEngine.getVolume());
	}
	void setSFX(String url){
		sfx = Gdx.audio.newSound(Gdx.files.internal("sfx" + url));
	}
	
	public static class None extends SFX{ }
	
	public static class LightHit extends SFX{ public LightHit(){ setSFX("/melee/softhit.wav"); } }
	
	public static class MidHit extends SFX{ public MidHit(){ setSFX("/melee/smack.wav"); } }
	
	public static class MeatyHit extends SFX{ public MeatyHit(){ setSFX("/melee/blow.wav"); } }
	
	public static class HeavyHit extends SFX{ public HeavyHit(){ setSFX("/melee/heavy2.wav"); } }
	
	public static class LaserFire extends SFX{
		public LaserFire(){ setSFX("/melee/pew.wav"); }
	}
	
	public static class ChargeLaserFire extends SFX{
		public ChargeLaserFire(){ setSFX("/melee/vwoop.wav"); }
	}
	
	public static class Explode extends SFX{
		public Explode(){ setSFX("/melee/explode.wav"); }
	}
	
	public static class HomeRun extends SFX{
		public HomeRun(){ setSFX("/melee/KRRIIIIING.wav"); }
	}
	
}
