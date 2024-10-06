package commands;
import java.io.IOException;

import abstracts.Command;

public class InfosCommand extends Command {
	
	public static final String BATTERY = "battery";
	public static final String SPEED = "speed";
	public static final String TIME = "time";

	public InfosCommand(String value) {
		this.value = value;
	}

	@Override
	protected void validateInputs() throws IOException {
		if(value == null) throw new IOException("'value' Can´t Be Null");

		if(
			!value.equals(BATTERY) &&
			!value.equals(SPEED) &&
			!value.equals(TIME)
		) {
			throw new IOException("'value' Not Allowed");
		}
	}

	@Override
	public String getCommand() throws IOException {
		validateInputs();

		return value+"?";
	}

	@Override
	public boolean isWorked() {
		if(received == null) return false;
		return !received.contains("error");
	}

	public int parseReceivedToInt() {
		if(received == null) return -1;
		return Integer.parseInt(received);
	}
}
