# Отчёт 4

## **Music Player**
Создан новый проект `Lesson4` с использованием `View Binding` для связывания графических компонентов с кодом.
- Включена функция View Binding путем добавления `buildFeatures { viewBinding = true }` в `build.gradle`.
- Создана разметка `activity_main.xml` для портретной и альбомной ориентации.
- В `MainActivity.java` продемонстрировано использование сгенерированного класса `ActivityMainBinding`: 
	- создание объекта класса через `inflate`, 
	- установка корневого `View` через `binding.getRoot()` в `setContentView`, 
	- доступ к отдельным компонентам разметки через поля binding объекта.
- Реализованы примеры слушателей событий для кнопок, показывающие, как взаимодействовать с элементами через `View Binding`.

### Экраны
![1-MusicPlayer-1](https://github.com/user-attachments/assets/64b5e165-10e0-448e-b3d0-793ff17c855c)

![1-MusicPlayer-2](https://github.com/user-attachments/assets/3d015704-7bbd-4a63-a527-61aff94e14d3)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.lesson4;  
  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
import ru.mirea.bublikov.lesson4.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
    private ActivityMainBinding binding;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        View rootView = binding.getRoot();  
        setContentView(rootView);  
  
        binding.textViewSongTitle.setText("Без названия");  
        binding.textViewArtist.setText("Мой номер по списку №4");  
  
        binding.buttonPlayPause.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Log.d(MainActivity.class.getSimpleName(), "Play/Pause Button Clicked");  
            }  
        });  
  
        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Log.d(MainActivity.class.getSimpleName(), "Previous Button Clicked");  
            }  
        });  
  
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Log.d(MainActivity.class.getSimpleName(), "Next Button Clicked");  
            }  
        });  
    }  
}
```

## **Thread**
Создан новый модуль `Thread`, демонстрирующий работу с главным и фоновыми потоками.
- В модуле активирована функция `View Binding`.
- Разработана разметка `activity_main.xml`, содержащая `TextView` для отображения информации о главном потоке, `EditText` для ввода данных (общее количество пар, учебные дни), `Button` для запуска вычислений и `TextView` для вывода результата.
- В `MainActivity.java` продемонстрировано:
    - Получение информации о главном потоке `Thread.currentThread()`, изменение его имени и вывод его стека вызовов и группы в `Logcat`.
    - Реализация обработчика нажатия кнопки, который запускает новый фоновый поток `new Thread(new Runnable() { ... }).start()`.
    - Выполнение расчета среднего количества пар в день в фоновом потоке.
    - Имитация длительной операции в фоновом потоке с использованием `wait()`.

### Экраны
![2-Thread-1](https://github.com/user-attachments/assets/0ca8c75d-8578-429c-b5de-2010f777a098)

![2-Thread-2](https://github.com/user-attachments/assets/09f611ad-5299-442e-abf1-1e160b72a4c9)

### Логи
![2-Thread-3](https://github.com/user-attachments/assets/665d1fc2-38e0-4989-b45d-b856b88b5dea)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.thread;  
  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.TextView;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
import java.util.Arrays;  
  
import ru.mirea.bublikov.thread.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
  
    private ActivityMainBinding binding;  
    private int counter = 0;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        View rootView = binding.getRoot();  
        setContentView(rootView);  
  
        TextView mainThreadInfoTextView = binding.textViewMainThreadInfo;  
        Thread mainThread = Thread.currentThread();  
        mainThreadInfoTextView.setText("Имя текущего потока: " + mainThread.getName());  
        // Меняем имя и выводим в текстовом поле  
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-09-22, НОМЕР ПО СПИСКУ: 4, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: МАТРИЦА");  
        mainThreadInfoTextView.append("\nНовое имя потока: " + mainThread.getName());  
  
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));  
        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());  
  
        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                new Thread(new Runnable() {  
                    @Override  
                    public void run() {  
                        int numberThread = counter++;  
                        Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %d ", numberThread, "БСБО-09-22", 4));  
  
                        String totalPairsStr = binding.editTextTotalPairs.getText().toString();  
                        String studyDaysStr = binding.editTextStudyDays.getText().toString();  
  
                        int totalPairs = Integer.parseInt(totalPairsStr);  
                        int studyDays = Integer.parseInt(studyDaysStr);  
                        double averagePairsPerDay = (double) totalPairs / studyDays;  
                        String resultMessage = String.format("Среднее количество пар в день: %.2f", averagePairsPerDay);  
  
                        long endTime = System.currentTimeMillis() + 20 * 1000;  
  
                        while (System.currentTimeMillis() < endTime) {  
                            synchronized (this) {  
                                try {  
                                    wait(endTime - System.currentTimeMillis());  
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);  
                                } catch (Exception e) {  
                                    throw new RuntimeException(e);  
                                }  
                            }  
                            Log.d("ThreadProject", "Выполнен поток № " + numberThread);  
                        }  
  
                        runOnUiThread(new Runnable() {  
                            @Override  
                            public void run() {  
                                binding.textViewResult.setText(resultMessage);  
                            }  
                        });  
                    }  
                }).start();  
            }  
        });  
    }  
}
```

