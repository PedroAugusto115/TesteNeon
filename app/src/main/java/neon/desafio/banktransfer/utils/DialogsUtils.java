package neon.desafio.banktransfer.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import neon.desafio.banktransfer.R;

public class DialogsUtils {

    public static AlertDialog newDialog(Context context, int layoutID, int backgroundResource) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(layoutID)
                .show();

        if (null != alertDialog.getWindow())
            alertDialog.getWindow().setBackgroundDrawableResource(backgroundResource);

        return alertDialog;
    }

    public static void newDialog(Context context, String title, String message, String positiveButton) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, null)
                .show();

        setButtonParams(alertDialog.getButton(AlertDialog.BUTTON_POSITIVE));
    }

    private static void setButtonParams(Button button) {
        if (null == button) return;
        button.setAllCaps(true);
    }

}
