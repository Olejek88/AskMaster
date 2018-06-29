package ru.shtrm.askmaster.mvp.addanswer;

import android.content.Context;

import java.util.ArrayList;

import io.realm.RealmList;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface AddAnswerContract {

    interface View extends BaseView<Presenter> {
        void showQuestion();
        void showTitleError();
    }

    interface Presenter extends BasePresenter {

        void saveAnswer(Context context, String id, String title, String text,
                        User user, ArrayList<Image> images, Question question);

    }

}
