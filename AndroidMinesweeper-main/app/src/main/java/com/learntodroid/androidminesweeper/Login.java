package com.learntodroid.androidminesweeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;

public class Login extends AppCompatActivity {

    EditText nameText,passwordText,change;
    Button login,register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        nameText = (EditText) findViewById(R.id.nameText);
        passwordText =(EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterClick(v);
            }
        });
    }

    public void btnLoginClick(View view) {
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        mAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Giris başarılı", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Giris başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnRegisterClick(View view) {
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        mAuth.createUserWithEmailAndPassword(name, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Kayıt başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteuser(String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
                                                Toast.makeText(Login.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }



    private void changeemail(String email, final String password) {

        change = findViewById(R.id.newPassword);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("value", "User re-authenticated.");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Email Changed" + " Current Email is " + change.getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void btnDeleteClick(View view) {
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        deleteuser(name,password);
    }

    public void btnEditClick(View view) {
        String name = nameText.getText().toString();
        String password = passwordText.getText().toString();
        changeemail(name,password);
    }

}
