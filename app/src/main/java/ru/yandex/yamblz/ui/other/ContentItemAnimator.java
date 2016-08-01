package ru.yandex.yamblz.ui.other;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.List;

import ru.yandex.yamblz.ui.fragments.ContentAdapter;

/**
 * Created by shmakova on 28.07.16.
 */

public class ContentItemAnimator extends DefaultItemAnimator {
    private AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator();
    private DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private ArgbEvaluator colorEvaluator = new ArgbEvaluator();

    private ArrayMap<RecyclerView.ViewHolder, AnimatorInfo> mAnimatorMap = new ArrayMap<>();

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    private ItemHolderInfo getItemHolderInfo(ContentAdapter.ContentHolder viewHolder, ColorTextInfo info) {
        final ContentAdapter.ContentHolder myHolder = viewHolder;
        final int bgColor = ((ColorDrawable) myHolder.itemView.getBackground()).getColor();
        info.color = bgColor;
        info.text = (String) ((TextView) myHolder.itemView).getText();
        return info;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(RecyclerView.State state,
                                                     RecyclerView.ViewHolder viewHolder, int changeFlags, List<Object> payloads) {
        ColorTextInfo info = (ColorTextInfo) super.recordPreLayoutInformation(state, viewHolder,
                changeFlags, payloads);
        return getItemHolderInfo((ContentAdapter.ContentHolder) viewHolder, info);
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPostLayoutInformation(@NonNull RecyclerView.State state,
                                                      @NonNull RecyclerView.ViewHolder viewHolder) {
        ColorTextInfo info = (ColorTextInfo) super.recordPostLayoutInformation(state, viewHolder);
        return getItemHolderInfo((ContentAdapter.ContentHolder) viewHolder, info);
    }

    @Override
    public ItemHolderInfo obtainHolderInfo() {
        return new ColorTextInfo();
    }

    private class ColorTextInfo extends ItemHolderInfo {
        int color;
        String text;
    }

    private class AnimatorInfo {
        Animator overallAnim;
        ObjectAnimator fadeToBlackAnim, fadeFromBlackAnim, oldTextRotator, newTextRotator;

        public AnimatorInfo(Animator overallAnim,
                            ObjectAnimator fadeToBlackAnim, ObjectAnimator fadeFromBlackAnim,
                            ObjectAnimator oldTextRotator, ObjectAnimator newTextRotator) {
            this.overallAnim = overallAnim;
            this.fadeToBlackAnim = fadeToBlackAnim;
            this.fadeFromBlackAnim = fadeFromBlackAnim;
            this.oldTextRotator = oldTextRotator;
            this.newTextRotator = newTextRotator;
        }
    }


    @Override
    public boolean animateChange(@NonNull final RecyclerView.ViewHolder oldHolder,
                                 @NonNull final RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {

        if (oldHolder != newHolder) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
        }

        final ContentAdapter.ContentHolder viewHolder = (ContentAdapter.ContentHolder) newHolder;

        ColorTextInfo oldInfo = (ColorTextInfo) preInfo;
        ColorTextInfo newInfo = (ColorTextInfo) postInfo;
        int oldColor = oldInfo.color;
        int newColor = newInfo.color;
        final String oldText = oldInfo.text;
        final String newText = newInfo.text;

        View newContainer = viewHolder.itemView;
        final TextView newTextView = (TextView) viewHolder.itemView;

        AnimatorInfo runningInfo = mAnimatorMap.get(newHolder);
        long prevAnimPlayTime = 0;
        boolean firstHalf = false;
        if (runningInfo != null) {
            firstHalf = runningInfo.oldTextRotator != null &&
                    runningInfo.oldTextRotator.isRunning();
            prevAnimPlayTime = firstHalf ?
                    runningInfo.oldTextRotator.getCurrentPlayTime() :
                    runningInfo.newTextRotator.getCurrentPlayTime();
            runningInfo.overallAnim.cancel();
        }

        ObjectAnimator fadeToBlack = null, fadeFromBlack;
        if (runningInfo == null || firstHalf) {
            int startColor = oldColor;
            if (runningInfo != null) {
                startColor = (Integer) runningInfo.fadeToBlackAnim.getAnimatedValue();
            }
            fadeToBlack = ObjectAnimator.ofInt(newContainer, "backgroundColor",
                    startColor, Color.BLACK);
            fadeToBlack.setEvaluator(colorEvaluator);
            if (runningInfo != null) {
                fadeToBlack.setCurrentPlayTime(prevAnimPlayTime);
            }
        }

        fadeFromBlack = ObjectAnimator.ofInt(newContainer, "backgroundColor",
                Color.BLACK, newColor);
        fadeFromBlack.setEvaluator(colorEvaluator);
        if (runningInfo != null && !firstHalf) {
            fadeFromBlack.setCurrentPlayTime(prevAnimPlayTime);
        }

        AnimatorSet bgAnim = new AnimatorSet();
        if (fadeToBlack != null) {
            bgAnim.playSequentially(fadeToBlack, fadeFromBlack);
        } else {
            bgAnim.play(fadeFromBlack);
        }

        ObjectAnimator oldTextRotate = null, newTextRotate;

        if (runningInfo == null || firstHalf) {
            oldTextRotate = ObjectAnimator.ofFloat(newTextView, View.ROTATION_X, 0, 90);
            oldTextRotate.setInterpolator(mAccelerateInterpolator);

            if (runningInfo != null) {
                oldTextRotate.setCurrentPlayTime(prevAnimPlayTime);
            }
            oldTextRotate.addListener(new AnimatorListenerAdapter() {
                boolean mCanceled = false;

                @Override
                public void onAnimationStart(Animator animation) {
                    newTextView.setText(oldText);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCanceled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mCanceled) {
                        newTextView.setText(newText);
                    }
                }
            });
        }

        newTextRotate = ObjectAnimator.ofFloat(newTextView, View.ROTATION_X, -90, 0);
        newTextRotate.setInterpolator(mDecelerateInterpolator);
        if (runningInfo != null && !firstHalf) {
            newTextRotate.setCurrentPlayTime(prevAnimPlayTime);
        }

        AnimatorSet textAnim = new AnimatorSet();
        if (oldTextRotate != null) {
            textAnim.playSequentially(oldTextRotate, newTextRotate);
        } else {
            textAnim.play(newTextRotate);
        }

        AnimatorSet changeAnim = new AnimatorSet();
        changeAnim.playTogether(bgAnim, textAnim);
        changeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAnimationFinished(newHolder);
                mAnimatorMap.remove(newHolder);
            }
        });
        changeAnim.start();

        AnimatorInfo runningAnimInfo = new AnimatorInfo(changeAnim, fadeToBlack, fadeFromBlack,
                oldTextRotate, newTextRotate);
        mAnimatorMap.put(newHolder, runningAnimInfo);

        return true;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        if (!mAnimatorMap.isEmpty()) {
            final int numRunning = mAnimatorMap.size();
            for (int i = numRunning; i >= 0; i--) {
                if (item == mAnimatorMap.keyAt(i)) {
                    mAnimatorMap.valueAt(i).overallAnim.cancel();
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return super.isRunning() || !mAnimatorMap.isEmpty();
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        if (!mAnimatorMap.isEmpty()) {
            final int numRunning = mAnimatorMap.size();
            for (int i = numRunning; i >= 0; i--) {
                mAnimatorMap.valueAt(i).overallAnim.cancel();
            }
        }
    }
}
