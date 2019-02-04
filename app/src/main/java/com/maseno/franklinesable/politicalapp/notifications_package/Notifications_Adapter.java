package com.maseno.franklinesable.politicalapp.notifications_package;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.sharedmethods.PreferencesHandler;

import java.util.List;

/**
 * Created by Frankline Sable on 25/02/2017. from 01:01
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

public class Notifications_Adapter extends RecyclerView.Adapter<Notifications_Adapter.ViewHolder> {

    private List<Notifications> notificationsList;
    private PreferencesHandler savePrefs;

    public Notifications_Adapter(List<Notifications> notificationsList, Context mContext) {
        this.notificationsList = notificationsList;
        savePrefs = new PreferencesHandler(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notifications, parent, false);
        // TODO set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notifications notes = notificationsList.get(position);
        holder.name.setText(notes.getName());
        holder.title.setText(notes.getTitle());

        if (notes.getFlag() == 1)
            holder.flag.setColorFilter(Color.RED);//Important

        String summary = notes.getSummary();
        if (summary.contains("user"))
            summary = summary.replaceAll("user", savePrefs.getFullName());

        holder.summary.setText(summary);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onDoubleTap(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, title, summary, time;
        ImageView flag;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            name = (TextView) mView.findViewById(R.id.name);
            title = (TextView) mView.findViewById(R.id.title);
            summary = (TextView) mView.findViewById(R.id.summary);
            time = (TextView) mView.findViewById(R.id.time);
            flag = (ImageView) mView.findViewById(R.id.flag);
        }
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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

                @Override
                public boolean onDoubleTap(MotionEvent e){
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onDoubleTap(child, recyclerView.getChildAdapterPosition(child));

                    }
                    return true;
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
}
