package com.maseno.franklinesable.politicalapp.notifications_package;

/**
 * Created by Frankline Sable on 26/02/2017. from 19:02
 * at Maseno University @ Home
 * Project PoliticalAppEp
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

public class _notification_adapters extends RecyclerView.Adapter<_notification_adapters.ViewHolder> {

    private List<_notification_contents> viewNotesList;

    public _notification_adapters(List<_notification_contents> viewNotesList) {
        this.viewNotesList = viewNotesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notification_view, parent, false);
        // TODO set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        _notification_contents notes = viewNotesList.get(position);
        holder.senderText.setText(notes.getSenderMessage());
        holder.senderTime.setText(notes.getSenderTime());
        holder.recipientText.setText(notes.getRecipientMessage());
        holder.recipientTime.setText(notes.getRecipientTime());

        if(notes.getSenderMessage()==null) {
            holder.senderCard.setVisibility(View.GONE);
        }
        else if(notes.getRecipientMessage()==null){
            holder.recipientCard.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return viewNotesList.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView senderText, senderTime, recipientText, recipientTime;
        private View recipientCard,senderCard;
        public ViewHolder(View itemView) {
            super(itemView);
            senderText = (TextView) itemView.findViewById(R.id.senderText);
            senderTime = (TextView) itemView.findViewById(R.id.senderTime);
            recipientText= (TextView) itemView.findViewById(R.id.recipientText);
            recipientTime = (TextView) itemView.findViewById(R.id.recipientTime);
            recipientCard=itemView.findViewById(R.id.recipientCard);
            senderCard=itemView.findViewById(R.id.senderCard);
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
