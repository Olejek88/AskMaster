package ru.shtrm.askmaster.mvp.profile;

import android.graphics.Bitmap;

import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface UserDetailContract {

    interface View extends BaseView<Presenter> {

        void setUserName(String name);

        void setUserAddress(String address);

        void setUserWebsite(String website);

        void showErrorMsg();

    }

    interface Presenter extends BasePresenter {

        void saveUser(String id, String name, String address, String website, String phone,
                             Bitmap avatar, User user);

        }

}
