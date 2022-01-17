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

public class StudentSignupActivity extends AppCompatActivity implements View.OnClickListener{

    //private static final String TAG = StudentSignupActivity.class.getSimpleName();

    TextView email;
    EditText studentName, studentID, faculty, programCode;
    Button registerBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference df;

    public StudentSignupActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        FirebaseUser user = fAuth.getCurrentUser();
        df = fStore.collection("Students").document(user.getUid());
        studentName = findViewById(R.id.studentName);
        studentID = findViewById(R.id.studentID);
        faculty = findViewById(R.id.faculty);
        programCode = findViewById(R.id.codeProgram);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        email = findViewById(R.id.email);
        email.setText(user.getEmail());

    }
    private void studentInformation(){
        FirebaseUser user = fAuth.getCurrentUser();
        df = fStore.collection("Students").document(user.getUid());
        Map<String, Object> studentInfo = new HashMap<>();
        studentInfo.put("Student Name",studentName.getText().toString());
        studentInfo.put("Student ID",studentID.getText().toString());
        studentInfo.put("Faculty",faculty.getText().toString());
        studentInfo.put("Program Code",programCode.getText().toString());
        df.update(studentInfo);
        Toast.makeText(getApplicationContext(),"Student information updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view==registerBtn){
            studentInformation();
            startActivity(new Intent(StudentSignupActivity.this, UserHomepageActivity.class));
            finish();
        }
    }

}
