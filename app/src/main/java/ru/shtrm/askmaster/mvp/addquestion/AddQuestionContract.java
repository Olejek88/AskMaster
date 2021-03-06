package ru.shtrm.askmaster.mvp.addquestion;

import android.content.Context;

import java.util.ArrayList;

import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface AddQuestionContract {

    interface View extends BaseView<Presenter> {

        void showTitleExistError();

        void showTitleError();

        void showQuestionsList();

    }

    interface Presenter extends BasePresenter {

        void saveQuestion(Context context, String id, String title, String text,
                          User user, ArrayList<Image> images);

    }

}
