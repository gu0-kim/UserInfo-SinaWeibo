package com.gu.devel.sinaweibo.userinfo.mvp.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gu.devel.sinaweibo.userinfo.R;
import com.gu.mvp.view.adapter.IBaseAdapter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderAdapter extends IBaseAdapter<String, RecyclerView.ViewHolder> {

  private static final int HEADER_VIEW_TYPE = 1;
  private static final int DATA_VIEW_TYPE = 2;
  private static final int DIVIDER_TYPE = 3;
  private static final int HEADER_COUNT = 2;
  private LayoutInflater mInflater;

  public HeaderAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public int getItemCount() {
    return super.getItemCount() + HEADER_COUNT;
  }

  @Override
  public int getItemViewType(int position) {
    return position == 0 ? HEADER_VIEW_TYPE : position == 1 ? DIVIDER_TYPE : DATA_VIEW_TYPE;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == HEADER_VIEW_TYPE)
      return new DefaultViewHolder(mInflater.inflate(R.layout.recycler_header, parent, false));
    else if (viewType == DIVIDER_TYPE) {
      return new DescriptionViewHolder(mInflater.inflate(R.layout.des_layout, parent, false));
    } else return new DataViewHolder(mInflater.inflate(R.layout.text_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (position == HEADER_COUNT - 1) {
      ((DescriptionViewHolder) holder)
          .text.setText(String.format(Locale.getDefault(), "全部微博(%d)", data.size()));
    } else if (position > HEADER_COUNT - 1) {
      ((DataViewHolder) holder).text.setText(data.get(position - HEADER_COUNT));
    }
  }

  class DataViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text)
    TextView text;

    DataViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (mItemClickListener != null) mItemClickListener.onItemClick(getAdapterPosition());
            }
          });
    }
  }

  class DescriptionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.num_des_text)
    TextView text;

    DescriptionViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class DefaultViewHolder extends RecyclerView.ViewHolder {
    DefaultViewHolder(View itemView) {
      super(itemView);
    }
  }
}
