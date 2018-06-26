package ru.shtrm.askmaster.mvp.profileedit;

import android.graphics.Bitmap;

import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface UserEditContract {

    interface View extends BaseView<Presenter> {

        void setUserName(String name);

        void setUserAddress(String address);

        void setUserWebsite(String website);

        void showErrorMsg();

    }

    interface Presenter extends BasePresenter {

        void saveUser(String id, String name, String address, String website, String phone,
                      String imageName, Bitmap avatar, User user);

        }

}
