package input;

import timers.Timer;
import entities.Fighter;

public abstract class InputHandlerPlayer extends InputHandler {
	
	private final Timer techTimer = new Timer(20);

	public InputHandlerPlayer(Fighter player) {
		super(player);
	}

	public void update(){
		super.update();
		inputToCommand(attack(), commandAttack);
		inputToCommand(special(), commandSpecial);
		inputToCommand(charge(), commandCharge);
		inputToCommand(jump(), commandJump);
		inputToCommand(grab(), commandGrab);
		inputToCommand(block(), commandDodge);
		inputToCommand(taunt(), commandTaunt);
		inputToCommand(flickLeft(), commandStickLeft);
		inputToCommand(flickRight(), commandStickRight);
		inputToCommand(flickUp(), commandStickUp);
		inputToCommand(flickDown(), commandStickDown);
		
		fighter.handleJumpHeld(jumpHold());
		fighter.handleBlockHeld(blockHold());
		
		if (block() && techTimer.timeUp()) techTimer.restart();
	}

	private void inputToCommand(boolean input, int command){
		if (input) handleCommand(command);
	}
	
	public boolean isCharging(){
		return chargeHold();
	}
	
	public boolean isTeching(){
		return !techTimer.timeUp();
	}

	abstract boolean attack();
	abstract boolean special();
	abstract boolean charge();
	abstract boolean jump();
	abstract boolean grab();
	abstract boolean block();
	abstract boolean taunt();
	abstract boolean flickLeft();
	abstract boolean flickRight();
	abstract boolean flickUp();
	abstract boolean flickDown();
	abstract boolean pause();

	abstract boolean chargeHold();
	abstract boolean jumpHold();
	abstract boolean blockHold();

}
