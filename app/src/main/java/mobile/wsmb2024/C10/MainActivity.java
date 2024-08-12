package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText etICnumL, etPasswordL;
    TextView tvSignUp;
    Button btnLogin;
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etICnumL = findViewById(R.id.etICNumL);
        etPasswordL = findViewById(R.id.etPasswordL);
        tvSignUp = findViewById(R.id.tvSignIn);
        btnLogin = findViewById(R.id.btnLogin);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterLoginActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String icnum = etICnumL.getText().toString();
                String password = etPasswordL.getText().toString();

                loginUser(icnum, password);
            }
        });
    }

    public void loginUser(String icnum, String password){
        userRef.orderByChild("icnum").equalTo(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        String email = user.email;

                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                                     Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);SharedPreferences sp;
                                     i.putExtra("icnum",icnum);
                                     sp = getSharedPreferences("user",MODE_PRIVATE);
                                     SharedPreferences.Editor editor = sp.edit();
                                     editor.putString("icnum",icnum);
                                     editor.commit();
                                     startActivity(i);
                                 }
                                 else{
                                     Toast.makeText(MainActivity.this, "Invalid IC Number or Password!", Toast.LENGTH_SHORT).show();
                                     etICnumL.setText("");
                                     etPasswordL.setText("");
                                 }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase","",error.toException());
            }
        });
    }
}