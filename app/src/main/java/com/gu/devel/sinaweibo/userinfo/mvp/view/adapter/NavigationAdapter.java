package com.gu.devel.sinaweibo.userinfo.mvp.view.adapter;

import android.support.v4.app.FragmentManager;

import com.gu.devel.sinaweibo.userinfo.mvp.view.RefreshableFragment;

// viewpager adapter
public class NavigationAdapter extends CacheFragmentStatePagerAdapter<RefreshableFragment> {
  private String[] TITLES;

  public NavigationAdapter(FragmentManager fm, String[] TITLES) {
    super(fm);
    this.TITLES = TITLES;
  }

  @Override
  protected RefreshableFragment createItem(int position) {
    return RefreshableFragment.getInstance(position);
  }

  @Override
  public int getCount() {
    return TITLES.length;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return TITLES[position];
  }
}
