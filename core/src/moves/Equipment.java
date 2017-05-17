package moves;

public abstract class Equipment {

	public float getSpeedMod() { return 1; }
	public float getPowerMod() { return 1; }
	public float getWeightMod() { return 1; }
	public float getArmorMod() { return 0; }
	public float getJumpAccMod() { return 1; }
	public float getWalkAccMod() { return 1; }
	public float getWalkSpeedMod() { return 1; }
	public float getRunAccMod() { return 1; }
	public float getRunSpeedMod() { return 1; }
	public float getAirAccMod() { return 1; }
	public float getAirSpeedMod() { return 1; }
	public float getFallSpeedMod() { return 1; }
	public float getGravityMod() { return 1; }
	public float getFrictionMod() { return 1; }
	public float getAirFrictionMod() { return 1; }
	public float getWallSlideMod() { return 1; }
	
	public static class Default extends Equipment{}
	public static class SpringShoes extends Equipment{
		@Override public float getJumpAccMod() { return 1.3f; }
	}
	public static class FocusBand extends Equipment{
		@Override public float getArmorMod() { return 1; }
	}
	public static class SlipSocks extends Equipment{
		@Override public float getFrictionMod() { return 0.25f; }
	}
	public static class IronBoots extends Equipment{
		@Override public float getWeightMod() { return 1.25f; }
	}
	public static class LeadBoots extends Equipment{
		@Override public float getWeightMod() { return 1.6f; }
		@Override public float getFrictionMod() { return 1.5f; }
		@Override public float getArmorMod() { return 2; }
		@Override public float getSpeedMod() { return 0.8f; }
		@Override public float getFallSpeedMod() { return 1.2f; }
		@Override public float getGravityMod() { return 1.2f; }
	}
	public static class OverDriveHelmet extends Equipment{
		@Override public float getPowerMod() { return 1.3f; }
		@Override public float getSpeedMod() { return 1.2f; }
		@Override public float getWeightMod() { return 0.5f; }
	}
	public static class NinjaBoots extends Equipment{
		@Override public float getSpeedMod() { return 1.35f; }
		@Override public float getFrictionMod() { return 1.6f; }
		@Override public float getWeightMod() { return 0.75f; }
		@Override public float getJumpAccMod() { return 1.2f; }
		@Override public float getFallSpeedMod() { return 1.3f; }
		@Override public float getGravityMod() { return 1.1f; }
		@Override public float getWallSlideMod() { return 0.1f; }
		@Override public float getArmorMod() { return -1; }
		
	}
	
}
