package neon.desafio.banktransfer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import neon.desafio.banktransfer.event.ErrorEvent;

@EActivity
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            Log.i(this.toString(), "Unregistered EventBus");
        }

        super.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        try {
            EventBus.getDefault().unregister(this);
        } finally {
            super.onDestroy();
        }
    }

    protected void handleErrorAndShouldContinueToDefault(ErrorEvent errorEvent) { }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent) {
        handleErrorAndShouldContinueToDefault(errorEvent);
    }
}
