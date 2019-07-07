package com.ticktockx.sdk.pluginx.event;

import com.ticktockx.sdk.QQMessage;

public class SendGroupEvent extends SendMessageEvent {

    public SendGroupEvent(QQMessage message) {
        super(message);
    }
}
