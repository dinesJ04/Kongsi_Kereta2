package mobile.wsmb2024.C10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingStatusActivity extends AppCompatActivity {
    TextView tvDate, tvTime, tvOrigin, tvDestination, tvFare, tvDriverName, tvDriverPhone, tvCarModel, tvCarNum, tvStatus;
    Button btnCancel, btnBackS;
    DatabaseReference bookRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status);


        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvDestination = findViewById(R.id.tvDestination);
        tvFare = findViewById(R.id.tvFare);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverPhone = findViewById(R.id.tvDriverPhone);
        tvCarModel = findViewById(R.id.tvCarModel);
        tvCarNum = findViewById(R.id.tvCarNum);
        tvStatus = findViewById(R.id.tvStatus);
        btnCancel = findViewById(R.id.btnCancel);
        btnBackS = findViewById(R.id.btnBackS);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bookRef = database.getReference("ride_bookings");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        rideStatus(icnum);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = tvDate.getText().toString();
                String time = tvTime.getText().toString();
                String origin = tvOrigin.getText().toString();
                String destination = tvDestination.getText().toString();
                String fare = tvFare.getText().toString();
                String model = tvCarModel.getText().toString();
                String number = tvCarNum.getText().toString();
                String name = tvDriverName.getText().toString();
                String phone = tvDriverPhone.getText().toString();

                cancelRide(icnum, date, time, origin, destination, fare, name, phone, model, number);
            }
        });

        btnBackS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                startActivity(i);
            }
        });
    }
    public void rideStatus(String icnum){
        bookRef.child(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel booking = snapshot.getValue(BookModel.class);

                String date = booking.date;
                String time = booking.time;
                String origin = booking.origin;
                String destination = booking.destination;
                String fare = booking.fare;
                String model = booking.cModel;
                String number = booking.cNum;
                String name = booking.dName;
                String phone = booking.dPhone;
                String status = booking.status;

                tvDate.setText(date);
                tvTime.setText(time);
                tvOrigin.setText(origin);
                tvDestination.setText(destination);
                tvFare.setText(fare);
                tvDriverName.setText(name);
                tvDriverPhone.setText(phone);
                tvCarModel.setText(model);
                tvCarNum.setText(number);
                tvStatus.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase","",error.toException());
            }
        });
    }

    public void cancelRide(String icnum, String date, String time, String origin, String destination, String fare, String dName, String dPhone, String cModel, String cNum){
        String status = "Cancelled";
        BookModel booking = new BookModel(icnum, date, time, origin, destination, fare, dName, dPhone, cModel, cNum, status);

        bookRef.child(icnum).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(BookingStatusActivity.this, "Your Ride Is Cancelled!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(BookingStatusActivity.this, "Failed To Cancel Ride!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}