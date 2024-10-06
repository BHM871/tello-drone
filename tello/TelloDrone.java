package tello;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import abstracts.Client;
import abstracts.Command;
import abstracts.Drone;
import commands.ConfigCommand;
import commands.InfosCommand;
import commands.MoveCommand;
import commands.SetupCommand;

public class TelloDrone implements Drone {

	private int battery;
	private float speed;
	private int time;
	private boolean isInited;
	private Client client;
	private List<Command> commands = new ArrayList<>();

	private Logger logger = Logger.getLogger(TelloDrone.class.getName());

	public TelloDrone(TelloClient client) {
		this.client = client;
	}

	@Override
	public boolean initDrone() {
		try {
			Command initCommand = new ConfigCommand(ConfigCommand.INIT_COMMANDS);
			Command getBattery = new InfosCommand(InfosCommand.BATTERY);
			Command initDrone = new ConfigCommand(ConfigCommand.INIT_DRONE);
			Command upCommand = new MoveCommand(MoveCommand.UP, 30);
			
			if(
				!initCommand.execute(client) ||
				!getBattery.execute(client) 
			) throw new RuntimeException("Some Error Init Drone");

			if(
				((InfosCommand) getBattery).parseReceivedToInt() < getCriticalBatteryValue()
			) throw new RuntimeException("Minimal Value Not Allowd");

			this.battery = Integer.parseInt(getBattery.getReceived());

			if(
				!initDrone.execute(client) ||
				!upCommand.execute(client)
			) throw new RuntimeException("Some Error Init Drone");

			isInited = true;

			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can´t Init Drone", e);
			landDrone();
			return false;
		}
	}

	@Override
	public boolean landDrone() {
		try {
			return new ConfigCommand(ConfigCommand.LAND).execute(client);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can´t Stop Drone", e);
			return false;
		}
	}

	/**
	 * 
	 * If sended command was a error, a message will be logged;
	 * 
	 * @param command -> A commando to send to drone
	 * @return Drone received message
	 */
	@Override
	public String executeCommand(Command command) {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return "error";
		}
		if(battery < getCriticalBatteryValue()) {
			logger.log(Level.WARNING, "Low Battery");
			return "error";
		}

		try{
			command.execute(client);
			String received = command.getReceived();
			
			if(!command.isWorked()){
				landDrone();
			}

			return received != null ? received : "error";
		} catch (Exception e) {
			try {
				logger.log(Level.SEVERE, "Can´t Execute Command: " + command.getCommand());
			} catch (IOException ex) {
			}

			return "error";
		}

	}

	@Override
	public boolean executePath() {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return false;
		}
		if(battery < getCriticalBatteryValue()) {
			logger.log(Level.WARNING, "Low Battery");
			return false;
		}

		try {
			for(Command command : commands) {
				if(!command.execute(client)){
					throw new RuntimeException("Command: " + command.getCommand() + " Not Worked");
				}
			}

			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Some Command Was An Error. Stoping!", e);
			landDrone();
			return false;
		}
	}

	@Override
	public void finish() {
		client.finish();
	}

	@Override
	public int getBattery() {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return battery;
		}
		if(client == null) {
			logger.log(Level.SEVERE, "Client Is Not Defined");
			return battery;
		}

		try {
			Command command = new InfosCommand(InfosCommand.BATTERY);
			if(command.execute(client))
				this.battery = Integer.parseInt(command.getReceived());

			return battery;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Battery Can´t Be Getted", e);
			return battery;
		}
	}

	@Override
	public float getSpeed() {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return speed;
		}

		try {
			
			Command command = new InfosCommand(InfosCommand.SPEED);
			if(command.execute(client))
				this.speed = Float.parseFloat(command.getReceived());

			return speed;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Battery Can´t Be Getted", e);
			return speed;
		}
	}

	@Override
	public void setSpeed(int speed) {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return;
		}

		try {
			Command command = new SetupCommand(SetupCommand.SPEED, speed);
			if(command.execute(client))
				this.speed = speed;

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can´t Setted Speed", e);
		}
	}

	@Override
	public int getTime() {
		if(!isInited) {
			logger.log(Level.WARNING, "First Init Drone");
			return time;
		}

		try {
			Command command = new InfosCommand(InfosCommand.TIME);
			if(command.execute(client))
				this.time = Integer.parseInt(command.getReceived());
			return time;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Battery Can´t Be Getted", e);
			return time;
		}
	}

	@Override
	public void addCommand(Command command) {
		commands.add(command);
	}

	private int getCriticalBatteryValue() {
		return 10;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
