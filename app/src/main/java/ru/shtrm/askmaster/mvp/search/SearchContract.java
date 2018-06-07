package ru.shtrm.askmaster.mvp.search;

import java.util.List;

import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showResult(List<Question> questions, List<User> users);

    }

    interface Presenter extends BasePresenter {

        void search(String keyWords);

    }

}
