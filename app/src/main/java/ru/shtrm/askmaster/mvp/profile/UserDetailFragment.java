package ru.shtrm.askmaster.mvp.profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.customtabs.CustomTabsHelper;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.util.MainUtil;

public class UserDetailFragment extends Fragment
        implements UserDetailContract.View {
    private Activity mainActivityConnector = null;

    // View references.
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewWebsite;
    private TextView textViewPhone;
    private ImageView imageView;
    private Button submitButton;

    private UserDetailContract.Presenter presenter;

    private String address;
    private String website;
    private String userUuid;
    private User user;
    private boolean owner = true;

    public UserDetailFragment() {}

    public static UserDetailFragment newInstance() {
        return new UserDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        Bundle b = getArguments();
        assert b != null;
        userUuid = b.getString("id");
        user = UsersLocalDataSource.getInstance().getAuthorisedUser();
        if (user!=null && !user.getId().equals(userUuid)) {
            user = UsersLocalDataSource.getInstance().getUser()
        }

        initViews(view);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveUser (java.util.UUID.randomUUID().toString(),
                        textViewName.getText().toString(),
                        textViewAddress.getText().toString(),
                        textViewWebsite.getText().toString(),
                        textViewPhone.getText().toString(),
                        imageView.getDrawingCache(), user);
            }
        });

        textViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (website != null) {
                    CustomTabsHelper.openUrl(getContext(), website);
                }
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        UserDetailActivity activity = (UserDetailActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewName = view.findViewById(R.id.profile_add_name);
        textViewAddress = view.findViewById(R.id.profile_add_address);
        textViewWebsite = view.findViewById(R.id.profile_add_website);
        textViewPhone = view.findViewById(R.id.profile_add_phone);
        imageView = view.findViewById(R.id.profile_add_image);
        submitButton = view.findViewById(R.id.profile_button_submit);

        if (user!=null) {
            textViewName.setText(user.getName());
            textViewAddress.setText(user.getAddress());
            textViewWebsite.setText(user.getWebsite());
            textViewPhone.setText(user.getPhone());
            imageView.setImageBitmap(MainUtil.getBitmapByPath(MainUtil.getPicturesDirectory(),user.getAvatar()));
        }
    }

    @Override
    public void setPresenter(@NonNull UserDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUserName(String name) {
        textViewName.setText(name);
    }

    @Override
    public void setUserAddress(String address) {
        this.address = address;
        textViewAddress.setText(address);
    }

    @Override
    public void setUserWebsite(String website) {
        this.website = website;
        Spannable spannable = new SpannableStringBuilder(website);
        spannable.setSpan(new URLSpan(website), 0, website.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWebsite.setText(spannable);
    }

    @Override
    public void showErrorMsg() {
        Snackbar.make(submitButton, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }
}
