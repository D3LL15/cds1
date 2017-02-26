package cds1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class NTPClient {

	public static void main(String[] args) {
		try{
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName("ntp0.cl.cam.ac.uk");
			
			byte[] sendData = new byte[48];
			byte[] receiveData = new byte[1024];
			
			byte firstOctet = 0b00011011;
			sendData[0] = firstOctet;
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 123);
			clientSocket.send(sendPacket);
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			
			receiveData = receivePacket.getData();
			
			Date date1 = getDateForNTPBytes(receiveData, 32);
			Date date2 = getDateForNTPBytes(receiveData, 40);

			Date date = new Date((date1.getTime() + date2.getTime()) / 2);
			
			System.out.println(date.toString());
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Something went wrong");
			e.printStackTrace();
		}
		
	}
	
	private static Date getDateForNTPBytes(byte[] bytes, int offset) {
		long seconds = 0;
		for (int i = 0; i < 3; i++) {
			seconds += bytes[offset + i] & 0xFF;
			seconds = seconds << 8;
		}
		seconds += bytes[offset + 4] & 0xFF;
		seconds -= 70 * 365.25 * 24 * 60 * 60;
		Date date= new Date(seconds*1000);
		return date;
	}

}
