package ru.shtrm.askmaster.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;
import ru.shtrm.askmaster.data.source.UsersDataSource;
import ru.shtrm.askmaster.retrofit.RetrofitClient;
import ru.shtrm.askmaster.retrofit.RetrofitService;

import static ru.shtrm.askmaster.realm.RealmHelper.DATABASE_NAME;

public class UsersRemoteDataSource implements UsersDataSource {

    @Nullable
    private static UsersRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private UsersRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<User>> getUsers() {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public User getUserById(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public void saveUser(@NonNull User user) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public void deleteUser(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public boolean isUserExist(@NonNull String id) {
        // Not required
        return false;
    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        // Not required
        return null;
    }

}