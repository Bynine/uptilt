package inputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import timers.DurationTimer;
import timers.Timer;
import entities.Entity.State;
import entities.InputPackage;

public abstract class Brain{
	protected final InputHandlerCPU body;
	protected final Timer changeUpDown = new DurationTimer(90);
	protected final List<Timer> timerList = new ArrayList<Timer>();
	protected final Timer waitToUseUpSpecial = new Timer(30);
	protected final Timer tryJump = new Timer(30);
	protected final Timer performJump = new Timer(20);
	protected final Timer changeDirection = new Timer(30);
	protected final Timer dodgeTimer = new Timer(6);
	protected InputPackage pack;
	
	public Brain (InputHandlerCPU body){
		this.body = body;
		timerList.addAll(Arrays.asList(changeUpDown, waitToUseUpSpecial, tryJump, changeDirection, performJump, dodgeTimer) );
	}
	
	void update(InputPackage pack){
		this.pack = pack;
		float random = 0.9f;
		for (Timer t: timerList) if (Math.random() < random) t.countUp();
	}
	
	/* BEHAVIORS */

	float setInput(float value){
		return MathUtils.clamp(value, -1, 1);
	}
	
	boolean isCharging(){
		if (Math.random() < 0.95) return true;
		else return false;
	}

	void getUp(){
		if (Math.random() < 0.4)  body.handleCommand(InputHandler.commandStickDown);
		else {
			if (Math.random() < 0.5) body.handleCommand(InputHandler.commandStickLeft);
			else body.handleCommand(InputHandler.commandStickRight);
		}
	}

	void attemptRecovery(Timer waitToUseUpSpecial){
		body.yInput = 1;
		body.xInput = setInput(pack.distanceFromCenter);
		if (pack.isBelowStage){
			if (pack.state == State.WALLSLIDE) {
				if (Math.random() < 0.1) body.handleCommand(InputHandler.commandJump);
			}
			else if (!pack.hasDoubleJumped) {
				body.handleCommand(InputHandler.commandJump);
				waitToUseUpSpecial.restart();
			}
			else if (waitToUseUpSpecial.timeUp()) body.handleCommand(InputHandler.commandSpecial);
		}
	}

	void jumpAtPlayer(Timer tryJump, Timer performJump){
		tryJump.restart();
		if (performJump.timeUp() && Math.random() < (pack.distanceYFromPlayer * 0.02f) ) {
			body.handleCommand(InputHandler.commandJump);
			performJump.restart();
		}
	}

	void performJump(Timer performJump){
		body.handleJumpHeld();
		if (Math.random() < 0.5) performJump.countUp(); // modulates jump height
	}

	void attackPlayer(int command){
		facePlayer();
		body.handleCommand(command);
	}

	void facePlayer(){
		if (pack.direct != Math.signum(pack.distanceXFromPlayer)) body.xInput *= -1; // turn around if behind
		if (pack.distanceYFromPlayer > 20 && Math.random() < 0.5) body.yInput = 1;
	}

