package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.User;

public interface UsersDataSource {

    Observable<List<User>> getUsers();

    Observable<User> getUser(@NonNull String userId);

    Observable<List<User>> searchUsers(@NonNull String keyWords);

    User getUserById(@NonNull String userId);

    void saveUser(@NonNull User user);

    void deleteUser(@NonNull String id);

    boolean isUserExist(@NonNull String id);

}
