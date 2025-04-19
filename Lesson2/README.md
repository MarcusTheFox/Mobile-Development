# Отчёт 2

## **Задание 1**
___

Создан новый модуль `Activity Lifecycle`.
В `MainActivity.java` переопределены основные методы жизненного цикла родительского класса:
- `onCreate(Bundle saveInstanceState)`
- `onStart()`
- `onRestoreInstanceState(Bundle saveInstanceState)`
- `onRestart()`
- `onResume()`
- `onPause()`
- `onSaveInstanceState(Bundle saveInstanceState)`
- `onStop()`
- `onDestroy()`
В каждом методе отображается сообщение, содержащее в себе название метода.
### Экраны
В файле `activity_main.xml` было добавлено поле ввода текста.
![1 задание - activity_main xml](https://github.com/user-attachments/assets/5c1f7605-81bc-4518-95e0-7648b652d9b1)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.activitylifecycle;  
  
import android.os.Bundle;  
import android.util.Log;  
  
import androidx.activity.EdgeToEdge;  
import androidx.annotation.NonNull;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
  
    private String TAG = MainActivity.class.getSimpleName();  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        EdgeToEdge.enable(this);  
        setContentView(R.layout.activity_main);  
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {  
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());  
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);  
            return insets;  
        });  
        Log.i(TAG, "onCreate()");  
    }  
  
    @Override  
    protected void onStart() {  
        super.onStart();  
        Log.i(TAG, "onStart()");  
    }  
  
    @Override  
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);  
        Log.i(TAG, "onRestoreInstanceState()");  
    }  
  
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        Log.i(TAG, "onRestart()");  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        Log.i(TAG, "onResume()");  
    }  
  
    @Override  
    protected void onPause() {  
        super.onPause();  
        Log.i(TAG, "onPause()");  
    }  
  
    @Override  
    protected void onSaveInstanceState(@NonNull Bundle outState) {  
        super.onSaveInstanceState(outState);  
        Log.i(TAG, "onSaveInstanceState()");  
    }  
  
    @Override  
    protected void onStop() {  
        super.onStop();  
        Log.i(TAG, "onStop()");  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        Log.i(TAG, "onDestroy()");  
    }  
}
```
### Логи
Для ответа на поставленные вопросы были выполнены следующие действия:
- открыто приложение
- изменен текст в поле ввода
- нажата кнопка "Домой"
- вернулись в приложение
- нажата кнопка "Назад"
- вернулись в приложение

Все действия были зафиксированы в логах logcat:
![1 задание - logcat](https://github.com/user-attachments/assets/0a00ce30-ab04-4040-94ba-39bf58f9b65e)

### Вопросы
1. Будет ли вызван метод onCreate после нажатия на кнопку «Home» и возврата в приложение? 
   **Ответ: Нет.** При нажатии на кнопку "Домой" вызываются только методы onPause(), onStop() и onSaveInstanceState(). Следовательно, активность не уничтожается и при возвращении в приложение метод onCreate() не вызывается.
2. Изменится ли значение поля EditText после нажатия на кнопку «Home» и возврата в приложение? 
   **Ответ: Нет.** Так как активность не уничтожается, то EditText не изменится.
3. Изменится ли значение поля EditText после нажатия на кнопку «Back» и возврата в приложение? 
   **Ответ: Нет.** Так как активность является корневой, то нажатие на кнопку "Назад" работает как нажатие на кнопку "Домой", то есть приложение просто сворачивается.

## **Задание 2**
---
Было создан новый модуль `MultiActivity` с двумя активностями: `MainActivity` и `SecondActivity`.
### Экраны
![2 задание - MainActivity](https://github.com/user-attachments/assets/155c8ea6-f581-4d45-8bbe-3bc99a5468d4)

![2 задание - SecondActivity](https://github.com/user-attachments/assets/bf773907-2b0a-45c4-bfa8-903b7bc9099f)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.multiactivity;  
  
import android.content.Intent;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.EditText;  
  
import androidx.annotation.NonNull;  
import androidx.appcompat.app.AppCompatActivity;  
  
public class MainActivity extends AppCompatActivity {  
    private String TAG = MainActivity.class.getSimpleName();  
    private EditText editText;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        editText = findViewById(R.id.editTextText);  
        Log.i(TAG, "onCreate()");  
    }  
  
    @Override  
    protected void onStart() {  
        super.onStart();  
        Log.i(TAG, "onStart()");  
    }  
  
    @Override  
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);  
        Log.i(TAG, "onRestoreInstanceState()");  
    }  
  
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        Log.i(TAG, "onRestart()");  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        Log.i(TAG, "onResume()");  
    }  
  
    @Override  
    protected void onPause() {  
        super.onPause();  
        Log.i(TAG, "onPause()");  
    }  
  
    @Override  
    protected void onSaveInstanceState(@NonNull Bundle outState) {  
        super.onSaveInstanceState(outState);  
        Log.i(TAG, "onSaveInstanceState()");  
    }  
  
    @Override  
    protected void onStop() {  
        super.onStop();  
        Log.i(TAG, "onStop()");  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        Log.i(TAG, "onDestroy()");  
    }  
  
    public void onClickNewActivity(View view) {  
        String text = editText.getText().toString();  
        Intent intent = new Intent(this, SecondActivity.class);  
        intent.putExtra("key", text);  
        startActivity(intent);  
    }  
}
```

