package com.gu.devel.sinaweibo.userinfo.mvp.presenter;

import com.gu.devel.sinaweibo.userinfo.di.component.ComponentController;
import com.gu.devel.sinaweibo.userinfo.mvp.model.PageItemModel;
import com.gu.devel.sinaweibo.userinfo.mvp.view.RefreshableFragment;
import com.gu.mvp.presenter.IPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PagePresenter extends IPresenter<RefreshableFragment> {

  @Inject PageItemModel mModel;

  public PagePresenter() {
    ComponentController.getInstance().getComponent().inject(this);
  }

  public void loadData() {
    addTask(
        Observable.just("load")
            .doOnNext(
                new Consumer<String>() {
                  @Override
                  public void accept(String arg) throws Exception {
                    view.showLoading();
                    view.notifyStartLoad();
                  }
                })
            .observeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .map(
                new Function<String, List<String>>() {
                  @Override
                  public List<String> apply(String list) throws Exception {
                    return mModel.getDummyData(20);
                  }
                })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Consumer<List<String>>() {
                  @Override
                  public void accept(List<String> strings) throws Exception {
                    view.setData(strings);
                    view.stopLoading();
                    view.notifyFinishLoad();
                  }
                },
                new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                  }
                }));
  }
}
