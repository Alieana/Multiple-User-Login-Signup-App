package com.example.loginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ClubSignupActivity extends AppCompatActivity implements View.OnClickListener{

    //private static final String TAG = StudentSignupActivity.class.getSimpleName();

    TextView email;
    EditText clubName;
    EditText picName;
    Button registerBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference df;

    public ClubSignupActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_signup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        FirebaseUser user = fAuth.getCurrentUser();
        df = fStore.collection("Clubs").document(user.getUid());
        clubName = findViewById(R.id.clubName);
        picName = findViewById(R.id.picName);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        email = findViewById(R.id.email);
        email.setText(user.getEmail());

    }
    private void clubInformation(){
        FirebaseUser user = fAuth.getCurrentUser();
        df = fStore.collection("Clubs").document(user.getUid());
        Map<String, Object> clubInfo = new HashMap<>();
        clubInfo.put("Club Name",clubName.getText().toString());
        clubInfo.put("PIC Name",picName.getText().toString());
        df.update(clubInfo);
        Toast.makeText(getApplicationContext(),"Club information updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view==registerBtn){
            clubInformation();
            finish();
            startActivity(new Intent(ClubSignupActivity.this, MainActivity.class));
        }
    }

}
