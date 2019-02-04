package com.maseno.franklinesable.politicalapp.log_in_package;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maseno.franklinesable.politicalapp.Forgot_password;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.RegisterActivity;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;
import com.maseno.franklinesable.politicalapp.sharedmethods.simple_about_dialog;

import butterknife.ButterKnife;
import butterknife.BindView;

public class Front_EndActivity extends AppCompatActivity {

    public static Boolean exitClick = false;
    public static Boolean fragmentDestroyed = true;
    Boolean mShowingBack = false;
    PreferencesHandler savePref;
    @BindView(R.id.welcomeTxt)
    TextView launchText;
    @BindView(R.id.dev_intro)
    TextView devIntro;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.credBat)
    Button credBat;
    @BindView(R.id.welImg)
    ImageView welImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_end);
        ButterKnife.bind(this);

        savePref = new PreferencesHandler(this);
        savePref.setFirstLaunch(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        toolbar.setTitleTextAppearance(this, android.R.style.TextAppearance_Large);
        toolbar.setTitleTextColor(Color.parseColor("#F0F0F0"));
        toolbar.setSubtitleTextColor(Color.parseColor("#c70039"));

        Typeface typeface = Typeface.createFromAsset(getAssets(), "DancingScript_Bold.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "segoesc.ttf");

        launchText.setTypeface(typeface);
        devIntro.setTypeface(typeface2);


        if (checkPrefStates()) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        } else {
            layout1.setVisibility(View.VISIBLE);
        }

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // BounceInterpolator interpolator = new BounceInterpolator(0.2, 5);
        anim.setInterpolator(this, android.R.interpolator.bounce);
        credBat.startAnimation(anim);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<5;i++){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            credBat.clearAnimation();
                            credBat.startAnimation(anim);

                            if(finalI ==4){
                                welImg.animate().rotationY(360).setDuration(android.R.integer.config_longAnimTime);
                                credBat.setText("Press here to continue>>");
                            }
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {

        if (fragmentDestroyed) {
            if (!exitClick) {
                Toast.makeText(this, R.string.exiting, Toast.LENGTH_SHORT).show();
                exitClick = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(3000);
                        exitClick = false;
                    }
                }).start();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.front_end, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        } else {
            //about app
            DialogFragment aboutDialog=new simple_about_dialog();

            aboutDialog.show(getFragmentManager(),"about");

        }


        return super.onOptionsItemSelected(item);
    }

    public void execCredentialsManager(View v) {
        fragmentDestroyed = false;
        mShowingBack = false;
        if (checkInternetConnection()) {
            flipCard(new LogInFragment());
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialogStyleLaunchScreen);
            LayoutInflater inflater = getLayoutInflater();
            final View layout = inflater.inflate(R.layout.internet_alert_dialog, null);
            final ImageView launchConn = (ImageView) layout.findViewById(R.id.launchSettings);

            if (savePref.getAskConnection()) {
                builder.setCancelable(false).setIconAttribute(R.attr.background).setView(layout).setIcon(R.drawable.ic_report_black_24dp)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                flipCard(new LogInFragment());
                            }
                        }).setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setTitle(R.string.internet_warning);
                builder.create().show();
            }
            launchConn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            });
        }
    }

    private Boolean checkPrefStates() {
        if (savePref.getLoggedInState() > 0) {
            return true;
        }
        return false;
    }

    private Boolean checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ntwInfo = connMgr.getActiveNetworkInfo();
        if (ntwInfo != null && ntwInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void flipCard(Fragment setFragment) {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        // Flip to the back.

        mShowingBack = true;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).
                .replace(R.id.container, setFragment)

                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                .addToBackStack(null)

                // Commit the transaction.
                .commit();
    }

    public void execSignUp(View v) {
        startActivity(new Intent(this, RegisterActivity.class));

    }

    public void execForgotPassword(View v) {
        startActivity(new Intent(this, Forgot_password.class));

    }

    public void openDevWeb(View v) {
       /* Uri webpage=new Uri.parse("http://www.fsDevs.com");
        Intent intent=new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);*/
    }
}
