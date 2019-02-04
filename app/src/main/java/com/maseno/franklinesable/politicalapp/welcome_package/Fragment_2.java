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
import android.widget.RelativeLayout;

import com.maseno.franklinesable.politicalapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_2 extends Fragment {
    AnimatorSet set;

    public Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.blinky_fragment2);
        ArrayList<Animator> animations = set.getChildAnimations();
        for (Animator animator : animations) {
            if (animator instanceof ObjectAnimator) {
                ObjectAnimator anim = (ObjectAnimator) animator;
                if (anim.getPropertyName().compareTo("backgroundColor") == 0) {
                    anim.setEvaluator(new ArgbEvaluator());
                }
            }
        }
        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.layout2);
        set.setTarget(layout);
        set.setInterpolator(new LinearInterpolator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }
    public void animateView() {
        set.start();
    }


}
