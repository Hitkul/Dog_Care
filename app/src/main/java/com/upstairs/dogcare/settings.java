package com.upstairs.dogcare;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(com.upstairs.dogcare.R.layout.activity_settings);

        final ImageButton backButton = (ImageButton) findViewById(com.upstairs.dogcare.R.id.backButton);
        final TextView title = (TextView) findViewById(com.upstairs.dogcare.R.id.title_settings);
        final TextView clearRecent = (TextView) findViewById(com.upstairs.dogcare.R.id.clearRecent);
        final Switch notificationSwitch = (Switch) findViewById(com.upstairs.dogcare.R.id.Notification_switch);
        Typeface font = Typeface.createFromAsset(getAssets(), "raleway.ttf");
        title.setTypeface(font);
        clearRecent.setTypeface(font);
        notificationSwitch.setTypeface(font);

        notificationSwitch.setChecked(SharedPrefHelper.CheckNotificationFlag(getApplicationContext()));


        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPrefHelper.setNotificationFlag(getApplicationContext(),true);
                }else{
                    SharedPrefHelper.setNotificationFlag(getApplicationContext(),false);
                }
            }
        });

        clearRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefHelper.clearString(getApplicationContext(),SharedPrefHelper.LAST_BREED_SWIPED);
                SharedPrefHelper.clearString(getApplicationContext(),SharedPrefHelper.LAST_CATEGORY_SWIPED);
                Toast.makeText(getApplication(),"Cleared your Recent",Toast.LENGTH_SHORT).show();
            }
        });


        assert backButton != null;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSettings = new Intent(settings.this, Home_Activity.class);
                toSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toSettings);
                overridePendingTransition(com.upstairs.dogcare.R.anim.pull_in_left, com.upstairs.dogcare.R.anim.push_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent toSettings = new Intent(settings.this, Home_Activity.class);
        toSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toSettings);
        overridePendingTransition(com.upstairs.dogcare.R.anim.pull_in_left, com.upstairs.dogcare.R.anim.push_out_right);
    }
}
