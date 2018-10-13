package nl.dronexpert.wildfiremapper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;

import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.dronexpert.wildfiremapper.R;

public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.getDefault()).format(new Date());
    }

    public static Bitmap iconicsDrawableToBitmap(IconicsDrawable iconicsDrawable) {
        final Bitmap bitmap = Bitmap.createBitmap(iconicsDrawable.getIntrinsicWidth(),
                iconicsDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(bitmap);
        iconicsDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        iconicsDrawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap rotateBitmap(final Bitmap bitmap, float degrees) {
        Matrix matrix  = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(bitmap,
                0, 0,
                bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }
}
