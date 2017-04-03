package moves;

import entities.Fighter;

public abstract class Effect extends Action{

	private final int start, end;
	
	Effect(int start, int end){
		this.start = start;
		this.end = end;
	}
	
	public int getStart(){ return start; }
	public int getEnd() { return end; }
	
	public static class ConstantVelocity extends Effect{
		float velX, velY;
		Fighter user;
		public static final float noChange = -999f;
		
		ConstantVelocity(Fighter user, float velX, float velY, int start, int end){
			super(start, end);
			this.user = user;
			this.velX = velX;
			this.velY = velY;
		}
		
		void performAction(){
			if (velX != noChange) user.getVelocity().x = velX;
			if (velY != noChange) user.getVelocity().y = velY;
		}
	}
	
}