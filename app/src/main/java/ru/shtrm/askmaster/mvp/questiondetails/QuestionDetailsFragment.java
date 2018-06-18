package ru.shtrm.askmaster.mvp.questiondetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.source.QuestionsRepository;
import ru.shtrm.askmaster.util.MainUtil;

public class QuestionDetailsFragment extends Fragment
        implements QuestionDetailsContract.View {
    private Activity mainActivityConnector = null;

    private RecyclerView recyclerView;
    private AppCompatTextView userName;
    private AppCompatTextView userStatus;
    private AppCompatTextView userStats;
    private ImageView imageView;

    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;

    private QuestionDetailsAdapter adapter;

    private QuestionDetailsContract.Presenter presenter;

    public QuestionDetailsFragment() {}

    public static QuestionDetailsFragment newInstance() {
        return new QuestionDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_details, container, false);

        initViews(view);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTextDialog();
            }
        });

/*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshQuestion();
            }
        });
*/

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
    public void onDestroy() {
        super.onDestroy();
        QuestionsRepository.destroyInstance();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            exit();

        } else if (id == R.id.action_delete) {
            showDeleteAlertDialog();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {
        QuestionDetailsActivity activity = (QuestionDetailsActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);
/*
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(mainActivityConnector, R.color.colorPrimary));
*/

        RelativeLayout user_info = view.findViewById(R.id.user_detail);
        userName = user_info.findViewById(R.id.profile_name);
        userStatus = user_info.findViewById(R.id.profile_status);
        userStats = user_info.findViewById(R.id.profile_stats);
        imageView = user_info.findViewById(R.id.profile_image);
    }

    /**
     * Bind the presenter to view.
     * @param presenter The presenter. See at {@link QuestionDetailsPresenter}
     */
    @Override
    public void setPresenter(@NonNull QuestionDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Set the refresh layout as an indicator. Change the indicator's loading status.
     * @param loading Whether is loading.
     */
    @Override
    public void setLoadingIndicator(final boolean loading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(loading);
            }
        });
    }

    /**
     * When the network is slow or is not connected, show this message.
     */
    @Override
    public void showNetworkError() {
        Snackbar.make(fab, R.string.network_error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    /**
     * Show the question details.
     * @param question The question. See at {@link Question}
     */
    @Override
    public void showQuestionDetails(@NonNull Question question) {
        if (adapter == null) {
            adapter = new QuestionDetailsAdapter(mainActivityConnector, question);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(question.getAnswers());
        }
        if (question.getUser()!=null) {
            userStatus.setText(R.string.user_master);
            String stats = " [Q: ".
                        concat(Integer.toString(question.getUser().getQuestions().size())).
                        concat(" A: ").
                        concat(Integer.toString(question.getUser().getAnswers().size())).
                        concat("]");
            userStats.setText(stats);
            userName.setText(question.getUser().getName());

            String path = MainUtil.getPicturesDirectory(mainActivityConnector.getApplicationContext());
            if (path != null) {
                String avatar = question.getUser().getAvatar();
                if (avatar != null)
                    imageView.setImageBitmap(MainUtil.getBitmapByPath(path, avatar));
            }
        }
    }

    /**
     * Finish the activity.
     */
    @Override
    public void exit() {
        mainActivityConnector.onBackPressed();
    }

    /**
     * Show a dialog when user select the DELETE option menu item.
     */
    private void showDeleteAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector).create();
        dialog.setTitle(R.string.delete);
        dialog.setMessage(getString(R.string.delete_question_message));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.deleteQuestion();
            }
        });
        dialog.show();
    }

    /**
     * Show the dialog which contains an EditText.
     */
    private void showEditTextDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(getString(R.string.edit_name));

        View view = mainActivityConnector.getLayoutInflater().
                inflate(R.layout.dialog_edit_question_title, null);
        final AppCompatEditText editText = view.findViewById(R.id.editTextName);
        editText.setText(presenter.getQuestionTitle());
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    showInputIsEmpty();
                } else {
                    presenter.updateQuestionTitle(input);
                }
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Show the message that the user's input is empty.
     */
    private void showInputIsEmpty() {
        Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
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
