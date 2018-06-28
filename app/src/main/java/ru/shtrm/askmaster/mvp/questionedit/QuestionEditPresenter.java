package ru.shtrm.askmaster.mvp.questionedit;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.QuestionsRepository;

public class QuestionEditPresenter implements QuestionEditContract.Presenter {

    @NonNull
    private QuestionEditContract.View view;

    @NonNull
    private QuestionsRepository questionsRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    private String questionTitle;
    private String questionText;
    private ArrayList<Image> images;

    @NonNull
    private String questionId;

    public QuestionEditPresenter(@NonNull String id,
                                    @NonNull QuestionsRepository questionsRepository,
                                    @NonNull QuestionEditContract.View questionDetailView) {
        this.questionId = id;
        this.view = questionDetailView;
        this.questionsRepository = questionsRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        openDetail();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    /**
     * Load data from repository.
     */
    private void openDetail() {
        Disposable disposable = questionsRepository
                .getQuestion(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Question>() {
                    @Override
                    public void onNext(Question value) {
                        questionTitle = value.getTitle();
                        questionText = value.getText();
                        if (value.getImages().size()>0)
                            images = new ArrayList<>(value.getImages());
                        view.showQuestionEdit(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);

    }

    @Override
    public void saveQuestion(String id, String title, String text, Date date, boolean closed,
                             RealmList<Image> images, RealmList<Answer> answers, User user) {

    }
}
