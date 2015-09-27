package yesteam.code4pilar2015.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.services.DownloadEvents;

public class SplashActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DELAY);

        ImageView img = (ImageView) findViewById(R.id.splash_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        launchUpdate();

        /*final TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }

            @Override
            public boolean cancel() {
                Boolean bool = super.cancel();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();

                return bool;
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

        ImageView img = (ImageView) findViewById(R.id.splash_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel();
            }
        });*/
    }

    private void launchUpdate() {
        startService(new Intent(SplashActivity.this, DownloadEvents.class));
    }
}
