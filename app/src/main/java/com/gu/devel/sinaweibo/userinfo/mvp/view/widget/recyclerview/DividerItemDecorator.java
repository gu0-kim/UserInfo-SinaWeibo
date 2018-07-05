package com.gu.devel.sinaweibo.userinfo.mvp.view.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gu.devel.sinaweibo.userinfo.R;

public class DividerItemDecorator extends RecyclerView.ItemDecoration {
  private Paint mLinePaint;
  private int mLineDividerSize;

  public DividerItemDecorator(Context context) {
    super();
    mLineDividerSize = 2;
    mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mLinePaint.setStrokeWidth(mLineDividerSize);
    mLinePaint.setColor(ContextCompat.getColor(context, R.color.divideColor));
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    int childCount = parent.getChildCount();
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();
    for (int i = 0; i < childCount; i++) {
      View view = parent.getChildAt(i);
      int pos = parent.getChildAdapterPosition(view);
      if (pos > 0) {
        c.drawLine(left, view.getTop(), right, view.getTop(), mLinePaint);
      }
    }
  }

  @Override
  public void getItemOffsets(
      Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    int pos = parent.getChildAdapterPosition(view);
    if (pos > 0) outRect.top = mLineDividerSize;
  }
}
