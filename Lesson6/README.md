# Отчёт 6

## **App**
Создан проект Lesson6 для работы с SharedPreferences.
- Приложение сохраняет (номер группы, номер по списку, фильм) из EditText и загружает их при запуске с помощью getSharedPreferences.
- Файл настроек SharedPreferences найден через Device Explorer, его скриншот помещен в res/raw.
### Экраны
![1-App-1](https://github.com/user-attachments/assets/82e0a7ec-0886-462a-824e-a1f9b3625b45)

![1-App-2](https://github.com/user-attachments/assets/77613629-de82-41f5-9e2e-a54e2f42e4d2)

![1-App-3](https://github.com/user-attachments/assets/b38e997e-9897-48a1-994c-19e56a42f2f7)

![1-App-4](https://github.com/user-attachments/assets/805be1df-159c-4d44-8128-ea8fc6926f8e)

### Код
#### MainActivity.java
``` java
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
```

## **Secure Shared Preferences**
Создан Android-модуль "SecureSharedPreferences".
Приложение отображает изображение и имя любимого поэта.
Имя поэта безопасно хранится и загружается с использованием EncryptedSharedPreferences.
Добавлена зависимость security-crypto и реализована инициализация EncryptedSharedPreferences с Master Key.
Найден файл настроек через Device Explorer. Данные этого файла зашифрованы.
Скриншот зашифрованного файла настроек помещен в res/raw.

### Экраны
![2-SecureSharedPreferences-1](https://github.com/user-attachments/assets/b6e77ee0-e88f-48d4-a84b-bca106b39a9e)

![2-SecureSharedPreferences-2](https://github.com/user-attachments/assets/a5098bda-cf2c-4ae7-8721-bb556077cabe)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.securesharedpreferences;  
  
import android.content.SharedPreferences;  
import android.os.Bundle;  
import android.security.keystore.KeyGenParameterSpec;  
  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.security.crypto.EncryptedSharedPreferences;  
import androidx.security.crypto.MasterKeys;  
  
import java.io.IOException;  
import java.security.GeneralSecurityException;  
  
import ru.mirea.bublikov.securesharedpreferences.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
  
    private ActivityMainBinding binding;  
    private SharedPreferences secureSharedPreferences;  
    private static final String SECURE_PREFS_NAME = "secure";  
    private static final String KEY_POET_NAME = "poet_name";  
  
    private static final String DEFAULT_POET_NAME = "Гера Шипов";  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        try {  
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;  
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);  
            secureSharedPreferences = EncryptedSharedPreferences.create(  
                    SECURE_PREFS_NAME,  
                    mainKeyAlias,  
                    getBaseContext(),  
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,  
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM  
            );  
            secureSharedPreferences.edit().putString(KEY_POET_NAME, DEFAULT_POET_NAME).apply();  
        } catch (GeneralSecurityException | IOException e) {  
            e.printStackTrace();  
        }  
  
        String poetName = secureSharedPreferences.getString(KEY_POET_NAME, "[Имя поэта]");  
        binding.textViewPoetName.setText(poetName);  
        binding.imageViewPoet.setImageResource(R.drawable.gera_chipov);  
  
    }  
}
```

## **Internal File Storage**
Создан новый модуль "InternalFileStorage" для работы с файлами во внутреннем хранилище.
Приложение позволяет сохранить дату и описание в файл (history_event.txt) и загрузить их оттуда.
Добавлены кнопки "Сохранить в файл" и "Загрузить из файла".
Сохранение выполняется методом openFileOutput, чтение - openFileInput (в фоновом потоке).
Кнопка "Загрузить из файла" неактивна при старте, если файл еще не создан, и становится активной после первого сохранения.
Файл history_event.txt найден через Device Explorer и помещен в res/raw.

### Экраны
![3-InternalFileStorage-1](https://github.com/user-attachments/assets/28ff61b5-a0a1-4007-9522-bc2a79fa70a8)

![3-InternalFileStorage-2](https://github.com/user-attachments/assets/6fc3632d-be9c-40cd-8f37-4558442dd808)

![3-InternalFileStorage-3](https://github.com/user-attachments/assets/3cae1e2a-0068-4bd8-bc7f-ea5425f0e095)

![3-InternalFileStorage-4](https://github.com/user-attachments/assets/1a787ccb-a061-435a-b0e7-80c5a5d55405)

![3-InternalFileStorage-5](https://github.com/user-attachments/assets/89e9ea59-3994-489b-9efb-55569347a2bb)

### Код
#### MainActivity.java
``` java
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
```

## **Notebook**
Создан новый модуль "Notebook".
Приложение позволяет сохранить и загрузить текст (цитату) с указанным именем файла.
Файлы сохраняются в публичную директорию Documents (Environment.DIRECTORY_DOCUMENTS).
Используются методы Java I/O (FileOutputStream, FileInputStream, BufferedReader, OutputStreamWriter).
Созданы два файла с цитатами известных людей с помощью приложения.
Создана директория res/raw, и созданные файлы скопированы туда с устройства.

### Экраны
![4-Notebook-1](https://github.com/user-attachments/assets/1d177353-33cb-49c4-9abc-becd47ebf68e)

![4-Notebook-2](https://github.com/user-attachments/assets/085b8330-31af-4c7c-828f-ca1d9b5a7ebc)

![4-Notebook-3](https://github.com/user-attachments/assets/e982118e-0953-44d6-a4c6-e3a55d2720c5)

![4-Notebook-4](https://github.com/user-attachments/assets/b64b9e97-d136-4425-a2a4-42efc2c51da8)

![4-Notebook-5](https://github.com/user-attachments/assets/3476d5b6-5cf3-42df-9bb8-bd46d719d1db)

### Код
#### MainActivity.java
``` java
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
```

## **Employee DB**
Создан новый модуль "EmployeeDB" для работы с базой данных супергероев с использованием Room.
Добавлена зависимость Room в build.gradle.
Созданы компоненты Room: @Entity (Superhero), @Dao (SuperheroDao), @Database (AppDatabase).
Класс Superhero представляет таблицу супергероев.
SuperheroDao определяет методы для операций с БД (CRUD и запросы).
AppDatabase является точкой входа к БД и DAO.
Создан Singleton-класс App, который инициализирует базу данных при старте приложения.
В MainActivity продемонстрированы операции с базой данных (вставка, чтение, обновление супергероев) через DAO.

### Логи
![5-EmployeeDB-1](https://github.com/user-attachments/assets/cc4a3808-6535-4d40-ab6b-1252374347df)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.employeedb;  
  
import android.os.Bundle;  
import android.util.Log;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
import java.util.List;  
  
public class MainActivity extends AppCompatActivity {  
    private static final String TAG = MainActivity.class.getSimpleName();  
    private static final String DATABASE_NAME = "superheroes_database";  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        Log.d(TAG, "MainActivity onCreate");  
  
        AppDatabase db = App.getInstance().getDatabase();  
        SuperheroDao superheroDao = db.superheroDao();  
  
        Superhero superhero1 = new Superhero("Spider-Man",  
                "Wall-crawling, Spider-Sense, Super Strength");  
        Superhero superhero2 = new Superhero("Iron Man",  
                "Powered Armor Suit, Genius-Level Intellect");  
  
        Log.d(TAG, "Созданы объекты супергероев: " + superhero1 + ", " + superhero2);  
  
        superheroDao.deleteAll();  
        Log.d(TAG, "Таблица Superhero очищена.");  
  
        superheroDao.insert(superhero1, superhero2);  
        Log.d(TAG, "Супергерои вставлены в базу.");  
  
        List<Superhero> allSuperheroes = superheroDao.getAll();  
        Log.d(TAG, "Загружены все супергерои:");  
        for (Superhero hero : allSuperheroes) {  
            Log.d(TAG, hero.toString());  
        }  
  
        Superhero firstSuperhero = superheroDao.getById(1);  
  
        if (firstSuperhero != null) {  
            Log.d(TAG, "Загружен супергерой по ID=1: " + firstSuperhero.toString());  
            firstSuperhero.superpower = "Enhanced Spider-Sense";  
  
            superheroDao.update(firstSuperhero);  
            Log.d(TAG, "Супергерой с ID=1 обновлен: " + firstSuperhero.toString());  
  
            Superhero updatedSuperhero = superheroDao.getById(1);  
            Log.d(TAG, "Супергерой с ID=1 после обновления: " + updatedSuperhero.toString());  
  
        } else {  
            Log.w(TAG, "Супергерой с ID=1 не найден после вставки.");  
        }  
  
        List<Superhero> finalSuperheroes = superheroDao.getAll();  
        Log.d(TAG, "Все супергерои после всех операций:");  
        if (finalSuperheroes.isEmpty()) {  
            Log.d(TAG, "Таблица пуста.");  
        } else {  
            for (Superhero hero : finalSuperheroes) {  
                Log.d(TAG, hero.toString());  
            }  
        }  
  
        db.close();  
        App.getInstance().deleteDatabase();  
    }  
}
```

#### App.java
``` java
package ru.mirea.bublikov.employeedb;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    private static final String DATABASE_NAME = "superheroes_database";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public boolean deleteDatabase() {
        return getApplicationContext().deleteDatabase(DATABASE_NAME);
    }
}

```

#### AppDatabase.java
``` java
package ru.mirea.bublikov.employeedb;  
  
import androidx.room.Database;  
import androidx.room.RoomDatabase;  
  
@Database(entities = {Superhero.class}, version = 1)  
public abstract class AppDatabase extends RoomDatabase {  
    public abstract SuperheroDao superheroDao();  
}
```

#### Superhero.java
``` java
package ru.mirea.bublikov.employeedb;  
  
import androidx.room.Entity;  
import androidx.room.PrimaryKey;  
  
@Entity  
public class Superhero {  
    @PrimaryKey(autoGenerate = true)  
    public long id;  
  
    public String name;  
    public String superpower;  
  
    public Superhero() {  
    }  
  
    public Superhero(String name, String superpower) {  
        this.name = name;  
        this.superpower = superpower;  
    }  
  
    @Override  
    public String toString() {  
        return "Superhero{" +  
                "id=" + id +  
                ", name='" + name + '\'' +  
                ", superpower='" + superpower + '\'' +  
                '}';  
    }  
}
```

#### SuperheroDao.java
``` java
package ru.mirea.bublikov.employeedb;  
  
import androidx.room.Dao;  
import androidx.room.Delete;  
import androidx.room.Insert;  
import androidx.room.OnConflictStrategy;  
import androidx.room.Query;  
import androidx.room.Update;  
  
import java.util.List;  
  
@Dao  
public interface SuperheroDao {  
    @Query("SELECT * FROM superhero")  
    List<Superhero> getAll();  
  
    @Query("SELECT * FROM superhero WHERE id = :id")  
    Superhero getById(long id);  
  
    @Insert(onConflict = OnConflictStrategy.REPLACE)  
    void insert(Superhero... superheroes);  
  
    @Update  
    void update(Superhero... superheroes);  
  
    @Delete  
    void delete(Superhero... superheroes);  
  
    @Query("DELETE FROM superhero")  
    void deleteAll();  
}
```

## **Mirea Project**
В проект "MireaProject" добавлены фрагменты "Профиль" и "Работа с файлами".
Фрагмент "Профиль": Позволяет пользователю вводить и сохранять/загружать параметры профиля (Имя, Возраст, Цвет) с использованием SharedPreferences.
Фрагмент "Работа с файлами": Содержит FAB для вызова диалога создания записи, поле ввода имени файла и кнопку "Извлечь секрет" для чтения файла и извлечения скрытого сообщения.
Диалог создания записи: Позволяет ввести заголовок, содержание (обложку) и секретное сообщение. Реализована "обработка" - скрытие секретного сообщения внутри обложки методом текстовой стеганографии (изменение регистра букв), включая кодирование длины секрета. Результат сохраняется в файл.
Фрагменты интегрированы в навигацию приложения.

### Экраны
![6-MireaProject-1](https://github.com/user-attachments/assets/607c9c29-bbb8-4250-9534-ef869d7e923f)

![6-MireaProject-2](https://github.com/user-attachments/assets/228fd53e-88a3-435c-8d31-af9325da45e3)

![6-MireaProject-3](https://github.com/user-attachments/assets/3ca34aa3-4c29-4c86-b4e8-ea5bbca34021)

![6-MireaProject-4](https://github.com/user-attachments/assets/0a097f96-30e2-4910-8092-4aa68dc0c176)

![6-MireaProject-5](https://github.com/user-attachments/assets/edd5f346-9c41-49c2-abb3-55c6d0f9c8d0)

![6-MireaProject-6](https://github.com/user-attachments/assets/1667639c-6f03-4d65-beca-1a3d29421fbf)

![6-MireaProject-7](https://github.com/user-attachments/assets/4f9aca4e-423d-4f67-98af-d032b7703b84)

![6-MireaProject-8](https://github.com/user-attachments/assets/1095cd61-5a7d-4e73-ba79-193173bf4604)

![6-MireaProject-9](https://github.com/user-attachments/assets/daac9744-8733-4340-9017-3793d030f2e5)

![6-MireaProject-10](https://github.com/user-attachments/assets/953749cb-549a-453c-a8b8-631746f58c74)

![6-MireaProject-11](https://github.com/user-attachments/assets/0f82430e-e30c-4112-b00a-49ab42082d38)

#### Project_Code.txt
```
here is a sampLE blOcK of TExt. iT Doesn'T hAVE tO mAkE PeRFEct SEnse, AS lONg As it coNtains a GOod nUMbER oF ALPhABetIc chARacTeRs. tHE StEganOgraphy AlgoriThM WiLL manIPUlate tHE cAse of THese lEtTers tO hiDE thE sEcRET iNforMAtion. PleASe enSurE ThiS parT Is lONg enough.
```

### Код
#### ProfileFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfilePrefs";

    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_COLOR = "color";

    private static final String TAG = ProfileFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadProfileData();

        binding.buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return root;
    }

    private void saveProfileData() {
        String name = binding.editTextProfileName.getText().toString();
        String ageStr = binding.editTextProfileAge.getText().toString();
        String color = binding.editTextProfileColor.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_AGE, ageStr);
        editor.putString(KEY_COLOR, color);

        editor.apply();

        Log.d(TAG, "Профиль сохранен: Имя=" + name + ", Возраст=" + ageStr + ", Цвет=" + color);
        Toast.makeText(requireContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        String ageStr = sharedPreferences.getString(KEY_AGE, "");
        String color = sharedPreferences.getString(KEY_COLOR, "");

        binding.editTextProfileName.setText(name);
        binding.editTextProfileAge.setText(ageStr);
        binding.editTextProfileColor.setText(color);

        Log.d(TAG, "Профиль загружен: Имя=" + name + ", Возраст=" + ageStr + ", Цвет=" + color);
        Toast.makeText(requireContext(), "Профиль загружен", Toast.LENGTH_SHORT).show();
    }
}
```

#### FileManagerFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentFileManagerBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileManagerFragment extends Fragment {

    private FragmentFileManagerBinding binding;
    private TextView textViewInfo;
    private FloatingActionButton fabAddNote;

    private EditText editTextFileNameToExtract;
    private Button buttonExtractSecret;

    private static final String TAG = FileManagerFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FileManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileManagerFragment newInstance(String param1, String param2) {
        FileManagerFragment fragment = new FileManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewInfo = binding.textViewFileManagerInfo;
        fabAddNote = binding.fabAddNote;

        editTextFileNameToExtract = binding.editTextFileNameToExtract;
        buttonExtractSecret = binding.buttonExtractSecret;

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateNoteDialog();
            }
        });

        buttonExtractSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractSecret();
            }
        });

        return root;
    }

    private void extractSecret() {
        String fileName = editTextFileNameToExtract.getText().toString().trim();

        if (fileName.isEmpty()) {
            Toast.makeText(requireContext(), "Введите имя файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
        }

        File file = new File(requireContext().getFilesDir(), fileName);

        if (!file.exists()) {
            Log.w(TAG, "Файл не найден для извлечения: " + file.getAbsolutePath());
            Toast.makeText(requireContext(), "Файл \"" + fileName + "\" не найден.", Toast.LENGTH_SHORT).show();
            return;
        }

        String stegoContent = readFileContent(file);

        if (stegoContent == null) {
            Toast.makeText(requireContext(), "Ошибка чтения файла или файл пуст.", Toast.LENGTH_SHORT).show();
            return;
        }

        String extractedSecret = CreateNoteDialogFragment.extractSecretMessage(stegoContent);

        if (extractedSecret != null) {
            Toast.makeText(requireContext(), "Извлеченный секрет:\n" + extractedSecret, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Извлечен секрет: " + extractedSecret);
            textViewInfo.setText("Извлеченный секрет:\n" + extractedSecret);
        } else {
            Toast.makeText(requireContext(), "Не удалось извлечь секрет.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Метод извлечения секрета вернул null.");
        }
    }

    private String readFileContent(File file) {
        FileInputStream fin = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            fin = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            Log.d(TAG, "Файл успешно прочитан: " + file.getName());
            return stringBuilder.toString();

        } catch (IOException e) {
            Log.e(TAG, "Ошибка чтения файла: " + file.getName(), e);
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (fin != null) fin.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при закрытии потоков чтения!", e);
            }
        }
    }

    private void showCreateNoteDialog() {
        CreateNoteDialogFragment dialogFragment = new CreateNoteDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "CreateNoteDialog");
    }
}
```

#### CreateNoteDialogFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentCreateNoteDialogBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNoteDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNoteDialogFragment extends DialogFragment {

    private FragmentCreateNoteDialogBinding binding;
    private EditText editTextTitle;
    private EditText editTextContent;
    private EditText editTextSecretMessage;
    private Button buttonCancel;
    private Button buttonSave;

    private static final String TAG = CreateNoteDialogFragment.class.getSimpleName();

    private static final int LENGTH_BITS = 16;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateNoteDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNoteDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNoteDialogFragment newInstance(String param1, String param2) {
        CreateNoteDialogFragment fragment = new CreateNoteDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Новая запись");

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNoteDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextTitle = binding.editTextNoteTitle;
        editTextContent = binding.editTextNoteContent;
        editTextSecretMessage = binding.editTextSecretMessage;
        buttonCancel = binding.buttonCancel;
        buttonSave = binding.buttonSave;

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Нажата кнопка Отмена");
                dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String secretMessage = editTextSecretMessage.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(requireContext(), "Заголовок и содержание не могут быть пустыми", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Нажата кнопка Сохранить. Заголовок: " + title + ", Обложка: " + content + ", Секрет: " + secretMessage);

                String stegoContent = null;
                if (!secretMessage.isEmpty()) {
                    stegoContent = hideSecretMessage(content, secretMessage);
                    if (stegoContent == null) {
                        Toast.makeText(requireContext(), "Ошибка: обложка слишком короткая для скрытия секрета!", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Обложка слишком короткая для скрытия секрета.");
                        return;
                    }
                    Log.d(TAG, "Секрет скрыт в обложке. Результат (часть): " + stegoContent.substring(0, Math.min(stegoContent.length(), 50)) + "...");

                } else {
                    stegoContent = content;
                    Log.d(TAG, "Секретное сообщение отсутствует. Сохраняем только обложку.");
                }

                saveNoteToFile(title, stegoContent);

                dismiss();
            }
        });

        return root;
    }

    private String hideSecretMessage(String coverText, String secretMessage) {
        if (coverText == null || secretMessage == null) {
            Log.w(TAG, "hideSecretMessage: coverText or secretMessage is null.");
            return null;
        }

        byte[] secretBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        int secretLength = secretBytes.length;

        byte[] lengthBytes = new byte[] {
                (byte) (secretLength >> 8),
                (byte) secretLength
        };
        
        int totalBitsToHide = LENGTH_BITS + secretLength * 8;

        StringBuilder stegoTextBuilder = new StringBuilder();
        int bitsHiddenCount = 0;

        for (char coverChar : coverText.toCharArray()) {
            if (Character.isLetter(coverChar)) {
                if (bitsHiddenCount < totalBitsToHide) {
                    int bitToHide = getBitToHide(bitsHiddenCount, lengthBytes, secretBytes);

                    if (bitToHide == 0) {
                        stegoTextBuilder.append(Character.toLowerCase(coverChar));
                    } else {
                        stegoTextBuilder.append(Character.toUpperCase(coverChar));
                    }

                    bitsHiddenCount++;

                } else {
                    stegoTextBuilder.append(coverChar);
                }
            } else {
                stegoTextBuilder.append(coverChar);
            }
        }

        if (bitsHiddenCount < totalBitsToHide) {
            Log.w(TAG, "hideSecretMessage: Не хватило букв в обложке (" + bitsHiddenCount + ") для скрытия всех битов (" + totalBitsToHide + ") секрета.");
            return null;
        }

        return stegoTextBuilder.toString();
    }

    private static int getBitToHide(int bitsHiddenCount, byte[] lengthBytes, byte[] secretBytes) {
        int bitToHide;
        if (bitsHiddenCount < LENGTH_BITS) {
            int currentLengthByteIndex = bitsHiddenCount / 8;
            int currentLengthBitIndex = bitsHiddenCount % 8;

            byte currentLengthByte = lengthBytes[currentLengthByteIndex];
            bitToHide = (currentLengthByte >> (7 - currentLengthBitIndex)) & 1;
        } else {
            int secretBitsCount = bitsHiddenCount - LENGTH_BITS;
            int currentSecretByteIndex = secretBitsCount / 8;
            int currentSecretBitIndex = secretBitsCount % 8;

            byte currentSecretByte = secretBytes[currentSecretByteIndex];
            bitToHide = (currentSecretByte >> (7 - currentSecretBitIndex)) & 1;
        }
        return bitToHide;
    }

    private void saveNoteToFile(String title, String stegoContent) {
        String fileName = title.replaceAll("[^a-zA-Z0-9-_.]", "_");

        if (fileName.isEmpty()) {
            fileName = "untitled_note";
        }
        fileName += ".txt";

        FileOutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            outputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

            outputStreamWriter.write(stegoContent);

            Log.d(TAG, "Заметка со скрытым секретом успешно записана: " + fileName);
            Toast.makeText(requireContext(), "Запись \"" + title + "\" сохранена (с секретом)", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "Ошибка записи заметки в файл!", e);
            Toast.makeText(requireContext(), "Ошибка при сохранении записи", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при закрытии потока записи заметки!", e);
            }
        }
    }

    public static String extractSecretMessage(String stegoText) {
        if (stegoText == null) {
            Log.w(TAG, "extractSecretMessage: stegoText is null.");
            return null;
        }

        List<Integer> bits = new ArrayList<>();
        for (char stegoChar : stegoText.toCharArray()) {
            if (Character.isLetter(stegoChar)) {
                if (Character.isLowerCase(stegoChar)) {
                    bits.add(0);
                } else {
                    bits.add(1);
                }
            }
        }

        if (bits.size() < LENGTH_BITS) {
            Log.w(TAG, "extractSecretMessage: Недостаточно битов (" + bits.size() + ") для извлечения длины секрета (" + LENGTH_BITS + ").");
            return null;
        }

        int secretLength = 0;
        for (int i = 0; i < LENGTH_BITS; i++) {
            secretLength = (secretLength << 1) | bits.get(i);
        }
        Log.d(TAG, "extractSecretMessage: Извлеченная длина секрета (в байтах): " + secretLength);

        if (secretLength < 0) {
            Log.w(TAG, "extractSecretMessage: Извлечена некорректная длина секрета (отрицательная).");
            return null;
        }

        int totalHiddenBits = LENGTH_BITS + secretLength * 8;

        if (bits.size() < totalHiddenBits) {
            Log.w(TAG, "extractSecretMessage: Недостаточно битов (" + bits.size() + ") для извлечения всего секрета (ожидается " + totalHiddenBits + ").");
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int currentByte = 0;
        int bitCount = 0;

        for (int i = LENGTH_BITS; i < totalHiddenBits; i++) {
            int bit = bits.get(i);
            currentByte = (currentByte << 1) | bit;
            bitCount++;
            if (bitCount == 8) {
                baos.write(currentByte);
                currentByte = 0;
                bitCount = 0;
            }
        }

        byte[] extractedBytes = baos.toByteArray();
        try {
            String secret = new String(extractedBytes, StandardCharsets.UTF_8);
            Log.d(TAG, "extractSecretMessage: Извлечено байт: " + extractedBytes.length + ", Секрет: " + secret);
            return secret;
        } catch (Exception e) {
            Log.e(TAG, "extractSecretMessage: Ошибка при преобразовании байт секрета в строку!", e);
            return null;
        }
    }
}
```
