package ru.shtrm.askmaster.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.source.AnswersDataSource;
import ru.shtrm.askmaster.realm.RealmHelper;

public class AnswersLocalDataSource implements AnswersDataSource {

    @Nullable
    private static AnswersLocalDataSource INSTANCE;

    // Prevent direct instantiation
    private AnswersLocalDataSource() {
    }

    // Access this instance for other classes.
    public static AnswersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswersLocalDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Get the Answers in database and sort them in timestamp descending.
     * @return The observable packages from database.
     */
    @Override
    public Observable<List<Answer>> getAnswers(String questionId) {
        Realm rlm = RealmHelper.newRealmInstance();

        return Observable.just(rlm.copyFromRealm(rlm.where(Answer.class)
                .equalTo("question.id", questionId)
                .findAllSorted("date", Sort.DESCENDING)));
    }

    /**
     * Get a Answer in database of specific id.
     * @param id The primary key
     *                   or in another words, the package id.
     *                   See {@link Answer#id}
     * @return The observable package from database.
     */
    @Override
    public Observable<Answer> getAnswer(@NonNull String id) {
        Realm rlm = RealmHelper.newRealmInstance();
        return Observable.just(rlm.copyFromRealm(rlm.where(Answer.class)
                .equalTo("id", id)
                .findFirst()));
    }

    /**
     * Save a Answer to database.
     * @param Answer The Answer to save. See {@link Answer}
     */
    @Override
    public void saveAnswer(@NonNull final Answer Answer) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(Answer);
            }
        });
        realm.close();
    }

    /**
     * Delete a Answer with specific id from database.
     * @param id The primary key of a package
     *                  See {@link Answer#id}
     */
    @Override
    public void deleteAnswer(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Answer Answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (Answer != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Answer.deleteFromRealm();
                }
            });
        }
        realm.close();
    }

    @Override
    public Observable<List<Answer>> refreshAnswers(String questionId) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    @Override
    public void updateAnswerTitle(@NonNull String id, @NonNull String title) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer Answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (Answer != null) {
            realm.beginTransaction();
            Answer.setTitle(title);
            realm.copyToRealmOrUpdate(Answer);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public void updateAnswerText(@NonNull String id, @NonNull String text) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer Answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (Answer != null) {
            realm.beginTransaction();
            realm.beginTransaction();
            Answer.setTitle(text);
            realm.copyToRealmOrUpdate(Answer);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public Answer getAnswerById(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer Answer = realm.where(Answer.class).equalTo("id", id).findFirst();
        if (Answer!=null)
            return realm.copyFromRealm(Answer);
        return null;
    }


    @Override
    public Observable<List<Answer>> searchAnswers(@NonNull String keyWords) {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable.fromIterable(realm.copyFromRealm(
                realm.where(Answer.class)
                        .like("title", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("text", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAll()))
                .toList()
                .toObservable();
    }
}