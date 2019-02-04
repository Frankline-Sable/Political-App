package com.maseno.franklinesable.politicalapp.welcome_package;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_5 extends Fragment {

AnimatorSet set;
    public Fragment_5() {
        // Required empty public constructor

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.blinky_fragment5);
        ArrayList<Animator> animations = set.getChildAnimations();
        for (Animator animator : animations) {
            if (animator instanceof ObjectAnimator) {
                ObjectAnimator anim = (ObjectAnimator) animator;
                if (anim.getPropertyName().compareTo("backgroundColor") == 0) {
                    anim.setEvaluator(new ArgbEvaluator());
                }
            }
        }
        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.layout5);
        set.setTarget(layout);
        set.setInterpolator(new LinearInterpolator());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_5, container, false);
        TextView txtView=(TextView)view.findViewById(R.id.userAgreementView);//"Or <font color='#FF5733'>Sign up</font>";
        String signUp_Tint = "If you want to read more about the Political App, have a look at the <a href=\"http://www.franklinesable.blogspot.co.ke\">user guide</a>";
        txtView.setClickable(true);
        txtView.setMovementMethod(LinkMovementMethod.getInstance());
        txtView.setText(Html.fromHtml(signUp_Tint));
        //"If you want to read more about the Political App, have a look at the user guide";//

        //"Or <font color='#FF5733'>Sign up</font>";
        //signUpView.setText(Html.fromHtml(signUp_Tint));
        return view;
    }
    public void animateView() {
        set.start();
    }

}
