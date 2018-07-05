package com.gu.devel.sinaweibo.userinfo.di.module;

import com.gu.devel.sinaweibo.userinfo.mvp.model.PageItemModel;
import com.gu.devel.sinaweibo.userinfo.mvp.presenter.HomePagePresenter;
import com.gu.devel.sinaweibo.userinfo.mvp.presenter.PagePresenter;
import com.gu.mvp.bus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/** Created by devel on 2018/4/8. */
@Module
public class AppModule {
  @Provides
  PagePresenter getItemViewPresenter() {
    return new PagePresenter();
  }

  @Provides
  HomePagePresenter getHomePagePresenter() {
    return new HomePagePresenter();
  }

  @Provides
  PageItemModel getPageItemModel() {
    return new PageItemModel();
  }

  @Singleton
  @Provides
  RxBus getRxBus() {
    return new RxBus();
  }
}
