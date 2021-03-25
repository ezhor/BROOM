package com.arensis.broom.manager;

import com.arensis.broom.model.BroomStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationManager {
	private static final int BROOM_PORT = 2727;
	private OutputStream outputStream;
	private InputStream inputStream;
	private  ServerSocket serverSocket;

	public void start() {
		try {
			serverSocket = new ServerSocket(BROOM_PORT);
			Socket socket = serverSocket.accept();
			this.outputStream = socket.getOutputStream();
			this.inputStream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(BroomStatus broomStatus) {
		send(broomStatus.toString());
	}

	private void send(String string){
		try {
			outputStream.write(string.concat("\n").getBytes());
			System.out.println(string);
		} catch (IOException e) {
			e.printStackTrace();
			start();
		}
	}

	public void stop(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
