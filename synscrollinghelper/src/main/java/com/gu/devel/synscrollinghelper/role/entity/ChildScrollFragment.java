package com.gu.devel.synscrollinghelper.role.entity;

import android.view.View;

import com.gu.devel.synscrollinghelper.role.IChildView;
import com.gu.devel.synscrollinghelper.scrollobservable.ScrollCallBack;
import com.gu.devel.synscrollinghelper.scrollobservable.Scrollable;
import com.gu.mvp.presenter.IPresenter;
import com.gu.mvp.view.IView;

/** viewpager item */
public abstract class ChildScrollFragment<T extends IPresenter> extends IView<T>
    implements ScrollCallBack, IChildView {

  public ChildScrollFragment() {}

  // 由实现类给出Scrollable
  public abstract Scrollable getScrollable();

  @Override
  public void onScroll(int scrollY, int deltaY) {
    if (getView() == null) {
      return;
    }
    notifyParentScrollChanged(scrollY, deltaY);
  }

  @Override
  public View getView() {
    return super.getView();
  }

  @Override
  public void syncMove(int deltaY) {
    if (deltaY != 0) getScrollable().scrollBy(0, deltaY);
  }
}
