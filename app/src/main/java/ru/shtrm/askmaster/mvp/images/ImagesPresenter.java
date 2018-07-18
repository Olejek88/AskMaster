package ru.shtrm.askmaster.mvp.images;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;

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

    public Image getImage(String id) {
        return imagesDataSource.getImage(id);
    }

}
