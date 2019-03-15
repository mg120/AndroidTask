package com.androidtask.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidtask.db.DatabaseHelper;
import com.androidtask.R;
import com.fourhcode.forhutils.FUtilsValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    @BindView(R.id.firstName_ed_id)
    EditText firstName_ed;
    @BindView(R.id.lastName_ed_id)
    EditText lastName_ed;
    @BindView(R.id.Email_ed_id)
    EditText email_ed;
    @BindView(R.id.phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.password_ed_id)
    EditText password_ed;
    @BindView(R.id.gender_radio_group)
    RadioGroup gender_rG;

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    int gender = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        gender_rG.check(R.id.male_rb_id);
        sqLiteOpenHelper = new DatabaseHelper(Register.this);
    }

    @OnClick(R.id.go_login_txt_id)
    public void goLogin() {
        Intent intent = new Intent(Register.this, LogIn.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.register_btn_id)
    public void onClick() {
        if (!FUtilsValidation.isEmpty(firstName_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(lastName_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))
                ) {
            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            String firstName = firstName_ed.getText().toString();
            String lastName = lastName_ed.getText().toString();
            String email = email_ed.getText().toString();
            String phone = phone_ed.getText().toString();
            String password = password_ed.getText().toString();

            insertData(firstName, lastName, email, phone, password);

            Toast.makeText(this, "Registered Successfully..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Register.this, LogIn.class));
            finish();
        };
    }

    private void insertData(String firstName, String lastName, String email, String phone, String pass) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Col_2, firstName);
        values.put(DatabaseHelper.Col_3, lastName);
        values.put(DatabaseHelper.Col_4, pass);
        values.put(DatabaseHelper.Col_5, email);
        values.put(DatabaseHelper.Col_6, phone);
        values.put(DatabaseHelper.Col_7, gender);

        long id = sqLiteDatabase.insert(DatabaseHelper.Table_Name, null, values);
    }

    public void onRadioContactClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male_rb_id:
                if (checked) {
                    // Pirates are the best
                    gender = 1;
                }
                break;
            case R.id.female_rb_id:
                if (checked) {
                    // Ninjas rule
                    gender = 0;
                }
                break;
        }
    }
}