package ru.mirea.bublikov.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.bublikov.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FILE_NAME = "history_event.txt";
    private File internalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        internalFile = getFileStreamPath(FILE_NAME);
        if (internalFile.exists()) {
            binding.buttonLoad.setEnabled(true);
        } else {
            binding.buttonLoad.setEnabled(false);
            binding.textViewFileContent.setText("Файл \"" + FILE_NAME + "\" еще не создан");
        }

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextToFile();
                binding.buttonLoad.setEnabled(true);
                binding.textViewFileContent.setText("Файл \"" + FILE_NAME + "\" создан");
            }
        });

        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String fileContent = readTextFromFile();
                        binding.textViewFileContent.post(new Runnable() {
                            public void run() {
                                if (fileContent != null) {
                                    binding.textViewFileContent.setText(fileContent);
                                } else {
                                    binding.textViewFileContent.setText("Ошибка чтения файла или файл пуст");
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void saveTextToFile() {
        String date = binding.editTextDate.getText().toString();
        String description = binding.editTextDescription.getText().toString();

        String textToSave = "Дата: " + date + "\nОписание: " + description;

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(textToSave.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(TAG, text);
            return text;
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}