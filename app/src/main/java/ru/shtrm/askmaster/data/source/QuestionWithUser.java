package ru.shtrm.askmaster.data.source;

import io.realm.RealmObject;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;

public class QuestionWithUser extends RealmObject {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    private Question question;

    public QuestionWithUser(Question question, User user) {
        this.user = user;
        this.question = question;
    }

}
