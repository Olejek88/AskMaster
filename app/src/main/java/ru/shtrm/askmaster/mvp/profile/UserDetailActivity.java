package ru.shtrm.askmaster.mvp.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class UserDetailActivity extends AppCompatActivity {

    private UserDetailFragment fragment;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (UserDetailFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserDetailFragment");
        } else {
            fragment = UserDetailFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "UserDetailFragment")
                    .commit();
        }

        new UserDetailPresenter(
                fragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                getIntent().getStringExtra(USER_ID));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UserDetailFragment", fragment);
        }
    }
}
