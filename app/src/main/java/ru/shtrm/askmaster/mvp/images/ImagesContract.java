package ru.shtrm.askmaster.mvp.images;

import java.util.List;

import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.mvp.BasePresenter;
import ru.shtrm.askmaster.mvp.BaseView;

public interface ImagesContract {

    interface View extends BaseView<Presenter> {

        void showImagesList();

    }

    interface Presenter extends BasePresenter {
        List<Image> getImages();
        Image getImage(String id);
    }

}
