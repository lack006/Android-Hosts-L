package com.lack006.hosts_l.About;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lack006.hosts_l.Consistent.ConsistentHtml;
import com.lack006.hosts_l.R;

/**
 * Created by lack on 2016/12/11.
 * AndroidHosts-LV7
 */

public class AboutHelper {
    private Context mContext = null;

    public void about(Context context) {
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.update_log);
        LayoutInflater factory = LayoutInflater.from(mContext);
        View layout = factory.inflate(R.layout.about_dialog_layout, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setText(R.string.update_logs);
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(R.string.explanation, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                explanation();
            }
        });
        builder.setView(layout);
        builder.show();
    }

    private void explanation() {


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.explanation);

        LayoutInflater factory = LayoutInflater.from(mContext);
        View layout = factory.inflate(R.layout.about_dialog_layout, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(ConsistentHtml.EXPLANATION_HTML));
        } else {
            text.setText(Html.fromHtml(ConsistentHtml.EXPLANATION_HTML, Html.FROM_HTML_MODE_LEGACY));
        }
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setView(layout);
        builder.show();

    }
}
