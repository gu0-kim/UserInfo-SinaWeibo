package com.gu.devel.sinaweibo.userinfo.di.component;

/** Created by devel on 2018/4/12. */
public class ComponentController {
  private static ComponentController instance;
  private AppComponent component;

  private ComponentController() {
    component = DaggerAppComponent.builder().build();
  }

  public static ComponentController getInstance() {
    if (instance == null) {
      instance = new ComponentController();
    }
    return instance;
  }

  public AppComponent getComponent() {
    return component;
  }

  public void release() {
    instance = null;
    component = null;
  }
}
