package abstracts;

public interface Drone {

	public boolean initDrone();

	public boolean landDrone();

	public String executeCommand(Command command);

	public boolean executePath();

	public int getBattery();

	public float getSpeed();

	public void setSpeed(int speed);

	public int getTime();

	public void addCommand(Command command);

	public void finish();

}
