package ru.mirea.bublikov.data_thread;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

import ru.mirea.bublikov.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.textViewInfo.setText("runn1");
                Log.d("DataThread", "runn1 executed");
            }
        };
        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.textViewInfo.setText("runn2");
                Log.d("DataThread", "runn2 executed");
            }
        };
        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.textViewInfo.setText("runn3");
                Log.d("DataThread", "runn3 executed");
            }
        };

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    Log.d("DataThread", "runOnUiThread(runn1) called");
                    TimeUnit.SECONDS.sleep(1);
                    binding.textViewInfo.postDelayed(runn3, 2000);
                    Log.d("DataThread", "binding.textViewInfo.postDelayed(runn3, 2000) called");
                    binding.textViewInfo.post(runn2);
                    Log.d("DataThread", "binding.textViewInfo.post(runn2) called");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        binding.textViewInfo.append(
                "Различия между методами для работы с UI из фона:\n" +
                    "- runOnUiThread(Runnable): Помещает Runnable в очередь сообщений UI-потока для немедленного выполнения (как только UI-поток освободится).\n" +
                    "- View.post(Runnable): Помещает Runnable в очередь сообщений UI-потока. Выполняется после обработки текущих событий в очереди.\n" +
                    "- View.postDelayed(Runnable, delay): Помещает Runnable в очередь сообщений UI-потока с указанной задержкой в миллисекундах.\n\n" +
                "Последовательность выполнения:\n" +
                    "1. runn1 (после 2с задержки в фоне)\n" +
                    "2. runn2 (после 2с + 1с = 3с задержки в фоне, сразу после postDelayed вызова)\n" +
                    "3. runn3 (после 3с задержки в фоне + 2с задержки postDelayed = 5с)"
        );
    }
}