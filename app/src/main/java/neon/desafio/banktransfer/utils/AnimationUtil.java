package neon.desafio.banktransfer.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

public class AnimationUtil {

    public static void shakeView(final View view) {
        ObjectAnimator physX = ObjectAnimator.ofFloat(view, "translationX", -12f, 12f);
        physX.setDuration(90);
        physX.setRepeatCount(4);
        physX.setRepeatMode(ObjectAnimator.RESTART);
        physX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().translationX(0f).setDuration(10);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        physX.start();
        try {
            Vibrator v = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(350);
        }catch (Exception e){
            Log.e("ShakeView", "erro ao tentar recuperar o serviço de vibração", e);
        }
    }

}
