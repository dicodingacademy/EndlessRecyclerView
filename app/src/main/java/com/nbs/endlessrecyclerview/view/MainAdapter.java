package com.nbs.endlessrecyclerview.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbs.endlessrecyclerview.R;

import java.util.ArrayList;

/**
 * Created by sidiqpermana on 1/17/18.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> items;

    private final int VIEW_ITEM = 1;

    private final int VIEW_LOADMORE = 0;

    private boolean isMoreLoading = false;

    private int visibleThreshold = 10;

    private int lasttVisibleItem, totalItemCount;

    private LinearLayoutManager linearLayoutManager;

    private OnLoadMoreListener onLoadMoreListener;

    private RecyclerView recyclerView;

    private RecyclerView.OnScrollListener scrollListener;

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public void setRecyclerView(RecyclerView recyclerView){

        this.recyclerView = recyclerView;

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = getLinearLayoutManager().getItemCount();
                lasttVisibleItem = getLinearLayoutManager().findLastVisibleItemPosition();
                if (!isMoreLoading && totalItemCount <= (lasttVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        getOnLoadMoreListener().onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    public void removeScrollListener(){
        recyclerView.removeOnScrollListener(scrollListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new MainViewholder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadmore, parent, false);
            return new LoadmoreViewholder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewholder){
            ((MainViewholder) holder).bind(getItems().get(position));
        }

        if (holder instanceof LoadmoreViewholder){
            ((LoadmoreViewholder)holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItems().get(position) != null ? VIEW_ITEM : VIEW_LOADMORE;
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public void setLoadmoreProgress(final boolean isProgress) {
        isMoreLoading = isProgress;
        if (isProgress) {
            getItems().add(getItems().size(), null);
            notifyDataSetChanged();
        } else {
            if (getItems().size() > 0){
                getItems().remove(getItems().size() - 1);
                notifyDataSetChanged();
            }
        }
    }

    public class MainViewholder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public MainViewholder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_item);
        }

        public void bind(String item){
            tvItem.setText(item);
        }
    }

    public class LoadmoreViewholder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public LoadmoreViewholder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }

        public void bind(){
            if (isMoreLoading){
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