	float runTendency = 0.01f;
	void headTowardPlayer(Timer changeDirection){
		body.xInput = setInput(pack.distanceXFromPlayer);
		if (Math.random() < runTendency && Math.abs(pack.distanceXFromPlayer) > 300) { // run toward
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
	
	boolean inVerticalAttackRange(){
		return Math.abs(pack.distanceYFromPlayer + 40) < 90 && !pack.isGrounded || Math.abs(pack.distanceYFromPlayer) < 40;
	}

	boolean shouldAttack(double chance, int distance, boolean mustBeGrounded){
		boolean shouldAttack = Math.random() < chance && Math.abs(pack.distanceXFromPlayer) < distance;
		if (mustBeGrounded) shouldAttack = shouldAttack && pack.isGrounded;
		return shouldAttack;
	}
	
	boolean shouldAttack(double chance, int closeDistance, int farDistance, boolean mustBeGrounded){
		return shouldAttack(chance, farDistance, mustBeGrounded) && Math.abs(pack.distanceXFromPlayer) > closeDistance;
	}
	
	boolean shouldGetUp(double d){
		return pack.state == State.FALLEN && Math.random() < d;
	}
	
	void avoidBadThings(){
		for (Vector2 badThing: pack.shitToAvoid){
			if (badThing.dst(pack.position.add(pack.velocity.scl(5))) < 50 ){
				// TODO: avoid behavior, change vector2 to rectangle
			}
		}
	}

	/* BRAINS */

	public static class Braindead extends Brain{ // does nothing

		public Braindead(InputHandlerCPU body) {
			super(body);
		}

	}

	public static class Recover extends Brain{ // Recovers if thrown offstage, mixes up vertical DI

		public Recover(InputHandlerCPU body) {
			super(body);
			timerList.addAll(Arrays.asList(waitToUseUpSpecial));
		}

		void update(InputPackage pack){
			super.update(pack);
			body.xInput = 0;
			if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
		}

	}

	public static class MookBrain extends Brain{

		public MookBrain(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			super.update(pack);
			body.yInput = 0;
			if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 25 ) headTowardPlayer(changeDirection);
			if (!performJump.timeUp()) performJump(performJump);
			if (shouldGetUp(0.02)) getUp();
			else if (pack.distanceYFromPlayer > 20 && tryJump.timeUp()) jumpAtPlayer(tryJump, performJump);
			else if (inVerticalAttackRange()) chooseAttack();
			else if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
		}
		
		void chooseAttack(){
			if		(shouldAttack(0.02, 70,  true))		performJump(performJump);
			if 		(shouldAttack(0.22, 25, false))		attackPlayer(InputHandler.commandAttack);
			else if (shouldAttack(0.08, 30,  true))		attackPlayer(InputHandler.commandSpecial);
			else if (shouldAttack(0.04, 40, 70, false)) attackPlayer(InputHandler.commandCharge);
		}

	}

	public static class GunminBrain extends Brain{

		public GunminBrain(InputHandlerCPU body) {
			super(body);
		}

		void update(InputPackage pack){
			super.update(pack);
			if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
			else if (changeDirection.timeUp() && Math.abs(pack.distanceXFromPlayer) > 100) headTowardPlayer(changeDirection);
			else if (!performJump.timeUp()) performJump(performJump);
			else if (shouldGetUp(0.02)) getUp();
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpAtPlayer(tryJump, performJump);
			if (Math.abs(pack.distanceYFromPlayer) < 100){
				if (Math.abs(pack.distanceXFromPlayer) < 30) attackPlayerClose();
				else if (Math.abs(pack.distanceXFromPlayer) < 600 && Math.random() < 0.05) attackPlayerDistant();
			}
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		private void attackPlayerDistant(){ // ranged moves are fweak, uweak, dweak, fair, uair, dair
			if (Math.abs(body.xInput) < 0.8 && Math.abs(body.yInput) < 0.8) body.xInput = pack.direct;
			if (pack.distanceYFromPlayer < 20) body.yInput = 1;
			else if (body.yInput > 0) body.yInput *= -1;
			body.handleCommand(InputHandler.commandAttack);
		}

		private void attackPlayerClose(){ // close moves are nweak, nair, bair
			if (!(!pack.isGrounded && body.xInput < 0)) body.xInput = 0;
			body.handleCommand(InputHandler.commandAttack);
		}

	}

	public static class SpeedyBrain extends Brain{

		public SpeedyBrain(InputHandlerCPU body) {
			super(body);
			runTendency *= 3;
			waitToUseUpSpecial.setEndTime(15);
		}

		void update(InputPackage pack){
			super.update(pack);
			if (pack.state == State.WALLSLIDE) return;
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.FALLEN && Math.random() < 0.02) getUp();
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpAtPlayer(tryJump, performJump);
			else if (inVerticalAttackRange()) {
				if (Math.abs(pack.distanceXFromPlayer) < 30 && Math.random() < 0.02) crouchBeforeAttacking();
				if (Math.abs(pack.distanceXFromPlayer) < 70 && Math.random() < 0.02 && pack.isGrounded) performJump(performJump);
				if (Math.random() < 0.7) {
					if (Math.random() < 0.06 && Math.abs(pack.distanceXFromPlayer) < 600) attackPlayerDistant();
					if (pack.isRunning && Math.abs(pack.distanceXFromPlayer) < 100) attackPlayer(InputHandler.commandAttack);
					else if (Math.abs(pack.distanceXFromPlayer) < 30) attackPlayer(InputHandler.commandAttack);
				}
				else if (Math.random() < 0.2 && Math.abs(pack.distanceXFromPlayer) < 60) attackPlayer(InputHandler.commandCharge);
			}
			if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		void headTowardPlayer(Timer changeDirection){
			body.xInput = setInput(-pack.distanceXFromPlayer);
			if (Math.random() < 0.75 && Math.abs(pack.distanceXFromPlayer) > 100) { // run toward
				if (pack.distanceXFromPlayer > 0) body.handleCommand(InputHandler.commandStickRight);
				else body.handleCommand(InputHandler.commandStickLeft);
			}
			changeDirection.restart();
		}

		void attemptRecovery(Timer waitToUseUpSpecial){
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
		
		private void attackPlayerDistant(){
			facePlayer();
			body.xInput = 0;
			body.handleCommand(InputHandler.commandAttack);
		}

	}

	public static class KickerBrain extends Brain{

		public KickerBrain(InputHandlerCPU body) {
			super(body);
			changeDirection.setEndTime(20);
		}

		void update(InputPackage pack){
			super.update(pack);
			if (changeDirection.timeUp()) headTowardPlayer(changeDirection);
			if (!performJump.timeUp()) performJump(performJump);
			if (pack.state == State.FALLEN && Math.random() < 0.05) getUp();
			else if (pack.distanceYFromPlayer < 20 && tryJump.timeUp()) jumpAtPlayer(tryJump, performJump);
			else if (pack.isOffStage) attemptRecovery(waitToUseUpSpecial);
			else if (inVerticalAttackRange()){
				if (Math.abs(pack.distanceXFromPlayer) < 30 && Math.random() < 0.02) crouchBeforeAttacking();
				if (Math.abs(pack.distanceXFromPlayer) < 80 && Math.random() < 0.02 && pack.isGrounded) performJump(performJump);
				if (Math.abs(pack.distanceXFromPlayer) < 30){
					if (Math.random() < 0.5) attackPlayer(InputHandler.commandGrab);
					else pickLongerMove();
				}
				else if (Math.abs(pack.distanceXFromPlayer) < 70) pickLongerMove();
			}
			if (changeUpDown.timeUp()) changeUpDown(); 
		}

		boolean isCharging(){
			if (Math.random() < 0.9) return true;
			else return false;
		}

		void pickLongerMove(){
			if (Math.random() < 0.2) attackPlayer(InputHandler.commandCharge);
			else attackPlayer(InputHandler.commandAttack);
		}

	}
}
