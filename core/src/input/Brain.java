package input;

import timers.Timer;
import entities.Entity.State;
import entities.InputPackage;

public abstract class Brain{
	final InputHandlerCPU body;
	public Brain (InputHandlerCPU body){
		this.body = body;
	}
	abstract void update(InputPackage pack);
	abstract boolean isCharging();

	public static class Braindead extends Brain{ // does nothing

		public Braindead(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
		}

		boolean isCharging(){
			return false;
		}

	}

	public static class Recover extends Brain{ // Recovers if thrown offstage, mixes up vertical DI

		public Recover(InputHandlerCPU body) {
			super(body);
		}

		Timer changeDI = new Timer(60, true);
		Timer waitToUseUpSpecial = new Timer(30, false);

		void update(InputPackage pack){
			body.xInput = 0;
			if (pack.isOffStage){
				body.yInput = 1;
				if (pack.distanceFromCenter < 0) body.xInput = 1;
				else body.xInput = -1;
				if (pack.isBelowStage){
					if (!pack.hasDoubleJumped) {
						body.handleCommand(InputHandler.commandJumpX);
						waitToUseUpSpecial.restart();
					}
					else if (waitToUseUpSpecial.timeUp()) body.handleCommand(InputHandler.commandSpecial);
				}
			}
			if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJumpX);
			changeDI.countUp();
			waitToUseUpSpecial.countUp();
			if (changeDI.timeUp()) changeDI(); 
		}

		boolean isCharging(){
			return false;
		}

		private void changeDI(){
			body.yInput = (float) (1 - (2 * Math.random()) );
			changeDI.restart();
		}

	}
}
