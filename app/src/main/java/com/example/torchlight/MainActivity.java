package com.example.torchlight;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity {

    private ImageButton powerBtn;
    private Boolean isTorchOn, isMuted;
    private SoundPool soundPool;
    private int clickSound;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        powerBtn = (ImageButton) findViewById(R.id.power_button);
        isTorchOn = false;

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        }

        clickSound = soundPool.load(this, R.raw.click2_sebastian, 1);


        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {

            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }


        flashLightOnAtStartup();

        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isTorchOn) {
                        powerBtn.setImageResource(R.drawable.power_off_24dp);
                        stopService(new Intent(MainActivity.this, FlashService.class));
                        isTorchOn = false;
                    } else {
                        powerBtn.setImageResource(R.drawable.power_on_24dp);
                        startService(new Intent(MainActivity.this, FlashService.class));
                        isTorchOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isMuted = sharedPreferences.getBoolean("mute", false);
                if (!isMuted) {
                    soundPool.play(clickSound, 1, 1, 0, 0, 1);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Boolean turnOffAtExit = sharedPreferences.getBoolean("turn_off_at_exit", false);
        if (turnOffAtExit) {
            stopService(new Intent(getApplicationContext(), FlashService.class));
        }
    }


    public void flashLightOnAtStartup() {
        Boolean turnOnAtStart = sharedPreferences.getBoolean("turn_on_at_start", false);
        if (turnOnAtStart) {
            startService(new Intent(MainActivity.this, FlashService.class));
            powerBtn.setImageResource(R.drawable.power_on_24dp);
            isTorchOn = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);

            default:
                return true;
        }

    }
}






