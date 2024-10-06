package abstracts;

import java.io.IOException;

public abstract class Command {

	protected String value;
	protected Boolean isExcuted = false;
	protected String received;

	protected void validateInputs() throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getCommand() throws IOException {
		validateInputs();
		
		return value;
	}

	public boolean execute(Client client) throws IOException{
		validateInputs();
		
		if(isExcuted) throw new IOException("Command Already Executed");
		if(client == null) return false;
		
		try{
			received = client.sendMessage(getCommand());
			isExcuted = true;
			return isWorked();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isWorked() {
		if(!isExcuted || received == null) return false;
		return received.equals("ok");
	}

	public String getReceived() {
		return this.received;
	}

}
