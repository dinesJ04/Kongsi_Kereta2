package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    EditText etNameE, etICNumE, etPhoneE, etEmailE, etAddressE, etPasswordE;
    Button btnSaveChanges, btnBackE;
    RadioGroup rgGenderE;
    RadioButton rbMaleE, rbFemaleE;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNameE = findViewById(R.id.etNameE);
        etICNumE = findViewById(R.id.etICNumE);
        etPhoneE = findViewById(R.id.etPhoneE);
        etEmailE = findViewById(R.id.etEmailE);
        etAddressE = findViewById(R.id.etAddressE);
        etPasswordE = findViewById(R.id.etPasswordE);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBackE = findViewById(R.id.btnBackE);
        rgGenderE = findViewById(R.id.rgGenderE);
        rbMaleE = findViewById(R.id.rbMaleE);
        rbFemaleE = findViewById(R.id.rbFemaleE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        userRef.orderByChild("icnum").equalTo(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        UserModel user = datasnapshot.getValue(UserModel.class);

                        if(user != null){
                            String name = user.name;
                            String gender = user.gender;
                            String phonenum = user.phone;
                            String email = user.email;
                            String address = user.address;
                            String password = user.password;

                            etNameE.setText(name);
                            etICNumE.setText(icnum);
                            if(!Objects.equals(gender, "Female")){
                                rbMaleE.setChecked(true);
                                rbFemaleE.setChecked(false);
                            }else{
                                rbFemaleE.setChecked(true);
                                rbMaleE.setChecked(false);
                            }
                            etPhoneE.setText(phonenum);
                            etEmailE.setText(email);
                            etAddressE.setText(address);
                            etPasswordE.setText(password);
                        }
                        else{
                            etNameE.setText("Unknown Field");
                            etICNumE.setText("Unknown Field");
                            rbMaleE.setChecked(false);
                            rbFemaleE.setChecked(false);
                            etPhoneE.setText("Unknown Field");
                            etEmailE.setText("Unknown Field");
                            etAddressE.setText("Unknown Field");
                        }
                    }
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","",error.toException());
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etNameE.getText().toString();
                String icnum = etICNumE.getText().toString();
                String phone = etPhoneE.getText().toString();
                String email = etEmailE.getText().toString();
                String address = etAddressE.getText().toString();
                String password = etPasswordE.getText().toString();

                String editGender = "";
                if(rbMaleE.isChecked()){
                    editGender = "Male";
                }else if(rbFemaleE.isChecked()){
                    editGender = "Female";
                }else{
                    Toast.makeText(EditProfileActivity.this, "Please Choose Your Gender!", Toast.LENGTH_SHORT).show();
                }

                updateProfile(name, icnum, editGender, phone, email, address, password);
            }
        });

        btnBackE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                startActivity(i);
            }
        });
    }

    public void updateProfile(String name, String icnum, String editGender, String phone, String email, String address, String password){
        UpdateModel update = new UpdateModel(name, icnum, editGender, phone, email, address, password);

        userRef.child(icnum).setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "User Profile Updated!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Failed To Update!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}