package ru.shtrm.askmaster.mvp.profile;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.CompaniesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.askmaster.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    @NonNull
    private UserDetailContract.View view;

    @NonNull
    private UsersRepository usersRepository;

    @NonNull
    private String companyId;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public UserDetailPresenter(@NonNull UserDetailContract.View view,
                                  @NonNull UsersRepository usersRepository,
                                  @NonNull String companyId) {
        this.view = view;
        this.companiesRepository = companiesRepository;
        this.companyId = companyId;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        fetchCompanyData();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void fetchCompanyData() {
        Disposable disposable = companiesRepository
                .getCompany(companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Company>() {
                    @Override
                    public void onNext(Company value) {
                        if (value != null) {
                            view.setCompanyName(value.getName());
                            view.setCompanyTel(value.getTel());
                            view.setCompanyWebsite(value.getWebsite());
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
    public void saveQuestion(String id, String title, String text, User user, ArrayList<Image> images) {
        compositeDisposable.clear();
        checkQuestion(id, title, text, user, images);
    }

    private void checkQuestion(final String id, final String title,
                               final String text, User user, ArrayList<Image> images) {
        // TODO id - это uuid так что не сработает
        if (questionsDataSource.isQuestionExist(id)) {
            return;
        }
        Question question = new Question();
        question.setId(java.util.UUID.randomUUID().toString());
        question.setPushable(true);
        question.setUser(user);
        question.setTitle(title);
        question.setText(text);
        question.setDate(new Date());
        questionsDataSource.saveQuestion(question);

        for (Image image : images) {
            // TODO добавить сохранение изображений в базе
            //imagesDataSource.saveImage(image);
        }

        view.showQuestionsList();
    }
}
