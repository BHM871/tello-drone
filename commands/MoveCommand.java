package commands;
import java.io.IOException;

import abstracts.Command;

public class MoveCommand extends Command {

	private int param;
	private int minValueParam = 20;
	private int maxValueParam = 500;

	public static final String UP = "up";
	public static final String DOWN = "down";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";
	public static final String FORWARD = "forward";
	public static final String BACK = "back";
	
	public MoveCommand(String value, int param) {
		this.value = value;
		this.param = param;
	}

	@Override
	protected void validateInputs() throws IOException {
		if(value == null) throw new IOException("'value' Can´t Be Null");

		if(
			!value.equals(UP) &&
			!value.equals(DOWN) &&
			!value.equals(LEFT) &&
			!value.equals(RIGHT) &&
			!value.equals(FORWARD) &&
			!value.equals(BACK)
		) throw new IOException("'value' Has A Invalid Value");

		if(param < minValueParam || param > maxValueParam) throw new IOException("'param' Is Not Allowed");
	}

	@Override
	public String getCommand() throws IOException {
		validateInputs();
		return value + " " + param;
	}

	public int getMinValueParam() {
		return minValueParam;
	}

	public void setMinValueParam(int minValueParam) {
		this.minValueParam = minValueParam;
	}

	public int getMaxValueParam() {
		return maxValueParam;
	}

	public void setMaxValueParam(int maxValueParam) {
		this.maxValueParam = maxValueParam;
	}
}
