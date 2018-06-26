package ru.shtrm.askmaster.mvp.tricks;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.askmaster.data.Trick;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface TricksContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView(boolean toShow);

        void showTricks(@NonNull List<Trick> list);
    }

    interface Presenter extends BasePresenter {

        void loadTricks();
    }

}
