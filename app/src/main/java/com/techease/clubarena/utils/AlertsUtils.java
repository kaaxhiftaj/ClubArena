package com.techease.clubarena.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.techease.clubarena.R;
//import com.techease.clubarena.ui.widgets.LatoBoldTextView;




public class AlertsUtils {


    /**
     * add new patient
     *
     * @param activity
     */
    public static void showErrorDialog(Activity activity, String message) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
    //    final View dialogView = inflater.inflate(R.layout.alert_error_dialog_view, null);
     //   dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
    //    LatoBoldTextView tvError = dialogView.findViewById(R.id.tv_error);
    //    tvError.setText(message);
    //    Button btnOk = dialogView.findViewById(R.id.btn_ok);
    //    btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//        alertDialog.show();
    }
}
