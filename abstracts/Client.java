package abstracts;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public interface Client {

	public DatagramPacket getPacket(String message);

	public void sendPacket(DatagramPacket packet) throws IOException;

	public String getRecivedAnswer() throws IOException;

	public void closeConnection() throws SocketException;

	public String sendMessage(String message) throws Exception;

	public void finish();

}