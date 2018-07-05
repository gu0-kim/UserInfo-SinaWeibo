package com.gu.devel.sinaweibo.userinfo.mvp.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gu.devel.sinaweibo.userinfo.R;
import com.gu.devel.sinaweibo.userinfo.di.component.ComponentController;
import com.gu.devel.sinaweibo.userinfo.mvp.presenter.PagePresenter;
import com.gu.devel.sinaweibo.userinfo.mvp.view.adapter.HeaderAdapter;
import com.gu.devel.sinaweibo.userinfo.mvp.view.base.IRefreshableView;
import com.gu.devel.sinaweibo.userinfo.mvp.view.rxbus.PageItemMessage;
import com.gu.devel.sinaweibo.userinfo.mvp.view.widget.recyclerview.DividerItemDecorator;
import com.gu.devel.synscrollinghelper.role.IParentView;
import com.gu.devel.synscrollinghelper.role.entity.ChildScrollFragment;
import com.gu.devel.synscrollinghelper.scrollobservable.Scrollable;
import com.gu.devel.synscrollinghelper.scrollobservable.widget.ScrollListenRecyclerView;
import com.gu.mvp.bus.RxBus;
import com.gu.mvp.view.adapter.IBaseAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RefreshableFragment extends ChildScrollFragment<PagePresenter>
    implements IRefreshableView, IBaseAdapter.ItemClickListener {
  //  private int mLastScrollY;
  private HeaderAdapter mHeaderAdapter;
  private boolean loading;

  @Inject RxBus mRxBus;
  private Unbinder mUnbinder;

  @BindView(R.id.error_page)
  TextView error_page;

  @BindView(R.id.loading_view)
  LinearLayout loading_view;

  @BindView(R.id.fragment_root)
  FrameLayout fragment_root;

  @BindView(R.id.scroll)
  ScrollListenRecyclerView recyclerView;

  private int index;

  private void setArguments(int index) {
    Bundle b = new Bundle();
    b.putInt("index", index);
    this.index = index;
    setArguments(b);
  }

  private int getIndex() {
    return index;
  }

  public static RefreshableFragment getInstance(int index) {
    RefreshableFragment f = new RefreshableFragment();
    f.setArguments(index);
    return f;
  }

  @Override
  public boolean containsToolBar() {
    return false;
  }

  @Override
  public void inject() {
    ComponentController.getInstance().getComponent().inject(this);
  }

  @Override
  public int getLayoutId() {
    return R.layout.userinfo_list_fragment;
  }

  @Override
  public void initView(View parent) {
    mUnbinder = ButterKnife.bind(this, parent);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.addItemDecoration(new DividerItemDecorator(getContext()));
    recyclerView.setHasFixedSize(true);
    mHeaderAdapter = new HeaderAdapter(getContext());
    mHeaderAdapter.setItemClickListener(this);
    recyclerView.setAdapter(mHeaderAdapter);
    recyclerView.setScrollCallBack(this);
    presenter.loadData();
  }

  @Override
  public void destroyView() {
    mHeaderAdapter.clearAll();
    recyclerView.releaseListener();
    mUnbinder.unbind();
    mUnbinder = null;
  }
  // --------------- IChildView --------------- //

  @Override
  public IParentView findParentView() {
    return ((HomePageView) getParentFragment()).getParent();
  }

  @Override
  public void notifyParentScrollChanged(int scrollY, int deltaY) {
    // 向上层传递事件
    IParentView parent = findParentView();
    if (parent != null && parent.getCurrentIndex() == getIndex()) {
      parent.onChildScrollChanged(this, scrollY, deltaY);
    }
  }

  @Override
  public Scrollable getScrollable() {
    return recyclerView;
  }

  // --------------------------------------------- //

  @Override
  public void showLoading() {
    error_page.setVisibility(View.GONE);
    recyclerView.setVisibility(View.GONE);
    loading_view.setVisibility(View.VISIBLE);
    loading = true;
  }

  @Override
  public void stopLoading() {
    recyclerView.setVisibility(View.VISIBLE);
    loading_view.setVisibility(View.GONE);
    loading = false;
  }

  @Override
  public void notifyStartLoad() {
    mRxBus.send(new PageItemMessage(getIndex(), PageItemMessage.START_LOAD));
  }

  @Override
  public boolean isTop() {
    return recyclerView.getCurrentScrollY() == 0;
  }

  @Override
  public void notifyFinishLoad() {
    mRxBus.send(new PageItemMessage(getIndex(), PageItemMessage.FINISH_LOAD));
  }

  @Override
  public boolean isLoading() {
    return loading;
  }

  @Override
  public void showError(String error) {
    recyclerView.setVisibility(View.GONE);
    error_page.setVisibility(View.VISIBLE);
  }

  @Override
  public void setData(List<String> list) {
    if (list != null && !list.isEmpty()) {
      mHeaderAdapter.addAll(list);
      mHeaderAdapter.notifyDataSetChanged();
      recyclerView.invalidateItemDecorations();
    }
  }

  @Override
  public void onItemClick(int pos) {
    if (!forbidOnceItemClick)
      showToast(getActivity().getApplicationContext(), "onItemClick: " + pos);
    forbidOnceItemClick = false;
  }

  private boolean forbidOnceItemClick;

  public void forbidOnceClickItem(boolean forbid) {
    forbidOnceItemClick = forbid;
  }

  @Override
  public void onItemLongClick(int pos) {}
}
