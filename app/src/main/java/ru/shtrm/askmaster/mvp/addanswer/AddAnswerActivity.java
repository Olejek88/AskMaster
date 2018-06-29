package ru.shtrm.askmaster.mvp.addanswer;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.source.AnswersRepository;
import ru.shtrm.askmaster.data.source.QuestionsRepository;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.data.source.local.AnswersLocalDataSource;
import ru.shtrm.askmaster.data.source.local.ImagesLocalDataSource;
import ru.shtrm.askmaster.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.data.source.remote.AnswersRemoteDataSource;
import ru.shtrm.askmaster.data.source.remote.QuestionsRemoteDataSource;

public class AddAnswerActivity extends AppCompatActivity {

    private AddAnswerFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        if (savedInstanceState != null) {
            fragment = (AddAnswerFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AddAnswerFragment");
        } else {
            fragment = AddAnswerFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, fragment, "AddAnswerFragment")
                    .commit();
        }

        // Create the presenter.
        new AddAnswerPresenter(QuestionsRepository.getInstance(
                QuestionsRemoteDataSource.getInstance(),
                QuestionsLocalDataSource.getInstance()),
                AnswersRepository.getInstance(AnswersRemoteDataSource.getInstance(),
                        AnswersLocalDataSource.getInstance()),
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                ImagesLocalDataSource.getInstance(),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddAnswerFragment", fragment);
    }

}
