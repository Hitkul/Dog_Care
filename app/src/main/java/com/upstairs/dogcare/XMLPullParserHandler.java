package com.upstairs.dogcare;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karm on 17/6/16.
 */
public class XMLPullParserHandler {
    List<Card> Cards;
    private Card card;
    private String text;
    private String tempList;
        public XMLPullParserHandler(){
        Cards = new ArrayList<Card>();
    }

    public List<Card> getCards(){return Cards;}
    public List<Card> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("card")) {
                            // create a new instance of employee
                            card = new Card();
                        }else if(tagname.equalsIgnoreCase("list")){
                            tempList="";
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("card")) {
                            // add employee object to list
                            Cards.add(card);

                        } else{
                            if(text!=null){
                                if (tagname.equalsIgnoreCase("title")) {
                                    card.setTitle(text.trim());
                                } else if (tagname.equalsIgnoreCase("text")) {
                                    String temp= text.replaceAll("(\\s)\\1","$1");
                                    List<String> parts = new ArrayList<String>(Arrays.asList(temp.split("\n")));
                                    temp="";
                                    for (int i =0; i<parts.size();i++)
                                        temp+=(parts.get(i).trim()+"\n\n");
                                    card.setText(temp.trim());
                                } else if (tagname.equalsIgnoreCase("image")) {
                                    card.setImage(text.trim());
                                } else if (tagname.equalsIgnoreCase("classification")) {
                                    card.setClassification(text.trim());
                                } else if (tagname.equalsIgnoreCase("subclassification")) {
                                    card.setSubclassification(text.trim());
                                } else if (tagname.equalsIgnoreCase("subsubclassification")) {
                                    card.setSubsubclassification(text.trim());
                                } else if (tagname.equalsIgnoreCase("list")) {
                                    card.setList_string(tempList);
                                } else if(tagname.equalsIgnoreCase("li")){
                                    tempList+=(text.trim()+"#");
                                } else if(tagname.equalsIgnoreCase("ul")){
                                    tempList+=("\u25cf"+"\u0020"+text.trim()+"#");
                                } else if(tagname.equalsIgnoreCase("position")){
                                    card.setCardPosition(Integer.parseInt(text));
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Cards;
    }
}

