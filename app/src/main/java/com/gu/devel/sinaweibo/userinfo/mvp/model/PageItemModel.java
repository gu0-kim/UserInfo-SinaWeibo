package com.gu.devel.sinaweibo.userinfo.mvp.model;

import java.util.ArrayList;
import java.util.List;

public class PageItemModel {
  public List<String> getDummyData(int num) {
    ArrayList<String> items = new ArrayList<>();
    for (int i = 1; i <= num; i++) {
      items.add("Item " + i);
    }
    return items;
  }
}
