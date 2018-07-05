package com.gu.devel.sinaweibo.userinfo.mvp.view.base;

import java.util.List;

/**
 * @author developergu
 * @version v1.0.0
 * @since 2017/10/20
 */
public interface IRefreshableView {
  void showLoading();

  boolean isLoading();

  void showError(String error);

  void stopLoading();

  void notifyFinishLoad();

  void notifyStartLoad();

  boolean isTop();

  void setData(List<String> list);
}
