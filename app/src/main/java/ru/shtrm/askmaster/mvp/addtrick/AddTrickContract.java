package ru.shtrm.askmaster.mvp.addtrick;

import android.content.Context;

import java.util.ArrayList;

import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface AddTrickContract {

    interface View extends BaseView<Presenter> {

        void showTitleError();

        void showTricksList();
    }

    interface Presenter extends BasePresenter {

        void saveTrick(Context context, String id, String title, String text,
                          User user, ArrayList<Image> images);

    }

}
