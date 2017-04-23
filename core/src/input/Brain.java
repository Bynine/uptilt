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
	Timer waitToUseUpSpecial = new Timer(30);
	Timer tryJump = new Timer(30);
	Timer performJump = new Timer(20);
	Timer changeDirection = new Timer(30);
	Timer dodgeTimer = new Timer(6);
	float aggressiveness = 0.5f;
	public Brain (InputHandlerCPU body){
		this.body = body;
		timerList.addAll(Arrays.asList(changeUpDown, waitToUseUpSpecial, tryJump, changeDirection, performJump, dodgeTimer) );
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

	void getUp(){
		if (Math.random() < 0.4)  body.handleCommand(InputHandler.commandStickDown);
		else {
			if (Math.random() < 0.5) body.handleCommand(InputHandler.commandStickLeft);
			else body.handleCommand(InputHandler.commandStickRight);
		}
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
		if (performJump.timeUp() && (Math.random() < (-pack.distanceYFromPlayer/100f)) ) {
			body.handleCommand(InputHandler.commandJump);
			performJump.restart();
		}
	}

	void performJump(Timer performJump){
		body.handleJumpHeld();
		if (Math.random() < 0.5) performJump.countUp(); // modulates jump height
	}

	void attackPlayer(InputPackage pack, int command){
		facePlayer(pack);
		body.handleCommand(command);
	}

	void facePlayer(InputPackage pack){
		if (pack.direct == Math.signum(pack.distanceXFromPlayer)) body.xInput *= -1; // turn around if behind
		if (pack.distanceYFromPlayer < -20 && Math.random() < 0.5) body.yInput = 1;
	}

	void headTowardPlayer(Timer changeDirection, InputPackage pack){
		body.xInput = setInput(-pack.distanceXFromPlayer);
		if (Math.random() < 0.3 && Math.abs(pack.distanceXFromPlayer) > 300) { // run toward
			if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
			else body.handleCommand(InputHandler.commandStickLeft);
		}
		else if (Math.random() < 0.1) body.xInput = 0;
		changeDirection.restart();
	}

	void changeUpDown(){
		double ud = (1 - (2 * Math.random()) );
		ud = Math.signum(ud) * Math.pow(Math.abs(ud), 0.7);
		body.yInput = (float) MathUtils.clamp(ud, -1, 1);
		changeUpDown.restart();
	}

	void crouchBeforeAttacking(){
		body.yInput = -1;
		body.xInput = 0;
	}
	
	boolean inVerticalAttackRange(InputPackage pack){
		return Math.abs(pack.distanceYFromPlayer - 40) < 90 && !pack.isGrounded || Math.abs(pack.distanceYFromPlayer) < 40;
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

		public MookBrain(InputHandlerCPU body) {
			super(body);
			aggressiveness = 0.3f;
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection, pack);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.FALLEN && Math.random() < 0.02) getUp();
			else if (pack.state == State.WALLSLIDE || Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpTowardPlayer(tryJump, performJump, pack);
			else if (inVerticalAttackRange(pack)) {
				if (Math.abs(pack.distanceXFromPlayer) < 30 && Math.random() < 0.02) crouchBeforeAttacking();
				if (Math.abs(pack.distanceXFromPlayer) < 70 && Math.random() < 0.02 && pack.isGrounded) performJump(performJump);
				if (Math.random() < 0.8 && Math.abs(pack.distanceXFromPlayer) < 30) attackPlayer(pack, InputHandler.commandAttack);
				else if (Math.abs(pack.distanceXFromPlayer) < 60 && Math.random() < 0.05) attackPlayer(pack, InputHandler.commandCharge);
			}
			else if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		boolean isCharging(InputPackage pack){
			if (Math.random() < 0.9) return true;
			else return false;
		}

	}

	public static class GunminBrain extends Brain{

		public GunminBrain(InputHandlerCPU body) {
			super(body);
			aggressiveness = 0.1f;
		}

		void update(InputPackage pack){
			super.update(pack);
			if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			else if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 100) headTowardPlayer(changeDirection, pack);
			else if (!performJump.timeUp()) performJump(performJump);
			else if (pack.state == State.FALLEN && Math.random() < 0.02) getUp();
			else if (pack.state == State.WALLSLIDE || Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpTowardPlayer(tryJump, performJump, pack);
			if (Math.abs(pack.distanceYFromPlayer) < 100){
				if (Math.abs(pack.distanceXFromPlayer) < 30) attackPlayerClose(pack);
				else if (Math.abs(pack.distanceXFromPlayer) < 600 && Math.random() < 0.05) attackPlayerDistant(pack);
			}
			if (pack.state == State.WALLSLIDE) body.handleCommand(InputHandler.commandJump);
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		private void attackPlayerDistant(InputPackage pack){ // ranged moves are fweak, uweak, dweak, fair, uair, dair
			if (Math.abs(body.xInput) < 0.8 && Math.abs(body.yInput) < 0.8) body.xInput = pack.direct;
			if (pack.distanceYFromPlayer < 20) body.yInput = 1;
			else if (body.yInput > 0) body.yInput *= -1;
			body.handleCommand(InputHandler.commandAttack);
		}

		private void attackPlayerClose(InputPackage pack){ // close moves are nweak, nair, bair
			if (!(!pack.isGrounded && body.xInput < 0)) body.xInput = 0;
			body.handleCommand(InputHandler.commandAttack);
		}

		boolean isCharging(InputPackage pack){
			return false;
		}

	}

	public static class SpeedyBrain extends Brain{

		public SpeedyBrain(InputHandlerCPU body) {
			super(body);
			aggressiveness = 0.6f;
			waitToUseUpSpecial.setEndTime(15);
		}

		void update(InputPackage pack){
			super.update(pack);
			if (pack.state == State.WALLSLIDE) return;
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection, pack);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.FALLEN && Math.random() < 0.02) getUp();
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpTowardPlayer(tryJump, performJump, pack);
			else if (inVerticalAttackRange(pack)) {
				if (Math.abs(pack.distanceXFromPlayer) < 30 && Math.random() < 0.02) crouchBeforeAttacking();
				if (Math.abs(pack.distanceXFromPlayer) < 70 && Math.random() < 0.02 && pack.isGrounded) performJump(performJump);
				if (Math.random() < 0.8) {
					if (pack.isRunning && Math.abs(pack.distanceXFromPlayer) < 100) attackPlayer(pack, InputHandler.commandAttack);
					else if (Math.abs(pack.distanceXFromPlayer) < 30) attackPlayer(pack, InputHandler.commandAttack);
				}
				else if (Math.random() < 0.05 && Math.abs(pack.distanceXFromPlayer) < 60) attackPlayer(pack, InputHandler.commandCharge);
			}
			if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		boolean isCharging(InputPackage pack){
			if (Math.random() < 0.9) return true;
			else return false;
		}

		void headTowardPlayer(Timer changeDirection, InputPackage pack){
			body.xInput = setInput(-pack.distanceXFromPlayer);
			if (Math.random() < 0.75 && Math.abs(pack.distanceXFromPlayer) > 100) { // run toward
				if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
				else body.handleCommand(InputHandler.commandStickLeft);
			}
			changeDirection.restart();
		}

		void attemptRecovery(InputPackage pack, Timer waitToUseUpSpecial){
			if (pack.distanceFromCenter < 0) body.xInput = 1;
			else body.xInput = -1;
			if (pack.awayFromWall){
				if (!pack.hasDoubleJumped) {
					body.handleCommand(InputHandler.commandJump);
					waitToUseUpSpecial.restart();
				}
				else if (waitToUseUpSpecial.timeUp()) body.handleCommand(InputHandler.commandSpecial);
			}
		}

	}

	public static class KickerBrain extends Brain{

		public KickerBrain(InputHandlerCPU body) {
			super(body);
			aggressiveness = 0.5f;
			changeDirection.setEndTime(20);
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection, pack);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.FALLEN && Math.random() < 0.05) getUp();
			else if (pack.state == State.WALLSLIDE || Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpTowardPlayer(tryJump, performJump, pack);
			else if (pack.isOffStage) attemptRecovery(pack, waitToUseUpSpecial);
			else if (inVerticalAttackRange(pack)){
				if (Math.abs(pack.distanceXFromPlayer) < 30 && Math.random() < 0.02) crouchBeforeAttacking();
				if (Math.abs(pack.distanceXFromPlayer) < 80 && Math.random() < 0.02 && pack.isGrounded) performJump(performJump);
				if (Math.abs(pack.distanceXFromPlayer) < 30){
					if (Math.random() < 0.5) attackPlayer(pack, InputHandler.commandGrab);
					else pickLongerMove(pack);
				}
				else if (Math.abs(pack.distanceXFromPlayer) < 70) pickLongerMove(pack);
			}
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		boolean isCharging(InputPackage pack){
			if (Math.random() < 0.9) return true;
			else return false;
		}

		void pickLongerMove(InputPackage pack){
			if (Math.random() < 0.2) attackPlayer(pack, InputHandler.commandCharge);
			else attackPlayer(pack, InputHandler.commandAttack);
		}

	}
}
