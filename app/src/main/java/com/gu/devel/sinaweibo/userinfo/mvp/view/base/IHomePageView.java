package com.gu.devel.sinaweibo.userinfo.mvp.view.base;

import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;

/**
 * 主页接口
 *
 * @param <T> 主页中viewpager的item
 */
public interface IHomePageView<T extends IRefreshableView> extends PtrUIHandler, PtrHandler {
  void showLoading();

  void stopLoading();

  boolean checkState(int index);

  void showLoadError();

  void loadComplete();

  boolean canPull();

  boolean isValidPullDistance(int pullSize);

  void showProgressBar();

  void showButton();

  void rotateProgressBar(float delta);

  void stopProgressBarAnim();

  void horizontalScrollable(boolean can);

  /**
   * 主页下拉
   *
   * @param distance pull size
   */
  void pull(int distance);

  T getCurrentPage();
}