#### SecondActivity.java
``` java
package ru.mirea.Bublikov.multiactivity;  
  
import android.os.Bundle;  
import com.google.android.material.snackbar.Snackbar;  
  
import androidx.annotation.NonNull;  
import androidx.appcompat.app.AppCompatActivity;  
  
import android.util.Log;  
import android.view.View;  
import android.widget.TextView;  
  
import androidx.navigation.NavController;  
import androidx.navigation.Navigation;  
import androidx.navigation.ui.AppBarConfiguration;  
import androidx.navigation.ui.NavigationUI;  
import ru.mirea.Bublikov.multiactivity.databinding.ActivitySecondBinding;  
  
public class SecondActivity extends AppCompatActivity {  
    private String TAG = SecondActivity.class.getSimpleName();  
  
    private AppBarConfiguration appBarConfiguration;  
    private ActivitySecondBinding binding;  
  
    private TextView textView;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
  
        binding = ActivitySecondBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        setSupportActionBar(binding.toolbar);  
  
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);  
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();  
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);  
  
        binding.fab.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View view) {  
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)  
                        .setAnchorView(R.id.fab)  
                        .setAction("Action", null).show();  
            }  
        });  
  
        String text = (String) getIntent().getSerializableExtra("key");  
        textView = findViewById(R.id.textView2);  
        textView.setText(text);  
        Log.i(TAG, "onCreate()");  
    }  
  
    @Override  
    protected void onStart() {  
        super.onStart();  
        Log.i(TAG, "onStart()");  
    }  
  
    @Override  
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);  
        Log.i(TAG, "onRestoreInstanceState()");  
    }  
  
    @Override  
    protected void onRestart() {  
        super.onRestart();  
        Log.i(TAG, "onRestart()");  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        Log.i(TAG, "onResume()");  
    }  
  
    @Override  
    protected void onPause() {  
        super.onPause();  
        Log.i(TAG, "onPause()");  
    }  
  
    @Override  
    protected void onSaveInstanceState(@NonNull Bundle outState) {  
        super.onSaveInstanceState(outState);  
        Log.i(TAG, "onSaveInstanceState()");  
    }  
  
    @Override  
    protected void onStop() {  
        super.onStop();  
        Log.i(TAG, "onStop()");  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        Log.i(TAG, "onDestroy()");  
    }  
  
    @Override  
    public boolean onSupportNavigateUp() {  
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);  
        return NavigationUI.navigateUp(navController, appBarConfiguration)  
                || super.onSupportNavigateUp();  
    }  
}
```
### Логи
В соответствии с заданием были выполнены необходимые переходы. 
![2 задание - logcat](https://github.com/user-attachments/assets/d659b712-098f-45b1-b323-021b11ec8776)

## **Задание 3**
___
Создан новый модуль `Intent Filter`.
На экране расположено две кнопки: `Web browser` - открывает страницу https://www.mirea.ru/, и `Share` - отправляет в другое приложение определенный текст.
### Экраны
![3 задание - Main](https://github.com/user-attachments/assets/dea7bbe4-b057-4077-b56b-ca2cef234fd4)

![3 задание - Web browser](https://github.com/user-attachments/assets/07a42e38-c6f0-4bd6-919d-668c2325ec01)

![3 задание - Share](https://github.com/user-attachments/assets/a57ab53f-bb62-4cd4-b8d4-9a038eeda22b)

## **Задание 4**
___
### Toast App
Создан новый модуль `Toast App`.
На экране расположено поле ввода текста и кнопка, которая считает количество символов в поле ввода и отображает их во всплывающем уведомлении.
#### Экраны
![4 задание - Toast](https://github.com/user-attachments/assets/96b220f1-104a-444b-becd-f88dc3658485)

#### Код
##### MainActivity.java
``` java
package ru.mirea.Bublikov.toastapp;  
  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.Toast;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
  
    private EditText editText;  
    private Button button;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        EdgeToEdge.enable(this);  
        setContentView(R.layout.activity_main);  
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {  
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());  
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);  
            return insets;  
        });  
  
        editText = findViewById(R.id.editTextText);  
        button = findViewById(R.id.button);  
  
        button.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                int charCount = editText.getText().length();  
                String toastText = "СТУДЕНТ № 4 ГРУППА БСБО-09-22 Количество символов - " + charCount;  
                Toast toast = Toast.makeText(getApplicationContext(),  
                        toastText,  
                        Toast.LENGTH_SHORT);  
                toast.show();  
            }  
        });  
    }  
}
```
### Notification App
Создан новый модуль `Notification App`.
На экране расположена кнопка, которая создает уведомление.
#### Экраны
![4 задание - NotifApp Main](https://github.com/user-attachments/assets/0e9c1f5b-debc-4b85-8f0f-5ada52b65334)

![4 задание - NotifApp Result](https://github.com/user-attachments/assets/8ac05787-fe90-4c9f-9866-847c692fce3d)

#### Код
##### MainActivity.java
``` java
package ru.mirea.Bublikov.notificationapp;  
  
import static android.Manifest.permission.POST_NOTIFICATIONS;  
  
import android.app.NotificationChannel;  
import android.app.NotificationManager;  
import android.content.pm.PackageManager;  
import android.os.Build;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
  
import androidx.activity.EdgeToEdge;  
import androidx.annotation.RequiresApi;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.app.ActivityCompat;  
import androidx.core.app.NotificationCompat;  
import androidx.core.app.NotificationManagerCompat;  
import androidx.core.content.ContextCompat;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
    private static final String CHANNEL_ID = "com.mirea.asd.notification.ANDROID";  
    private int PermissionCode = 200;  
  
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {  
            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены");  
        } else {  
            Log.d(MainActivity.class.getSimpleName().toString(), "Нет разрешений!");  
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, PermissionCode);  
        }  
    }  
  
    public void onClickSendNotification(View view) {  
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {  
            return;  
        }  
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)  
                .setContentText("Congratulation!")  
                .setSmallIcon(R.drawable.ic_launcher_foreground)  
                .setPriority(NotificationCompat.PRIORITY_HIGH)  
                .setStyle(new NotificationCompat.BigTextStyle()  
                        .bigText("Much longer text that cannot fit one line...\nStudent Бубликов Михаил Александрович Notification"))  
                .setContentTitle("Mirea");  
        int importance = NotificationManager.IMPORTANCE_DEFAULT;  
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student Бубликов Михаил Александрович Notification", importance);  
        channel.setDescription("MIREA Channel");  
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);  
        notificationManager.createNotificationChannel(channel);  
        notificationManager.notify(1, builder.build());  
    }  
}
```
### Dialog
Создан новый модуль `Dialog`.
На экране расположена кнопка, которая создает диалоговое окно с тремя кнопками.
#### Экраны
![4 задание - Dialog Main](https://github.com/user-attachments/assets/ecd4b7c8-9125-46c2-b533-951fe3269875)

![4 задание - Dialog Dialog](https://github.com/user-attachments/assets/a43f4892-5de0-4a7c-a00f-606528e15a8b)

![4 задание - Dialog Toast](https://github.com/user-attachments/assets/0729672b-338a-4869-a3f3-ff282bbb877c)

#### Код
##### MainActivity.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Toast;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
    }  
  
    public void onClickShowDialog(View view) {  
        MyDialogFragment dialogFragment = new MyDialogFragment();  
        dialogFragment.show(getSupportFragmentManager(), "mirea");  
    }  
  
    public void onOkClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG).show();  
    }  
  
    public void onCancelClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG).show();  
    }  
  
    public void onNeutralClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG).show();  
    }  
}
```

##### MyDialogFragment.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.app.AlertDialog;  
import android.app.Dialog;  
import android.content.DialogInterface;  
import android.os.Bundle;  
  
import androidx.annotation.NonNull;  
import androidx.fragment.app.DialogFragment;  
  
public class MyDialogFragment extends DialogFragment {  
    @NonNull  
    @Override    public Dialog onCreateDialog(Bundle savedInstanceState) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        builder.setTitle("Здравствуй МИРЭА!").setMessage("Успех близок?").setIcon(R.mipmap.ic_launcher_round).setPositiveButton("Иду дальше", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int id) { // Закрываем окно  
                ((MainActivity) getActivity()).onOkClicked();  
                dialog.cancel();  
            }  
        }).setNeutralButton("На паузе", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int id) {  
                ((MainActivity) getActivity()).onNeutralClicked();  
                dialog.cancel();  
            }  
        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int id) {  
                ((MainActivity) getActivity()).onCancelClicked();  
                dialog.cancel();  
            }  
        });  
        return builder.create();  
    }  
}
```

## **Самостоятельная работа**
___
В модуль `Dialog` были добавлены классы `TimePickerDialog`, `DatePickerDialog`, `ProgressDialog`.
Для каждого диалога на главном экране были добавлены соответствующие кнопки.
### Экраны
![5 задание - Main](https://github.com/user-attachments/assets/9dc3468e-c8ab-4b81-9b00-aa7e3c7b3238)

![5 задание - TimePickerDialog](https://github.com/user-attachments/assets/85bafc03-3403-4b33-8438-7e1954d73e7a)

![5 задание - snackbar](https://github.com/user-attachments/assets/f34f9920-5bc5-4b5a-93a2-e961ab6e12ff)

![5 задание - DatePickerDialog](https://github.com/user-attachments/assets/1761fade-b821-4b8c-99c0-75b97584d053)

![5 задание - ProgressDialog](https://github.com/user-attachments/assets/d3fafea7-5b96-4161-a227-267ba91eb7f0)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Toast;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
    }  
  
    public void onClickShowDialog(View view) {  
        MyDialogFragment dialogFragment = new MyDialogFragment();  
        dialogFragment.show(getSupportFragmentManager(), "mirea");  
    }  
  
    public void onClickMyTimeDialogFragment(View view) {  
        MyTimeDialogFragment dialogFragment = new MyTimeDialogFragment();  
        dialogFragment.show(getSupportFragmentManager(), "TimePickerDialog");  
    }  
  
    public void onClickMyDateDialogFragment(View view) {  
        MyDateDialogFragment dialogFragment = new MyDateDialogFragment();  
        dialogFragment.show(getSupportFragmentManager(), "DatePickerDialog");  
    }  
  
    public void onClickMyProgressDialogFragment(View view) {  
        MyProgressDialogFragment dialogFragment = new MyProgressDialogFragment();  
        dialogFragment.show(getSupportFragmentManager(), "ProgressDialog");  
    }  
  
    public void onOkClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG).show();  
    }  
  
    public void onCancelClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG).show();  
    }  
  
    public void onNeutralClicked() {  
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG).show();  
    }  
}
```
#### MyTimeDialogFragment.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.app.Dialog;  
import android.app.TimePickerDialog;  
import android.os.Bundle;  
import android.text.format.DateFormat;  
import android.widget.TimePicker;  
  
import androidx.annotation.NonNull;  
import androidx.fragment.app.DialogFragment;  
  
import com.google.android.material.snackbar.Snackbar;  
  
import java.util.Calendar;  
  
public class MyTimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {  
    @NonNull  
    @Override    
    public Dialog onCreateDialog(Bundle savedInstanceState) {  
        final Calendar c = Calendar.getInstance();  
        int hour = c.get(Calendar.HOUR_OF_DAY);  
        int minute = c.get(Calendar.MINUTE);  
  
        return new TimePickerDialog(getActivity(), this, hour, minute,  
                DateFormat.is24HourFormat(getActivity()));  
    }  
  
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
        String time = "Выбранное время: " + hourOfDay + ":" + minute;  
        Snackbar.make(getActivity().findViewById(android.R.id.content), time, Snackbar.LENGTH_SHORT).show();  
    }  
}
```
#### MyDateDialogFragment.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.app.DatePickerDialog;  
import android.app.Dialog;  
import android.os.Bundle;  
import android.widget.DatePicker;  
  
