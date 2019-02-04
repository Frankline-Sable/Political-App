package com.maseno.franklinesable.politicalapp.welcome_package;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;
import com.maseno.franklinesable.politicalapp.sharedmethods.TypefaceHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_1 extends Fragment {

    private ImageView cloudImage, courthouseImage, eventImage, forumImage, groupImage, parliamentImage, voting_hand;

    public Fragment_1() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        TypefaceHandler tp = new TypefaceHandler(getActivity());
        TextView addInfo = (TextView) view.findViewById(R.id.viewMore);
        TextView welcomeView = (TextView) view.findViewById(R.id.welcome);
        TextView appName = (TextView) view.findViewById(R.id.appName);
        cloudImage = (ImageView) view.findViewById(R.id.cloud_img);
        eventImage = (ImageView) view.findViewById(R.id.event_img);
        courthouseImage = (ImageView) view.findViewById(R.id.court_house_img);
        parliamentImage = (ImageView) view.findViewById(R.id.parliament_img);
        forumImage = (ImageView) view.findViewById(R.id.forum_img);
        groupImage = (ImageView) view.findViewById(R.id.people_group_img);
        voting_hand = (ImageView) view.findViewById(R.id.voting_hand);

        addInfo.setTypeface(tp.setTp("Segoe UI Light.ttf"));
        welcomeView.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        appName.setTypeface(tp.setTp("Helvetica Neue Light (Open Type).ttf"));
        return view;
    }

    private void animateView() {
        Animation itemAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        cloudImage.startAnimation(itemAnimation);
        eventImage.startAnimation(itemAnimation);
        courthouseImage.startAnimation(itemAnimation);
        parliamentImage.startAnimation(itemAnimation);
        forumImage.startAnimation(itemAnimation);
        groupImage.startAnimation(itemAnimation);

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.blinky_fragment1);
        ArrayList<Animator> animations = set.getChildAnimations();
        for (Animator animator : animations) {
            if (animator instanceof ObjectAnimator) {
                ObjectAnimator anim = (ObjectAnimator) animator;
                if (anim.getPropertyName().compareTo("backgroundColor") == 0) {
                    anim.setEvaluator(new ArgbEvaluator());
                }
            }
        }
        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.layout);
        set.setTarget(layout);
        set.setInterpolator(new LinearInterpolator());
        set.start();
    }

    public void animateBackground(int pos) {
        eventImage.setTranslationY(0 - pos);
        groupImage.setTranslationY(0 - pos);
        groupImage.setTranslationX(0 - pos);
        parliamentImage.setTranslationY(0 - pos);
        parliamentImage.setTranslationX(0 - pos);
        courthouseImage.setTranslationX(0 - pos);
        courthouseImage.setTranslationY(0 - pos);
        if (pos < 100)
            cloudImage.setTranslationY(0 - pos);
        if (pos < 200)
            forumImage.setTranslationY(0 - pos);
        if (pos < 150)
            voting_hand.setTranslationY(0 - pos);
    }
}
