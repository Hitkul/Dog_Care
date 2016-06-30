package com.upstairs.dogcare;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hitkul on 26/6/16.
 */
public class HomeScreenListAdapter extends ArrayAdapter<Card> {

    private Activity context;
    private List<String> list;
    private LayoutInflater mLayoutInflater = null;

    public HomeScreenListAdapter(Activity context) {

        super(context, com.upstairs.dogcare.R.layout.card_layout_4);
        this.context = context;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {


            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(com.upstairs.dogcare.R.layout.card_layout_4, null, true);

        TextView mTVItem = (TextView) view.findViewById(com.upstairs.dogcare.R.id.txt);
        CircularImageView mIMGItem = (CircularImageView) view.findViewById(com.upstairs.dogcare.R.id.img);


        if(getItem(position).getClassification().equalsIgnoreCase("Famous Dogs")){

                Typeface font_text = Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
                TextView mTVItem_1 = (TextView) view.findViewById(com.upstairs.dogcare.R.id.txt_1);
                mTVItem_1.setVisibility(View.GONE);
                if (getItem(position).getText() != null) {
                    mTVItem.setText(getItem(position).getText());
                    mTVItem.setTypeface(font_text);
                }
                if (getItem(position).getImage() != null) {
                    Resources res = context.getResources();
                    String mDrawableName = getItem(position).getImage().replaceAll(" ", "_").toLowerCase();
                    int resID = res.getIdentifier(mDrawableName, "drawable", context.getPackageName());
                    Picasso.with(context).load(resID).into(mIMGItem);
                }
        }else{
            Typeface font_text = Typeface.createFromAsset(context.getAssets(), "raleway.ttf");
            Typeface font = Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
            TextView mTVItem_1 = (TextView) view.findViewById(com.upstairs.dogcare.R.id.txt_1);
            if (getItem(position).getSubclassification() != null) {
                mTVItem.setText(getItem(position).getSubclassification());
                mTVItem.setTypeface(font_text);
                mTVItem.setGravity(Gravity.CENTER);
                mTVItem.setTextSize(30);

            }

            if (getItem(position).getText() != null) {

                mTVItem_1.setText(getItem(position).getText());
                mTVItem_1.setTypeface(font);
                mTVItem_1.setTextSize(20);
            }
            if (getItem(position).getImage() != null) {
                Resources res = context.getResources();
                String mDrawableName = getItem(position).getImage().replaceAll(" ", "_").toLowerCase();
                int resID = res.getIdentifier(mDrawableName, "drawable", context.getPackageName());
                Picasso.with(context).load(resID).resize(100, 100).centerCrop().into(mIMGItem);
            }
        }


            return view;

    }

}


