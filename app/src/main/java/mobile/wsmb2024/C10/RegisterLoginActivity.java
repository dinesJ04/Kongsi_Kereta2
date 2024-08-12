package mobile.wsmb2024.C10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterLoginActivity extends AppCompatActivity {
    EditText etName, etICNum, etPhone, etEmail, etAddress, etPassword;
    RadioButton rbMale, rbFemale;
    RadioGroup rgGender;
    Button btnRegister;
    TextView tvSignIn;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        etName = findViewById(R.id.etName);
        etICNum = findViewById(R.id.etICNum);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        rbMale = findViewById(R.id.rbMaleE);
        rbFemale = findViewById(R.id.rbFemaleE);
        rgGender = findViewById(R.id.rgGenderE);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.tvSignIn);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedGender = "";
                if(rbMale.isChecked()){
                    selectedGender = "Male";
                } else if (rbFemale.isChecked()) {
                    selectedGender = "Female";
                }else{
                    Toast.makeText(RegisterLoginActivity.this, "Please Choose Your Gender!", Toast.LENGTH_SHORT).show();
                }

                String name = etName.getText().toString();
                String icnum = etICNum.getText().toString();
                String gender = selectedGender.toString();
                String phone = etPhone.getText().toString();
                String email = etEmail.getText().toString();
                String address = etAddress.getText().toString();
                String password = etPassword.getText().toString();

                registerUser(name, icnum, gender, phone, email, address, password);
            }
        });
    }

    public void registerUser(String name, String icnum, String gender, String phone, String email, String address, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserModel user = new UserModel(name,icnum, gender, phone, email, address, password);
                            userRef.child(icnum).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterLoginActivity.this, "Registration Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(RegisterLoginActivity.this, "Registration Failed. Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterLoginActivity.this, "User Account Already Exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}