package com.ticktockx.sdk.pluginx.event;


import com.ticktockx.sdk.QQMessage;

public class SendMessageEvent extends Event{

    private QQMessage message;

    public SendMessageEvent(QQMessage message){
        this.message = message;
    }

    public QQMessage getMessage() {
        return message;
    }
}
