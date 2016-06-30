package com.upstairs.dogcare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karm on 17/6/16.
 */
public class CardDBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CARDS =
            "CREATE TABLE " + CardDBContract.CardTable.TABLE_NAME + " (" +
                    CardDBContract.CardTable.COLUMN_NAME_CARD_ID + " INTEGER PRIMARY KEY," +
                    CardDBContract.CardTable.COLUMN_NAME_CARD_POSITION+ " INTEGER,"+
                    CardDBContract.CardTable.COLUMN_NAME_CARD_TITLE + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_CARD_IMAGE + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_CARD_TEXT  + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_CARD_LIST  + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION + TEXT_TYPE + COMMA_SEP +
                    CardDBContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION + TEXT_TYPE+" )";
    public static final String SQL_DELETE_CARDS =
            "DROP TABLE IF EXISTS " + CardDBContract.CardTable.TABLE_NAME;

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="PetCare.db";//Change this for neatness

    public CardDBHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CARDS);
        }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_CARDS);

        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }
    public void onDelete(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_CARDS);
    }
}