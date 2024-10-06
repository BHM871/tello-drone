import commands.AcrobacyCommand;
import commands.MoveCommand;
import tello.TelloClient;
import tello.TelloDrone;

public class Main {

	public static void main(String[] args) {
		
		TelloDrone tello = new TelloDrone(new TelloClient());

		tello.addCommand(new AcrobacyCommand(AcrobacyCommand.CW, new Integer[]{180}));

		// // Move to front and up
		tello.addCommand(new MoveCommand(MoveCommand.FORWARD, 50));
		tello.addCommand(new MoveCommand(MoveCommand.UP, 50));
		tello.addCommand(new MoveCommand(MoveCommand.BACK, 50));

		// // Do a square
		tello.addCommand(new MoveCommand(MoveCommand.RIGHT, 40));
		tello.addCommand(new MoveCommand(MoveCommand.BACK, 40));
		tello.addCommand(new MoveCommand(MoveCommand.FORWARD, 40));
		tello.addCommand(new MoveCommand(MoveCommand.LEFT, 40));

		// // Do a complex move
		tello.addCommand(new AcrobacyCommand(AcrobacyCommand.GO, new Integer[]{50, 50, 50, 30}));
		tello.addCommand(new AcrobacyCommand(AcrobacyCommand.GO, new Integer[]{-50, -50, -50, 30}));

		if(!tello.initDrone()) return;
		if(!tello.executePath()) return;
		tello.landDrone();
		tello.finish();

	}
	
}
