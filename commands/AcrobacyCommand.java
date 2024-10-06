package commands;
import java.io.IOException;

import abstracts.Command;

public class AcrobacyCommand extends Command {

	private Object[] params;

	public static final String CW = "cw";
	public static final String CCW = "ccw";
	public static final String FLIP = "flip";
	public static final String GO = "go";
	public static final String CURVE = "curve";
	
	public AcrobacyCommand(String value, Object[] params) {
		this.value = value;
		this.params = params;
	}

	@Override
	protected void validateInputs() throws IOException {
		if(value == null || params == null) 
			throw new IOException("'value' And 'params' Are Mandatory");

		if(
			!value.equals(CW) && 
			!value.equals(CCW) && 
			!value.equals(FLIP) && 
			!value.equals(GO) &&
			!value.equals(CURVE)
		)
			throw new IOException("'value' Is Invalid");

		if(params == null || params.length == 0)
			throw new IOException("'params' Can´t Be Null");
		
		switch (value) {
			case CW:
			case CCW:
				if(
					!(params instanceof Integer[]) ||
					((Integer) params[0]) < 1 || 
					((Integer) params[0]) > 360
				) throw new IOException("Params Are Invalid");
				break;

			case FLIP:
				if(
					!(params instanceof String[]) ||
					(
						!params[0].equals("l") &&
						!params[0].equals("r") &&
						!params[0].equals("f") &&
						!params[0].equals("b")
					)
				) throw new IOException("Params Are Invalid");
				break;

			case GO:
				if(
					!(params instanceof Integer[]) ||
					params.length != 4
				) throw new IOException("Params Are Invalid");

				for(int i = 0; i < 4; i++) {
					Integer num = (Integer) params[i];

					if(i != 3 && (num < -500 || num > 500)) throw new IOException("Params Are Invalid");
					if(i == 3 && (num < 10 || num > 100)) throw new IOException("Params Are Invalid");
				}
				break;

			case CURVE:
				if(
					!(params instanceof Integer[]) ||
					params.length != 7
				) throw new IOException("Params Are Invalid");

				for(int i = 0; i < 7; i++) {
					Integer num = (Integer) params[i];

					if(i != 6 && (num < -500 || num > 500)) throw new IOException("Params Are Invalid");
					if(i == 6 && (num < 10 || num > 60)) throw new IOException("Params Are Invalid");
				}
				break;
		}
	}

	@Override
	public String getCommand() throws IOException {
		validateInputs();

		String out = value;

		switch (value) {
			case CW:
			case CCW:
				out += " " + ((Integer) params[0]);
				break;

			case FLIP:
				out += " " + params[0].toString().trim();
				break;

			case GO:
				for(int i = 0; i < 4; i++)
					out += " " + params[i];
				break;

			case CURVE:
				for(int i = 0; i < 7; i++)
					out += " " + params[i];
				break;
		}

		return out;
	}
}
