package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public abstract class SFX {
	
	Sound sfx = null;
	float volume = 1f;
	public void play() {
		if (null != sfx) sfx.play(volume);
	}
	
	public static class None extends SFX{ }
	
	public static class LightHit extends SFX{
		public LightHit(){ sfx = Gdx.audio.newSound(Gdx.files.internal("sfx/melee/softhit.wav")); }
	}
	
	public static class MidHit extends SFX{
		public MidHit(){ sfx = Gdx.audio.newSound(Gdx.files.internal("sfx/melee/smack.wav")); }
	}
	
	public static class HeavyHit extends SFX{
		public HeavyHit(){ sfx = Gdx.audio.newSound(Gdx.files.internal("sfx/melee/heavy2.wav")); }
	}
	
	public static class Explode extends SFX{
		public Explode(){ sfx = Gdx.audio.newSound(Gdx.files.internal("sfx/melee/explode.wav")); }
	}
}
