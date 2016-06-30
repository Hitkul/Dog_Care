package com.upstairs.dogcare;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

/**
 * Created by karm on 17/6/16.
 */
public class XMLAssetHandler extends AsyncTask<Void, Void, Void> {
    Context context;
    MainActivity activity;
    CardDBHelper cardbhelper;
    ProgressDialog pd;


    public XMLAssetHandler(Context foo, MainActivity activity){//constructor to pass it an application context
        context = foo;
        this.activity = activity;
        pd=new ProgressDialog(activity);
    }
    @Override
    protected Void doInBackground(Void... params) {

        this.cardbhelper= new CardDBHelper(context);
        List<Card> Cards = null;
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            //Populate Cards and call XML Handler
            Cards = parser.parse(context.getAssets().open("try1.xml"));
            writeCardstodb(Cards);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;//why does this function need a return?
    }

    private void writeCardstodb(List<Card> Cards) {
        for(int i=0;i<Cards.size();i++){
            writetodb(Cards.get(i));
        }
    }

    private void writetodb(Card temp) {
        SQLiteDatabase db = cardbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(temp!=null) {
            if (temp.getTitle()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_CARD_TITLE, temp.getTitle());
            if (temp.getText()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_CARD_TEXT, temp.getText());
            if (temp.getImage()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_CARD_IMAGE, temp.getImage());
            if (temp.getList_string()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_CARD_LIST, temp.getList_string());
            if (temp.getClassification()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION, temp.getClassification());
            if (temp.getSubclassification()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION, temp.getSubclassification());
            if (temp.getSubsubclassification()!=null)
                values.put(CardDBContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION, temp.getSubsubclassification());
                values.put(CardDBContract.CardTable.COLUMN_NAME_CARD_POSITION,temp.getCardPosition());
            long newRowId = db.insert(CardDBContract.CardTable.TABLE_NAME,
                    CardDBContract.CardTable.COLUMN_NAME_CARD_TEXT,
                    values);
        }
    }
    @Override
    protected void onPreExecute(){

        pd.setMessage("Letting the Dogs out...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

    }


    @Override
    protected void  onPostExecute(Void res){
        if(pd.isShowing())
            pd.dismiss();
        SharedPrefHelper.setDbReady(context);
        Intent foo = new Intent(context,Home_Activity.class);
        foo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(foo);
        activity.finish();

    }

}
