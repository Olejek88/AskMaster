package ru.shtrm.askmaster.mvp.addquestion;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;
import ru.shtrm.askmaster.data.source.UsersDataSource;

public class AddQuestionPresenter implements AddQuestionContract.Presenter{

    @NonNull
    private final AddQuestionContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public AddQuestionPresenter(@NonNull QuestionsDataSource dataSource,
                                @NonNull UsersDataSource userDataSource,
                                @NonNull AddQuestionContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
        this.usersDataSource = userDataSource;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
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
