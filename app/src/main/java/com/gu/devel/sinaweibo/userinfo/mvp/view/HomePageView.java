package com.gu.devel.sinaweibo.userinfo.mvp.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gu.devel.sinaweibo.userinfo.R;
import com.gu.devel.sinaweibo.userinfo.di.component.ComponentController;
import com.gu.devel.sinaweibo.userinfo.glide.GlideCircleTransformWithBorder;
import com.gu.devel.sinaweibo.userinfo.mvp.presenter.HomePagePresenter;
import com.gu.devel.sinaweibo.userinfo.mvp.view.adapter.NavigationAdapter;
import com.gu.devel.sinaweibo.userinfo.mvp.view.base.IHomePageView;
import com.gu.devel.sinaweibo.userinfo.mvp.view.rxbus.PageItemMessage;
import com.gu.devel.sinaweibo.userinfo.mvp.view.widget.layout.TouchInterceptLayout;
import com.gu.devel.synscrollinghelper.role.IParentView;
import com.gu.devel.synscrollinghelper.role.entity.SyncScrollControlViewPager;
import com.gu.indicator.TabLayout;
import com.gu.mvp.bus.Message;
import com.gu.mvp.bus.RxBus;
import com.gu.mvp.bus.RxBusMessageCallback;
import com.gu.mvp.glide.GlideApp;
import com.gu.mvp.utils.scroll.ScrollUtils;
import com.gu.mvp.view.IView;
import com.nineoldandroids.view.ViewPropertyAnimator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class HomePageView extends IView<HomePagePresenter>
    implements IHomePageView<RefreshableFragment>,
        RxBusMessageCallback,
        SyncScrollControlViewPager.UIScrollCallBack {

  private static final int MAX_PULL_DISTANCE = 300;
  // tab标题
  private static final String[] TITLES = new String[] {"主页", "微博", "相册"};
  Unbinder mUnbinder;

  @BindView(R.id.image)
  ImageView image;

  @BindView(R.id.headerlayout)
  LinearLayout headerLayout;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.pager)
  SyncScrollControlViewPager mViewPager;

  @BindView(R.id.tablayout)
  TabLayout tablayout;

  @BindView(R.id.pager_wrapper)
  PtrClassicFrameLayout ptrFrame;

  @BindView(R.id.headerimg)
  ImageView userImg;

  @BindView(R.id.title_center)
  TextView toolbarTitle;

  @BindView(R.id.navImg)
  ImageView navImg;

  @BindView(R.id.pb)
  ImageView pb;

  @BindView(R.id.search)
  ImageView search;

  private int mFlexibleSpaceHeight;

  private int mToolbarSize;
  private int headerLayoutInitY, tabLayoutInitY;
  private int maxImageMoveDistance;
  private int mFrontViewScrollY;

  private NavigationAdapter mPagerAdapter;
  private boolean mValidPull = true;
  private Animation mRotateAnimation;

  @Inject RxBus mRxBus;

  public static HomePageView getInstance() {
    return new HomePageView();
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
    return R.layout.home_page_layout;
  }

  @Override
  public void initView(View parent) {
    mRxBus.register(this, PageItemMessage.class);
    mUnbinder = ButterKnife.bind(this, parent);
    mPagerAdapter = new NavigationAdapter(getChildFragmentManager(), TITLES);
    mViewPager.setCallBack(this);
    mViewPager.setOffscreenPageLimit(TITLES.length);
    mViewPager.setAdapter(mPagerAdapter);
    if (getContext() != null) {
      tablayout
          .setMargin(80)
          .setTextSize(14)
          .setRd(8)
          .setIndicatorColor(ContextCompat.getColor(getContext(), R.color.orange_800))
          .setTextColors(
              ContextCompat.getColorStateList(getContext(), R.color.tab_text_state_color))
          .createContentByTitles(TITLES)
          .setViewPager(mViewPager)
          .combine();
    }
    navImg.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mActivity.finish();
          }
        });
    // Initialize the first Fragment's state when layout is completed.
    ScrollUtils.addOnGlobalLayoutListener(
        tablayout,
        new Runnable() {
          @Override
          public void run() {
            loadDimens();
            translateTab(0);
          }
        });

    ptrFrame.disableWhenHorizontalMove(true);
    ptrFrame.getHeader().setVisibility(View.INVISIBLE);
    ptrFrame.setPtrHandler(this);
    ptrFrame.addPtrUIHandler(this);

    glideLoadImage(getContext());
    ((TouchInterceptLayout) parent)
        .setOnTouchInFrontImageViewListener(
            new TouchInterceptLayout.OnTouchInFrontImageViewListener() {
              @Override
              public void notifyInterceptOnce(boolean intercept) {
                // 阻止透点情况发生
                getCurrentPage().forbidOnceClickItem(intercept);
              }
            });
  }

  @Override
  public void destroyView() {
    GlideApp.with(this).clear(userImg);
    if (getView() != null) ((TouchInterceptLayout) getView()).clear();
    mViewPager.clear();
    mRxBus.unRegister(this);
    mRxBus.destroyBus();
    mRxBus = null;
    mUnbinder.unbind();
    mUnbinder = null;
  }

  public IParentView getParent() {
    return mViewPager;
  }
  // --------------- UIScrollCallBack --------------- //
  /**
   * Front view move up
   *
   * @param deltaY others scroll deltaY when currentItem moves up.
   */
  @Override
  public void onMoveUpBy(int deltaY) {
    int adjustedScrollY = Math.min(mFrontViewScrollY + deltaY, maxImageMoveDistance);
    translateTab(adjustedScrollY);
    mViewPager.dispatchScroll2BackgroundChildren(adjustedScrollY - mFrontViewScrollY);
    mFrontViewScrollY = adjustedScrollY;
  }

  @Override
  public void onMoveDownTo(int scrollY) {
    translateTab(scrollY);
    mViewPager.dispatchScroll2BackgroundChildren(scrollY - mFrontViewScrollY);
    mFrontViewScrollY = scrollY;
  }

  @Override
  public boolean canMoveDown(int scrollY) {
    return scrollY < mFrontViewScrollY;
  }

  // --------------- IHomePageView --------------- //
  @Override
  public void showLoading() {
    if (mRotateAnimation == null) {
      mRotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pb_rotate_anim);
    }
    pb.startAnimation(mRotateAnimation);
  }

  @Override
  public void stopLoading() {
    stopProgressBarAnim();
    pb.setImageLevel(0);
    pb.setRotation(0);
  }

  @Override
  public boolean checkState(int index) {
    return mPagerAdapter.getItem(index).isLoading();
  }

  @Override
  public void showLoadError() {}

  @Override
  public void loadComplete() {
    ptrFrame.refreshComplete();
  }

  @Override
  public boolean canPull() {
    return mValidPull
        && mFrontViewScrollY == 0
        && !getCurrentPage().isLoading()
        && getCurrentPage().isTop();
  }

  @Override
  public boolean isValidPullDistance(int pullDistance) {
    return pullDistance <= MAX_PULL_DISTANCE;
  }

  @Override
  public void showProgressBar() {
    pb.setImageLevel(2);
  }

  @Override
  public void showButton() {
    pb.setImageLevel(0);
  }

  @Override
  public void rotateProgressBar(float delta) {
    pb.setPivotX(pb.getWidth() / 2);
    pb.setPivotY(pb.getHeight() / 2);
    pb.setRotation(delta * 360);
  }

  @Override
  public void stopProgressBarAnim() {
    pb.clearAnimation();
  }

  @Override
  public void horizontalScrollable(boolean scrollable) {
    mViewPager.canHorizontalScroll(scrollable);
  }

  @Override
  public RefreshableFragment getCurrentPage() {
    return mPagerAdapter.getItemAt(mViewPager.getCurrentItem());
  }

  @Override
  public void pull(int distance) {
    //    float scale = (float) (distance + mFlexibleSpaceHeight) / mFlexibleSpaceHeight;
    //    image.setPivotX(image.getWidth() / 2);
    //    image.setPivotY(0);
    //    image.setScaleX(scale);
    //    image.setScaleY(scale);
    image.setTranslationY(distance * 0.5f);
    tablayout.setTranslationY(tabLayoutInitY + distance);
    headerLayout.setTranslationY(headerLayoutInitY + distance);
  }

  // --------------- PtrUIHandler, PtrHandler --------------- //
  @Override
  public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
    return presenter.canPull();
  }

  @Override
  public void onRefreshBegin(PtrFrameLayout frame) {
    presenter.refreshView();
  }

  @Override
  public void onUIReset(PtrFrameLayout frame) {
    showButton();
  }

  @Override
  public void onUIRefreshPrepare(PtrFrameLayout frame) {
    showProgressBar();
  }

  @Override
  public void onUIRefreshBegin(PtrFrameLayout frame) {}

  @Override
  public void onUIRefreshComplete(PtrFrameLayout frame) {}

  @Override
  public void onUIPositionChange(
      PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
    updatePullFlag(ptrIndicator.getCurrentPosY());
    pull(ptrIndicator.getCurrentPosY());
    if (ptrIndicator.isOverOffsetToRefresh()) {
      float delta =
          ptrIndicator.getCurrentPercent() - ptrIndicator.getRatioOfHeaderToHeightRefresh();
      rotateProgressBar(delta);
    }
    if (ptrIndicator.getCurrentPosY() == 0) {
      pb.setRotation(0);
    }
  }

  // --------------- RxBusMessageCallback --------------- //
  @Override
  public void accept(Message message) {
    if ((Integer) message.getData() != mViewPager.getCurrentIndex()) {
      // do nothing
      return;
    }
    if (message.typeCode() == PageItemMessage.START_LOAD) {
      horizontalScrollable(false);
      showProgressBar();
      showLoading();
    } else if (message.typeCode() == PageItemMessage.FINISH_LOAD) {
      horizontalScrollable(true);
      stopLoading();
      loadComplete();
    }
  }

  // --------------------------------------------- //
  private void glideLoadImage(Context context) {
    if (context != null)
      GlideApp.with(this)
          .load(R.drawable.user_img)
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .transform(
              new GlideCircleTransformWithBorder(
                  context, 4, ContextCompat.getColor(context, R.color.grey_500)))
          .into(userImg);
  }

  private void updatePullFlag(int pullDistance) {
    mValidPull = isValidPullDistance(pullDistance);
  }

  /** call from addOnGlobalLayoutListener,load height and initY parameter */
  private void loadDimens() {
    mFlexibleSpaceHeight = image.getBottom();
    int tabHeight = tablayout.getHeight();
    //    if (getContext() != null && DimenUtils.currentSdkUpVersion(19)) {
    //      DimenUtils.setToolbarFitsSystemWindows(getContext(), toolbar);
    //    }
    mToolbarSize = toolbar.getLayoutParams().height;
    maxImageMoveDistance = mFlexibleSpaceHeight - mToolbarSize - tabHeight;
    tabLayoutInitY = mFlexibleSpaceHeight - tabHeight;
    headerLayoutInitY = (mFlexibleSpaceHeight - tabHeight - headerLayout.getHeight()) / 2;
  }

  // move tabLayout
  private void translateTab(int scrollY) {
    boolean toolbarStick = scrollY >= maxImageMoveDistance;
    // 渐变alpha
    float alpha = Math.max(0.5f, 1 - (float) scrollY / maxImageMoveDistance);
    // stick alpha
    float toolbarAlpha = toolbarStick ? 1 : 0;
    if (getContext() != null) {
      pb.setImageLevel(toolbarStick ? 1 : 0);
      search.setImageLevel(toolbarStick ? 1 : 0);
      navImg.setImageLevel(toolbarStick ? 1 : 0);
      toolbarTitle.setVisibility(toolbarStick ? View.VISIBLE : View.INVISIBLE);
      toolbar.setBackgroundColor(
          ScrollUtils.getColorWithAlpha(
              toolbarAlpha, ContextCompat.getColor(getContext(), R.color.toolbar_color_grey)));
    }
    headerLayout.setAlpha(alpha);
    float tranY = ScrollUtils.getFloat(-scrollY, -maxImageMoveDistance, 0);

    image.setTranslationY(tranY);
    ViewPropertyAnimator.animate(tablayout).cancel();
    float tabTranslationY =
        ScrollUtils.getFloat(-scrollY + tabLayoutInitY, mToolbarSize, tabLayoutInitY);
    float headLayoutTranslationY =
        ScrollUtils.getFloat(
            -scrollY + headerLayoutInitY, -headerLayout.getHeight(), headerLayoutInitY);
    tablayout.setTranslationY(tabTranslationY);
    headerLayout.setTranslationY(headLayoutTranslationY);
  }
}
