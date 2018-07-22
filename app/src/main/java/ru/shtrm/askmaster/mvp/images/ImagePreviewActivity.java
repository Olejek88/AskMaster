package ru.shtrm.askmaster.mvp.images;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.data.source.ImagesDataSource;
import ru.shtrm.askmaster.data.source.ImagesRepository;
import ru.shtrm.askmaster.data.source.local.ImagesLocalDataSource;
import ru.shtrm.askmaster.util.MainUtil;

import static ru.shtrm.askmaster.mvp.images.ImagesActivity.IMAGE_ID;

public class ImagePreviewActivity extends AppCompatActivity {

    private ImagesContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_image_full);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImagesDataSource imagesDataSource =
            ImagesRepository.getInstance(ImagesLocalDataSource.getInstance());
        Intent intent = getIntent();
        if (intent!=null && imagesDataSource!=null) {
            String imageId = intent.getStringExtra(IMAGE_ID);
             if (imageId !=null) {
                 Image image = imagesDataSource.getImage(imageId);
                 if (image !=null) {
                     ImageView imageView = findViewById(R.id.fullImage);
                     imageView.setImageBitmap(
                             MainUtil.getBitmapByPath(
                                     MainUtil.getPicturesDirectory
                                             (getApplicationContext()), image.getImageName()));
                 }
             }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
