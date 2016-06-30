package com.upstairs.dogcare;

import android.provider.BaseColumns;

/**
 * Created by karm on 17/6/16.
 */
public class CardDBContract {
    public CardDBContract(){}


    //empty constructor to prevent instantiation
    public static abstract class CardTable implements BaseColumns {
        public static final String TABLE_NAME="cards";
        public static final String COLUMN_NAME_CARD_ID="cardid";
        public static final String COLUMN_NAME_CARD_TITLE="cardtitle";
        public static final String COLUMN_NAME_CARD_IMAGE="cardimage";
        public static final String COLUMN_NAME_CLASSIFICATION="classification";
        public static final String COLUMN_NAME_SUBCLASSIFICATION="subclassification";
        public static final String COLUMN_NAME_SUBSUBCLASSIFICATION="subsubclassification";
        public static final String COLUMN_NAME_CARD_TEXT="cardtext";
        public static final String COLUMN_NAME_CARD_LIST="cardlist";
        public static final String COLUMN_NAME_CARD_POSITION="pos";
    }
}
