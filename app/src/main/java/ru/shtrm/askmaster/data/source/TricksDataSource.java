package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.askmaster.data.Trick;

public interface TricksDataSource {

    List<Trick> getTricks();

    Trick getTrick(@NonNull final String id);

    void saveTrick(@NonNull Trick trick);

    void deleteTrick(@NonNull String id);

}