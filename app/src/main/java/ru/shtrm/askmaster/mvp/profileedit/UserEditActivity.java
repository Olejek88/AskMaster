package ru.shtrm.askmaster.mvp.profileedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class UserEditActivity extends AppCompatActivity {

    private UserEditFragment fragment;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (UserEditFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserEditFragment");
        } else {
            fragment = UserEditFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            b.putString(USER_ID,getIntent().getStringExtra(USER_ID));
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "UserEditFragment")
                    .commit();
        }

        new UserEditPresenter(
                fragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                getIntent().getStringExtra(USER_ID));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UserEditFragment", fragment);
        }
    }
}
