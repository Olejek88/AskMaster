package ru.shtrm.askmaster.mvp.profile;

import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface UserDetailContract {

    interface View extends BaseView<Presenter> {

        void setUserName(String name);

        void setUserAddress(String address);

        void setUserWebsite(String website);

    }

    interface Presenter extends BasePresenter {
    }
}
