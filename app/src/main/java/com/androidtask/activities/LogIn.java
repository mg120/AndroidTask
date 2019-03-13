package com.androidtask.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtask.db.DatabaseHelper;
import com.androidtask.R;
import com.androidtask.model.User;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogIn extends AppCompatActivity {

    @BindView(R.id.login_email_ed_id)
    EditText email_ed;
    @BindView(R.id.login_pass_ed_id)
    EditText pass_ed;

    private final String TAG = this.getClass().getName();
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase database;
    Cursor cursor;
    User user;

    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
        Log.e(TAG, "onCreateStarted");

        sqLiteOpenHelper = new DatabaseHelper(this);
        database = sqLiteOpenHelper.getReadableDatabase();

        //   get data from shared preferences ...
        sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
        email_ed.setText(sharedPreferences.getString("email", ""));//"No name defined" is the default value.
        pass_ed.setText(sharedPreferences.getString("password", "")); //0 is the default value.
    }

    @OnClick(R.id.login_btn_id)
    public void logincCick() {
        Log.e(TAG, "LoginStarted");
        String email = email_ed.getText().toString();
        String password = pass_ed.getText().toString();

        if (!FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))
                ) {
            cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.Table_Name + " WHERE " + DatabaseHelper.Col_5 + "=? AND " + DatabaseHelper.Col_4 + "=?", new String[]{email, password});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
//                    cursor.moveToNext();
                    user = new User();
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "" + cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_5)), Toast.LENGTH_SHORT).show();
                    // Get imageData in byte[]. Easy, right?
                    user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_1))));
                    user.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_2)));
                    user.setLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_3)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_5)));
                    user.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Col_6)));

                    // Save Data
                    user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    user_data_edito.putString("userObject", json);
                    user_data_edito.commit();
                    user_data_edito.apply();

                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    intent.putExtra("userData", user);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "Error Login Data", Toast.LENGTH_SHORT).show();
            }
        }
        ;
    }

    @OnClick(R.id.register_txt_id)
    public void goRegister() {
        Intent intent = new Intent(LogIn.this, Register.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", email_ed.getText().toString().trim());
        editor.putString("password", pass_ed.getText().toString().trim());
        editor.commit();
        editor.apply();
    }
}
