package ru.shtrm.askmaster.mvp.questions;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface QuestionsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showEmptyView(boolean toShow);

        void showQuestions(@NonNull List<Question> list);
    }

    interface Presenter extends BasePresenter {

        void loadQuestions();

        void refreshQuestions();

        void setFiltering(@NonNull QuestionFilterType requestType);

        QuestionFilterType getFiltering();

        void deleteQuestion(int position);
    }

}
