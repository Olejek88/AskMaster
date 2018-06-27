package ru.shtrm.askmaster.mvp.questionedit;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.source.QuestionsRepository;
import ru.shtrm.askmaster.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.askmaster.data.source.remote.QuestionsRemoteDataSource;

public class QuestionEditActivity extends AppCompatActivity{

    private QuestionEditFragment fragment;

    public static final String QUESTION_ID = "QUESTION_ID";

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

        // Restore the status.
        if (savedInstanceState != null) {
            fragment = (QuestionEditFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "QuestionEditFragment");
        } else {
            fragment = QuestionEditFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        // Create the presenter.
        new QuestionEditPresenter(
                getIntent().getStringExtra(QUESTION_ID),
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "QuestionEditFragment", fragment);
    }

}
