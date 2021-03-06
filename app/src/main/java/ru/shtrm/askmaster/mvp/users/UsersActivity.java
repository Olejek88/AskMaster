package ru.shtrm.askmaster.mvp.users;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class UsersActivity extends AppCompatActivity {

    private UsersFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (UsersFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UsersFragment");
        } else {
            fragment = UsersFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "UsersFragment")
                    .commit();
        }

        new UsersPresenter(fragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UsersFragment", fragment);
        }
    }
}
