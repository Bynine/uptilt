package moves;

import java.util.List;

import entities.Fighter;

public abstract class Effect extends Action{

	protected int start, end;
	
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
	
	public static class Charge extends Effect{
		private float heldCharge = 1;
		private final float chargeSpeed;
		private final Move move;
		private final Fighter user;
		private final int maxTime;

		Charge(int start, int maxTime, float chargeSpeed, Fighter user, Move move) {
			super(start, start+1);
			this.chargeSpeed = chargeSpeed;
			this.move = move;
			this.user = user;
			this.maxTime = maxTime;
		}
		
		void performAction() {
			if (user.isCharging() && move.duration.getCounter() <= maxTime) hold();
			heldCharge += chargeSpeed;
		}
		
		private void hold(){
			this.end += 1;
			List<Integer> actionStartTimes = move.actionList.actionStartTimes;
			for (int i = 0; i < actionStartTimes.size(); ++i) actionStartTimes.set(i, actionStartTimes.get(i) + 1);
			move.duration.setEndTime(move.duration.getEndTime() + 1);
		}
		
		float getHeldCharge(){
			return heldCharge;
		}
	}
	
}