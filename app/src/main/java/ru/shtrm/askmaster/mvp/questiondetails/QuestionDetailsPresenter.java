package ru.shtrm.askmaster.mvp.questiondetails;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.askmaster.data.source.QuestionsRepository;

public class QuestionDetailsPresenter implements QuestionDetailsContract.Presenter {

    @NonNull
    private QuestionDetailsContract.View view;

    @NonNull
    private QuestionsRepository questionsRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    private String questionTitle;

    private String questionText;

    private ArrayList<Answer> answers;
    private ArrayList<Image> images;

    @NonNull
    private String questionId;

    public QuestionDetailsPresenter(@NonNull String id,
                                    @NonNull QuestionsRepository questionsRepository,
                                    @NonNull QuestionDetailsContract.View questionDetailView) {
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
                        if (value.getAnswers().size()>0)
                            answers = new ArrayList<>(value.getAnswers());
                        if (value.getImages().size()>0)
                            images = new ArrayList<>(value.getImages());

                        view.showQuestionDetails(value);
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

    /**
     * Refresh the question by access the network.
     * No matter the refresh is successful or failed,
     * Do not forget to stop the indicator.
     */
    @Override
    public void refreshQuestion() {
        Disposable disposable = questionsRepository
                .refreshQuestion(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Question>() {
                    @Override
                    public void onNext(Question value) {
                        view.showQuestionDetails(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //view.setLoadingIndicator(false);
                        view.showNetworkError();
                    }

                    @Override
                    public void onComplete() {

                        //view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);

    }

    /**
     * Delete the question from repository(both in cache and database).
     */
    @Override
    public void deleteQuestion() {
        questionsRepository.deleteQuestion(questionId);
        view.exit();
    }

    /**
     * Get the current question title.
     * @return Question title. See at {@link Question#title}
     */
    @Override
    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * Set the current question a new title(both in cache and database).
     * @param newTitle The new title of .
     *                See at {@link Question#title}
     */
    @Override
    public void updateQuestionTitle(String newTitle) {
        questionsRepository.updateQuestionTitle(questionId, newTitle);
        openDetail();
    }

}
