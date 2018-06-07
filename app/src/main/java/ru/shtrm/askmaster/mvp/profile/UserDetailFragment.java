package ru.shtrm.askmaster.mvp.profile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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

public class UserDetailFragment extends Fragment
        implements UserDetailContract.View {

    // View references.
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewWebsite;
    private ImageView imageView;
    private Button submitButton;

    private UserDetailContract.Presenter presenter;

    private String address;
    private String website;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        initViews(view);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveUser (java.util.UUID.randomUUID().toString(), title,
                        editText.getText().toString(), user, images);
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
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        UserDetailActivity activity = (UserDetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewName = view.findViewById(R.id.profile_add_name);
        textViewAddress = view.findViewById(R.id.profile_add_address);
        textViewWebsite = view.findViewById(R.id.profile_add_website);
        imageView = view.findViewById(R.id.profile_add_image);
        submitButton = view.findViewById(R.id.profile_button_submit);
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
}
