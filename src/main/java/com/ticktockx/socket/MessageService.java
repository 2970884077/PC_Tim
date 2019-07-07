package com.ticktockx.socket;


import com.ticktockx.QQRobot;
import com.ticktockx.QQUser;

import java.util.Date;

public class MessageService
{
	private Thread thread;
	private QQUser user;
	private MessageManager messagemanager;
	private Udpsocket socket;
	
	public MessageService(QQUser _user,Udpsocket _socket,QQRobot _robot){
		this.user = _user;
		this.socket = _socket;
		this.messagemanager = new MessageManager(this.user,this.socket,_robot);
		this.thread = new Thread(){
			public void run(){
				while(true){
					   byte[] data = socket.receiveMessage();
				    if (data != null){
						user.lastMessage = new Date().getTime();
				        messagemanager.manage(data);
				    }
				}
			}
		};
	}
	public void start(){
		this.thread.start();
		
	}
	
	public void stop(){
		this.thread.stop();

	}
}
