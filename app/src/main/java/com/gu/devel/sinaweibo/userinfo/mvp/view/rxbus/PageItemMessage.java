package com.gu.devel.sinaweibo.userinfo.mvp.view.rxbus;

import com.gu.mvp.bus.Message;

public class PageItemMessage extends Message {
  public static final int START_LOAD = 0x1;
  public static final int FINISH_LOAD = 0x2;

  public PageItemMessage(int index, int type) {
    super(type, index);
  }
}
