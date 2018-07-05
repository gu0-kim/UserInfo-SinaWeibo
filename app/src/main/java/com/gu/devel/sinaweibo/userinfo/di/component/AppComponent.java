package com.gu.devel.sinaweibo.userinfo.di.component;

import com.gu.devel.sinaweibo.userinfo.di.module.AppModule;
import com.gu.devel.sinaweibo.userinfo.mvp.presenter.PagePresenter;
import com.gu.devel.sinaweibo.userinfo.mvp.view.HomePageView;
import com.gu.devel.sinaweibo.userinfo.mvp.view.RefreshableFragment;

import javax.inject.Singleton;

import dagger.Component;

/** Created by devel on 2018/4/8. */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  public void inject(RefreshableFragment view);

  public void inject(HomePageView view);

  public void inject(PagePresenter presenter);
}
