package com.maseno.franklinesable.politicalapp.welcome_package;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.log_in_package.Front_EndActivity;
import com.maseno.franklinesable.politicalapp.central_class_boss.Main_BossClass;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class WelcomeActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;
    @BindView(R.id.nextBtn)
    Button mNextButton;
    @BindView(R.id.skipBtn)
    Button mSkipButton;
    @BindView(R.id.licenceTextBox)
    CheckBox licenseView;
    @BindView(R.id.viewPager)
    ViewPager mPager;
    Boolean mShowingBack = false;
    private Fragment_1 frag1;
    private Fragment_2 frag2;
    private Fragment_3 frag3;
    private Fragment_4 frag4;
    private Fragment_5 frag5;
    private ViewPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesHandler savePref = new PreferencesHandler(this);
        if (savePref.getAccountState() && savePref.getFirstLaunch()) {
            startActivity(new Intent(this, Main_BossClass.class));
            finish();
        } else if (savePref.getFirstLaunch()) {
            startActivity(new Intent(this, Front_EndActivity.class));
            finish();
        }
        //Making notification bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_welcome_first);
        ButterKnife.bind(this);

        addBottonDots(0);
        changeStatusColor();
        setUpSpannableText();

        frag1 = new Fragment_1();
        frag2 = new Fragment_2();
        frag3 = new Fragment_3();
        frag4 = new Fragment_4();
        frag5 = new Fragment_5();

        mPager.setPageTransformer(true, new DepthPageTransformer());
        setupViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Fragment_1 frag1 = (Fragment_1) mPager.getAdapter().instantiateItem(mPager, 0);
                if (position < 2)
                    frag1.animateBackground(positionOffsetPixels);

                switch (position) {
                    case 1:
                        //frag2.animateView();
                        //  Fragment_2 frag2 = (Fragment_2) mPager.getAdapter().instantiateItem(mPager, 1);
                        frag2.animateView();
                        break;
                    case 2:
                        //frag3.animateView();
                        // Fragment_3 frag3 = (Fragment_3) mPager.getAdapter().instantiateItem(mPager, 2);
                        frag3.animateView();
                        break;
                    case 3:
                        //frag4.animateView();
                        // Fragment_4 frag4 = (Fragment_4) mPager.getAdapter().instantiateItem(mPager, 3);
                        frag4.animateView();
                        break;
                    case 4:
                        // frag5.animateView();
                        //    Fragment_5 fragLast = (Fragment_5) mPager.getAdapter().instantiateItem(mPager, 4);
                        frag5.animateView();
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                addBottonDots(position);
                if (position == NUM_PAGES - 1) {
                    mNextButton.setText(R.string.done);
                    mSkipButton.setVisibility(View.GONE);
                    licenseView.setVisibility(View.VISIBLE);
                } else {
                    mNextButton.setText(getResources().getString(R.string.next));
                    mSkipButton.setVisibility(View.VISIBLE);
                    licenseView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < NUM_PAGES) {
                    mPager.setCurrentItem(current);
                } else {
                    checkUserAgreement();
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(4);
            }
        });
    }

    private void addBottonDots(int currentPage) {
        TextView[] dots = new TextView[NUM_PAGES];
        int colorsActive[] = getResources().getIntArray(R.array.array_dot_active);
        int colorsInActive[] = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInActive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }

    public void setupViewPager(ViewPager mPager) {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(frag1);
        mPagerAdapter.addFragment(frag2);
        mPagerAdapter.addFragment(frag3);
        mPagerAdapter.addFragment(frag4);
        mPagerAdapter.addFragment(frag5);
        mPager.setAdapter(mPagerAdapter);
    }

    private void changeStatusColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setUpSpannableText() {
        SpannableString spannableString = new SpannableString(getString(R.string.license_accept));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //licence view
                showLicenseView();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan, 13, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        licenseView.setText(spannableString);
        licenseView.setMovementMethod(LinkMovementMethod.getInstance());
        //licenseView.setHighlightColor(Color.RED);
    }

    private void checkUserAgreement() {
        Boolean checked = licenseView.isChecked();
        if (checked) {
            startActivity(new Intent(this, Front_EndActivity.class));
            finish();
        } else
            Toast.makeText(this, R.string.license_warning, Toast.LENGTH_SHORT).show();

    }

    private void showLicenseView() {
        LicenseLoader loadLicense = new LicenseLoader();
        loadLicense.execute(true);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private class LicenseLoader extends AsyncTask<Boolean, Void, String> {
        private ProgressBar progressBar;
        private String licence_read = null;

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Boolean... booleen) {

            InputStream readLicence = getResources().openRawResource(R.raw.licence);
            try {
                byte[] licence_Input = new byte[readLicence.available()];
                while (readLicence.read(licence_Input) != -1) {
                    //ignore loop
                }
                licence_read = new String(licence_Input);
            } catch (IOException e) {

                licence_read = getString(R.string.license_error);
            } finally {

                if (readLicence != null) {
                    try {
                        readLicence.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return licence_read;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
            builder.setMessage(s)
                    .setPositiveButton(R.string.done2, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    }


}
