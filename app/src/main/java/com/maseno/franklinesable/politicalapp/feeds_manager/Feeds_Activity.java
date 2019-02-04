package com.maseno.franklinesable.politicalapp.feeds_manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maseno.franklinesable.politicalapp.Account_Settings;
import com.maseno.franklinesable.politicalapp.AppDatabase;
import com.maseno.franklinesable.politicalapp.DividerItemDecoration;
import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import java.util.ArrayList;
import java.util.List;

public class Feeds_Activity extends AppCompatActivity {

    private List<FeedsContents> FeedsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Feeds_Adapter fAdapter;
    private ImageView headerImageView;
    public static final String JSON_URL_FEEDS ="http://192.168.75.2/polits_server/feeds_section/feeds_section.json";
    private fetchFeedData asyncFetch;
    PreferencesHandler savePrefs,modulePrefs;
    private Feeds_Database  dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        savePrefs=new PreferencesHandler(this);
        modulePrefs=new PreferencesHandler(this);
        dbHandler=new Feeds_Database(Feeds_Activity.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        fAdapter = new Feeds_Adapter(FeedsList,Feeds_Activity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(fAdapter);

        mRecyclerView.addOnItemTouchListener(new Feeds_Adapter.RecyclerTouchListener(getApplicationContext(), mRecyclerView, new Feeds_Adapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FeedsContents movie = FeedsList.get(position);
                //Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareFeedsData();
        configureHeader();
    }
    private void prepareFeedsData(){

        asyncFetch = new fetchFeedData();
        asyncFetch.execute();
     /*   String array[][] = {
                {"Upcoming Political Event", "Politicians are pigs all of them are a bunch of pigs and they should FAIL!","2h",Integer.toString(R.drawable.govern_pic)},
                {"Corrupt Government of Kenya", "Politicians are pigs all of them are a bunch of pigs and they should FAIL!","2h",Integer.toString(R.drawable.sunset_view)},
                {"Working on Project", "Politicians are pigs all of them are a bunch of pigs and they should FAIL!","2h",Integer.toString(R.drawable.beautiful_nature_paintings)},
                {"Upcoming Political Event", "Politicians are pigs all of them are a bunch of pigs and they should FAIL!","2h",Integer.toString(R.drawable.govern_pic)}};


        for(int i=0; i<array.length;i++){
           // FeedsContents a =new FeedsContents(array[i][0],array[i][1],array[i][2], Integer.parseInt(array[i][3]));
           // FeedsList.add(a);
        }

       // fAdapter.notifyDataSetChanged();*/
    }
    private void configureHeader(){
        headerImageView=(ImageView)findViewById(R.id.headerImage);
        Drawable headerDrawable=getResources().getDrawable(R.drawable.pirate_beach_msa);
        Bitmap headerBitmap=((BitmapDrawable)headerDrawable).getBitmap();
        headerImageView.setImageBitmap(headerBitmap);

        invertBitmap(headerBitmap);
    }
    private void invertBitmap(Bitmap headerBitmap){
        Bitmap headerImageBitmap=invertImage(headerBitmap);
        headerImageView.setImageBitmap(headerImageBitmap);
    }
    protected static Bitmap invertImage(Bitmap original_Img){

        Bitmap create_FinalImage=Bitmap.createBitmap(original_Img.getWidth(), original_Img.getHeight(), original_Img.getConfig());
        int pixel;
        int A,R,G,B;
        int height=original_Img.getHeight();
        int width=original_Img.getWidth();

        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){

                pixel=original_Img.getPixel(x, y);
                A= Color.alpha(pixel);
                R=Color.red(pixel);
                G=Color.green(pixel);
                B=200-Color.blue(pixel);

                create_FinalImage.setPixel(x,y,Color.rgb(R,G,B));

            }
        }
        return create_FinalImage;
    }

    public class fetchFeedData extends AsyncTask<Void, Cursor, Cursor> {
        private ProgressDialog dialogFeeds;
        private String feedsFetch[];
        @Override
        protected void onPreExecute() {
            dialogFeeds = new ProgressDialog(Feeds_Activity.this);
            dialogFeeds.setIndeterminate(false);
            dialogFeeds.setCancelable(true);
            dialogFeeds.setMessage("Loading feeds data...");
            dialogFeeds.show();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            dbHandler.open();
            Cursor cursor = dbHandler.readDb(Feeds_Database.tb_Struct.KEY_DELETED+" = "+0,null,Feeds_Database.tb_Struct.KEY_CREATED_DATE_TIME+" ASC");
            cursor.moveToFirst();
            FeedsList.clear();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int pid = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_PID);
            int title= cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_TITLE);
            int summary = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_DESCRIPTION);
            int image = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_IMAGE_URL);
            int feed_id = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_FEED_ID);
            int date = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_CREATED_DATE_TIME);
            int deleted = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_DELETED);
            int hidden = cursor.getColumnIndexOrThrow(Feeds_Database.tb_Struct.KEY_HIDDEN);

            while (!cursor.isAfterLast()) {
                FeedsContents feeds =new FeedsContents(cursor.getString(title), cursor.getString(summary),"3h",cursor.getString(image),cursor.getInt(feed_id),cursor.getInt(hidden));
                FeedsList.add(feeds);
                cursor.moveToNext();
            }
            cursor.close();
            fAdapter.notifyDataSetChanged();
            dialogFeeds.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncFetch = null;
            dialogFeeds.cancel();
        }

    }

    @Override
    protected void onResume() {
        dbHandler.open();
        super.onResume();
        modulePrefs.saveFeeds(0);
    }

    @Override
    protected void onPause() {
        dbHandler.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feeds_,menu);
        return true;
    }
}
