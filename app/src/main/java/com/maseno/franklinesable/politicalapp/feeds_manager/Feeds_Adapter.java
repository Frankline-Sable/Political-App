package com.maseno.franklinesable.politicalapp.feeds_manager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Frankline Sable on 09/02/2017. from 16:43
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

class Feeds_Adapter extends RecyclerView.Adapter<Feeds_Adapter.MyViewHolder> {
    private List<FeedsContents> feedList;
    private Context mContext;
    private int feedID;
    private Boolean showVisiblity;
    private executeDbTaskAsync dbTaskAsync = null;
    private Feeds_Database dbHandler;

    Feeds_Adapter(List<FeedsContents> feedList, Context mContext) {
        this.feedList = feedList;
        this.mContext = mContext;
        dbHandler = new Feeds_Database(mContext);
    }

    @Override
    public Feeds_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_feeds, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Feeds_Adapter.MyViewHolder holder, int position) {
        final FeedsContents feed = feedList.get(position);
        holder.summary.setText(feed.getSummary());
        holder.posterTime.setText(feed.getPosterTime());
        holder.headline.setText(feed.getHeadLine());

        if (feed.getHiddenState() == 1)
            holder.lowerRelativeView.setVisibility(View.GONE);
        // holder.feedImage.setImageResource(feed.getFeedImg());

        Picasso.with(mContext)
                .load(feed.getFeedImg())//I have emitted the placeholder
                .error(R.drawable.side_nav_bar)
                .into(holder.feedImage);

        holder.feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Fragments
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Fragments
            }
        });
        holder.overflowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedID = feed.getPid();
                showPopUpMenu(view);
            }
        });
        holder.visibilityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedID = feed.getPid();
                holder.lowerRelativeView.setVisibility(View.VISIBLE);
                showVisiblity = true;
                dbTaskAsync = new executeDbTaskAsync(false);
                dbTaskAsync.execute(feedID);
            }
        });
    }

    private void showPopUpMenu(final View v) {
        final PopupMenu popupMenu = new PopupMenu(mContext, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.feed_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int itemId = item.getItemId();
                View lowerView = (View) v.getParent();

                if (itemId == R.id.action_delete) {
                   /* 
                    View linearView = (View) parentView.getParent();
                    View recycler = (View) linearView.getParent();
                    RecyclerView recyclerView = (RecyclerView) recycler;
                    //int pos = recyclerView.getChildLayoutPosition(linearView);*/
                    View parentView = (View) lowerView.getParent();
                    parentView.setVisibility(View.GONE);
                    dbTaskAsync = new executeDbTaskAsync(true);
                    dbTaskAsync.execute(feedID);
                    return true;
                } else if (itemId == R.id.action_hide) {
                    lowerView.setVisibility(View.GONE);
                    showVisiblity = false;
                    dbTaskAsync = new executeDbTaskAsync(false);
                    dbTaskAsync.execute(feedID);
                    return true;
                } else {
                    //TODO add actions and return true in each case
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView headline, summary, viewMore, posterTime, shareButton;
        ImageView feedImage, overflowImage, visibilityImage;
        RelativeLayout lowerRelativeView;

        MyViewHolder(View view) {
            super(view);
            mView = view;

            summary = (TextView) view.findViewById(R.id.summary);
            viewMore = (TextView) view.findViewById(R.id.more);
            posterTime = (TextView) view.findViewById(R.id.posterTime);
            shareButton = (TextView) view.findViewById(R.id.share);
            headline = (TextView) view.findViewById(R.id.headLine);
            feedImage = (ImageView) view.findViewById(R.id.feedImage);
            overflowImage = (ImageView) view.findViewById(R.id.overflowFeedMenu);
            visibilityImage = (ImageView) view.findViewById(R.id.visibilityButton);
            lowerRelativeView = (RelativeLayout) view.findViewById(R.id.lowerRelativeView);

        }
    }

    private class executeDbTaskAsync extends AsyncTask<Integer, Void, Boolean> {

        private Boolean deleteDb;

        executeDbTaskAsync(Boolean deleteDb) {
            this.deleteDb = deleteDb;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            dbHandler.open();
            if (deleteDb) {
                dbHandler.updateFeedsDb(integers[0], false, true);
            } else {
                if (showVisiblity)
                    dbHandler.updateFeedsDb(integers[0], false, false);
                else
                    dbHandler.updateFeedsDb(integers[0], true, false);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dbTaskAsync = null;
        }
    }
}
