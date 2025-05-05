package ru.mirea.bublikov.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.bublikov.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "mirea_settings";

    private static final String KEY_GROUP = "group";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_MOVIE = "movie";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadData();

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        String group = binding.editTextGroup.getText().toString();
        String number = binding.editTextNumber.getText().toString();
        String movie = binding.editTextMovie.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_GROUP, group);
        editor.putString(KEY_NUMBER, number);
        editor.putString(KEY_MOVIE, movie);

        editor.apply();

        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        String group = sharedPreferences.getString(KEY_GROUP, "");
        String number = sharedPreferences.getString(KEY_NUMBER, "");
        String movie = sharedPreferences.getString(KEY_MOVIE, "");

        binding.editTextGroup.setText(group);
        binding.editTextNumber.setText(number);
        binding.editTextMovie.setText(movie);

        Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show();
    }
}