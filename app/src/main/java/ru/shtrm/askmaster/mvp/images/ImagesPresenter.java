package ru.shtrm.askmaster.mvp.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.RealmList;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;
import ru.shtrm.askmaster.data.source.UsersDataSource;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.util.MainUtil;

public class ImagesPresenter implements ImagesContract.Presenter{

    @NonNull
    private final ImagesContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    public ImagesPresenter(@NonNull QuestionsDataSource dataSource,
                    @NonNull ImagesDataSource imagesDataSource,
                    @NonNull ImagesContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
        this.imagesDataSource = imagesDataSource;
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
    }

    public List<Image> getImages() {
        return imagesDataSource.getImages();
    }
}
