package ru.shtrm.askmaster.mvp.questionedit;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmList;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface QuestionEditContract {

    interface View extends BaseView<Presenter> {

        void showQuestionEdit(@NonNull Question question);

        void showErrorMsg();

        void exit();

    }

    interface Presenter extends BasePresenter {

        void saveQuestion(String id, String title, String text, Date date, boolean closed,
                          RealmList<Image> images,
                          RealmList<Answer> answers,
                          User user);
    }

}
