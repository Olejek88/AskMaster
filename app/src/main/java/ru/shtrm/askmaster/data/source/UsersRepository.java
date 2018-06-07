package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.User;

public class UsersRepository implements UsersDataSource {

    @Nullable
    private static UsersRepository INSTANCE = null;

    @NonNull
    private final UsersDataSource localDataSource;

    // Prevent direct instantiation
    private UsersRepository(@NonNull UsersDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static UsersRepository getInstance(@NonNull UsersDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<User>> getUsers() {
        return localDataSource.getUsers();
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        return localDataSource.getUser(id);
    }

    @Override
    public void initData() {
        localDataSource.initData();
    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        return localDataSource.searchUsers(keyWords);
    }

}
