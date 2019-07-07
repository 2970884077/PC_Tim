package com.ticktockx.sdk;

import com.ticktockx.sdk.pluginx.EventRegisterHandler;
import com.ticktockx.sdk.pluginx.event.SendFriendEvent;
import com.ticktockx.sdk.pluginx.event.SendMessageEvent;

abstract public class PluginBase implements Plugin{

  protected API m_api;

  protected MessageFactory factory = new MessageFactory();

  public abstract void onFriendMessageHandler(QQMessage message);

  public abstract void onGroupMessageHandler(QQMessage message);

  public final void onLoad(API api)
  {
    m_api = api;
    onEnable();
  }

  public final void onMessageHandler(QQMessage message)
  {
    if (message.Group_uin == 0L) {
      SendFriendEvent event = new SendFriendEvent(message);
      EventRegisterHandler.getHandler().callEvent(event);
      onFriendMessageHandler(message);
    } else {
      SendMessageEvent event = new SendMessageEvent(message);
      EventRegisterHandler.getHandler().callEvent(event);
      onGroupMessageHandler(message);
    }
  }

  public final void sendFriendMessage(long qq,String message)
  {
    clearFactory();
    factory.message_type = 0;
    factory.Friend_uin = qq;
    factory.Message = message;
    m_api.sendFriendMessage(factory);
  }

  public final void sendGroupMessage(long qq,String message)
  {
    clearFactory();
    factory.message_type = 0;
    factory.Group_uin = qq;
    factory.Message = message;
    m_api.sendGroupMessage(factory);
  }

  private final void clearFactory()
  {
    factory.Friend_uin = 0;
    factory.Group_uin = 0;
    factory.Message = "";
    factory.message_type = 0;
  }

}
