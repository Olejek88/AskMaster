package ru.shtrm.askmaster.mvp.addquestion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmList;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;
import ru.shtrm.askmaster.data.source.UsersDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class AddQuestionPresenter implements AddQuestionContract.Presenter{

    @NonNull
    private final AddQuestionContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public AddQuestionPresenter(@NonNull QuestionsDataSource dataSource,
                                @NonNull UsersDataSource userDataSource,
                                @NonNull ImagesDataSource imagesDataSource,
                                @NonNull AddQuestionContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
        this.imagesDataSource = imagesDataSource;
        this.usersDataSource = userDataSource;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void saveQuestion(Context context, String id, String title, String text, User user,
                             ArrayList<Image> images) {
        compositeDisposable.clear();
        checkQuestion(context, id, title, text, user, images);
    }

    private void checkQuestion(Context context, final String id, final String title,
                               final String text, User user, ArrayList<Image> images) {
        // TODO id - это uuid так что не сработает
        if (questionsDataSource.isQuestionExist(id)) {
            return;
        }
        Question question = new Question();
        question.setId(java.util.UUID.randomUUID().toString());
        question.setPushable(true);
        question.setClosed(false);
        question.setUser(user);
        question.setTitle(title);
        question.setText(text);
        question.setDate(new Date());
        for (Image image : images) {
            saveImage(context, title, image.getImageName());
        }
        question.setImages(imagesDataSource.saveImages(images));
        questionsDataSource.saveQuestion(question);

        view.showQuestionsList();
    }

    private void saveImage(Context context, String title, String imageName) {
        Bitmap bitmap = MainUtil.getBitmapByPath(MainUtil.getPicturesDirectory(context),imageName);
        if (bitmap!=null) {
            Image image = new Image();
            image.setId(java.util.UUID.randomUUID().toString());
            image.setImageName(imageName);
            image.setDate(new Date());
            image.setTitle(title);
            imagesDataSource.saveImage(image);
        }
    }

}
