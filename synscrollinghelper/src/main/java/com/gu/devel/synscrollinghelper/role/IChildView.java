package com.gu.devel.synscrollinghelper.role;

import android.view.View;

public interface IChildView {

  IParentView findParentView();
  /**
   * 通知parent,pagerFragment发生滚动
   *
   * @param scrollY scroll y
   * @param deltaY y changed
   */
  void notifyParentScrollChanged(int scrollY, int deltaY);

  /**
   * parent收到一个pagerFragment的滚动消息后，同步其他pagerFragment的滚动时回调
   *
   * @param deltaY the distance of scroll dy
   */
  void syncMove(int deltaY);

  View getView();
}
