package ru.shtrm.askmaster.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.askmaster.data.Question;

public interface RetrofitService {

    //@GET(Api.USER_QUERY)
    //Observable<UserRecognition> query(@Query("text") String number);

    @GET(Api.QUESTION_STATE)
    Observable<Question> getQuestionClosed(@Query("type") String type, @Query("id") String id);

    @GET(Api.QUESTION_ID)
    Observable<Question> getQuestion(@Query("id") String id);
}
