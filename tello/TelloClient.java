package tello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import abstracts.Client;

public class TelloClient implements Client {

    private String ip;
    private int port;
	private int timeout;
	private InetAddress address = null;
	private DatagramSocket dataSocket = null;

	private Logger logger = Logger.getLogger(Client.class.getName());
	
	public TelloClient(){
		this("192.168.10.1", 8889, 15);
	}

	public TelloClient(int timeout){
		this("192.168.10.1", 8889, timeout);
	}

	public TelloClient(String ip, int port) {
		this(ip, port, 15);
	}

	public TelloClient(String ip, int port, int timeout) {
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPorta(int port) {
        this.port = port;
    }

	protected boolean canEstablishConnection() {
		try {
            address = InetAddress.getByName(ip);

	        if (dataSocket == null) {
                dataSocket = new DatagramSocket(port);
				dataSocket.setSoTimeout(timeout*1000);
			}

			dataSocket.connect(address, port);
			dataSocket.disconnect();

			return true;
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, "Host Not Found", ex);
        } catch (SocketException ex) {
            logger.log(Level.SEVERE, "Port Is Not Open", ex);
		}

		return false;
	}

	public DatagramPacket getPacket(String message) {
		if(address == null && !canEstablishConnection()) {
			throw new RuntimeException("Address Don´t Defined");
		}

        byte buf[] = null;
        buf = message.getBytes();
        return new DatagramPacket(buf, buf.length, address, port);
	}

	public void sendPacket(DatagramPacket packet) throws IOException {
		try {
            dataSocket.send(packet);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "DatagramPacket Can´t Sended", ex);
			throw new IOException(ex);
        }
	}

	public String getRecivedAnswer() throws IOException {
        byte[] receivedBytes = new byte[1024];
        final DatagramPacket receivedPacket = new DatagramPacket(receivedBytes, receivedBytes.length);

        try {
            dataSocket.receive(receivedPacket);
			return packetToString(receivedBytes, receivedPacket);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Response Don´t Received, Timeout!", ex);
			throw ex;
        }
	}

    protected String packetToString(byte[] receivedBytes, DatagramPacket receivedPacket) {
        receivedBytes = Arrays.copyOf(receivedBytes, receivedPacket.getLength());
        return new String(receivedBytes, StandardCharsets.UTF_8).trim();
    }

	public void closeConnection() throws SocketException {
        try {
            dataSocket.setReuseAddress(false);
			dataSocket.disconnect();
        } catch (SocketException ex) {
            logger.log(Level.SEVERE, "Can´t Setup Address Reuse", ex);
			throw ex;
        }
	}

    public String sendMessage(String mensagem) throws Exception {
		try {
			logger.info("Sending command: " + mensagem);
			sendPacket(getPacket(mensagem));

			String received = getRecivedAnswer();
			logger.info("Received message: " + received);
			
			closeConnection();

			return received;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Can´t Execute This Command", e);
			throw e;
		}
    }

	@Override
	public void finish() {
		if(dataSocket != null && dataSocket.isClosed())
			dataSocket.close();
	}
}
