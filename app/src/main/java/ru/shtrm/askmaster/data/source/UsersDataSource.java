package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.User;

public interface UsersDataSource {

    Observable<List<User>> getUsers();

    Observable<User> getUser(@NonNull String userId);

    void initData();

    Observable<List<User>> searchUsers(@NonNull String keyWords);

}
