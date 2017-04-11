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
	abstract void finish();
	
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
			if (velX != noChange) user.getVelocity().x = velX * user.direct();
			if (user.doesCollide(user.getPosition().x, user.getPosition().y + velY)) {
				System.out.println("basement");
				velY = noChange; 
				return;
			}
			if (velY != noChange) user.getVelocity().y = velY;
		}

		void finish() {

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
			List<Integer> actionStartTimes = move.eventList.actionStartTimes;
			for (int i = 0; i < actionStartTimes.size(); ++i) actionStartTimes.set(i, actionStartTimes.get(i) + 1);
			move.addFrame();
			user.attackTimer.countDown();
		}
		
		float getHeldCharge(){
			return heldCharge;
		}

		void finish() {
			
		}
	}
	
	public static class Armor extends Effect {
		private final Move move;
		private final float armor;

		Armor(Move move, int start, int end, float armor) {
			super(start, end);
			this.move = move;
			this.armor = armor;
		}
		
		void performAction() {
			move.setArmor(armor);
		}

		void finish() {
			move.setArmor(0);
		}

	}
	
}