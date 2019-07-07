package com.ticktockx.socket;



import com.ticktockx.QQUser;
import com.ticktockx.SendPackageFactory;

import java.util.Date;


public class HeartBeat
{
	private QQUser user;
	private Thread thread;
	private long time_miles = 0;
	private Udpsocket socket;
	public HeartBeat(final QQUser _user,Udpsocket _socket){
		
		this.user = _user;
		this.socket = _socket;
		this.thread = new Thread(()->{
			while(true){
				try
				{
					byte[] data = SendPackageFactory.getHeartBeatPack(_user);
					time_miles  = new Date().getTime();
					socket.sendMessage(data);
					Thread.sleep(20000);
				}
				catch (InterruptedException e)
				{
					System.out.println(e.getMessage());
				}
			}
		});
		
	}

	public QQUser getUser() {
		return user;
	}

	public long getTime_miles() {
		return time_miles;
	}

	public void start(){
		this.thread.start();
		
	}
	
	public void stop(){
		this.thread.stop();
	}
	
}
