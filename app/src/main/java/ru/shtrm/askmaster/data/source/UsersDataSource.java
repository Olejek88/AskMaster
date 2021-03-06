package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.Trick;
import ru.shtrm.askmaster.data.User;

public interface UsersDataSource {

    Observable<List<User>> getUsers();

    Observable<User> getUser(@NonNull String userId);

    Observable<List<User>> searchUsers(@NonNull String keyWords);

    User getUserById(@NonNull String userId);

    void saveUser(@NonNull User user);

    void deleteUser(@NonNull String id);

    boolean isUserExist(@NonNull String id);

    void addQuestion(@NonNull Question question, User user);

    void addTrick(@NonNull Trick trick, User user);

    void addAnswer(@NonNull Answer answer, User user);
}
