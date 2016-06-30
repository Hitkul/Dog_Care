package com.upstairs.dogcare;

import android.database.Cursor;

/**
 * Created by karm on 17/6/16.
 */
public class Card {
    private int CardPosition;
    private int CardLayoutType; //Card Layout Type should be 1,2,3,4 Too sleepy to write enum
    //Data Members
    private String text;
    private String title;
    private String image;
    private String List_string;
    //MetaData Members
    private String classification;
    private String subclassification;
    private String subsubclassification;


    public Card(){
        //DEFAULT CONSTRUCTOR SET EVERYTHING TO NULL
        text = null;
        title = null;
        image = null;
        List_string = null;
        classification = null;
        subclassification = null;
        subsubclassification = null;

    }

    public Card(Cursor c) {
        text  = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CARD_TEXT));
        title = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CARD_TITLE));
        image = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CARD_IMAGE));
        String tempStr = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CARD_LIST));
        if(tempStr!=null) {
            List_string = tempStr.replaceAll("#","\n\n");
        }
        classification = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_CLASSIFICATION));
        subclassification = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_SUBCLASSIFICATION));
        subsubclassification = c.getString(c.getColumnIndexOrThrow(CardDBContract.CardTable.COLUMN_NAME_SUBSUBCLASSIFICATION));
        if(title==null)
            title=subsubclassification;
        setCardLayoutType();
    }


    public void setCardLayoutType(){

        if(text!=null   &&
                title!=null  &&
                image!=null  &&
                List_string !=null)
            CardLayoutType=1;//Card Layout Type 1 has everything.
        else if(text==null&&image==null)
            CardLayoutType=2;//Card Layout Type 2 should have only Title and List
        else if(image==null&& List_string ==null)
            CardLayoutType=3;//Card Layout Type 2 should have only Title and Text
        else if(List_string ==null)
            CardLayoutType=4;//Card Layout Type 4 has image, title and text

    }

    public int getCardLayoutType(){return CardLayoutType;}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getSubclassification() {
        return subclassification;
    }

    public void setSubclassification(String subclassification) {
        this.subclassification = subclassification;
    }

    public String getSubsubclassification() {
        return subsubclassification;
    }

    public void setSubsubclassification(String subsubclassification) {
        this.subsubclassification = subsubclassification;
    }
    @Override
    public String toString(){
        String temp="";
        if(title!=null)
            temp+=("TITLE: "+title);
        if(text!=null)
            temp+=("TXT: "+text);
        if(image!=null)
            temp+=("IMG: "+image);
        if(classification!=null)
            temp+=("CLASS: "+classification);
        if( subclassification!=null)
            temp+=("SUBCLASS: "+subclassification);
        if(subsubclassification!=null)
            temp+=("SUBSUBCLASS: "+subsubclassification);
        return temp;
    }

    public int getCardPosition() {
        return CardPosition;
    }

    public void setCardPosition(int cardPosition) {
        CardPosition = cardPosition;
    }

    public String getList_string() {
        return List_string;
    }

    public void setList_string(String list_string) {
        this.List_string = list_string;
    }
}
