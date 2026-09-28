package main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.aguhan.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;

import database.AccountType;
import database.DatabaseManager;
import database.ErrorArr;
import session.GradeLevel;
import session.TransactionType;
import session.ViewType;

public class util {
    public static boolean isDashboardStarted = false;
    public static Fragment currentFragment =  null;
    public static String toTitleCase(String s) {

        final String ACTIONABLE_DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static boolean isPasswordMatch(String password, String confirmPassword)
    {
        if(password.equals(confirmPassword))
            return true;
        else
            return false;
    }
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public static Bitmap getRoundedCornerBitmap(Context c, Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        pixels = 50;
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pxToDp(c, pixels);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newEdge = Math.min(width, height);

        int xOffset = (width - newEdge) / 2;
        int yOffset = (height - newEdge) / 2;

        return Bitmap.createBitmap(bitmap, xOffset, yOffset, newEdge, newEdge);
    }
    public static int pxToDp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
    public static TableRow createStudentRow(Context context, String[]rows, String id, String firstName, AccountType accountType) {
        TableRow row = new TableRow(context);

        int horizontal = (int) (16 * context.getResources().getDisplayMetrics().density);
        int vertical = (int) (12 * context.getResources().getDisplayMetrics().density);
        row.setPadding(horizontal, vertical, horizontal, vertical);

        TextView nameView = new TextView(context);
        nameView.setText(rows[0]);
        nameView.setGravity(Gravity.CENTER);
        nameView.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_START);
        nameView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        nameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));

        TextView gradeView = new TextView(context);
        TextView sectionView = new TextView(context);
        if(accountType == AccountType.STUDENT || accountType == AccountType.TEACHER)
        {
            gradeView.setText(rows[1]);
            gradeView.setGravity(Gravity.START);
            gradeView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            gradeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        }
        if(accountType == AccountType.STUDENT)
        {
            sectionView.setText(rows[2]);
            sectionView.setGravity(Gravity.START);
            sectionView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            sectionView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
        row.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("accountType", accountType);
            intent.putExtra("viewType", ViewType.ACCOUNT);
            intent.putExtra("firstName", firstName);
            context.startActivity(intent);
        });

        row.addView(nameView);
        if(accountType == AccountType.STUDENT || accountType == AccountType.TEACHER)
            row.addView(gradeView);
        if(accountType == AccountType.STUDENT)
            row.addView(sectionView);


        return row;
    }
    public static TableRow createHistoryRow(Context context, String firstName, String rows[], String id, ViewType viewType, TransactionType transactionType) {
        TableRow row = new TableRow(context);

        int horizontal = (int) (16 * context.getResources().getDisplayMetrics().density);
        int vertical = (int) (12 * context.getResources().getDisplayMetrics().density);
        row.setPadding(horizontal, vertical, horizontal, vertical);

        /*TextView nameView = new TextView(context);
        nameView.setText(rows[0]);
        nameView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        nameView.setGravity(Gravity.CENTER);
        nameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        nameView.setTextAppearance(context, R.style.Base_Theme_Aguhan_Font_SmallCardText);*/

        TextView dateView = new TextView(context);
        dateView.setText(rows[1]);
        dateView.setTextColor(ContextCompat.getColor(context, R.color.muted));
        dateView.setGravity(Gravity.CENTER);
        dateView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dateView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        dateView.setTextAppearance(context, R.style.Base_Theme_Aguhan_Font_SmallCardText);

        TextView typeView = new TextView(context);
        typeView.setText(rows[2]);
        typeView.setTextColor(ContextCompat.getColor(context, R.color.orange));
        typeView.setGravity(Gravity.CENTER);
        typeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        typeView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
        typeView.setTextAppearance(context, R.style.Base_Theme_Aguhan_Font_SmallCardText);
        if(transactionType == TransactionType.CREATE)
            typeView.setTextColor(ContextCompat.getColor(context, R.color.green));
        else if(transactionType == TransactionType.EDIT)
            typeView.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        else if(transactionType == TransactionType.DELETE)
            typeView.setTextColor(ContextCompat.getColor(context, R.color.red));

        TextView actionView = new TextView(context);
        actionView.setText("View");
        actionView.setTextColor(ContextCompat.getColor(context, R.color.accent));
        actionView.setTypeface(null, Typeface.BOLD);
        actionView.setGravity(Gravity.CENTER);
        actionView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        actionView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f));
        actionView.setTextAppearance(context, R.style.Base_Theme_Aguhan_Font_SmallCardText);

        //row.addView(nameView);
        row.addView(dateView);
        row.addView(typeView);

        return row;
    }
    public static TableRow createScheduleRow(Context context, String[] rows, String id) {

        TableRow tableRow = new TableRow(context);

        int horizontal = (int) (16 * context.getResources().getDisplayMetrics().density);
        int vertical = (int) (12 * context.getResources().getDisplayMetrics().density);

        tableRow.setPadding(horizontal, vertical, horizontal, vertical);


        TextView tvSubject = new TextView(context);
        tvSubject.setText(rows[0]);
        tvSubject.setTextColor(Color.BLACK);
        tvSubject.setGravity(Gravity.CENTER);
        tvSubject.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvSubject.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvSubject.setTextSize(14);

        TextView tvTeacher = new TextView(context);
        tvTeacher.setText(rows[1]);
        tvTeacher.setTextColor(Color.BLACK);
        tvTeacher.setGravity(Gravity.CENTER);
        tvTeacher.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvTeacher.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvTeacher.setTextSize(14);

        TextView tvTime = new TextView(context);
        tvTime.setText(rows[2]);
        tvTime.setTextColor(context.getResources().getColor(android.R.color.black));
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvTime.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvTime.setTextSize(14);

        TextView tvDay = new TextView(context);
        String[] daysOfWeekArr = rows[3].split(",");
        String daysOfWeekFormatted = "";
        for(int i=0; i< daysOfWeekArr.length; i++)
        {
            if(i== daysOfWeekArr.length-1)
                daysOfWeekFormatted+=daysOfWeekArr[i].trim();
            else
                daysOfWeekFormatted+=daysOfWeekArr[i].trim()+"\n";
        }

        tvDay.setText(daysOfWeekFormatted);
        tvDay.setGravity(Gravity.CENTER);
        tvDay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvDay.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        tvDay.setTextSize(14);

        tableRow.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("viewType", ViewType.SCHEDULE);
            context.startActivity(intent);
        });

        tableRow.addView(tvSubject);
        tableRow.addView(tvTeacher);
        tableRow.addView(tvTime);
        tableRow.addView(tvDay);

        return tableRow;

    }
    public static TableRow createAnnouncementRow(Context context, String title, String targetGrade, String id) {

        TableRow tableRow = new TableRow(context);
        int paddingH = (int) (16 * context.getResources().getDisplayMetrics().density);
        int paddingV = (int) (12 * context.getResources().getDisplayMetrics().density);
        tableRow.setPadding(paddingH, paddingV, paddingH, paddingV);

        TextView tvTitle = new TextView(context);
        TableRow.LayoutParams lpTitle = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f);
        tvTitle.setLayoutParams(lpTitle);
        tvTitle.setText(title);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setTextSize(14);

        TextView tvGrade = new TextView(context);
        TableRow.LayoutParams lpGrade = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);
        tvGrade.setLayoutParams(lpGrade);
        tvGrade.setText(targetGrade);
        tvGrade.setTextColor(Color.BLACK);
        tvGrade.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvGrade.setTextSize(14);

        tableRow.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateAnnouncementActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("transactionType", TransactionType.EDIT);
            context.startActivity(intent);
        });

        tableRow.addView(tvTitle);
        tableRow.addView(tvGrade);

        return tableRow;
    }


    public static View createDivider(Context context) {
        View divider = new View(context);
        divider.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                (int) (1 * context.getResources().getDisplayMetrics().density)
        ));
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.muted));
        return divider;
    }
    public static ErrorArr isAnyEmpty(Context c,EditText[] editTexts) {
        ErrorArr error = new ErrorArr(true, "");
        for (EditText et : editTexts) {
            if (et == null || et.getText().toString().trim().isEmpty()) {
                error.success = false;
                int id = et.getId();
                String idString = c.getResources().getResourceEntryName(id);
                error.error+=idString.substring(2) +'\n';
            }
        }
        return error;
    }
    public static AlertDialog.Builder makeAlert(Context c, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title)
                .setMessage(message);
        return builder;
    }
    public static Bitmap loadCorrectly(Context context, Uri uri) throws IOException {
        // 1. Decode stream
        InputStream input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        input.close();

        // 2. Fix rotation using EXIF
        InputStream exifStream = context.getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(exifStream);
        exifStream.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
