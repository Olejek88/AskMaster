package ru.shtrm.askmaster.mvp.questiondetails;

import android.support.annotation.NonNull;

import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface QuestionDetailsContract {

    interface View extends BaseView<Presenter> {

        void showNetworkError();

        void showQuestionDetails(@NonNull Question question);

        void exit();

    }

    interface Presenter extends BasePresenter {

        void refreshQuestion();

        void deleteQuestion();

        String getQuestionTitle();

        void updateQuestionTitle(String newTitle);

    }

}
