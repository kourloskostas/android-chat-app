package com.example.mlem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    TextView forgotBtn;
    Button loginBtn;
    Button registerBtn;
    ProgressBar loading;
    boolean isLoading;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiate layout Views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        loading = findViewById(R.id.loading);
        isLoading = false;
        forgotBtn = findViewById(R.id.textView);

        //Get Google authenticator instance
        mAuth = FirebaseAuth.getInstance();


        //Login button on click listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set loading and log user in
                setLoading(true);
                isLoading = true;
                //if (isLoading) { return; }
                login(username.getText().toString(), password.getText().toString());

            }
        });
        //Register button on click listener
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set loading and register user
                setLoading(true);
                isLoading = true;
                //if (isLoading) { return; }

                validateRegister(username.getText().toString(), password.getText().toString());
            }
        });
    }

    public void validateRegister(final String email, final String password) {
        //If username or password is null , or <5 return
        if (username == null || password == null) {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!",
                    Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;

            // Username and password must be > x
        } else if (username.length() < 5 || password.length() < 3) {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!",
                    Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        }

        //todo check availability of username/email
        //else if
        //....


        // Change view to ask for username and proceed
        setContentView(R.layout.activity_register);

        final EditText userName = findViewById(R.id.usernameEditText);

        // Register button
        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                register(username, email, password); // Make a request to register user
            }
        });

    }

    // Register user usigng Google Authenticator
    public void register(final String username, final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Message", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            setLoading(true);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                login(email, password);
                                                Toast.makeText(getApplicationContext(), "Registered Successfully!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            //updateUI(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("feaf", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
        isLoading = false;
        setLoading(false);

    }

    //Log user in using Google Authenticator
    public void login(String email, String password) {

        if (username == null || password == null) {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!",
                    Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        } else if (username.length() < 5 || password.length() < 3) {
            Toast.makeText(getApplicationContext(), "Invalid Username or Password!",
                    Toast.LENGTH_SHORT).show();
            setLoading(false);
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.INVISIBLE);
                            setLoading(false);
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
        loading.setVisibility(View.INVISIBLE);
        setLoading(false);

    }


    // Change visibility of buttons when user clicks login/register
    public void setLoading(boolean isLoading) {
        if (isLoading) {
            loading.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            registerBtn.setVisibility(View.INVISIBLE);
            forgotBtn.setVisibility(View.INVISIBLE);
        } else {
            loading.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.VISIBLE);
            forgotBtn.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

}
