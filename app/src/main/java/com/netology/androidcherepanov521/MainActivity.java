package com.netology.androidcherepanov521;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText mLogin;
    private EditText mPassword;

    private final String ATTEMPTS = "attempts";
    private final int MAX_AUTH_ATTEMPTS = 3;
    private int attempts_counter;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ATTEMPTS, attempts_counter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (savedInstanceState != null) {
            attempts_counter = savedInstanceState.getInt(ATTEMPTS);
        } else {
            attempts_counter = 1;
        }
    }

    private void initView() {
        mLogin = findViewById(R.id.login);
        mPassword = findViewById(R.id.password);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputCorrect()) {
                    checkUserAuthData();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_date), Toast.LENGTH_LONG).show();
                    clearFields();
                }
            }
        });

        findViewById(R.id.reg_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputCorrect()) {
                    saveUserAuthData();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_date), Toast.LENGTH_LONG).show();
                    clearFields();
                }
            }
        });
    }

    private boolean isInputCorrect() {
        return !mLogin.getText().toString().equals("")
                && !mPassword.getText().toString().equals("");
    }

    private void checkUserAuthData() {
        String authFileName = mLogin.getText().toString().hashCode() + ".data";
        try {
            FileInputStream authFile = openFileInput(authFileName);
            InputStreamReader reader = new InputStreamReader(authFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            int hashedPassword = Integer.valueOf(bufferedReader.readLine());

            bufferedReader.close();
            reader.close();

            if (mPassword.getText().toString().hashCode() == hashedPassword) {
                Toast.makeText(getApplicationContext(), getString(R.string.login_ok), Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                userNotAuth();
            }

        } catch (FileNotFoundException e) {
            userNotAuth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userNotAuth() {
        Toast.makeText(getApplicationContext(), getString(R.string.error_log_pas), Toast.LENGTH_LONG).show();
        clearFields();
        attempts_counter++;
        if (attempts_counter > MAX_AUTH_ATTEMPTS) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_LONG).show();
            clearFields();
            finish();
        }
    }

    private void saveUserAuthData() {
        String authFileName = mLogin.getText().toString().hashCode() + ".data";
        try {
            FileOutputStream authFile = openFileOutput(authFileName, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(authFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(String.valueOf(mPassword.getText().toString().hashCode()));

            bufferedWriter.close();
            writer.close();
            Toast.makeText(getApplicationContext(), getString(R.string.reg_ok), Toast.LENGTH_LONG).show();
            clearFields();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        mLogin.setText("");
        mPassword.setText("");
    }
}
