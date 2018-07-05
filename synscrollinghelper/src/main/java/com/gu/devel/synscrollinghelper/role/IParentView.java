package com.gu.devel.synscrollinghelper.role;

public interface IParentView {

  //  IChildView getCurrentChild();
  //
  //  IChildView getChildAt(int pos);
  //
  int getCurrentIndex();
  //
  //  int getChildCount();

  /**
   * child监听滚动时回调
   *
   * @param scrollY itemPage的当前scrollY
   * @param deltaY itemPage的deltaY
   */
  void onChildScrollChanged(IChildView childView, int scrollY, int deltaY);

  /**
   * 把滚动传递给其他页的child(只滚动其他孩子)
   *
   * @param deltaY dy
   */
  void dispatchScroll2BackgroundChildren(int deltaY);

  void moveUpBy(int deltaY);

  void moveDownTo(int scrollY);

  boolean needMoveDown(int scrollY);
}
