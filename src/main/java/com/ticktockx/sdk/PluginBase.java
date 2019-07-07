package com.ticktockx.sdk;

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
      onFriendMessageHandler(message);
    } else {
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
