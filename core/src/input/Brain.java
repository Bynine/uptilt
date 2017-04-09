package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import timers.Timer;
import entities.Entity.State;
import entities.InputPackage;

public abstract class Brain{
	final InputHandlerCPU body;
	final List<Timer> timerList = new ArrayList<Timer>();
	public Brain (InputHandlerCPU body){
		this.body = body;
	}
	void update(InputPackage pack){
		for (Timer t: timerList) t.countUp();
	}
	abstract boolean isCharging();
	
	void attemptRecovery(InputPackage pack, Timer waitToUseUpSpecial){
		if (pack.isOffStage){
			body.yInput = 1;
			if (pack.distanceFromCenter < 0) body.xInput = 1;
			else body.xInput = -1;
			if (pack.isBelowStage){
				if (!pack.hasDoubleJumped) {
					body.handleCommand(InputHandler.commandJump);
					waitToUseUpSpecial.restart();
				}
				else if (waitToUseUpSpecial.timeUp()) body.handleCommand(InputHandler.commandSpecial);
			}
		}
	}
	
	float setInput(float value){
		return MathUtils.clamp(value, -1, 1);
	}

	public static class Braindead extends Brain{ // does nothing

		public Braindead(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			/* */
		}

		boolean isCharging(){
			return false;
		}

	}

	public static class Recover extends Brain{ // Recovers if thrown offstage, mixes up vertical DI

		Timer changeDI = new Timer(60, true);
		Timer waitToUseUpSpecial = new Timer(30, false);
		
		public Recover(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(changeDI, waitToUseUpSpecial));
		}

		void update(InputPackage pack){
			super.update(pack);
			body.xInput = 0;
			attemptRecovery(pack, waitToUseUpSpecial);
			if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJump);
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
	
	public static class Basic extends Brain{ // Walks at player and attacks with normals
		
		Timer changeDI = new Timer(60, true);
		Timer waitToUseUpSpecial = new Timer(30, false);
		Timer tryJump = new Timer(30, false);
		Timer changeDirection = new Timer(30, false);

		public Basic(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(changeDI, waitToUseUpSpecial, tryJump, changeDirection));
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()){
				if (Math.random() < 0.2) body.xInput = 0;
				if (Math.random() < 0.5) body.xInput = setInput(-pack.distanceXFromPlayer);
				changeDirection.restart();
			}
			if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 0 && tryJump.timeUp()) {
				tryJump.restart();
				if (Math.random() < 0.5) body.handleCommand(InputHandler.commandJump);
			}
			else if (Math.abs(pack.distanceYFromPlayer) < 80 && Math.abs(pack.distanceXFromPlayer) < 80) {
				if (Math.random() < 0.05) {
					if (pack.direct == Math.signum(pack.distanceXFromPlayer)) body.xInput *= -1; // turn around if behind
					body.handleCommand(InputHandler.commandAttack);
				}
			}
			else attemptRecovery(pack, waitToUseUpSpecial);
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
