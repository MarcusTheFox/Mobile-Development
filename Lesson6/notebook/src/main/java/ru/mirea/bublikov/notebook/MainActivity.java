package ru.mirea.bublikov.notebook;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import ru.mirea.bublikov.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private boolean isWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int writePermissionStatus = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int readPermissionStatus = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (writePermissionStatus == PackageManager.PERMISSION_GRANTED && readPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Необходимые разрешения на хранилище получены.");
            isWork = true;
        } else {
            Log.d(LOG_TAG, "Запрашиваем необходимые разрешения на хранилище.");
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFileToExternalStorage();
            }
        });

        binding.buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFileFromExternalStorage();
            }
        });
    }

    private void saveFileToExternalStorage() {
        String fileName = binding.editTextFileName.getText().toString().trim();
        String quote = binding.editTextQuote.getText().toString();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите имя файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        OutputStreamWriter outputStreamWriter = null;

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

            outputStreamWriter.write(quote);

            Toast.makeText(this, "Данные сохранены в файл \"" + fileName + "\"", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Ошибка записи в файл!", e);
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFileFromExternalStorage() {
        String fileName = binding.editTextFileName.getText().toString().trim();

        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите имя файла для загрузки!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        if (!file.exists()) {
            Log.w(LOG_TAG, "Файл не найден для загрузки: " + file.getAbsolutePath());
            Toast.makeText(this, "Файл \"" + fileName + "\" не найден.", Toast.LENGTH_SHORT).show();
            binding.editTextQuote.setText("");
            return;
        }

        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            String readQuote = stringBuilder.toString();

            binding.editTextQuote.setText(readQuote);
            Toast.makeText(this, "Файл \"" + fileName + "\" загружен", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка загрузки файла!", e);
            binding.editTextQuote.setText("");
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}