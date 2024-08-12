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

public class CheckAvailabilityActivity extends AppCompatActivity {
    TextView tvDate, tvTime, tvOrigin, tvDestination, tvFare, tvDriverName, tvDriverPhone, tvCarModel, tvCarNum;
    Button btnBook, btnBack1;

    DatabaseReference rideRef, carRef, userRef, bookRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_check_availability);

         tvDate = findViewById(R.id.tvDate);
         tvTime = findViewById(R.id.tvTime);
         tvOrigin = findViewById(R.id.tvOrigin);
         tvDestination = findViewById(R.id.tvDestination);
         tvFare = findViewById(R.id.tvFare);
         tvDriverName = findViewById(R.id.tvDriverName);
         tvDriverPhone = findViewById(R.id.tvDriverPhone);
         tvCarModel = findViewById(R.id.tvCarModel);
         tvCarNum = findViewById(R.id.tvCarNum);
         btnBook = findViewById(R.id.btnBook);
         btnBack1 = findViewById(R.id.btnBack1);

         FirebaseDatabase database = FirebaseDatabase.getInstance();
         rideRef = database.getReference("ride_info");
         carRef = database.getReference("car_info");
         userRef = database.getReference("users");
         bookRef = database.getReference("ride_bookings");

        SharedPreferences sp;
        sp = getSharedPreferences("user",MODE_PRIVATE);
        String icnum = sp.getString("icnum","");

        viewRide(icnum);
        carInfo(icnum);
        driverInfo(icnum);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = tvDate.getText().toString();
                String time = tvTime.getText().toString();
                String origin = tvOrigin.getText().toString();
                String destination = tvDestination.getText().toString();
                String fare = tvFare.getText().toString();
                String dName = tvDriverName.getText().toString();
                String dPhone = tvDriverPhone.getText().toString();
                String cModel = tvCarModel.getText().toString();
                String cNum = tvCarNum.getText().toString();

                addBooking(icnum, date, time, origin, destination, fare, dName, dPhone, cModel, cNum);
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                startActivity(i);
            }
        });

    }

    public void viewRide(String icnum){
        rideRef.child(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RideModel ride = snapshot.getValue(RideModel.class);

                String date = ride.date;
                String time = ride.time;
                String origin = ride.origin;
                String destination = ride.destination;
                String fare = ride.fare;

                tvDate.setText(date);
                tvTime.setText(time);
                tvOrigin.setText(origin);
                tvDestination.setText(destination);
                tvFare.setText(fare);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase","",error.toException());
            }
        });
    }

    public void carInfo(String icnum){
        carRef.child(icnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CarModel car = snapshot.getValue(CarModel.class);

                String model = car.model;
                String number = car.number;

                tvCarModel.setText(model);
                tvCarNum.setText(number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase","",error.toException());
            }
        });
    }

    public void driverInfo(String icnum){
        userRef.child("030923-10-2736").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);

                String name = user.name;
                String phone = user.phone;

                tvDriverName.setText(name);
                tvDriverPhone.setText(phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FireBase","",error.toException());
            }
        });
    }

    public void addBooking(String icnum, String date, String time, String origin, String destination, String fare, String dName, String dPhone, String cModel, String cNum){
        String status = "Active";
        BookModel booking = new BookModel(icnum, date, time, origin, destination, fare, dName, dPhone, cModel, cNum, status);

        bookRef.child(icnum).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CheckAvailabilityActivity.this, "Booking Successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), FirstPageActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(CheckAvailabilityActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}