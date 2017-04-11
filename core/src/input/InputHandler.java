package input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Fighter;

public abstract class InputHandler {

	public static final int commandNone		=-1, commandSpecial	= 1, commandAttack	= 0;
	public static final int commandJump	= 2, commandCharge	= 3, commandBlock	= 4, commandGrab = 5;
	public static final int commandTaunt = 6;
	public static final int commandStickUp 	=20, commandStickLeft	= 21, commandStickRight	= 22,commandStickDown	= 23;

	Fighter player;
	public InputHandler(Fighter player){
		this.player = player;
	}

	public void update(){
		try{
			if (player.inputQueueTimer.timeUp()) player.queuedCommand = commandNone;
			if (!player.inputQueueTimer.timeUp()) handleCommand(player.queuedCommand);
		}
		catch(Exception e) {};
	}

	public abstract float getXInput();
	public abstract float getYInput();
	public abstract boolean isCharging(); 

	public void handleCommand(int command){
		boolean wasCommandAccepted = false;

		if (player.canAct()){
			if (command == commandJump) wasCommandAccepted = player.tryJump();
		}
		if (player.canAttackBlock()){
			if (command == commandGrab) wasCommandAccepted = player.tryGrab();
		}
		if (player.canAttack()){
			switch (command){
			case commandAttack:			wasCommandAccepted = player.tryNormal(); break;
			case commandSpecial:		wasCommandAccepted = player.trySpecial(); break;
			case commandCharge: 		wasCommandAccepted = player.tryCharge(); break;
			case commandTaunt:			wasCommandAccepted = player.tryTaunt(); break;
			case commandStickUp:		wasCommandAccepted = player.tryStickUp(); break;
			case commandStickRight:		wasCommandAccepted = player.tryStickForward(); break;
			case commandStickLeft:		wasCommandAccepted = player.tryStickBack(); break;
			case commandStickDown:		wasCommandAccepted = player.tryStickDown(); break;
			default:					wasCommandAccepted = true; break;
			}
		}

		if (!wasCommandAccepted && player.inputQueueTimer.timeUp() && !stickCommands.contains(command)) {
			if (!(command == commandJump && player.isGrounded()) ){
				player.queuedCommand = command;
				player.inputQueueTimer.restart();
			}
		}
		else if (wasCommandAccepted) player.queuedCommand = commandNone;
	}

	private final List<Integer> stickCommands = new ArrayList<Integer>(Arrays.asList(commandStickUp, commandStickRight, commandStickLeft, commandStickDown));

}
