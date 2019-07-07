package com.ticktockx.sdk.pluginx;

import com.ticktockx.ex.PluginException;
import com.ticktockx.sdk.API;
import com.ticktockx.sdk.Plugin;
import com.ticktockx.sdk.pluginx.event.Listener;
import com.ticktockx.utils.Util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 扫描插件所有的包
 */
public class ReflectPluginScan implements PluginScanner{

    private String file;


    public ReflectPluginScan(String file){
        this.file = file;
    }
    public Plugin scan(API api) throws IOException,ClassNotFoundException,InstantiationException,IllegalAccessException {
        ClassLoader loader = new URLClassLoader(new URL[]{new File(file).toURI().toURL()}, this.getClass().getClassLoader());
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if(name.endsWith(".class")){
                Class<?> pluginClass = loader.loadClass(name.substring(0,name.lastIndexOf(".")).replace("/","."));
                Main main = pluginClass.getAnnotation(Main.class);
                if(main!=null){
                    String author = main.author();
                    String pluName = main.name();
                    String version = main.version();
                    Plugin plugin = (Plugin) pluginClass.newInstance();
                    plugin.onLoad(api);
                    Util.log("[Plugin::MainScan] the ["+pluName+"] started,author:"+author+" version: "+version);
                    return plugin;
                }
                Listener listener = pluginClass.getAnnotation(Listener.class);
                if(listener!=null){
                    EventRegisterHandler.getHandler().registerListener(pluginClass.newInstance());
                }
            }
        }
        throw new PluginException("the plugin must have the main class");
    }

}
