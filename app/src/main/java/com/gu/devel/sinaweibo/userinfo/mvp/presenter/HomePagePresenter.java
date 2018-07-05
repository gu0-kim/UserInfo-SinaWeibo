package com.gu.devel.sinaweibo.userinfo.mvp.presenter;

import com.gu.devel.sinaweibo.userinfo.mvp.view.HomePageView;
import com.gu.devel.sinaweibo.userinfo.mvp.view.RefreshableFragment;
import com.gu.mvp.presenter.IPresenter;

public class HomePagePresenter extends IPresenter<HomePageView> {

  public void refreshView() {
    RefreshableFragment itemView = view.getCurrentPage();
    itemView.getPresenter().loadData();
  }

  public boolean canPull() {
    return view.canPull();
  }
}
