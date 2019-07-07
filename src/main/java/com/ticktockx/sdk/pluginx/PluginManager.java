package com.ticktockx.sdk.pluginx;

import com.ticktockx.JarLib;
import com.ticktockx.QQUser;
import com.ticktockx.RobotApi;
import com.ticktockx.ex.PluginException;
import com.ticktockx.sdk.CallApi;
import com.ticktockx.sdk.Plugin;
import com.ticktockx.socket.Udpsocket;
import com.ticktockx.utils.Util;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginManager {


    private Yaml yaml;
    private RobotApi api;


    private List<Plugin> plugins =new ArrayList<>();


    public PluginManager(Udpsocket _socket, QQUser _user){
        this.yaml = new Yaml();
        this.api = new RobotApi(_socket, _user);
    }

    public void loadPlugins(){
        File pluginFile  = new File(JarLib.JAR_FOLDER+"/plugins/");
        File[] plugins = pluginFile.listFiles();
        CallApi.init(this.api);
        System.out.println("加载插件中");
        if(plugins!=null){
            for(File plugin:plugins){
                this.plugins.add(loadPlugin(plugin.toString()));
            }
        }
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void disable(){
        for(Plugin plugin:getPlugins()){
            plugin.onEnable();
        }
    }

    /**
     * 一个加强版的插件管理系统
     */

    public Plugin loadPlugin(String file)  {
        try {
            JarFile jarFile = new JarFile(file);
            ZipEntry entry = jarFile.getEntry("plugin.yml");
            if(entry!=null) {
                InputStream stream = jarFile.getInputStream(entry);
                Map map = yaml.loadAs(stream, LinkedHashMap.class);
                String main = map.get("main").toString();
                ClassLoader loader = new URLClassLoader(new URL[]{new File(file).toURI().toURL()}, this.getClass().getClassLoader());
                Class<?> claz = loader.loadClass(main);
                Object obj = claz.newInstance();
                if (obj instanceof Plugin) {
                    Plugin plugin = (Plugin) obj;
                    plugin.onLoad(api);
                    Util.log("[插件] 加载成功 [插件名]: " + plugin.name());
                    return plugin;
                } else {
                    throw new PluginException("the main class must extend the Plugin");
                }
            }else{
                //如果没有文件，则通过第二个策略
                PluginScanner scan = new ReflectPluginScan(file);
                return scan.scan(api);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new PluginException("other error");
    }



}
