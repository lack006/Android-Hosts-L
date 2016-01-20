package com.lack006.hosts_l;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.lack006.hosts_l.rootcommands.util.Log;

/**
 * Created by Lack006 on 2015/10/10.
 */
public class Showchoose {
    static int umeng = 0;
    static int mybus = 0;
    static int jiajia = 0;
    static int tieba = 0;
    static int nj = 0;

    static int google_map = 0;


    public static void ADchoose(final ProgressDialog mpDialog, final Context context, final String hosts_url, final Button btn_AD, final Button btn_RE, final Button btn_AR) {

        final String[] arrayFruit = new String[]{context.getString(R.string.Shield_umeng), context.getString(R.string.Shield_mybus), context.getString(R.string.Shield_jiajia), context.getString(R.string.Tieba_baidu), context.getString(R.string.Shield_nj)};
        final boolean[] arrayFruitSelected = new boolean[]{false, false, false, false, false};


        new AlertDialog.Builder(context).setCancelable(false).setTitle(context.getString(R.string.AD_warning)).setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                arrayFruitSelected[which] = isChecked;
            }
        }).setPositiveButton(context.getString(R.string.AD_Continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrayFruitSelected.length; i++) {
                    if (arrayFruitSelected[i]) {


                        if (i == 0) {
                            umeng = 1;
                        }
                        if (i == 1) {
                            mybus = 1;
                        }
                        if (i == 2) {
                            jiajia = 1;

                        }
                        if (i == 3) {
                            tieba = 1;

                        }
                        if (i == 4) {
                            nj = 1;

                        }
                        stringBuilder.append(arrayFruit[i]).append("、");

                    }
                }
                Downloadhosts.Download(mpDialog, context, hosts_url, btn_AD, btn_RE, btn_AR, umeng, mybus, jiajia, tieba, nj);
                umeng = 0;
                mybus = 0;
                jiajia = 0;
                tieba = 0;
                nj = 0;
            }
        })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                }).show();

    }

    public static void REchoose(final ProgressDialog mpDialog, final Context context, final String hosts_url, final Button btn_AD, final Button btn_RE, final Button btn_AR) {

        final String[] arrayFruit = new String[]{context.getString(R.string.Google_map_only)};
        final boolean[] arrayFruitSelected = new boolean[]{false};


        new AlertDialog.Builder(context).setCancelable(false).setTitle(context.getString(R.string.RE_tip)).setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                arrayFruitSelected[which] = isChecked;
            }
        }).setPositiveButton(context.getString(R.string.AD_Continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrayFruitSelected.length; i++) {
                    if (arrayFruitSelected[i]) {

                        if (i == 0) {
                            google_map = 1;
                            Log.e("google_map", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

                        }


                        stringBuilder.append(arrayFruit[i] + "、");

                    }
                }
                if (google_map == 1) {
                    Log.e("google_map", "1");
                    google_map = 0;
                    Downloadhosts.Download(mpDialog, context, context.getString(R.string.URL_RE_map), btn_AD, btn_RE, btn_AR, 0, 0, 0, 0, 0);
                } else {
                    Log.e("google_map", "0");
                    Downloadhosts.Download(mpDialog, context, context.getString(R.string.URL_RE_full), btn_AD, btn_RE, btn_AR, 0, 0, 0, 0, 0);
                }
            }
        })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                }).show();

    }

    public static void ARchoose(final ProgressDialog mpDialog, final Context context, final String hosts_url, final Button btn_AD, final Button btn_RE, final Button btn_AR) {

        final String[] arrayFruit = new String[]{context.getString(R.string.Shield_umeng), context.getString(R.string.Shield_mybus), context.getString(R.string.Shield_jiajia), context.getString(R.string.Tieba_baidu), context.getString(R.string.Shield_nj)};
        final boolean[] arrayFruitSelected = new boolean[]{false, false, false, false, false};


        new AlertDialog.Builder(context).setCancelable(false).setTitle(context.getString(R.string.AD_warning)).setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                arrayFruitSelected[which] = isChecked;
            }
        }).setPositiveButton(context.getString(R.string.AD_Continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < arrayFruitSelected.length; i++) {
                    if (arrayFruitSelected[i]) {


                        if (i == 0) {
                            umeng = 1;
                        }
                        if (i == 1) {
                            mybus = 1;
                        }
                        if (i == 2) {
                            jiajia = 1;

                        }
                        if (i == 3) {
                            tieba = 1;

                        }
                        if (i == 4) {
                            nj = 1;

                        }
                        stringBuilder.append(arrayFruit[i]).append("、");

                    }
                }
                final String[] arrayFruit = new String[]{context.getString(R.string.Google_map_only)};
                final boolean[] arrayFruitSelected = new boolean[]{false};


                new AlertDialog.Builder(context).setCancelable(false).setTitle(context.getString(R.string.RE_tip)).setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        arrayFruitSelected[which] = isChecked;
                    }
                }).setPositiveButton(context.getString(R.string.AD_Continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < arrayFruitSelected.length; i++) {
                            if (arrayFruitSelected[i]) {

                                if (i == 0) {
                                    google_map = 1;
                                }


                                stringBuilder.append(arrayFruit[i]).append("、");

                            }
                        }
                        if (google_map == 1) {
                            google_map = 0;
                            Downloadhosts.Download(mpDialog, context, context.getString(R.string.URL_AR_map), btn_AD, btn_RE, btn_AR, umeng, mybus, jiajia, tieba, nj);
                            umeng = 0;
                            mybus = 0;
                            jiajia = 0;
                            tieba = 0;
                            Log.e("ADdddddddddd","0");
                            nj = 0;
                        } else {

                            Downloadhosts.Download(mpDialog, context, context.getString(R.string.URL_AR_full), btn_AD, btn_RE, btn_AR, umeng, mybus, jiajia, tieba, nj);
                            umeng = 0;
                            mybus = 0;
                            jiajia = 0;
                            tieba = 0;
                            nj = 0;
                        }
                    }
                })
                        .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        }).show();

            }
        })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }
                }).show();

    }

}
