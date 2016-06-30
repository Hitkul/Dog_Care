package com.upstairs.dogcare;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hitkul on 26/6/16.
 */
public class SharedPrefHelper {
    //Strings to store keys of Shared Preferences
    public static final String SHARED_PREFERENCES_KEY = "petcareflags";
    public static final String LAST_CATEGORY_SWIPED = "last_category_swiped";
    public static final String LAST_BREED_SWIPED = "last_breed_swiped";
    public static final String DB_READY = "DB_Ready";
    public static final String DEFAULT_STRING = "Default";
    public static final String EMPTY_STRING = "";
    public static final boolean DEFAULT_BOOL = false;
    public static final String NUM_APP_USED = "num_app_used";
    public static final String PUT_RECENT = "put_recent";
    public static final String NOTIFICATION_FLAG = "notify_alarm_flag";
    public static final String FIRSTTIME_FLAG = "firsttime";

    public static boolean Checkif5Recent(Context appContext){
        boolean tempbool = false;
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String TempCat = Flag.getString(LAST_CATEGORY_SWIPED,DEFAULT_STRING);
        String TempBreed = Flag.getString(LAST_BREED_SWIPED,DEFAULT_STRING);
        int catOc = TempCat.length()-TempCat.replace("#","").length();
        int breOc = TempBreed.length()-TempBreed.replace("#","").length();
        if(catOc>=5&&breOc>=5)
            tempbool = true;
        return tempbool;
    }
    public static void clearString(Context appContext,String key){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= Flag.edit();
        editor.putString(key,EMPTY_STRING);
        editor.commit();

    }
    public static boolean CheckAppFirstTimeFlag(Context appContext){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        return Flag.getBoolean(FIRSTTIME_FLAG,DEFAULT_BOOL);
    }
    public static void setAppFirstTime(Context appContext){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        editor.putBoolean(FIRSTTIME_FLAG,true);
        editor.commit();

    }

    public static boolean CheckNotificationFlag(Context appContext){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        return Flag.getBoolean(NOTIFICATION_FLAG,DEFAULT_BOOL);

    }
    public static void setNotificationFlag(Context appContext, boolean flag){
        SharedPreferences Flag = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        editor.putBoolean(NOTIFICATION_FLAG,flag);
        editor.commit();

    }
    public static List<String> ReturnRecentCategories(Context applicationContext){

        SharedPreferences Flag = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String temp_cat = Flag.getString(LAST_CATEGORY_SWIPED,DEFAULT_STRING);
        List<String> items = Arrays.asList(temp_cat.split("\\s*#\\s*"));
        return items;
    }
    public static List<String> ReturnRecentBreeds(Context applicationContext){

        SharedPreferences Flag = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String temp_cat = Flag.getString(LAST_BREED_SWIPED,DEFAULT_STRING);
        List<String> items = Arrays.asList(temp_cat.split("\\s*#\\s*"));
        return items;
    }


    public static void storeLastSwiped(Context context,String Category_name, String Breed_name){
        SharedPreferences Flag = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        String TempCat = Flag.getString(LAST_CATEGORY_SWIPED,EMPTY_STRING);
        String TempBreed = Flag.getString(LAST_BREED_SWIPED,EMPTY_STRING);
        boolean tempbool = false;
        if(!TempBreed.contains(Breed_name)){
            TempCat+=(Category_name+"#");
            TempBreed+=(Breed_name+"#");
            int catOc = TempCat.length()-TempCat.replace("#","").length();
            int breOc = TempBreed.length()-TempBreed.replace("#","").length();
            if(catOc>5&&breOc>5){
                TempCat = TempCat.replaceFirst(TempCat.substring(0,TempCat.indexOf("#")+1),"");
                TempBreed = TempBreed.replace(TempBreed.substring(0,TempBreed.indexOf("#")+1),"");
                tempbool = true;
            }



            SharedPreferences.Editor editor = Flag.edit();
            editor.putBoolean(PUT_RECENT,tempbool);
            editor.putString(LAST_CATEGORY_SWIPED,TempCat);
            editor.putString(LAST_BREED_SWIPED,TempBreed);
            editor.commit();

        }

    }

    public static void setDBNotReady(Context context){
        SharedPreferences Flag = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        editor.putBoolean(DB_READY,DEFAULT_BOOL);
        editor.commit();
    }
    public static void setDbReady(Context context){
        SharedPreferences Flag = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        editor.putBoolean(DB_READY,true);
        editor.commit();
    }
    public static boolean checkDBReady(Context context){
        SharedPreferences Flag = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        return Flag.getBoolean(DB_READY,DEFAULT_BOOL);

    }

    public static int getNumUsed(Context applicationContext) {
        SharedPreferences Flag = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        return Flag.getInt(NUM_APP_USED,0);
    }
    public static void incrementNumUsed(Context applicationContext){
        SharedPreferences Flag = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Flag.edit();
        int temp = getNumUsed(applicationContext);
        Log.d("INTEGERSTORED",String.valueOf(temp));
        editor.putInt(NUM_APP_USED,temp+1);
        editor.commit();
    }
}
