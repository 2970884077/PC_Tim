package com.ticktockx;


import com.ticktockx.sdk.pluginx.EventRegisterHandler;
import com.ticktockx.sdk.pluginx.PluginManager;
import com.ticktockx.sdk.pluginx.event.SendMessageEvent;
import com.ticktockx.socket.Udpsocket;
import com.ticktockx.sdk.Plugin;
import com.ticktockx.sdk.QQMessage;


public class QQRobot
{

	private PluginManager manager;


	public QQRobot(Udpsocket _socket, QQUser _user)
	{
		manager = new PluginManager(_socket,_user);
		manager.loadPlugins();
	}

	public void call(final QQMessage qqmessage)
	{
		for (final Plugin plugin : manager.getPlugins())
		{
			new Thread(()->{
				plugin.onMessageHandler(qqmessage);
				SendMessageEvent event = new SendMessageEvent(qqmessage);
				EventRegisterHandler.getHandler().callEvent(event);
			}).start();
		}
	}

	public PluginManager getManager() {
		return manager;
	}
}




