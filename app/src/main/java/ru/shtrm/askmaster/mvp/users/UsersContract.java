package ru.shtrm.askmaster.mvp.users;

import java.util.List;

import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface UsersContract {

    interface View extends BaseView<Presenter> {

        void showUsers(List<User> list);

    }

    interface Presenter extends BasePresenter {

    }

}
