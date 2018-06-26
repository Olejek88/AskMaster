package ru.shtrm.askmaster.mvp.trickdetails;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import ru.shtrm.askmaster.data.Trick;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface TrickDetailsContract {

    interface View extends BaseView<Presenter> {

        void showTrickDetails(@NonNull Trick Trick);

        void exit();

    }

    interface Presenter extends BasePresenter {

        void deleteTrick();
    }

}
