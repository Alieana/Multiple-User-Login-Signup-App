package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText email,password;
    Button signupBtn,loginBtn;
    boolean valid = true;
    CheckBox isClubBox, isStudentBox;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        isClubBox = findViewById(R.id.isClub);
        isStudentBox = findViewById(R.id.isStudent);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);

        //checkbox logic
        isStudentBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isClubBox.setChecked(false);
                }
            }
        });

        isClubBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isStudentBox.setChecked(false);
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(email);
                checkField(password);

                //checkbox validation
                if(!(isClubBox.isChecked() || isStudentBox.isChecked())){
                    Toast.makeText(SignupActivity.this, "Select the Account Role", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(valid){
                    // start the user signup process
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Email",email.getText().toString());

                            Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                            //specify the user roles
                            if(isClubBox.isChecked()){
                                userInfo.put("Role", "Clubs");
                                df = fStore.collection("Clubs").document(user.getUid());
                                df.set(userInfo);
                            }
                            if(isStudentBox.isChecked()) {
                                userInfo.put("Role", "Students");
                                df = fStore.collection("Students").document(user.getUid());
                                df.set(userInfo);
                            }
                            if(isClubBox.isChecked()){
                                startActivity(new Intent(getApplicationContext(),ClubSignupActivity.class));
                                finish();
                            }

                            if(isStudentBox.isChecked()){
                                startActivity(new Intent(getApplicationContext(),StudentSignupActivity.class));
                                finish();
                            }

                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Required");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
}
