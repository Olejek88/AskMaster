package ru.shtrm.askmaster.mvp.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.util.MainUtil;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    @NonNull
    private UserDetailContract.View view;

    @NonNull
    private UsersRepository usersRepository;

    @NonNull
    private String userId;

    @NonNull
    private Context context;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public UserDetailPresenter(@NonNull UserDetailContract.View view,
                               @NonNull Context context,
                               @NonNull UsersRepository usersRepository,
                               @NonNull String userId) {
        this.view = view;
        this.context = context;
        this.usersRepository = usersRepository;
        this.userId = userId;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        fetchUserData();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void fetchUserData() {
        Disposable disposable = usersRepository
                .getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User value) {
                        if (value != null) {
                            view.setUserName(value.getName());
                            view.setUserAddress(value.getAddress());
                            view.setUserWebsite(value.getWebsite());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMsg();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void saveUser(String id, String name, String address, String website, String phone,
                         Bitmap avatar, User user) {
        compositeDisposable.clear();
        if (user==null) {
            User newUser = new User();
            String uuid = java.util.UUID.randomUUID().toString();
            String fileName = uuid.concat(".jpg");
            newUser.setId(uuid);
            newUser.setName(name);
            newUser.setAddress(address);
            newUser.setWebsite(website);
            newUser.setPhone(phone);
            MainUtil.storeNewImage(avatar, context, 512, fileName);
            newUser.setAvatar(fileName);
            usersRepository.saveUser(newUser);
        }
        else
            usersRepository.saveUser(user);
    }
}
