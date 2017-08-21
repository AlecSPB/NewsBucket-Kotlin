package com.angad.newsbucket.animations

import android.annotation.TargetApi
import android.os.Build
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.animation.AnticipateOvershootInterpolator

/**
 * Created by angad.tiwari on 21-Aug-17.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class DetailTransition : TransitionSet {

    constructor(duration:Long, delay: Long){
        setOrdering(ORDERING_TOGETHER)
        addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(ChangeImageTransform()).setDuration(duration).setStartDelay(delay).setInterpolator(AnticipateOvershootInterpolator())
    }
}