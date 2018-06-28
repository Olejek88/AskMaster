package ru.shtrm.askmaster.mvp.questionedit;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmList;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;
import ru.shtrm.askmaster.mvp.profileedit.UserEditContract;

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
