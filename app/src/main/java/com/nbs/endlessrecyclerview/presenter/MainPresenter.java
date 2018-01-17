package com.nbs.endlessrecyclerview.presenter;

import android.os.AsyncTask;

import com.nbs.endlessrecyclerview.MainActivityContract;

import java.util.ArrayList;

/**
 * Created by sidiqpermana on 1/17/18.
 */

public class MainPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    private final int LIMIT = 10;

    private final int MAX_PAGE = 5;

    private int CURRENT_PAGE = 0;

    public MainPresenter(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void getItems(boolean isLoadmore) {
        CURRENT_PAGE += 1;
        new DummyLoadItemsAsync(isLoadmore).execute();
    }

    @Override
    public void resetPagination() {
        CURRENT_PAGE = 0;
    }

    private class DummyLoadItemsAsync extends AsyncTask<Void, Void, ArrayList<String>>{

        private boolean isLoadmore;

        public DummyLoadItemsAsync(boolean isLoadmore) {
            this.isLoadmore = isLoadmore;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            }catch (Exception e){}

            ArrayList<String> items;

            if (CURRENT_PAGE < MAX_PAGE){
                items = new ArrayList<>();

                for (int i = 0; i < LIMIT; i++) {
                    items.add("Page "+CURRENT_PAGE+" item index "+i);
                }
            }else{
                items = null;
            }

            return items;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (isLoadmore){
                view.onLoadmoreStart();
            }else{
                view.showInitialLoading();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            if (isLoadmore){
                view.onLoadmoreComplete();

                if (strings == null){
                    view.onLoadmoreEnd();
                }
            }else{
                view.hideInitialLoading();
            }

            if (strings != null){
                view.showItems(strings);
            }
        }
    }
}
