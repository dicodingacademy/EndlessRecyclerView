package com.nbs.endlessrecyclerview.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.nbs.endlessrecyclerview.MainActivityContract;
import com.nbs.endlessrecyclerview.R;
import com.nbs.endlessrecyclerview.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View, MainAdapter.OnLoadMoreListener{

    private RecyclerView rvItems;

    private ProgressBar progressBar;

    private MainPresenter presenter;

    private MainAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        initPresenter();

        initLoadItem();

    }

    private void initLoadItem() {
        presenter.getItems(false);
    }

    private void initPresenter() {
        presenter = new MainPresenter(this);
    }

    private void bindView() {
        progressBar = findViewById(R.id.mainprogressbar);

        linearLayoutManager = new LinearLayoutManager(this);

        rvItems = findViewById(R.id.rv_items);
        rvItems.setHasFixedSize(true);
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new MainAdapter();
        adapter.setItems(new ArrayList<String>());
        adapter.setRecyclerView(rvItems);
        adapter.setLinearLayoutManager(linearLayoutManager);
        adapter.setOnLoadMoreListener(this);

        rvItems.setAdapter(adapter);
    }

    @Override
    public void showItems(ArrayList<String> items) {
        adapter.getItems().addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showInitialLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInitialLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadmoreStart() {
        adapter.setLoadmoreProgress(true);
    }

    @Override
    public void onLoadmoreComplete() {
        adapter.setLoadmoreProgress(false);
    }

    @Override
    public void onLoadmoreEnd() {
        adapter.removeScrollListener();
    }

    @Override
    public void onLoadMore() {
        presenter.getItems(true);
    }
}
