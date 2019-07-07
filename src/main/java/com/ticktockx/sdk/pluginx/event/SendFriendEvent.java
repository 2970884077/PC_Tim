package com.ticktockx.sdk.pluginx.event;

import com.ticktockx.sdk.QQMessage;

public class SendFriendEvent extends SendMessageEvent{

    public SendFriendEvent(QQMessage message) {
        super(message);
    }
}
