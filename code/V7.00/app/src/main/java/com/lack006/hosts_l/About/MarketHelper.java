package com.lack006.hosts_l.About;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lack006.hosts_l.Consistent.Consistent;

/**
 * Created by lack on 2016/12/11.
 * AndroidHosts-LV7
 */

public class MarketHelper {
    public void openApplicationMarket(Context context) {
//        Toast.makeText(context, context.getString(R.string.market_hint), Toast.LENGTH_LONG).show();
        if (Consistent.PLAY_VERSION) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uri = Consistent.MARKET_URI + context.getPackageName();
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        } else {
            openLinkBySystem(context);
        }

    }

    private void openLinkBySystem(Context context) {
        String url = Consistent.PROJECT_URL;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
