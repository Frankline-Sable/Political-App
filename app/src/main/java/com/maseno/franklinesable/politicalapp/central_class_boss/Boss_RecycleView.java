package com.maseno.franklinesable.politicalapp.central_class_boss;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.services.ModuleFetchService;

import java.util.List;

/**
 * Created by Frankline Sable on 05/02/2017.
 */

public class Boss_RecycleView extends RecyclerView.Adapter<Boss_RecycleView.Boss_ViewHolder>  {

    private List<Boss_Components> componentsList;
    private Context mContext;

    public class Boss_ViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleView, subtitleView, countView;
        protected ImageButton boss_Icon;

        public Boss_ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.boss_title);
            subtitleView = (TextView) v.findViewById(R.id.boss_subtitle);
            countView = (TextView) v.findViewById(R.id.updateCount);
            boss_Icon = (ImageButton) v.findViewById(R.id.bossIcon);
        }
    }

    public Boss_RecycleView(List<Boss_Components> componentsList, Context mContext) {
        this.componentsList = componentsList;
        this.mContext = mContext;
    }

    @Override
    public Boss_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_boss, parent, false);

        return new Boss_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Boss_ViewHolder holder, int position) {
        Boss_Components bossFetch = componentsList.get(position);
        holder.titleView.setText(bossFetch.getBoss_Title());
        holder.subtitleView.setText(bossFetch.getBoss_Post());

        if(bossFetch.getPost_Count()!=0){
            holder.countView.setText(" "+bossFetch.getPost_Count()+" ");
            holder.countView.setVisibility(View.VISIBLE);
        }else {
            holder.countView.setText(" "+bossFetch.getPost_Count()+" ");
            holder.countView.setVisibility(View.GONE);
        }
        holder.boss_Icon.setImageResource(bossFetch.getBoss_Icon());
        holder.boss_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
               // BounceInterpolator interpolator = new BounceInterpolator(0.2, 5);
                anim.setInterpolator(mContext, android.R.interpolator.bounce);
                view.startAnimation(anim);
            }
        });
        IntentFilter filter = new IntentFilter(ModuleFetchService.ACTION_REFRESH_MODULES);
       // LocalBroadcastManager.getInstance(mContext).registerReceiver(eventsReceiver, filter);
    }
   /* private BroadcastReceiver eventsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
                Toast.makeText(mContext, resultValue, Toast.LENGTH_SHORT).show();
                //Log.i("meh","alarm meia asis22222");
            }
        }
    };*/

    @Override
    public int getItemCount() {
        return componentsList.size();
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
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
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
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


}
