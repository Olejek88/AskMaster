package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.Answer;

public interface AnswersDataSource {

    Observable<List<Answer>> getAnswers(String questionId);

    Observable<Answer> getAnswer(@NonNull final String id);

    Answer getAnswerById(@NonNull final String id);

    void saveAnswer(@NonNull Answer Answer);

    void deleteAnswer(@NonNull String id);

    Observable<List<Answer>> refreshAnswers(String questionId);

    void updateAnswerTitle(@NonNull String id, @NonNull String title);

    void updateAnswerText(@NonNull String id, @NonNull String text);

    Observable<List<Answer>> searchAnswers(@NonNull String keyWords);

}
