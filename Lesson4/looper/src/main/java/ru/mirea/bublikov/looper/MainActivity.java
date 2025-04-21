package ru.mirea.bublikov.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.bublikov.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + result);
            }
        };

        MyLooper myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextProfession.setText("Мой номер по списку № 4");
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ageStr = binding.editTextAge.getText().toString();
                String profession = binding.editTextProfession.getText().toString();
                int age = Integer.parseInt(ageStr);

                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("age", age);
                bundle.putString("profession", profession);
                message.setData(bundle);
                myLooper.mHandler.sendMessage(message);
            }
        });
    }
}