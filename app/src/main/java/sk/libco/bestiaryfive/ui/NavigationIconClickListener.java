package sk.libco.bestiaryfive.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import sk.libco.bestiaryfive.R;

/**
 * {@link android.view.View.OnClickListener} used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
public class NavigationIconClickListener implements View.OnClickListener {

    private final AnimatorSet animatorSet = new AnimatorSet();
    private Context context;
    private View sheet;
    private View backdropSheet;
    private Interpolator interpolator;
    private int height;
    private boolean backdropShown = false;
    private Drawable openIcon;
    private Drawable closeIcon;

//    NavigationIconClickListener(Context context, View sheet, Vi) {
//        this(context, sheet, null);
//    }

//    NavigationIconClickListener(Context context, View sheet, View backdropSheet, @Nullable Interpolator interpolator) {
//        this(context, sheet, interpolator, null, null);
//    }

    NavigationIconClickListener(
            Context context, View sheet, View backdropSheet, @Nullable Interpolator interpolator,
            @Nullable Drawable openIcon, @Nullable Drawable closeIcon) {
        this.context = context;
        this.sheet = sheet;
        this.backdropSheet = backdropSheet;
        this.interpolator = interpolator;
        this.openIcon = openIcon;
        this.closeIcon = closeIcon;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View view) {
        backdropShown = !backdropShown;

        // Cancel the existing animations
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        if(view != null) {
            updateIcon(view);
        }



        ///////////

        try {
            ImageView b = this.sheet.findViewById(R.id.button_go_up);
            if(b != null) {
                if (backdropShown) {
                    b.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                } else {
                    b.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                }
            }
        } catch (Exception ignored) {}


        ////////


        final int translateY = height -
                context.getResources().getDimensionPixelSize(R.dimen.backdrop_reveal_height);
                //- this.backdropSheet.getHeight();

//        final int translateY = height - (int)sheet.getY() ;

        //final int translateY = height - this.backdropSheet.getHeight();

        ObjectAnimator animator = ObjectAnimator.ofFloat(sheet, "translationY", backdropShown ? translateY : 0);
        animator.setDuration(500);
        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }
        animatorSet.play(animator);
        animator.start();

        //TODO: show how many results we have from filtering

        //sheet.findViewById()
    }

    private void updateIcon(View view) {
        if (openIcon != null && closeIcon != null) {
            if (!(view instanceof ImageView)) {
                throw new IllegalArgumentException("updateIcon() must be called on an ImageView");
            }
            if (backdropShown) {
                ((ImageView) view).setImageDrawable(closeIcon);
            } else {
                ((ImageView) view).setImageDrawable(openIcon);
            }
        }
    }

    public boolean isBackdropShown() {
        return backdropShown;
    }
}
