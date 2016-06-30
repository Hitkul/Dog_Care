package com.upstairs.dogcare;

import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nikoyuwono.toolbarpanel.ToolbarPanelLayout;
import com.nikoyuwono.toolbarpanel.ToolbarPanelListener;
import com.nirhart.parallaxscroll.views.ParallaxListView;

import java.util.ArrayList;
import java.util.List;

public class Breeds_list extends AppCompatActivity {


    public boolean isPanelOpen = false;
    public ToolbarPanelLayout toolbarPanelLayout;
    public boolean menu_button_state = true; //true: menu    false: arrow
    public cat_list_view_adapter cat_adapter;

   private List<String> breeds = new ArrayList<String >();
    ParallaxListView breeds_list;
    private breed_list_view_adapter breeds_adapter;
    public String Category_name;

    public TextView cat_tag;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();


        setContentView(com.upstairs.dogcare.R.layout.activity_breeds_list);



        toolbarPanelLayout = (ToolbarPanelLayout) findViewById(com.upstairs.dogcare.R.id.sliding_down_toolbar_layout);

        final ImageButton menu_button = (ImageButton) findViewById(com.upstairs.dogcare.R.id.imageButton);
        final ImageButton home_button = (ImageButton) findViewById(com.upstairs.dogcare.R.id.homeButton);
        ImageButton searchButton = (ImageButton) findViewById(com.upstairs.dogcare.R.id.searchButton);
        final Toolbar toolbarView = (Toolbar) findViewById(com.upstairs.dogcare.R.id.toolbar);
        findViewById(com.upstairs.dogcare.R.id.panel).setY((float) getResources().getDimensionPixelSize(com.upstairs.dogcare.R.dimen.abc_action_bar_default_height_material));






        Category_name = getIntent().getExtras().getString("Category");
        final List<String> categories = getIntent().getStringArrayListExtra("Category_list");
        ListView Cat_list = (ListView) findViewById(com.upstairs.dogcare.R.id.cat_list);

        set_category_list(categories, Cat_list);

        Typeface font = Typeface.createFromAsset(getAssets(), "raleway.ttf");
        cat_tag = (TextView) findViewById(com.upstairs.dogcare.R.id.cat_tag);
        cat_tag.setText(Category_name);
        cat_tag.setTypeface(font);


        breeds_list = (ParallaxListView) findViewById(com.upstairs.dogcare.R.id.breed_list);
        breeds = getBreedsFromDb(Category_name);
        breeds_adapter = new breed_list_view_adapter(this);
        breeds_adapter.addAll(breeds);
        breeds_list.setAdapter(breeds_adapter);


