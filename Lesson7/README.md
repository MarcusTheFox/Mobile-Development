# Отчёт 7

## **TimeService**
Создан модуль TimeService, демонстрирующий получение времени с удаленного сервера по сокету.
В AndroidManifest.xml добавлено разрешение INTERNET.
Приложение использует AsyncTask для подключения к серверу времени (time.nist.gov:13) в фоновом потоке.
Полученная строка времени разбирается и отображается в TextView.
### Экраны
![1-TimeService-1](https://github.com/user-attachments/assets/c1d3ed1b-8c67-440a-b57b-7bd517e2097a)

![1-TimeService-2](https://github.com/user-attachments/assets/54230a07-9bbc-42ee-90f1-0a0a6f2a8ec4)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.timeservice;  
  
import android.os.AsyncTask;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
  
import androidx.appcompat.app.AppCompatActivity;  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.net.Socket;  
  
import ru.mirea.bublikov.timeservice.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
    private ActivityMainBinding binding;  
    private final String host = "time.nist.gov";  
    private final int port = 13;  
    private static final String TAG = MainActivity.class.getSimpleName();  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        binding.buttonGetTime.setOnClickListener(new View.OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                GetTimeTask timeTask = new GetTimeTask();  
                timeTask.execute();  
            }  
        });  
    }  
  
    private class GetTimeTask extends AsyncTask<Void, Void, String> {  
  
        @Override  
        protected String doInBackground(Void... voids) {  
            String timeResult = "";  
            try {  
                Socket socket = new Socket(host, port);  
                BufferedReader reader = SocketUtils.getReader(socket);  
                reader.readLine();  
                timeResult = reader.readLine();  
                Log.d(TAG, timeResult);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return timeResult;  
        }  
  
        @Override  
        protected void onPostExecute(String result) {  
            super.onPostExecute(result);  
            String[] parts = result.split(" ");  
            if (parts.length < 3) return;  
  
            String date = parts[1];  
            String time = parts[2];  
            binding.textViewTime.setText("Дата: " + date + "\nВремя: " + time);  
        }  
    }  
}
```

#### SocketUtils.java
``` java
package ru.mirea.bublikov.timeservice;  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.Socket;  
  
public class SocketUtils {  
  
    public static BufferedReader getReader(Socket s) throws IOException {  
        return (new BufferedReader(new InputStreamReader(s.getInputStream())));  
    }  
  
    public static PrintWriter getWriter(Socket s) throws IOException {  
        return (new PrintWriter(s.getOutputStream(), true));  
    }  
}
```

## **HttpURLConnection**
Создан новый модуль HttpURLConnection, демонстрирующий сетевые запросы по HTTP.
Приложение определяет внешний IP, местоположение и получает погоду.
Используется AsyncTask для выполнения HTTP GET запросов к API (ipinfo.io и open-meteo.com) в фоновом потоке.
Класс HttpURLConnection используется для установки соединений и чтения данных.
Ответы в формате JSON парсятся для извлечения IP, города, региона и температуры.
Полученная информация отображается в отдельных TextView.
### Экраны
![2-HttpURLConnection-1](https://github.com/user-attachments/assets/342888ba-e8d6-46ad-81ab-a6dd93064cdb)

![2-HttpURLConnection-2](https://github.com/user-attachments/assets/074a5e67-5706-470e-96d1-165fe9dccfb3)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import ru.mirea.bublikov.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String IP_INFO_URL = "https://ipinfo.io/json";
    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1/forecast?current_weather=true&latitude=%f&longitude=%f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGetInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (connectivityManager != null) {
                    networkinfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkinfo != null && networkinfo.isConnected()) {
                    new DownloadInfoTask().execute(IP_INFO_URL);
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                    binding.textViewStatus.setText("Статус: Нет интернета");
                }
            }
        });
    }

    private class DownloadInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textViewStatus.setText("Статус: Загрузка...");
            binding.textViewIP.setText("IP: ");
            binding.textViewCity.setText("Город: ");
            binding.textViewRegion.setText("Регион: ");
            binding.textViewWeather.setText("Погода: ");
            binding.buttonGetInfo.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String ipInfoJson = downloadIpInfo(urls[0]);
                JSONObject response = new JSONObject(ipInfoJson);

                String ip = response.optString("ip", "N/A");
                String city = response.optString("city", "N/A");
                String region = response.optString("region", "N/A");
                String loc = response.optString("loc", null);

                StringBuilder resultBuilder = new StringBuilder();
                resultBuilder.append("IP: ").append(ip).append("\n");
                resultBuilder.append("Город: ").append(city).append("\n");
                resultBuilder.append("Регион: ").append(region).append("\n");

                if (!loc.isEmpty()) {
                    String[] latLon = loc.split(",");
                    if (latLon.length == 2) {
                        double latitude = Double.parseDouble(latLon[0]);
                        double longitude = Double.parseDouble(latLon[1]);

                        String weatherUrl = String.format(Locale.ENGLISH, WEATHER_BASE_URL, latitude, longitude);

                        String weatherJson = downloadIpInfo(weatherUrl);

                        JSONObject weatherResponse = new JSONObject(weatherJson);
                        JSONObject currentWeather = weatherResponse.optJSONObject("current_weather");

                        if (currentWeather != null) {
                            double temperature = currentWeather.optDouble("temperature", Double.NaN);

                            if (!Double.isNaN(temperature)) {
                                resultBuilder.append("Погода: ").append(temperature).append("°C");
                            }
                        }
                    }
                }

                return resultBuilder.toString();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String[] lines = result.split("\n");
            for (String line : lines) {
                if (line.startsWith("IP: ")) {
                    binding.textViewIP.setText(line);
                } else if (line.startsWith("Город: ")) {
                    binding.textViewCity.setText(line);
                } else if (line.startsWith("Регион: ")) {
                    binding.textViewRegion.setText(line);
                } else if (line.startsWith("Погода: ")) {
                    binding.textViewWeather.setText(line);
                } else if (line.startsWith("Ошибка")) {
                    binding.textViewStatus.setText("Статус: " + line);
                }
            }

            if (!result.startsWith("Ошибка")) {
                binding.textViewStatus.setText("Статус: Загрузка завершена");
            }

            binding.buttonGetInfo.setEnabled(true);
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }
}
```

