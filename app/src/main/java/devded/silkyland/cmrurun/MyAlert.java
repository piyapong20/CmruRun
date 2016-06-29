package devded.silkyland.cmrurun;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by silkyland on 6/27/16 AD.
 */
public class MyAlert {

    public void myDialog(Context context, String strTitle, String strMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //ไม่สามารถกด Undo ได้
        builder.setCancelable(false);
        builder.setIcon(R.drawable.danger);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ปิดไปเฉยๆ
                dialog.dismiss();
            }
        });
        builder.show();

    }

} // Main Class
