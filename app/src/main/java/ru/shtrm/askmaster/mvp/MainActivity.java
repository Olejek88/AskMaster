package ru.shtrm.askmaster.mvp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.appwidget.AppWidgetProvider;
import ru.shtrm.askmaster.data.source.UsersRepository;
import ru.shtrm.askmaster.data.source.QuestionsRepository;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.askmaster.mvp.questions.QuestionsFragment;
import ru.shtrm.askmaster.mvp.users.UsersFragment;
import ru.shtrm.askmaster.mvp.questions.PackageFilterType;
import ru.shtrm.askmaster.mvp.questions.QuestionsPresenter;
import ru.shtrm.askmaster.mvp.users.UsersPresenter;
import ru.shtrm.askmaster.ui.PrefsActivity;
import ru.shtrm.askmaster.util.PushUtil;
import ru.shtrm.askmaster.util.SettingsUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private QuestionsFragment packagesFragment;

    private UsersFragment companiesFragment;
    private QuestionsPresenter packagesPresenter;

    private static final String KEY_NAV_ITEM = "CURRENT_NAV_ITEM";
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private int selectedNavItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        initViews();

        if (savedInstanceState != null) {
            packagesFragment = (QuestionsFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "QuestionsFragment");
            companiesFragment = (UsersFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UsersFragment");
            selectedNavItem = savedInstanceState.getInt(KEY_NAV_ITEM);
        } else {
            packagesFragment = (QuestionsFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (packagesFragment == null) {
                packagesFragment = QuestionsFragment.newInstance();
            }

            companiesFragment = (UsersFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (companiesFragment == null) {
                companiesFragment = UsersFragment.newInstance();
            }
        }


        // Add the fragments.
        if (!packagesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, packagesFragment, "QuestionsFragment")
                    .commit();
        }

        if (!companiesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, companiesFragment, "UsersFragment")
                    .commit();
        }

        // Make sure the data in repository is the latest.
        // Also to void the repo only contains a package
        // when user has already gone to detail page
        // by check a notification or widget.
        QuestionsRepository.destroyInstance();
        // Init the presenters.
        usersPresenter = new UsersPresenter(usersFragment,
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()));

        new UsersPresenter(companiesFragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

        // Get data from Bundle.
        if (savedInstanceState != null) {
            PackageFilterType currentFiltering = (PackageFilterType) savedInstanceState.
                    getSerializable(CURRENT_FILTERING_KEY);
            if (currentFiltering!=null)
                packagesPresenter.setFiltering(currentFiltering);
        }

        // Show the default fragment.
        if (selectedNavItem == 0) {
            showPackagesFragment();
        } else if (selectedNavItem == 1) {
            showCompaniesFragment();
        }

        PushUtil.startReminderService(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
    }

    /**
     * Close the drawer when a back click is called.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle different items of the navigation drawer
     * @param item The selected item.
     * @return Selected or not.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.nav_profile:
                showPackagesFragment();
                break;
            case R.id.nav_users:
                showCompaniesFragment();
                break;
            case R.id.nav_switch_theme:
                drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                                == Configuration.UI_MODE_NIGHT_YES) {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, false).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, true).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, PrefsActivity.class);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_SETTINGS);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent = new Intent(MainActivity.this, PrefsActivity.class);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_ABOUT);
                startActivity(intent);
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Store the state when the activity may be recycled.
     * @param outState The state data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, packagesPresenter.getFiltering());
        super.onSaveInstanceState(outState);
        Menu menu = navigationView.getMenu();
        if (menu.findItem(R.id.nav_profile).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 0);
        } else if (menu.findItem(R.id.nav_users).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 1);
        }
        // Store the fragments' states.
        if (packagesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "QuestionsFragment", packagesFragment);
        }
        if (companiesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UsersFragment", companiesFragment);
        }
    }

    /**
     * Init views by calling findViewById.
     */
    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * Show the packages list fragment.
     */
    private void showPackagesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(packagesFragment);
        fragmentTransaction.hide(companiesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.app_name));
        navigationView.setCheckedItem(R.id.nav_profile);

    }

    /**
     * Show the companies list fragment.
     */
    private void showCompaniesFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(companiesFragment);
        fragmentTransaction.hide(packagesFragment);
        fragmentTransaction.commit();

        toolbar.setTitle(getResources().getString(R.string.nav_users));
        navigationView.setCheckedItem(R.id.nav_users);

    }

    /**
     * Pass the selected package number to fragment.
     * @param id The selected package number.
     */
    public void setSelectedPackageId(@NonNull String id) {
        packagesFragment.setSelectedPackage(id);
    }

}
