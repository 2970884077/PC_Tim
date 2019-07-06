package com.ticktockx;


import com.ticktockx.ex.PluginException;
import com.ticktockx.socket.Udpsocket;
import com.ticktockx.utils.Util;
import com.ticktockx.sdk.Plugin;
import com.ticktockx.sdk.QQMessage;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class QQRobot
{
	private Yaml yaml;
	private Udpsocket socket = null;
	private QQUser user = null;
	private RobotApi api;
	private String exact_directory;

	List<Plugin> plugins =new ArrayList<Plugin>();

	public QQRobot(Udpsocket _socket, QQUser _user)
	{
		this.yaml = new Yaml();
		this.socket = _socket;
		this.user = _user;
		this.api = new RobotApi(this.socket, this.user);
		File directory = new File("");
		try
		{
			this.exact_directory  = directory.getCanonicalPath();
			File plugin_path = new File(exact_directory + "/plugin");
			String[] plugin_list = plugin_path.list();
			if (plugin_list != null)
			{
				List<String> list = Arrays.asList(plugin_list);
				for (String file: list)
				{
					if (file.endsWith(".jar"))
					{
						plugins.add(loadPlugin("file://" + this.exact_directory + "/plugin/"+file));
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

	}

	/**
	 * 一个加强版的插件管理系统
	 */

	public Plugin loadPlugin(String file)  {
		try {
			ClassLoader loader = new URLClassLoader(new URL[]{new File(file).toURI().toURL()}, this.getClass().getClassLoader());
			JarFile jarFile = new JarFile(file);
			InputStream stream = jarFile.getInputStream(jarFile.getEntry("plugin.yml"));
			Map map = yaml.loadAs(stream, LinkedHashMap.class);
			String main = map.get("main").toString();
			Class<?> claz = loader.loadClass(main);
			Object obj = claz.newInstance();
			if(obj instanceof Plugin){
				Plugin plugin = (Plugin)obj;
				plugin.onLoad(api);
				Util.log("[插件] 加载成功 [插件名]: " + plugin.name());

			}else{
				throw new PluginException("the main class must extend the Plugin");
			}
		}catch (IOException|ClassNotFoundException|InstantiationException|IllegalAccessException e){
			e.printStackTrace();
		}
		throw new PluginException("other error");
	}

	public void call(final QQMessage qqmessage)
	{
		for (final Plugin plugin : this.plugins)
		{
			new Thread(){
				public void run()
				{
					plugin.onMessageHandler(qqmessage);

				}
			}.start();
		}
	}

}




