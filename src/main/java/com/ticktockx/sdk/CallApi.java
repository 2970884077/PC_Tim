package com.ticktockx.sdk;

public class CallApi {

  private static API api;

  private static MessageFactory factory = new MessageFactory();

  public static void init(API _api)
  {
    api = _api;
  }

  public static void sendFriendMessage(long qq,String message)
  {
    clearFactory();
    factory.message_type = 0;
    factory.Friend_uin = qq;
    factory.Message = message;
    api.SendFriendMessage(factory);
  }

  public static void sendGroupMessage(long qq,String message)
  {
    clearFactory();
    factory.message_type = 0;
    factory.Group_uin = qq;
    factory.Message = message;
    api.SendGroupMessage(factory);
  }

  private static void clearFactory()
  {
    factory.Friend_uin = 0;
    factory.Group_uin = 0;
    factory.Message = "";
    factory.message_type = 0;
  }
}
