package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

import timers.DurationTimer;
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

		Timer changeDI = new DurationTimer(60);
		Timer waitToUseUpSpecial = new Timer(30);
		
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
	
	public static class Basic extends Brain{ // Recovers, walks/runs at player, and attacks with normals
		
		Timer changeDI = new DurationTimer(60);
		Timer waitToUseUpSpecial = new Timer(30);
		Timer tryJump = new Timer(30);
		Timer performJump = new Timer(20);
		Timer changeDirection = new Timer(30);
		float aggressiveness = 0.1f;

		public Basic(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(changeDI, waitToUseUpSpecial, tryJump, changeDirection, performJump));
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()){
				if (Math.random() < 0.5) body.xInput = setInput(-pack.distanceXFromPlayer);
				else if (Math.random() < 0.2) {
					if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
					else body.handleCommand(InputHandler.commandStickLeft);
				}
				else if (Math.random() < 0.2) body.xInput = 0;
				changeDirection.restart();
			}
			if (!performJump.timeUp()) {
				body.handleCommand(InputHandler.commandJump);
				if (Math.random() < 0.5) performJump.countUp(); // modulates jump height
			}
			else if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) {
				tryJump.restart();
				if (performJump.timeUp() && Math.random() < (-pack.distanceYFromPlayer/300f)) {
					body.handleJumpCommand();
					performJump.restart();
				}
			}
			else if (Math.abs(pack.distanceYFromPlayer) < 80 && Math.abs(pack.distanceXFromPlayer) < 80) {
				if (Math.random() < aggressiveness) {
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
