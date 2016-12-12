package com.lack006.hosts_l.Helper;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by lack006 on 2016/9/17.
 * AndroidHosts-LV7
 */
public class ObjectAnimatorHelper {
    public static void ShowView(View view, int duration, int times) {
        view.setRotation(0);
        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofFloat(View.ROTATION, 180);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolder);
        objectAnimator.setRepeatCount(times);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();


    }
}
