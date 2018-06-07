package ru.shtrm.askmaster.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.UsersDataSource;
import ru.shtrm.askmaster.realm.RealmHelper;
import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

public class UsersLocalDataSource implements UsersDataSource {

    @Nullable
    public static UsersLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private UsersLocalDataSource() {

    }

    public static UsersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<User>> getUsers() {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable
                .fromIterable(realm.copyFromRealm(
                        realm.where(User.class).findAllSorted("name", Sort.ASCENDING)))
                .toList()
                .toObservable();
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable
                .just(realm.copyFromRealm(realm.where(User.class).equalTo("id", id).findFirst()));
    }

    @Override
    public void initData() {
        Realm rlm = RealmHelper.newRealmInstance();
        rlm.beginTransaction();
        rlm.commitTransaction();
        rlm.close();

    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        Realm rlm = RealmHelper.newRealmInstance();
        List<User> results = rlm.copyFromRealm(
                rlm.where(User.class)
                        .like("name","*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("tel", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("website", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAllSorted("name", Sort.ASCENDING));
        return Observable.fromIterable(results)
                .toList()
                .toObservable();
    }

    public User getAuthorisedUser() {
        Realm realm = RealmHelper.newRealmInstance();
        return realm.where(User.class).equalTo("uuid",
                ru.shtrm.gosport.model.AuthorizedUser.getInstance().getUuid()).findFirst();
    }

}