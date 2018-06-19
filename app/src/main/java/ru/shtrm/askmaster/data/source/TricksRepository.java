package ru.shtrm.askmaster.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ru.shtrm.askmaster.data.Trick;

public class TricksRepository implements TricksDataSource {

    @Nullable
    private static TricksRepository INSTANCE = null;

    @NonNull
    private final TricksDataSource TricksRemoteDataSource;

    @NonNull
    private final TricksDataSource TricksLocalDataSource;

    private Map<String, Trick> cachedTricks;

    // Prevent direct instantiation
    private TricksRepository(@NonNull TricksDataSource TricksRemoteDataSource,
                             @NonNull TricksDataSource TricksLocalDataSource) {
        this.TricksRemoteDataSource = TricksRemoteDataSource;
        this.TricksLocalDataSource = TricksLocalDataSource;
    }

    // The access for other classes.
    public static TricksRepository getInstance(@NonNull TricksDataSource TricksRemoteDataSource,
                                               @NonNull TricksDataSource TricksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TricksRepository(TricksRemoteDataSource, TricksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * It is designed to gotten the Tricks from both database
     * and network. Which are faster then return them.
     * @return Tricks from {@link ru.shtrm.askmaster.data.source.local.TricksLocalDataSource}.
     */
    @Override
    public List<Trick> getTricks() {
        return TricksLocalDataSource.getTricks();
    }

    /**
     * Get a Trick of specific number from data source.
     * @param id The primary key or the Trick id. See {@link Trick}.
     * @return The Trick.
     */
    @Override
    public Trick getTrick(@NonNull final String id) {
        return TricksLocalDataSource.getTrick(id);
    }

    /**
     * Save the Trick to data source and cache.
     * It is supposed to save it to database and network too.
     * But we have no cloud(The account system) yet.
     * It may change either.
     * @param Trick The Trick to save. See more @{@link Trick}.
     */
    @Override
    public void saveTrick(@NonNull Trick Trick) {
        TricksLocalDataSource.saveTrick(Trick);
    }

    /**
     * Delete a Trick from data source and cache.
     * @param TrickId The primary id or in another words, the Trick number.
     *                  See more @{@link Trick#id}.
     */
    @Override
    public void deleteTrick(@NonNull String TrickId) {
        TricksLocalDataSource.deleteTrick(TrickId);
    }
}
