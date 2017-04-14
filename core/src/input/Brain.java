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
	final Timer changeUpDown = new DurationTimer(90);
	final List<Timer> timerList = new ArrayList<Timer>();
	public Brain (InputHandlerCPU body){
		this.body = body;
		timerList.add(changeUpDown);
	}
	private final float ran = 0.9f;
	void update(InputPackage pack){
		for (Timer t: timerList) {
			if (Math.random() < ran) t.countUp();
		}
	}
	boolean isCharging(InputPackage pack){
		return false;
	}
	
	float setInput(float value){
		return MathUtils.clamp(value, -1, 1);
	}

	void attemptRecovery(InputPackage pack, Timer waitToUseUpSpecial){
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
	
	void jumpTowardPlayer(Timer tryJump, Timer performJump, InputPackage pack){
		tryJump.restart();
		if (performJump.timeUp() && (Math.random() < (-pack.distanceYFromPlayer/300f)) ) {
			body.handleCommand(InputHandler.commandJump);
			performJump.restart();
		}
	}
	
	void performJump(Timer performJump){
		body.handleJumpHeld();
		if (Math.random() < 0.5) performJump.countUp(); // modulates jump height
	}

	void attackPlayerClose(float aggressiveness, InputPackage pack){
		if (Math.random() < aggressiveness) {
			if (pack.direct == Math.signum(pack.distanceXFromPlayer)) body.xInput *= -1; // turn around if behind
			if (Math.random() < 0.2) body.handleCommand(InputHandler.commandCharge);
			else body.handleCommand(InputHandler.commandAttack);
		}
	}
	
	void headTowardPlayer(Timer changeDirection, InputPackage pack){
		if (Math.random() < 0.5) body.xInput = setInput(-pack.distanceXFromPlayer);
		// setInput(setInput(-pack.distanceXFromPlayer)/2 + body.xInput);
		else if (Math.random() < 0.15) {
			if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
			else body.handleCommand(InputHandler.commandStickLeft);
		}
		else if (Math.random() < 0.3) body.xInput = 0;
		changeDirection.restart();
	}

	void changeUpDown(){
		double ud = (1 - (2 * Math.random()) );
		ud = Math.signum(ud) * Math.pow(Math.abs(ud), 0.1);
		body.yInput = (float) MathUtils.clamp(ud, -1, 1);
		System.out.println();
		changeUpDown.restart();
	}

	/* brains */

	public static class Braindead extends Brain{ // does nothing

		public Braindead(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			/* */
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
			if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJump);
			if (changeDI.timeUp()) changeUpDown(); 
		}

	}

	public static class MookBrain extends Brain{

		Timer waitToUseUpSpecial = new Timer(30);
		Timer tryJump = new Timer(30);
		Timer performJump = new Timer(20);
		Timer changeDirection = new Timer(30);
		float aggressiveness = 1f;

		public MookBrain(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(changeUpDown, waitToUseUpSpecial, tryJump, changeDirection, performJump));
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection, pack);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.WALLSLIDE || Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpTowardPlayer(tryJump, performJump, pack);
			else if (Math.abs(pack.distanceYFromPlayer - 30) < 60 && Math.abs(pack.distanceXFromPlayer) < 30) attackPlayerClose(aggressiveness, pack);
			else if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			if (changeUpDown.timeUp()) changeUpDown(); 
		}
		
		boolean isCharging(InputPackage pack){
			if (Math.random() < 0.9) return true;
			else return false;
		}

	}

	public static class Advanced extends Brain{

		public Advanced(InputHandlerCPU body) {
			super(body);
		}

		boolean isCharging(InputPackage pack) {
			return false; // if player is far away enough return true, else return false
		}

	}
}
