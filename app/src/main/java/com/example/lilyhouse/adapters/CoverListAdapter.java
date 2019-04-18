package com.example.lilyhouse.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.lilyhouse.GlideApp;
import com.example.lilyhouse.R;
import com.example.lilyhouse.models.MangaCoverItem;

import java.util.ArrayList;
import java.util.List;

public class CoverListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "======CoverListAdapter";
    private static final String BASIC_IMAGE_URL = "https://images.dmzj.com/";
    private ArrayList<MangaCoverItem> coverItems = new ArrayList<>();
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public CoverListAdapter(Context context) {
        this.context = context;
    }

    private static GlideUrl getGlideUrl(String urlString) {
        GlideUrl url = new GlideUrl(urlString, new LazyHeaders.Builder()
                .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365))
                .addHeader("Referer", "https://m.dmzj.com/classify.html")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build());
        return url;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if (i == MangaCoverItem.TYPE_FOOTER) {
            itemView = LayoutInflater.from(context).inflate(R.layout.footer_item_layout,
                    viewGroup, false);
            return new FooterVH(itemView);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.main_cover_item_layout,
                    viewGroup, false);
            return new NormalVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof FooterVH) return;
        NormalVH normalVH = (NormalVH) viewHolder;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_from_bottom);
        normalVH.itemView.startAnimation(animation);
        normalVH.tvTitle.setText(coverItems.get(i).name);
        normalVH.tvAuthor.setText(coverItems.get(i).authors);
        String imgUrl = BASIC_IMAGE_URL + coverItems.get(i).cover;
        GlideApp.with(normalVH.ivCover)
                .load(getGlideUrl(imgUrl))
                .error(R.drawable.error_bg)
                .placeholder(R.drawable.loading_bg)
                .centerCrop()
                .into(normalVH.ivCover);

    }

    @Override
    public int getItemCount() {
        return (coverItems == null ? 0 : coverItems.size());
    }

    @Override
    public long getItemId(int position) {
        if (coverItems == null || coverItems.size() == 0) return 0;
        return coverItems.get(position).id;
    }

    @Override
    public int getItemViewType(int position) {
        if (coverItems == null || coverItems.size() == 0) return 0;
        return coverItems.get(position).getItemType();
    }

    public void addItems(List<MangaCoverItem> items) {
        if (coverItems == null) coverItems = new ArrayList<>();
        int pos = coverItems.size();
        coverItems.addAll(items);
        notifyItemRangeInserted(pos, items.size());
    }

    public void setItems(List<MangaCoverItem> items) {
        if (coverItems == null) coverItems = new ArrayList<>();
        int pos = coverItems.size();
        int count = items.size() - coverItems.size();
        coverItems.clear();
        coverItems.addAll(items);
        notifyItemRangeInserted(pos, count);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void addFooter() {
        if (coverItems == null || coverItems.size() == 0 ||
                coverItems.get(coverItems.size() - 1).getItemType() == MangaCoverItem.TYPE_FOOTER)
            return;
        coverItems.add(new MangaCoverItem(MangaCoverItem.TYPE_FOOTER));
        notifyItemInserted(coverItems.size() - 1);
    }

    public void removeFooter() {
        if (getItemViewType(coverItems.size() - 1) == MangaCoverItem.TYPE_FOOTER) {
            coverItems.remove(coverItems.size() - 1);
//            notifyItemRemoved(coverItems.size());
        }
    }

    private class FooterVH extends RecyclerView.ViewHolder {
        public FooterVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class NormalVH extends RecyclerView.ViewHolder {

        private ImageView ivCover;
        private TextView tvTitle;
        private TextView tvAuthor;

        public NormalVH(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
