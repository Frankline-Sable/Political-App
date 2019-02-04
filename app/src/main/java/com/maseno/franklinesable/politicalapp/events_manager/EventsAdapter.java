package com.maseno.franklinesable.politicalapp.events_manager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;

import java.util.List;

/**
 * Created by Frankline Sable on 10/02/2017.
 */

public class EventsAdapter extends ArrayAdapter<EventsContents> {

    private List<EventsContents> eventsList;
    private Context mContext;
    LayoutInflater inflater;
    private Color eventColor;
    private SparseBooleanArray mSelectedItemsIds;
    private List<ItemObject> listItemStorage;

    public EventsAdapter(Context context, int resource, List<EventsContents> eventsList) {
        super(context, resource, eventsList);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
        this.eventsList = eventsList;
        inflater = LayoutInflater.from(context);
      //  roboto_Font = Typeface.createFromAsset(mContext.getAssets(), "HelveticaNeue Medium.ttf")listItemStorage=eventsList;
    }

    public class ViewHolder {
        public TextView title, subtitle, time, dayOfMonth, dayOfWeek;
        private CardView cardView;
        private ImageView notification_Icon;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.cardview_events, null);

            holder.notification_Icon=(ImageView)view.findViewById(R.id.notification_Icon);
            holder.title = (TextView) view.findViewById(R.id.event_Title);
            holder.subtitle = (TextView) view.findViewById(R.id.events_Subtitle);
            holder.time = (TextView) view.findViewById(R.id.eventTime);
            holder.dayOfMonth = (TextView) view.findViewById(R.id.dayOfMonth);
            holder.dayOfMonth.setTextColor(mContext.getResources().getColor(android.R.color.white));
            holder.dayOfWeek = (TextView) view.findViewById(R.id.dayOfWeek);
            holder.notification_Icon=(ImageView)view.findViewById(R.id.notification_Icon);
            holder.cardView=(CardView)view.findViewById(R.id.partOne);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        EventsContents events = eventsList.get(position);
//      lcdcholder.title.setTextColor(mContext.getResources().getColor(events.getEventColor()));
        holder.title.setText(events.getTitle());
        holder.subtitle.setText(events.getSubtitle());
        holder.dayOfMonth.setText(events.getDayOfMonth());
        holder.dayOfWeek.setText(events.getDayOfWeek());
        holder.time.setText(events.getTimeView());
        holder.notification_Icon.setColorFilter(events.getBellColor());

        holder.dayOfMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void remove(EventsContents object) {
        eventsList.remove(object);
        notifyDataSetChanged();
    }

    public List<EventsContents> getMyList() {
        return eventsList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }
    // Remove selection after unchecked

    public void removeSelection() {

        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {

        if (value)
            mSelectedItemsIds.put(position, value);

        else

            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();

    }

    public int getSelectedCount() {

        return mSelectedItemsIds.size();

    }

    public SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;
   }
}
