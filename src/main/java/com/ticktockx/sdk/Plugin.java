package com.ticktockx.sdk;

public interface Plugin {

    default String author(){
        return "";
    }

    default String version(){
        return "";
    }

    default String name(){
        return "";
    }

    default void onEnable(){

    }

    default void onDisable(){

    }

    void onLoad(API api);

    void onMessageHandler(QQMessage message);
}

