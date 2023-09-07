package com.example.meet_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText emailBox,passwordBox,nameBox;
    Button loginBtn,signupBtn,deletetableBtn,showPass;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameBox = findViewById(R.id.namebox);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.createbtn);
        showPass = findViewById(R.id.showpassword);
        deletetableBtn = findViewById(R.id.deletetbBtn);

        databaseHelper = new DatabaseHelper(this);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,pass,name;

                email = emailBox.getText().toString().trim();
                pass = passwordBox.getText().toString().trim();
                name = nameBox.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameBox.setError("Name is required");
                    nameBox.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailBox.setError("Email is required");
                    emailBox.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailBox.setError("Invalid email address");
                    emailBox.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    passwordBox.setError("Password is required");
                    passwordBox.requestFocus();
                    return;
                }

                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                String[] columns = {"name"};
                String selection = "email=?";
                String[] selectionArgs = {email};
                Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

                if (cursor.getCount() > 0) {
                    Toast.makeText(SignupActivity.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_NAME, name);
                    values.put(DatabaseHelper.COLUMN_EMAIL, email);
                    values.put(DatabaseHelper.COLUMN_PASSWORD, pass);

                    long id = db.insert(DatabaseHelper.TABLE_NAME, null, values);
                    if (id > 0) {
                        Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Error creating user", Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.close();
                db.close();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordBox.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                {
                    passwordBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showPass.setText("Show Password");
                } else {
                    passwordBox.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPass.setText("Hide Password");
                }
            }
        });

        deletetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.execSQL("DELETE FROM users");
                Toast.makeText(SignupActivity.this, "Table deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }
}