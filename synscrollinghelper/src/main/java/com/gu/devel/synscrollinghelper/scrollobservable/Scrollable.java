package com.gu.devel.synscrollinghelper.scrollobservable;

public interface Scrollable {
  void setScrollCallBack(ScrollCallBack callBack);

  void releaseListener();

  int getCurrentScrollY();

  void scrollBy(int x, int y);
}
