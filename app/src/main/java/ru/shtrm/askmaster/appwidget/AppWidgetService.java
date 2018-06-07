package ru.shtrm.askmaster.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class AppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListFactory(this.getApplicationContext());
    }

}
