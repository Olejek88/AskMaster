package ru.shtrm.askmaster.mvp.addanswer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.AnswersDataSource;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.data.source.QuestionsDataSource;
import ru.shtrm.askmaster.data.source.UsersDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class AddAnswerPresenter implements AddAnswerContract.Presenter{

    @NonNull
    private final AddAnswerContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final AnswersDataSource answersDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public AddAnswerPresenter(@NonNull QuestionsDataSource dataSource,
                              @NonNull AnswersDataSource answerDataSource,
                                @NonNull UsersDataSource userDataSource,
                                @NonNull ImagesDataSource imagesDataSource,
                                @NonNull AddAnswerContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
        this.answersDataSource = answerDataSource;
        this.imagesDataSource = imagesDataSource;
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
    public void saveAnswer(Context context, String id, String title, String text, User user,
                             ArrayList<Image> images, Question question) {
        compositeDisposable.clear();
        checkAnswer(context, id, title, text, user, images, question);
    }

    private void checkAnswer(Context context, final String id, final String title, final String text,
                             User user, ArrayList<Image> images, Question question) {
        // TODO id - это uuid так что не сработает
        if (questionsDataSource.isQuestionExist(id)) {
            return;
        }
        Answer answer = new Answer();
        answer.setId(java.util.UUID.randomUUID().toString());
        answer.setUser(user);
        answer.setTitle(title);
        answer.setText(text);
        answer.setDate(new Date());
        answer.setVoteDown(0);
        answer.setVoteUp(1);
        for (Image image : images) {
            saveImage(context, title, image.getImageName());
        }
        answer.setImages(imagesDataSource.saveImages(images));
        answersDataSource.saveAnswer(answer);
        user.getAnswers().add(answer);
        usersDataSource.saveUser(user);
        question.getAnswers().add(answer);
        questionsDataSource.saveQuestion(question);
        view.showQuestion();
    }

    private void saveImage(Context context, String title, String imageName) {
        Bitmap bitmap = MainUtil.getBitmapByPath(MainUtil.getPicturesDirectory(context),imageName);
        if (bitmap!=null) {
            Image image = new Image();
            image.setId(java.util.UUID.randomUUID().toString());
            image.setImageName(imageName);
            image.setDate(new Date());
            image.setTitle(title);
            //imagesDataSource.saveImage(image);
        }
    }

}
