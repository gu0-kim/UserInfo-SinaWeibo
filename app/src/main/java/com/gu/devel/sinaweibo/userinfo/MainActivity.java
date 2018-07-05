package com.gu.devel.sinaweibo.userinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gu.devel.sinaweibo.userinfo.app.MyApplication;
import com.gu.devel.sinaweibo.userinfo.di.component.ComponentController;
import com.gu.devel.sinaweibo.userinfo.mvp.view.HomePageView;
import com.gu.mvp.utils.leaks.CleanLeakUtils;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
    if (fragment == null) {
      fragment = HomePageView.getInstance();
      getSupportFragmentManager().beginTransaction().add(R.id.content_layout, fragment).commit();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    CleanLeakUtils.fixInputMethodManagerLeak(this);
    Log.e("TAG", "MainActivity onDestroy()");
    ComponentController.getInstance().release();
    ((MyApplication) getApplication()).getRefWatcher().watch(this);
  }
}