import androidx.annotation.NonNull;  
import androidx.fragment.app.DialogFragment;  
  
import com.google.android.material.snackbar.Snackbar;  
  
import java.util.Calendar;  
  
public class MyDateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {  
    @NonNull  
    @Override    
    public Dialog onCreateDialog(Bundle savedInstanceState) {  
        final Calendar c = Calendar.getInstance();  
        int year = c.get(Calendar.YEAR);  
        int month = c.get(Calendar.MONTH);  
        int day = c.get(Calendar.DAY_OF_MONTH);  
  
        return new DatePickerDialog(requireContext(), this, year, month, day);  
    }  
  
    public void onDateSet(DatePicker view, int year, int month, int day) {  
        String date = "Выбранная дата: " + day + "/" + (month + 1) + "/" + year;  
        Snackbar.make(getActivity().findViewById(android.R.id.content), date, Snackbar.LENGTH_SHORT).show();  
    }  
}
```
#### MyProgressDialogFragment.java
``` java
package ru.mirea.Bublikov.dialog;  
  
import android.app.Dialog;  
import android.app.ProgressDialog;  
import android.os.Bundle;  
  
import androidx.annotation.NonNull;  
import androidx.annotation.Nullable;  
import androidx.fragment.app.DialogFragment;  
  
public class MyProgressDialogFragment extends DialogFragment {  
    @NonNull  
    @Override    
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {  
        ProgressDialog progressDialog = new ProgressDialog(getActivity());  
        progressDialog.setMessage("Ждём...");  
        progressDialog.setIndeterminate(true);  
        progressDialog.setCancelable(true);  
        return progressDialog;  
    }  
}
```