## **FirebaseAuth**
Создан новый модуль FirebaseAuth для аутентификации с Firebase (Email/Password).
Модуль подключен к проекту Firebase, включен метод Email/Password.
Добавлены зависимости Firebase Auth SDK.
Создан UI для ввода email/пароля и кнопок действий (создание, вход, выход, верификация).
В MainActivity реализована логика: инициализация FirebaseAuth, методы createAccount, signIn, signOut, sendEmailVerification.
Метод updateUI управляет видимостью элементов в зависимости от статуса авторизации.
Создан пользователь через приложение, сделан скриншот приложения (авторизованный пользователь) и консоли Firebase (список пользователей), помещены в res/raw.
### Экраны
![3-FirebaseAuth-1](https://github.com/user-attachments/assets/586b3392-449c-4470-a009-d7e77709ed57)

![3-FirebaseAuth-2](https://github.com/user-attachments/assets/57d22c1f-b4cb-46c2-9ddd-cd8e186b4599)

![3-FirebaseAuth-3](https://github.com/user-attachments/assets/4fcdaaa7-1f24-4e27-9008-da96d928d938)

![3-FirebaseAuth-4](https://github.com/user-attachments/assets/023c23c8-b0e5-49b4-8ae4-b6d1f749e0a4)

![3-FirebaseAuth-5](https://github.com/user-attachments/assets/c1ec1e54-96cc-44db-a047-60b8d090162a)

![3-FirebaseAuth-6](https://github.com/user-attachments/assets/905c2c03-c6cd-44b1-b026-d50da1b73179)

![3-FirebaseAuth-7](https://github.com/user-attachments/assets/12f86e2f-4453-479a-a74e-88cfbfe0bbf9)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.firebaseauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.bublikov.firebaseauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.buttonCreateAccount.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            createAccount(email, password);
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            signIn(email, password);
        });

        binding.buttonSignOut.setOnClickListener(v -> signOut());

        binding.buttonVerifyEmail.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.statusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
            binding.detailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            binding.detailTextView.setVisibility(View.VISIBLE);
            binding.emailPasswordButtons.setVisibility(View.GONE);
            binding.emailPasswordFields.setVisibility(View.GONE);
            binding.signedInButtons.setVisibility(View.VISIBLE);
            binding.buttonVerifyEmail.setEnabled(!user.isEmailVerified());
        } else {
            binding.statusTextView.setText(R.string.signed_out);
            binding.detailTextView.setText(null);
            binding.emailPasswordButtons.setVisibility(View.VISIBLE);
            binding.emailPasswordFields.setVisibility(View.VISIBLE);
            binding.signedInButtons.setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.editTextEmail.setError("Required.");
            valid = false;
        } else {
            binding.editTextEmail.setError(null);
        }

        String password = binding.editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.editTextPassword.setError("Required.");
            valid = false;
        } else {
            binding.editTextPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(MainActivity.this, "Аккаунт успешно создан.", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Ошибка создания аккаунта.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(MainActivity.this, "Авторизация успешна.", Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Ошибка авторизации.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        binding.buttonVerifyEmail.setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(MainActivity.this, "Необходимо авторизоваться для верификации.", Toast.LENGTH_SHORT).show();
            binding.buttonVerifyEmail.setEnabled(true);
            return;
        }

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.buttonVerifyEmail.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Письмо верификации отправлено на " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Ошибка при отправке письма верификации.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
```

## **MireaProject**
В проект MireaProject добавлен экран входа через Firebase и фрагмент для отображения сетевых данных.
Создана новая Activity SignInActivity с UI для входа/регистрации Firebase Email/Password.
SignInActivity сделана точкой входа в приложение путем изменения AndroidManifest.xml.
После успешного входа в SignInActivity, пользователь перенаправляется на MainActivity. Если пользователь уже авторизован при запуске SignInActivity, перенаправление происходит сразу.
Создан новый фрагмент NetworkDataFragment с UI для отображения сетевых данных.
В NetworkDataFragment реализована загрузка данных из сетевого ресурса (https://jsonplaceholder.typicode.com/posts/1) с использованием AsyncTask и HttpURLConnection и отображение результата в TextView.
Фрагмент NetworkDataFragment добавлен в Navigation Graph и меню Navigation Drawer, обновлена AppBarConfiguration в MainActivity.
### Экраны
![4-MireaProject-1](https://github.com/user-attachments/assets/df03ae1d-c9c1-4357-9686-c4b9a4ed7322)

![4-MireaProject-2](https://github.com/user-attachments/assets/74265c64-cbb4-41ff-8bcf-2054e0e281bc)

![4-MireaProject-3](https://github.com/user-attachments/assets/aa5beebd-9b1e-4e6d-83a6-27c7d2dc08d9)

![4-MireaProject-4](https://github.com/user-attachments/assets/ef3ef007-75e4-4cb5-9678-e7b6cd14ac3b)

![4-MireaProject-5](https://github.com/user-attachments/assets/62fe5297-e885-467d-b640-a64b4e9f314b)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.Bublikov.mireaproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.dataFragment, R.id.webViewFragment, R.id.backgroundTaskFragment,
                R.id.hardwareFragment, R.id.profileFragment, R.id.fileManagerFragment,
                R.id.networkDataFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
```

#### AndroidManifest.xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MireaProject"
        tools:targetApi="31">
        <activity
            android:name=".SignInActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".MainActivity"
            android:exported="false"
            android:label="@string/app_name"
            android:theme="@style/Theme.MireaProject.NoActionBar">
        </activity>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="ru.mirea.Bublikov.mireaproject.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/paths" />
        </provider>
    </application>

</manifest>
```

#### SignInActivity.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mirea.Bublikov.mireaproject.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    private ActivitySignInBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.buttonCreateAccount.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            createAccount(email, password);
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            signIn(email, password);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.editTextEmail.setError("Required.");
            valid = false;
        } else {
            binding.editTextEmail.setError(null);
        }

        String password = binding.editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.editTextPassword.setError("Required.");
            valid = false;
        } else {
            binding.editTextPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignInActivity.this, "Аккаунт создан успешно.", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Ошибка создания аккаунта.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignInActivity.this, "Авторизация успешна.", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Ошибка авторизации.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
```

#### NetworkDataFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentNetworkDataBinding;

public class NetworkDataFragment extends Fragment {

    private FragmentNetworkDataBinding binding;
    private TextView textViewStatus;
    private TextView textViewData;
    private Button buttonFetch;

    private static final String TAG = "NetworkDataFrag";

    private static final String DATA_URL = "https://jsonplaceholder.typicode.com/posts/1";

    public NetworkDataFragment() {
        // Required empty public constructor
    }

    public static NetworkDataFragment newInstance() {
        return new NetworkDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewStatus = binding.textViewNetworkDataStatus;
        textViewData = binding.textViewNetworkData;
        buttonFetch = binding.buttonFetchData;

        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadDataTask().execute(DATA_URL);
                } else {
                    Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
                    textViewStatus.setText("Статус: Нет интернета");
                }
            }
        });

        return root;
    }
    
    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewStatus.setText("Статус: Загрузка...");
            textViewData.setText("");
            buttonFetch.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            if (urls == null || urls.length == 0 || urls[0] == null) {
                return "Ошибка: URL не предоставлен.";
            }
            String address = urls[0];
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            String data = "";

            try {
                URL url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.close();

                    data = bos.toString("UTF-8");
                } else {
                    data = "Ошибка HTTP: " + connection.getResponseMessage() + ". Код: " + responseCode;
                }
            } catch (IOException e) {
                data = "Ошибка сети: " + e.getMessage();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Ошибка закрытия потока!", e);
                }
                if (connection != null) connection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "AsyncTask: onPostExecute с результатом: " + result);

            if (result != null && !result.startsWith("Ошибка")) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String title = jsonResponse.optString("title", "N/A");
                    String body = jsonResponse.optString("body", "N/A");
                    String formattedData = "Заголовок:\n" + title + "\n\n" + "Содержание:\n" + body;
                    textViewData.setText(formattedData);
                    textViewStatus.setText("Статус: Загрузка завершена");
                    Toast.makeText(requireContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                textViewStatus.setText("Статус: " + result);
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }

            buttonFetch.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        textViewStatus = null;
        textViewData = null;
        buttonFetch = null;
    }
}
```
