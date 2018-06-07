package ru.shtrm.askmaster.mvp;

import android.view.View;

public interface BaseView<T> {

    void initViews(View view);

    void setPresenter(T presenter);

}
