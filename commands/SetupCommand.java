package commands;

import java.io.IOException;

import abstracts.Command;

public class SetupCommand extends Command {
	
	private int param;

	public static final String SPEED = "speed";

	public SetupCommand(String value, int param) {
		this.value = value;
		this.param = param;
	}

	@Override
	protected void validateInputs() throws IOException {
		if(value == null) throw new IOException("'value' Can´t Be Null");
		if(!value.equals(SPEED)) throw new IOException("'value' Has A Invalid Value");
		if(param < 10 || param > 100) throw new IOException("'param' Has A Invalid Value");
	}

	@Override
	public String getCommand() throws IOException {
		validateInputs();

		return value + " " + param;
	}

}
