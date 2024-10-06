package commands;
import java.io.IOException;

import abstracts.Command;

public class ConfigCommand extends Command {
	
	public static final String INIT_COMMANDS = "command";
	public static final String INIT_DRONE = "takeoff";
	public static final String DISABLE_M = "moff";
	public static final String LAND = "land";
	public static final String EMERGENCY = "emergency";
	
	public ConfigCommand(String value) {
		this.value = value;
	}

	@Override
	protected void validateInputs() throws IOException {
		if(value == null) throw new IOException("'value' Can´t Be Null");

		if(
			!value.equals(INIT_COMMANDS) &&
			!value.equals(INIT_DRONE) &&
			!value.equals(DISABLE_M) &&
			!value.equals(LAND) &&
			!value.equals(EMERGENCY)
		) {
			throw new IOException("'value' Not Allowed");
		}
	}
}
