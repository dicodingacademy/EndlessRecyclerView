package com.nbs.endlessrecyclerview;

import java.util.ArrayList;

/**
 * Created by sidiqpermana on 1/17/18.
 */

public interface MainActivityContract {
    interface View{
        void showItems(ArrayList<String> items);

        void showInitialLoading();

        void hideInitialLoading();

        void onLoadmoreStart();

        void onLoadmoreComplete();

        void onLoadmoreEnd();
    }

    interface Presenter{
        void getItems(boolean isLoadmore);

        void resetPagination();
    }
}
