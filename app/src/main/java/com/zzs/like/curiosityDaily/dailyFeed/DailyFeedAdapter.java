package com.zzs.like.curiosityDaily.dailyFeed;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzs.like.R;
import com.zzs.like.data.daily.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * DailyFeed适配器
 *
 * @author zzs
 * @date 2016.09.28
 */
public class DailyFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 上下文
    private Context mContext;
    // 数据源
    private List<Options> mOptions;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param options 选项集合
     */
    public DailyFeedAdapter(Context context, List<Options> options) {
        mContext = context;
        mOptions = options;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_daily_feed_option,null);
        return new FeedOptionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedOptionViewHolder feedOptionViewHolder = (FeedOptionViewHolder) holder;
        feedOptionViewHolder.bindItem(mOptions.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    /**
     * ViewHolder
     */
    class FeedOptionViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_feed_author_icon)
        ImageView iv_feed_author_icon;
        @Bind(R.id.tv_feed_author_name)
        TextView tv_feed_author_name;
        @Bind(R.id.tv_feed_content)
        TextView tv_feed_content;
        @Bind(R.id.card_option)
        CardView card_option;

        /**
         * 构造方法
         *
         * @param itemView itemView
         */
        public FeedOptionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        /**
         * 绑定Item
         * @param options 选项集合
         * @param position 位置
         */
        private void bindItem(Options options,int position){
            tv_feed_author_name.setText(options.getAuthor().getName());
            tv_feed_content.setText(options.getContent());
            Glide.with(mContext).load(options.getAuthor().getAvatar()).centerCrop().into(iv_feed_author_icon);

            if((position)%4==0){
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_1));
            }else if((position-1)%4==0){
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_2));
            }else if((position-2)%4==0){
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_3));
            }else if((position-3)%4==0){
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_4));
            }
        }
    }
}