## **Data Thread**
Создан модуль `data_thread` для демонстрации способов обновления UI из фонового потока.
Разработана разметка `activity_main.xml` с `TextView` для вывода результатов выполнения.
Реализованы три Runnable объекта, каждый из которых отображает свою строку в TextView при выполнении в UI-потоке.
Из фонового потока запланировано выполнение Runnable в UI-потоке с использованием `runOnUiThread()`, `View.post()` и `View.postDelayed()`.
В `TextView` добавлено объяснение различий между этими методами и описание последовательности их выполнения.

### Экраны
![3-data_thread-1](https://github.com/user-attachments/assets/198386cc-b0bb-495e-a12c-112518de23d0)

![3-data_thread-2](https://github.com/user-attachments/assets/65b274b9-f03f-4e29-9c57-1d0c75779cc7)

### Логи
![3-data_thread-3](https://github.com/user-attachments/assets/69d81acc-4e80-4565-8eaa-53033d7938c3)

### Код
#### MainActivity.java
``` java
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
```

## **Looper**
Создан модуль `looper`, демонстрирующий взаимодействие между потоками с использованием `Looper` и `Handler`.
- Создан пользовательский поток `MyLooper` с собственным `Looper` и `Handler` для обработки сообщений.
- MyLooper принимает сообщения, содержащие возраст и профессию, имитирует длительную работу с задержкой, равной возрасту (в секундах), и отправляет результат обратно в главный поток.
- В MainActivity:
	- Создан Handler для главного потока для приема результатов.
	- Запущен поток MyLooper.
	- Обработчик кнопки получает данные из полей ввода, формирует сообщение и отправляет его в MyLooper через его Handler.

### Экраны
![4-looper-1](https://github.com/user-attachments/assets/c7189300-81b8-4649-9336-ffa984d3bfc6)

### Логи
![4-looper-2](https://github.com/user-attachments/assets/a8ee31b6-6543-423f-8a63-0bc10d9976a5)

### Код
#### MainActivity.java
``` java
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
```

#### MyLooper.java
``` java
package ru.mirea.bublikov.looper;  
  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Looper;  
import android.os.Message;  
import android.util.Log;  
  
import java.util.concurrent.TimeUnit;  
  
public class MyLooper extends Thread {  
    public Handler mHandler;  
    private Handler mainHandler;  
    public MyLooper(Handler mainThreadHandler) {  
        mainHandler = mainThreadHandler;  
    }  
  
    public void run() {  
        Log.d("MyLooper", "run");  
        Looper.prepare();  
        mHandler = new Handler(Looper.myLooper()) {  
            public void handleMessage(Message msg) {  
                Bundle bundle = msg.getData();  
                int age = bundle.getInt("age");  
                String profession = bundle.getString("profession");  
                Log.d("MyLooper get message: ", String.format("Получено сообщение: Возраст = %d, Профессия = %s", age, profession));  
  
                try {  
                    TimeUnit.SECONDS.sleep(age);  
                } catch (InterruptedException e) {  
                    throw new RuntimeException(e);  
                }  
  
                String resultString = String.format("Выполнено: Возраст = %d, Профессия = %s", age, profession);  
                Log.d("MyLooper", "Результат вычисления: " + resultString);  
  
                Message message = Message.obtain();  
                Bundle resultBundle = new Bundle();  
                bundle.putString("result", resultString);  
                message.setData(resultBundle);  
                mainHandler.sendMessage(message);  
            }  
        };  
        Looper.loop();  
    }  
}
```

## **Crypto Loader**
Создан модуль `CryptoLoader`, демонстрирующий асинхронную обработку данных с использованием `Loader` и `LoaderManager`.
Реализован пользовательский загрузчик `CryptoTaskLoader` на базе `AsyncTaskLoader`.
`CryptoTaskLoader` выполняет дешифрование строки, получая зашифрованные данные и ключ через `Bundle`.
В `MainActivity` реализован интерфейс `LoaderManager.LoaderCallbacks`.
При нажатии на кнопку фраза шифруется (AES) и передается вместе с ключом в `CryptoTaskLoader` через `LoaderManager.initLoader()`.
После завершения загрузки в `onLoadFinished`, дешифрованная фраза отображается с помощью `Toast`.

### Экраны
![5-CryptoLoader-1](https://github.com/user-attachments/assets/f780751b-e725-4aa9-9279-c4250de737b9)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.cryptoloader;  
  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.Toast;  
  
import androidx.annotation.NonNull;  
import androidx.annotation.Nullable;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.loader.app.LoaderManager;  
import androidx.loader.content.Loader;  
  
import java.security.InvalidKeyException;  
import java.security.InvalidParameterException;  
import java.security.NoSuchAlgorithmException;  
import java.security.SecureRandom;  
  
import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.KeyGenerator;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  
  
import ru.mirea.bublikov.cryptoloader.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {  
  
    private ActivityMainBinding binding;  
    public final String TAG = this.getClass().getSimpleName();  
    private final int LoaderID = 1234;  
  
    private SecretKey secretKey;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        secretKey = generateKey();  
  
        binding.buttonEncryptAndDecrypt.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                String phrase = binding.editTextPhrase.getText().toString();  
                byte[] encryptedPhrase = encryptMsg(phrase, secretKey);  
                Log.d(TAG, "Фраза зашифрована");  
  
                Bundle bundle = new Bundle();  
                bundle.putByteArray(CryptoTaskLoader.ARG_WORD, encryptedPhrase);  
                bundle.putByteArray(CryptoTaskLoader.ARG_SECRET_KEY, secretKey.getEncoded());  
  
                LoaderManager.getInstance(MainActivity.this).initLoader(LoaderID, bundle, MainActivity.this);  
            }  
        });  
    }  
  
    @Override  
    public void onLoaderReset(@NonNull Loader<String> loader) {  
        Log.d(TAG, "onLoaderReset");  
    }  
  
    @NonNull  
    @Override    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {  
        if (i == LoaderID) {  
            Toast.makeText(this, "onCreateLoader:" + i, Toast.LENGTH_SHORT).show();  
            return new CryptoTaskLoader(this, bundle);  
        }  
        throw new InvalidParameterException("Invalid loader id");  
    }  
  
    @Override  
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {  
        if (loader.getId() == LoaderID) {  
            Log.d(TAG, "onLoadFinished: " + s);  
            Toast.makeText(this, "onLoadFinished: " + s, Toast.LENGTH_LONG).show();  
        }  
    }  
  
    public static SecretKey generateKey(){  
        try {  
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");  
            sr.setSeed("any data used as random seed".getBytes());  
            KeyGenerator kg = KeyGenerator.getInstance("AES");  
            kg.init(256, sr);  
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");  
        } catch (NoSuchAlgorithmException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    public static byte[] encryptMsg(String message, SecretKey secret) {  
        Cipher cipher = null;  
        try {  
            cipher = Cipher.getInstance("AES");  
            cipher.init(Cipher.ENCRYPT_MODE, secret);  
            return cipher.doFinal(message.getBytes());  
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |  
                 BadPaddingException | IllegalBlockSizeException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
```

#### CryptoTaskLoader.java
``` java
package ru.mirea.bublikov.cryptoloader;  
  
import android.content.Context;  
import android.os.Bundle;  
import android.os.SystemClock;  
import android.util.Log;  
  
import androidx.annotation.NonNull;  
import androidx.annotation.Nullable;  
import androidx.loader.content.AsyncTaskLoader;  
  
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
  
import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  
  
public class CryptoTaskLoader extends AsyncTaskLoader<String> {  
  
    private final String TAG = CryptoTaskLoader.class.getSimpleName();  
    private byte[] encryptedText;  
    private byte[] secretKeyEncoded;  
  
    public static final String ARG_WORD = "word";  
    public static final String ARG_SECRET_KEY = "secret_key";  
  
    public CryptoTaskLoader(@NonNull Context context, Bundle args) {  
        super(context);  
        if (args != null) {  
            encryptedText = args.getByteArray(ARG_WORD);  
            secretKeyEncoded = args.getByteArray(ARG_SECRET_KEY);  
        }  
        Log.d(TAG, "CryptoTaskLoader создан");  
    }  
  
    @Override  
    protected void onStartLoading() {  
        super.onStartLoading();  
        forceLoad();  
    }  
  
    @Nullable  
    @Override    public String loadInBackground() {  
        Log.d(TAG, "loadInBackground: Начата дешифровка...");  
        SystemClock.sleep(5000);  
  
        SecretKey originalKey = new SecretKeySpec(secretKeyEncoded, 0, secretKeyEncoded.length, "AES");  
        Log.d(TAG, "loadInBackground: Ключ восстановлен.");  
  
        try {  
            String decryptedText = decryptMsg(encryptedText, originalKey);  
            Log.d(TAG, "loadInBackground: Дешифровка успешна. Результат: " + decryptedText);  
            return decryptedText;  
        } catch (RuntimeException e) {  
            Log.e(TAG, "loadInBackground: Ошибка при дешифровании!", e);  
            return "Ошибка дешифрования: " + e.getMessage();  
        }  
    }  
  
    public static String decryptMsg(byte[] cipherText, SecretKey secret) {  
        try {  
            Cipher cipher = Cipher.getInstance("AES");  
            cipher.init(Cipher.DECRYPT_MODE, secret);  
            byte[] decryptedBytes = cipher.doFinal(cipherText);  
            return new String(decryptedBytes);  
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException  
                 | BadPaddingException | InvalidKeyException e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
```

## **Service App**
Создан модуль `ServiceApp`, демонстрирующий работу с `Foreground Service`.
*   Приложение содержит сервис `PlayerService` и активность `MainActivity`.
*   В ресурсы добавлена музыкальная композиция (`.mp3`).
*   `PlayerService` использует `MediaPlayer` для воспроизведения аудио в фоновом режиме.
*   Сервис запускается как `Foreground Service` с постоянным уведомлением, содержащим название композиции, что позволяет музыке играть при свернутом приложении.
*   В `MainActivity` добавлены кнопки "Play" и "Stop" для запуска (`startForegroundService`) и остановки (`stopService`) сервиса.
*   В манифест добавлены необходимые разрешения.

### Экраны
![6-ServiceApp-1](https://github.com/user-attachments/assets/37d1c190-21f0-45e7-917b-aeb6bc325931)

![6-ServiceApp-2](https://github.com/user-attachments/assets/2ffff292-27e1-41e4-905a-7b249bda6284)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.serviceapp;  
  
import static android.Manifest.permission.FOREGROUND_SERVICE;  
import static android.Manifest.permission.POST_NOTIFICATIONS;  
  
import android.content.Intent;  
import android.content.pm.PackageManager;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.app.ActivityCompat;  
import androidx.core.content.ContextCompat;  
  
import ru.mirea.bublikov.serviceapp.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
    private ActivityMainBinding binding;  
    private int PermissionCode = 200;  
  
    private final String TAG = MainActivity.class.getSimpleName();  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        boolean postNotificationsGranted = ContextCompat.checkSelfPermission(this,  
                POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;  
        boolean foregroundServiceGranted = ContextCompat.checkSelfPermission(this,  
                FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;  
  
        if (postNotificationsGranted && foregroundServiceGranted) {  
            Log.d(TAG, "Разрешения получены");  
        } else {  
            Log.d(TAG, "Нет разрешений!");  
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);  
        }  
  
        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);  
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);  
            }  
        });  
  
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);  
                stopService(serviceIntent);  
            }  
        });  
    }  
}
```

#### PlayerService.java
``` java
package ru.mirea.bublikov.serviceapp;  
  
import android.app.NotificationChannel;  
import android.app.NotificationManager;  
import android.app.Service;  
import android.content.Intent;  
import android.media.MediaPlayer;  
import android.os.IBinder;  
  
import androidx.core.app.NotificationCompat;  
import androidx.core.app.NotificationManagerCompat;  
  
public class PlayerService extends Service {  
    private MediaPlayer mediaPlayer;  
    public static final String CHANNEL_ID = "ForegroundServiceChannel";  
    private static final String SONG_TITLE_FOR_NOTIFICATION = "Man or Machine - Extra Terra, Megan McDuffee";  
  
    public PlayerService() {  
    }  
  
    @Override  
    public IBinder onBind(Intent intent) {  
        throw new UnsupportedOperationException("Not yet implemented");  
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        mediaPlayer.start();  
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
            @Override  
            public void onCompletion(MediaPlayer mp) {  
                stopForeground(true);  
            }  
        });  
        return super.onStartCommand(intent, flags, startId);  
    }  
  
    @Override  
    public void onCreate() {  
        super.onCreate();  
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)  
                .setContentText(SONG_TITLE_FOR_NOTIFICATION)  
                .setSmallIcon(R.mipmap.ic_launcher)  
                .setPriority(NotificationCompat.PRIORITY_HIGH)  
                .setStyle(new NotificationCompat.BigTextStyle()  
                        .bigText("Сейчас играет:\n" +  
                                SONG_TITLE_FOR_NOTIFICATION))  
                .setContentTitle("Music Player")  
                .setOngoing(true);  
        int importance = NotificationManager.IMPORTANCE_DEFAULT;  
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student Бубликов Михаил Александрович Notification", importance);  
        channel.setDescription("MIREA Channel");  
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);  
        notificationManager.createNotificationChannel(channel);  
        startForeground(1, builder.build());  
  
        mediaPlayer = MediaPlayer.create(this, R.raw.music);  
        mediaPlayer.setLooping(false);  
    }  
  
    @Override  
    public void onDestroy() {  
        stopForeground(true);  
        mediaPlayer.stop();  
    }  
}
```

## **Work Manager**
Создан новый модуль `WorkManager` для демонстрации использования `WorkManager` и установки критериев запуска фоновых задач.
- В `build.gradle` добавлена зависимость `WorkManager`.
- Создан класс `UploadWorker`, наследующийся от `Worker`, с методом `doWork()`, имитирующим длительную фоновую операцию и логированием начала/конца работы.
- В `MainActivity` создан объект `Constraints.Builder` для определения условий запуска задачи (например, наличие нетарифицируемой сети и зарядка устройства).
- Создан объект `OneTimeWorkRequest.Builder` для однократного выполнения `UploadWorker`, к которому применены заданные ограничения.
- Задача поставлена в очередь `WorkManager` с помощью `WorkManager.getInstance(this).enqueue()`.

### Логи
![7-WorkManager-1](https://github.com/user-attachments/assets/8dee1154-91da-41c5-9989-f48a3d7cda39)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.workmanager;  
  
import android.os.Bundle;  
import android.util.Log;  
  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.work.Constraints;  
import androidx.work.NetworkType;  
import androidx.work.OneTimeWorkRequest;  
import androidx.work.WorkManager;  
import androidx.work.WorkRequest;  
  
public class MainActivity extends AppCompatActivity {  
    private static final String TAG = "MainActivity";  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        Log.d(TAG, "MainActivity onCreate");  
        Constraints constraints = new Constraints.Builder()  
                .setRequiredNetworkType(NetworkType.UNMETERED)  
                .setRequiresCharging(true)  
                .build();  
        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)  
                .setConstraints(constraints)  
                .build();  
        WorkManager.getInstance(this)  
                .enqueue(uploadWorkRequest);  
        Log.d(TAG, "Задача SampleWorker поставлена в очередь WorkManager с ограничениями: " + constraints);  
    }  
}
```

#### UploadWorker.java
``` java
package ru.mirea.bublikov.workmanager;  
  
import android.content.Context;  
import android.util.Log;  
  
import androidx.annotation.NonNull;  
import androidx.work.Worker;  
import androidx.work.WorkerParameters;  
  
import java.util.concurrent.TimeUnit;  
  
public class UploadWorker extends Worker {  
    private static final String TAG = "UploadWorker";  
  
    public UploadWorker(  
            @NonNull Context context,  
            @NonNull WorkerParameters params) {  
        super(context, params);  
    }  
  
    @NonNull  
    @Override
    public Result doWork() {  
        Log.d(TAG, "doWork: start");  
        try {  
            TimeUnit.SECONDS.sleep(10);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
  
        Log.d(TAG, "doWork: end");  
        return Result.success();  
    }  
}
```

## **Mirea Project**
В проект `MireaProject` добавлена возможность выполнения фоновых задач с помощью компонента `WorkManager`.
- Создан новый фрагмент `BackgroundTaskFragment`, с которого пользователь может запустить фоновую задачу.
- Фоновая задача выполняется в отдельном классе `BackgroundTaskWorker` в фоне.
- Задача настроена так, чтобы учитывать определенные условия запуска (наличие интернета и подключение к зарядке).
- На экране фрагмента отображается текущий статус выполнения задачи и полученный после ее завершения результат.

### Экраны
![8-MireaProject-1](https://github.com/user-attachments/assets/9b11ff1b-4937-4427-b455-e268d51c145d)

![8-MireaProject-2](https://github.com/user-attachments/assets/8bc31bb6-0048-4af2-9bd5-7bb900aeee9b)

![8-MireaProject-3](https://github.com/user-attachments/assets/945a168c-247f-4a1d-8ffd-6fdc398f845e)

![8-MireaProject-4](https://github.com/user-attachments/assets/34c281b5-10f5-4a35-9583-8bf41e84f2be)

![8-MireaProject-5](https://github.com/user-attachments/assets/5b180b72-381c-4a38-ae16-4d03a450a935)

### Код
#### BackgroundTaskWorker.java
``` java
package ru.mirea.Bublikov.mireaproject;  
  
import android.content.Context;  
import android.util.Log;  
  
import androidx.annotation.NonNull;  
import androidx.work.Data;  
import androidx.work.Worker;  
import androidx.work.WorkerParameters;  
  
import java.util.concurrent.TimeUnit;  
  
public class BackgroundTaskWorker extends Worker {  
    private static final String TAG = BackgroundTaskWorker.class.getSimpleName();  
    public static final String INPUT_DATA_KEY = "input_data";  
    public static final String OUTPUT_RESULT_KEY = "output_result";  
  
    public BackgroundTaskWorker(  
            @NonNull Context context,  
            @NonNull WorkerParameters params) {  
        super(context, params);  
    }  
  
    @NonNull  
    @Override    
    public Result doWork() {  
        Log.d(TAG, "doWork: Задача " + TAG + " началась");  
  
        String inputData = getInputData().getString(INPUT_DATA_KEY);  
        Log.d(TAG, "doWork: Получены входные данные: " + inputData);  
  
        try {  
            TimeUnit.SECONDS.sleep(5);  
        } catch (InterruptedException e) {  
            Log.d(TAG, "doWork: Задача " + TAG + " прервана", e);  
            return Result.failure();  
        }  
  
        Log.d(TAG, "doWork: Задача " + TAG + " завершена");  
  
        Data outputData = new Data.Builder()  
                .putString(OUTPUT_RESULT_KEY, "Результат обработки: " + inputData + " (завершено)")  
                .build();  
  
        return Result.success(outputData);  
    }  
}
```

#### BackgroundTaskFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;  
  
import android.os.Bundle;  
  
import androidx.fragment.app.Fragment;  
import androidx.lifecycle.Observer;  
import androidx.work.Constraints;  
import androidx.work.Data;  
import androidx.work.NetworkType;  
import androidx.work.OneTimeWorkRequest;  
import androidx.work.WorkInfo;  
import androidx.work.WorkManager;  
import androidx.work.WorkRequest;  
  
import android.util.Log;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.Button;  
import android.widget.TextView;  
  
import java.util.UUID;  
  
/**  
 * A simple {@link Fragment} subclass.  
 * Use the {@link BackgroundTaskFragment#newInstance} factory method to  
 * create an instance of this fragment. */
public class BackgroundTaskFragment extends Fragment {  
    private static final String TAG = BackgroundTaskFragment.class.getSimpleName();  
    private TextView textViewStatus;  
    private TextView textViewResult;  
    private Button buttonStartWorker;  
  
    // TODO: Rename parameter arguments, choose names that match  
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER  
    private static final String ARG_PARAM1 = "param1";  
    private static final String ARG_PARAM2 = "param2";  
  
    // TODO: Rename and change types of parameters  
    private String mParam1;  
    private String mParam2;  
  
    public BackgroundTaskFragment() {  
        // Required empty public constructor  
    }  
  
    /**  
     * Use this factory method to create a new instance of     
     * this fragment using the provided parameters.     
     *     
     * @param param1 Parameter 1.  
     * @param param2 Parameter 2.  
     * @return A new instance of fragment BackgroundTaskFragment.  
     */    
     // TODO: Rename and change types and number of parameters  
    public static BackgroundTaskFragment newInstance(String param1, String param2) {  
        BackgroundTaskFragment fragment = new BackgroundTaskFragment();  
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
        // Inflate the layout for this fragment  
        View root = inflater.inflate(R.layout.fragment_background_task, container, false);  
  
        textViewStatus = root.findViewById(R.id.textViewStatus);  
        textViewResult = root.findViewById(R.id.textViewResult);  
        buttonStartWorker = root.findViewById(R.id.buttonStartWorker);  
  
        buttonStartWorker.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                startBackgroundWorker();  
            }  
        });  
  
        return root;  
    }  
  
    private void startBackgroundWorker() {  
        Log.d(TAG, "Запуск BackgroundWorker");  
  
        textViewStatus.setText("Статус задачи: Выполняется...");  
  
        Constraints constraints = new Constraints.Builder()  
                .setRequiredNetworkType(NetworkType.CONNECTED)  
                .setRequiresCharging(true)  
                .build();  
  
        Data inputData = new Data.Builder()  
                .putString(BackgroundTaskWorker.INPUT_DATA_KEY, "Какие-то данные")  
                .build();  
  
        WorkRequest backgroundWorkRequest =  
                new OneTimeWorkRequest.Builder(BackgroundTaskWorker.class)  
                        .setConstraints(constraints)  
                        .setInputData(inputData)  
                        .build();  
  
        WorkManager.getInstance(requireContext())  
                .enqueue(backgroundWorkRequest);  
  
        textViewStatus.setText("Статус задачи: Задача поставлена в очередь");  
        textViewResult.setText("Результат: ");  
  
        UUID workId = backgroundWorkRequest.getId();  
  
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workId)  
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {  
                    @Override  
                    public void onChanged(WorkInfo workInfo) {  
                        Log.d(TAG, "Статус задачи изменился: " + workInfo.getState());  
                        textViewStatus.setText("Статус задачи: " + workInfo.getState());  
  
                        if (workInfo.getState().isFinished()) {  
                            buttonStartWorker.setEnabled(true);  
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {  
                                Data outputData = workInfo.getOutputData();  
                                String result = outputData.getString(BackgroundTaskWorker.OUTPUT_RESULT_KEY);  
                                textViewResult.setText("Результат: " + result);  
                                Log.d(TAG, "Получен результат: " + result);  
                            } else if (workInfo.getState() == WorkInfo.State.FAILED) {  
                                textViewResult.setText("Результат: Задача провалена");  
                            } else if (workInfo.getState() == WorkInfo.State.CANCELLED) {  
                                textViewResult.setText("Результат: Задача отменена");  
                            }  
                        } else {  
                            buttonStartWorker.setEnabled(false);  
                        }  
                    }  
                });  
  
        buttonStartWorker.setEnabled(false);  
    }  
}
```

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
                R.id.dataFragment, R.id.webViewFragment, R.id.backgroundTaskFragment)  
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
