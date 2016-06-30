package com.upstairs.dogcare;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by hitkul on 20/6/16.
 */
public class cat_list_view_adapter extends ArrayAdapter<String>{


    private final Activity context;


    public cat_list_view_adapter(Activity context) {
        super(context, com.upstairs.dogcare.R.layout.cat_list_item_layout);
        this.context = context;

    }
    @Override
    public View getView(int position, View rowview, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowview= inflater.inflate(com.upstairs.dogcare.R.layout.cat_list_item_layout, null, true);

        TextView list_item = (TextView) rowview.findViewById(com.upstairs.dogcare.R.id.cat_list_item);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "raleway.ttf");
        list_item.setText(getItem(position));
        list_item.setTypeface(font);

        return rowview;
    }

}