        assert searchButton!=null;
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSearch = new Intent(Breeds_list.this, search.class);
                startActivity(toSearch);
                overridePendingTransition(com.upstairs.dogcare.R.anim.pull_in_right, com.upstairs.dogcare.R.anim.push_out_left);
            }
        });





        breeds_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Breeds_list.this, CardsActivity.class);
                intent.putExtra("Category",Category_name);
                intent.putExtra("Breed",breeds_adapter.getItem(position));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        Breeds_list.this,


                        new Pair<View, String>(view.findViewById(com.upstairs.dogcare.R.id.img),
                                getString(com.upstairs.dogcare.R.string.transition_name_image))

                );
                ActivityCompat.startActivity(Breeds_list.this, intent, options.toBundle());



            }
        });


        assert home_button != null;
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Breeds_list.this ,Home_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        assert Cat_list != null;
        Cat_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(cat_adapter.getItem(position).equals("Diet")){
                    Intent i = new Intent(Breeds_list.this, CardsActivity.class);
                    i.putExtra("Category", cat_adapter.getItem(position));
                    i.putExtra("Breed", cat_adapter.getItem(position));
                    startActivity(i);
                }else{
                    breeds.clear();
                    breeds_adapter.clear();
                    breeds = getBreedsFromDb(cat_adapter.getItem(position));
                    breeds_adapter.addAll(breeds);

                    Category_name = cat_adapter.getItem(position);
                    breeds_list.setAdapter(breeds_adapter);

                    toolbarPanelLayout.closePanel();
                }



            }
        });


        assert toolbarView != null;
        toolbarView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isPanelOpen)
                            toolbarPanelLayout.closePanel();
                        if (!isPanelOpen)
                            toolbarPanelLayout.openPanel();
                    }
                }
        );


        assert menu_button!=null;
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPanelOpen)
                    toolbarPanelLayout.closePanel();
                if (!isPanelOpen)
                    toolbarPanelLayout.openPanel();

            }
        });



        assert toolbarPanelLayout != null;
        toolbarPanelLayout.setToolbarPanelListener(new ToolbarPanelListener() {
            @Override
            public void onPanelOpened(Toolbar toolbar, View panelView) {

                cat_tag.setText("Categories");
                isPanelOpen = true;

            }

            @Override
            public void onPanelSlide(Toolbar toolbar, View panelView, float slideOffset) {


                toolbar.setY((float) 0.0);

                findViewById(com.upstairs.dogcare.R.id.cat_list).setAlpha(slideOffset);


                if(slideOffset<0.5){
                    cat_tag.setText(Category_name);
                    cat_tag.setAlpha(1-slideOffset);}



                if(slideOffset>=0.5){
                    cat_tag.setText("Categories");
                    cat_tag.setAlpha(slideOffset);}

                if(slideOffset>0.3 && !isPanelOpen){
                    menu_button_state = false;
                    menu_button.setBackgroundResource(com.upstairs.dogcare.R.drawable.menu_to_arrow);
                    ((AnimationDrawable) menu_button.getBackground()).start();

                }

                if(slideOffset<0.7 && isPanelOpen){
                    menu_button_state = true;
                    menu_button.setBackgroundResource(com.upstairs.dogcare.R.drawable.arrow_to_menu);
                    ((AnimationDrawable) menu_button.getBackground()).start();

                }

                if (slideOffset == 0.0)
                    onPanelClosed(toolbar, panelView);

                if (slideOffset == 1.0)
                    onPanelOpened(toolbar, panelView);


            }

            @Override
            public void onPanelClosed(Toolbar toolbar, View panelView) {

                if(!menu_button_state){
                    menu_button.setBackgroundResource(com.upstairs.dogcare.R.drawable.arrow_to_menu);
                    ((AnimationDrawable) menu_button.getBackground()).start();
                    menu_button_state = true;
                }

                panelView.setY((float) -(panelView.getHeight()-toolbarView.getHeight()));
                cat_tag.setText(Category_name);
                cat_tag.setAlpha((float) 1.0);
                isPanelOpen = false;

            }
        });



    }



    private List<String> getBreedsFromDb(String foo){
        List<String> Breeds = new ArrayList<String>();
        //Check Flag Again to ensure that the database is ready
            CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
            SQLiteDatabase db = cdbhelper.getReadableDatabase();
            String[] projection={
                    CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION
            };
            String selection = CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION+"=?";
            String[] selectionArgs = {foo};
            String sortOrder = CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION + " DESC";
            Cursor c = db.query(true, CardDBContract.CardTable.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder,null);

            if(c!=null){

                for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                    String temp = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION));
                    Breeds.add(temp);
                }
            db.close();
        }
        return Breeds;
    }


    private void set_category_list(List<String> categories, ListView listView){

        cat_adapter = new cat_list_view_adapter(Breeds_list.this);

        for (int i=0; i< categories.size();i++){
            if(!categories.get(i).equalsIgnoreCase("famous dogs"))
            cat_adapter.add(categories.get(i));
        }

        listView.setAdapter(cat_adapter);

    }


    @   Override
    public void onBackPressed(){
        if (isPanelOpen) {

            toolbarPanelLayout.closePanel();;
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }


}

