package com.maseno.franklinesable.politicalapp.sharedmethods;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.maseno.franklinesable.politicalapp.BuildConfig;
import com.maseno.franklinesable.politicalapp.R;

import java.util.zip.Inflater;

/**
 * Created by Frankline Sable on 25/03/2017.
 */

public class simple_about_dialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.dialog_about,null);

        TextView versionLabel=(TextView) dialogView.findViewById(R.id.versionLabel);
        versionLabel.setText(getString(R.string.versionLabel, BuildConfig.VERSION_NAME));
        builder.setView(dialogView)
                .setCancelable(true);
        return builder.create();
    }
}
