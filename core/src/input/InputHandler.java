package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Fighter;

public abstract class InputHandler {

	public static final int commandNone			=-1;
	public static final int commandAttack		= 0;
	public static final int commandSpecial		= 1;
	public static final int commandJump			= 2;
	public static final int commandCharge		= 3; 
	public static final int commandDodge		= 4;
	public static final int commandGrab 		= 5;
	public static final int commandTaunt 		= 6;
	public static final int commandStickUp 		=20;
	public static final int commandStickLeft	=21;
	public static final int commandStickRight	=22;
	public static final int commandStickDown	=23;

	Fighter fighter;
	public InputHandler(Fighter fighter){
		this.fighter = fighter;
	}

	public void update(){
		if (fighter.inputQueueTimer.timeUp()) fighter.queuedCommand = commandNone;
		if (!fighter.inputQueueTimer.timeUp()) handleCommand(fighter.queuedCommand);
	}

	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 

	protected void handleCommand(int command){
		boolean wasCommandAccepted = false;

		if (fighter.canMove()){
			switch (command){
			case commandStickRight:		wasCommandAccepted = fighter.tryStickForward(); break;
			case commandStickLeft:		wasCommandAccepted = fighter.tryStickBack(); break;
			case commandStickUp:		wasCommandAccepted = fighter.tryStickUp(); break;
			case commandStickDown:		wasCommandAccepted = fighter.tryStickDown(); break;
			}
		}
		if (fighter.canAct()){
			if (command == commandJump) wasCommandAccepted = fighter.tryJump();
		}
		if (fighter.canAttack()){
			switch (command){
			case commandAttack:			wasCommandAccepted = fighter.tryNormal(); break;
			case commandSpecial:		wasCommandAccepted = fighter.trySpecial(); break;
			case commandGrab:	 		wasCommandAccepted = fighter.tryGrab(); break;
			case commandCharge: 		wasCommandAccepted = fighter.tryCharge(); break;
			case commandTaunt:			wasCommandAccepted = fighter.tryTaunt(); break;
			case commandDodge:			wasCommandAccepted = fighter.tryDodge(); break;
			default:					wasCommandAccepted = true; break;
			}
		}

		if (!wasCommandAccepted && fighter.inputQueueTimer.timeUp() && !stickCommands.contains(command)) {
			if (!(command == commandJump && fighter.isGrounded()) ){
				fighter.queuedCommand = command;
				fighter.inputQueueTimer.restart();
			}
		}
		else if (wasCommandAccepted) fighter.queuedCommand = commandNone;
	}

	private final List<Integer> stickCommands = new ArrayList<Integer>(Arrays.asList(commandStickUp, commandStickRight, commandStickLeft, commandStickDown));

}
