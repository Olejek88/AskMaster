package ru.shtrm.askmaster.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Image;
import ru.shtrm.askmaster.util.MainUtil;

public final class PhotoGridAdapter extends BaseAdapter {
    @NonNull
    private final Context context;

    private List<Image> mItems;
    private final LayoutInflater mInflater;

    public PhotoGridAdapter(@NonNull Context context, List<Image> images) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mItems = images;
    }

    @Override
    public int getCount() {
        if (mItems!=null)
            return mItems.size();
        else
            return 0;
    }

    @Override
    public Image getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;

        if (v == null) {
            v = mInflater.inflate(R.layout.item_image, viewGroup, false);
            v.setTag(R.id.gridViewImage, v.findViewById(R.id.gridViewImage));
        }
        picture = (ImageView) v.getTag(R.id.gridViewImage);
        Image item = getItem(i);
        String path = MainUtil.getPicturesDirectory(context).concat(item.getImageName());
        Bitmap bitmap = MainUtil.getBitmapByPath(
                MainUtil.getPicturesDirectory(context),item.getImageName());
        if (bitmap!=null)
            picture.setImageBitmap(bitmap);
        return v;
    }
}