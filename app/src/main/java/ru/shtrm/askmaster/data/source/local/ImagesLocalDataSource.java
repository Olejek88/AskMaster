package ru.shtrm.askmaster.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.realm.RealmHelper;

public class ImagesLocalDataSource implements ImagesDataSource {

    @Nullable
    private static ImagesLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ImagesLocalDataSource() {

    }

    public static ImagesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImagesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Image>> getImages() {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable
                .fromIterable(realm.copyFromRealm(
                        realm.where(Image.class).findAllSorted("name", Sort.ASCENDING)))
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Image> getImage(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable
                .just(realm.copyFromRealm(realm.where(Image.class).equalTo("id", id).findFirst()));
    }

    public void deleteImage(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Image Image =  realm.where(Image.class).equalTo("id", id).findFirst();
        if (Image!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Image.deleteFromRealm();
                }
            });
        }
    }

    /**
     * Save a Image to database.
     * @param Image See {@link Image}
     */
    @Override
    public void saveImage(@NonNull final Image Image) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(Image);
            }
        });
        realm.close();
    }

}