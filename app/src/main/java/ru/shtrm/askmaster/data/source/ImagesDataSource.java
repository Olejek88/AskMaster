package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.RealmList;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;

public interface ImagesDataSource {

    Observable<List<Image>> getImages();

    Observable<Image> getImage(@NonNull final String id);

    void saveImage(@NonNull Image image);

    RealmList<Image> saveImages(@NonNull ArrayList<Image> images);

    void deleteImage(@NonNull String id);

}
