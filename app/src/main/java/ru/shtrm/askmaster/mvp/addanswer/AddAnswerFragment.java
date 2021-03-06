package ru.shtrm.askmaster.mvp.addanswer;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.data.User;
import ru.shtrm.askmaster.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.askmaster.data.source.local.UsersLocalDataSource;
import ru.shtrm.askmaster.mvp.images.ImageGridAdapter;
import ru.shtrm.askmaster.mvp.questionedit.QuestionEditActivity;
import ru.shtrm.askmaster.util.MainUtil;

import static android.app.Activity.RESULT_OK;

public class AddAnswerFragment extends Fragment
        implements AddAnswerContract.View {
    private Activity mainActivityConnector = null;
    private ArrayList<Image> images = new ArrayList<>();
    public final static int ACTIVITY_PHOTO = 1;
    public final static int REQUEST_CAMERA_PERMISSION_CODE = 0;

    public static final String ACTION_CODE = "ru.shtrm.askmaster.mvp.addpackage.ImagesActivity";

    // View references.
    private TextInputEditText editText;
    private FloatingActionButton fab;
    private ImageView imageView;
    private GridView gridView;
    private Question currentQuestion;

    private AddAnswerContract.Presenter presenter;

    public AddAnswerFragment() {}

    public static AddAnswerFragment newInstance() {
        return new AddAnswerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_answer, container, false);
        Bundle b = getArguments();
        if (b!=null) {
            String questionId = b.getString(QuestionEditActivity.QUESTION_ID);
            if (questionId !=null)
                currentQuestion= QuestionsLocalDataSource.getInstance().getQuestionById(questionId);
        }

        initViews(view);

        //addLayoutListener(scrollView, editTextName);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImm();
/*
                String title = editTextName.getText().toString();
                // Check the length of the input number
                if (title.length() < 5) {
                    showTitleError();
                    return;
                }
                editTextName.setText(title);
*/
                final User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
                presenter.saveAnswer (mainActivityConnector, java.util.UUID.randomUUID().toString(),
                        "", editText.getText().toString(), user, images, currentQuestion);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });

        String action = mainActivityConnector.getIntent().getAction();
        if (action != null && action.equals(ACTION_CODE)) {
            checkPermissionCamera();
        }

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
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {

        AddAnswerActivity activity = (AddAnswerActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = view.findViewById(R.id.answer_add_image);
        //editTextName = view.findViewById(R.id.editTextTitle);
        editText = view.findViewById(R.id.editDescription);
        fab = view.findViewById(R.id.fab);
        gridView = view.findViewById(R.id.gridview);
    }

    /**
     * Bind presenter to fragment(view).
     * @param presenter The presenter. See at {@link AddAnswerPresenter}.
     */
    @Override
    public void setPresenter(@NonNull AddAnswerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Handle the scanning result.
     * @param requestCode The request code. See at {@link AddAnswerFragment}.
     * @param resultCode The result code.
     * @param data The result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_PHOTO:
                if (resultCode == RESULT_OK) {
                    // TODO сохраняем в базу
                    // отображаем в гридвью
                    Image image = new Image();
                    String uuid = java.util.UUID.randomUUID().toString();
                    image.setId(uuid);
                    image.setImageName(uuid.concat(".jpg"));
                    image.setDate(new Date());
                    image.setTitle(getResources().getString(R.string.other));
                    images.add(image);
                    gridView.setAdapter(new ImageGridAdapter(mainActivityConnector,images));
                    gridView.invalidateViews();
                    if (data!=null && data.getData()!=null) {
                        InputStream inputStream;
                        try {
                            inputStream = mainActivityConnector.getApplicationContext()
                                    .getContentResolver().openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            if (bitmap!=null) {
                                MainUtil.storeNewImage(bitmap, getContext(),
                                        800, image.getImageName());
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * To handle the permission grant result.
     * If the user denied the permission, show a dialog to explain
     * the reason why the app need such permission and lead he/her
     * to the system settings to grant permission.
     * @param requestCode The request code. See at {@link AddAnswerFragment#REQUEST_CAMERA_PERMISSION_CODE}
     * @param permissions The wanted permissions.
     * @param grantResults The results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPhotoActivity();
                } else {
                    hideImm();
                    AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector)
                            .setTitle(R.string.require_permission)
                            .setMessage(R.string.require_permission)
                            .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Go to the detail settings of our application
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            mainActivityConnector.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                break;
            default:

        }
    }

    /**
     * Check whether the camera permission has been granted.
     */
    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(mainActivityConnector, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Notice: Do not use the below code.
            // ActivityCompat.requestPermissions(getActivity(),
            // new String[] {Manifest.permission.CAMERA}, 1);
            // Such code may still active the request permission dialog
            // but even the user has granted the permission,
            // app will response nothing.
            // The below code works perfect.
            requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            startPhotoActivity();
        }
    }

    /**
     * Launch the camera
     */
    private void startPhotoActivity() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, ACTIVITY_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showTitleError() {
        Snackbar.make(fab, R.string.wrong_title, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Finish current activity.
     */
    @Override
    public void showQuestion() {
        mainActivityConnector.setResult(Activity.RESULT_OK);
        mainActivityConnector.finish();
    }

    /**
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager)
                mainActivityConnector.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null && imm.isActive()) {
            imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
        }
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
