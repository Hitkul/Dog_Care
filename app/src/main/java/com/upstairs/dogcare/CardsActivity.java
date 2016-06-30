package com.upstairs.dogcare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wenchao.cardstack.CardStack;

import java.util.ArrayList;
import java.util.List;

public class CardsActivity extends AppCompatActivity {
    private  String Category_name;
    private  String Breed_name;
    private List<Card> getCardsFromDB(){
        List<Card> Cards = new ArrayList<Card>();
        CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
        SQLiteDatabase db = cdbhelper.getReadableDatabase();
        String[] projection={
                CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION,
                CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION,
                CardDBContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION,
                CardDBContract.CardTable.COLUMN_NAME_CARD_IMAGE,
                CardDBContract.CardTable.COLUMN_NAME_CARD_TEXT,
                CardDBContract.CardTable.COLUMN_NAME_CARD_LIST,
                CardDBContract.CardTable.COLUMN_NAME_CARD_TITLE,
                CardDBContract.CardTable.COLUMN_NAME_CARD_POSITION
        };
        String selection= CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION+" =? AND "+ CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION+" =?";
        String[] selectionargs = {
                Breed_name,
                Category_name
        };
        String sortOrder = CardDBContract.CardTable.COLUMN_NAME_CARD_POSITION + " ASC";
        Cursor c = db.query(CardDBContract.CardTable.TABLE_NAME,projection,selection,selectionargs,null,null,sortOrder);
        if(c!=null){
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                Card Temp = new Card(c);
                Cards.add(Temp);

            }
        }
        db.close();
        return Cards;
    }


    private CardStack mCardStack;
    private CardDataAdapter mCardAdapter;
    public List<Card> cards = null;
    public int last_card_swiped = 0;
    public TextView cardCount;
    ImageButton backButton;
    ImageButton homeButton ;
    TextView breedName;
    ImageView check;
    LinearLayout relodeButtonlast;
    LinearLayout backButtonlast;
    LinearLayout homeButtonlast;
    LinearLayout shareButtonlast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(com.upstairs.dogcare.R.layout.activity_cards);
        Intent foo = getIntent();
        Category_name = foo.getStringExtra("Category");
        Breed_name = foo.getStringExtra("Breed");
        SharedPrefHelper.storeLastSwiped(getApplicationContext(),Category_name,Breed_name);


        backButton = (ImageButton) findViewById(com.upstairs.dogcare.R.id.backButton);
        homeButton = (ImageButton) findViewById(com.upstairs.dogcare.R.id.homeButton);
        breedName =(TextView) findViewById(com.upstairs.dogcare.R.id.breedName);
        cardCount =(TextView) findViewById(com.upstairs.dogcare.R.id.cardCount);
        check = (ImageView) findViewById(com.upstairs.dogcare.R.id.check);
        relodeButtonlast = (LinearLayout)findViewById(com.upstairs.dogcare.R.id.relodeButtonlast);
        backButtonlast = (LinearLayout)findViewById(com.upstairs.dogcare.R.id.backButtonlast);
        homeButtonlast = (LinearLayout)findViewById(com.upstairs.dogcare.R.id.homeButtonlast);
        shareButtonlast = (LinearLayout)findViewById(com.upstairs.dogcare.R.id.shareButtonlast);
        final List<String> categories = getCategoriesFromDb();//Categories from DB

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            relodeButtonlast.setBackgroundResource(com.upstairs.dogcare.R.drawable.ripple_button);
            backButtonlast.setBackgroundResource(com.upstairs.dogcare.R.drawable.ripple_button);
            homeButtonlast.setBackgroundResource(com.upstairs.dogcare.R.drawable.ripple_button);
            shareButtonlast.setBackgroundResource(com.upstairs.dogcare.R.drawable.ripple_button);
        }

        cards = getCardsFromDB();

        Typeface font = Typeface.createFromAsset(getAssets(), "raleway.ttf");
        breedName.setText(Breed_name);
        breedName.setTypeface(font);

        cardCount.setTypeface(font);


        mCardStack = (CardStack)findViewById(com.upstairs.dogcare.R.id.container);
        mCardStack.setContentResource(com.upstairs.dogcare.R.layout.card_layout);
        mCardStack.setStackMargin(10);
        mCardAdapter = new CardDataAdapter(CardsActivity.this);
        setCards();


        assert  backButton !=null;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        assert homeButton != null;
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardsActivity.this ,Home_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



        mCardStack.setListener(new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int section, float distance) {


                if(distance>100.0)
                    return true;

                return false;
            }

            @Override
            public boolean swipeStart(int section, float distance) {
                return false;
            }

            @Override
            public boolean swipeContinue(int section, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void discarded(int mIndex, int direction) {
                last_card_swiped++;
                if(last_card_swiped == cards.size()) {
                    onLastCardSwiped();
                }else{
                    cardCount.setText(last_card_swiped+1 +"/"+cards.size());
                }
                int num_used=SharedPrefHelper.getNumUsed(getApplicationContext());

                if((last_card_swiped == 1)&&(num_used<5)){
                    Toast.makeText(getApplicationContext(), "Tap to get previous card", Toast.LENGTH_LONG).show();
                    SharedPrefHelper.incrementNumUsed(getApplicationContext());
                }

            }

            @Override
            public void topCardTapped() {

                if(last_card_swiped>0){

                    mCardAdapter.insert(cards.get(last_card_swiped-1),0);
                    mCardStack.setAdapter(mCardAdapter);
                    last_card_swiped--;
                    cardCount.setText(last_card_swiped+1 +"/"+cards.size());

                }
                mCardStack.setStackMargin(10);
            }
        });

        relodeButtonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRelode();
            }
        });

        backButtonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Category_name.equalsIgnoreCase("Diet")) {
                    homeButtonlast.callOnClick();
                } else {
                    Intent i = new Intent(CardsActivity.this, Breeds_list.class);
                    i.putExtra("Category", Category_name);
                    i.putStringArrayListExtra("Category_list", (ArrayList<String>) categories);
                    startActivity(i);
                    finish();
                }
            }
        });

        homeButtonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardsActivity.this ,Home_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        shareButtonlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.upstairs.dogcare"));
                startActivity(intent);
            }
        });

    }



    void onLastCardSwiped(){

        Animation fadeOut = new AlphaAnimation(1f,0f);
        fadeOut.setDuration(400);
        fadeOut.setFillEnabled(true);
        homeButton.startAnimation(fadeOut);
        backButton.startAnimation(fadeOut);
        breedName.startAnimation(fadeOut);
        cardCount.startAnimation(fadeOut);

        homeButton.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        breedName.setVisibility(View.GONE);
        cardCount.setVisibility(View.GONE);

        check.setVisibility(View.VISIBLE);
        relodeButtonlast.setVisibility(View.VISIBLE);
        backButtonlast.setVisibility(View.VISIBLE);
        homeButtonlast.setVisibility(View.VISIBLE);
        shareButtonlast.setVisibility(View.VISIBLE);





        Animation scale = new ScaleAnimation(0f, 1.1f, 0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(800);
        Animation fadeIn = new AlphaAnimation(0f,1f);
        fadeIn.setDuration(500);
        Animation scaleDown = new ScaleAnimation(1.1f, 1f, 1.1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleDown.setDuration(100);
        scaleDown.setStartOffset(800);
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(true);
        animSet.addAnimation(scale);
        animSet.addAnimation(fadeIn);
        animSet.addAnimation(scaleDown);
        check.startAnimation(animSet);




        Animation moveup = new TranslateAnimation(0f,0f,1100f,0f);
        moveup.setDuration(700);
        Animation fadeInButtons = new AlphaAnimation(0f,1f);
        fadeInButtons.setStartOffset(300);
        AnimationSet buttonAnimSet = new AnimationSet(true);
        buttonAnimSet.setFillEnabled(true);
        buttonAnimSet.addAnimation(moveup);
        buttonAnimSet.addAnimation(fadeInButtons);
        buttonAnimSet.setStartOffset(1100);
        relodeButtonlast.startAnimation(buttonAnimSet);
        backButtonlast.startAnimation(buttonAnimSet);
        homeButtonlast.startAnimation(buttonAnimSet);
        shareButtonlast.startAnimation(buttonAnimSet);


    }

    void onCardsSet(){
        homeButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        breedName.setVisibility(View.VISIBLE);
        cardCount.setVisibility(View.VISIBLE);

        check.setVisibility(View.GONE);
        relodeButtonlast.setVisibility(View.GONE);
        backButtonlast.setVisibility(View.GONE);
        homeButtonlast.setVisibility(View.GONE);
        shareButtonlast.setVisibility(View.GONE);
    }

    void setCards(){
        last_card_swiped = 0;
        cardCount.setText(last_card_swiped+1 +"/"+cards.size());
        mCardAdapter.clear();
        mCardAdapter.addAll(cards);
        mCardStack.setAdapter(mCardAdapter);
        onCardsSet();
    }

    void onRelode(){
        Intent i = new Intent(CardsActivity.this, CardsActivity.class);
        i.putExtra("Category", Category_name);
        i.putExtra("Breed", Breed_name);
        startActivity(i);
        finish();

    }

    private List<String> getCategoriesFromDb(){
        List<String> Categories = new ArrayList<String>();
        if(SharedPrefHelper.checkDBReady(getApplicationContext())){//Check Flag Again to ensure that the database is ready
            CardDBHelper cdbhelper=new CardDBHelper(getApplicationContext());
            SQLiteDatabase db = cdbhelper.getReadableDatabase();
            String[] projection={
                    CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION
            };
            String sortOrder = CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION + " DESC";
            Cursor c = db.query(true, CardDBContract.CardTable.TABLE_NAME,projection,null,null,null,null,sortOrder,null);

            if(c!=null){

                for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                    String temp = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION));
                    Categories.add(temp);
                }
            }
            db.close();
        }


        return Categories;
    }
}
