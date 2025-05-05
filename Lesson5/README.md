# Отчёт 5

## **Sensor**
Создан новый проект Lesson5 для работы с датчиками.
Приложение отображает список всех доступных датчиков устройства в ListView.
Используя SensorManager и SensorEventListener, приложение получает и в реальном времени выводит показания акселерометра (X, Y, Z) в логах.
### Экраны
![1-Sensor-1](https://github.com/user-attachments/assets/77ab97e9-f961-43a0-8d63-34710104cd8c)

### Логи
![1-Sensor-2](https://github.com/user-attachments/assets/d74c0f6c-a7ba-4178-9d4d-576d76e600de)

![1-Sensor-3](https://github.com/user-attachments/assets/841e942c-c411-4662-95ec-8d8500bd7da6)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.lesson5;  
  
import android.content.Context;  
import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorEventListener;  
import android.hardware.SensorManager;  
import android.os.Bundle;  
import android.util.Log;  
import android.widget.ListView;  
import android.widget.SimpleAdapter;  
  
import androidx.appcompat.app.AppCompatActivity;  
  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
  
import ru.mirea.bublikov.lesson5.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity implements SensorEventListener {  
    private static final String TAG = MainActivity.class.getSimpleName();  
  
    private ActivityMainBinding binding;  
    private SensorManager sensorManager;  
    private Sensor accelerometerSensor;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);  
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);  
  
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);  
        ListView listSensor = binding.listView;  
  
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();  
        for (int i = 0; i < sensors.size(); i++) {  
            HashMap<String, Object> sensorTypeList = new HashMap<>();  
            sensorTypeList.put("Name", sensors.get(i).getName());  
            sensorTypeList.put("Value", sensors.get(i).getMaximumRange());  
            arrayList.add(sensorTypeList);  
        }  
  
        SimpleAdapter mHistory = new SimpleAdapter(  
                this, arrayList, android.R.layout.simple_list_item_2,  
                new String[]{"Name", "Value"},  
                new int[]{android.R.id.text1, android.R.id.text2});  
  
        listSensor.setAdapter(mHistory);  
    }  
  
    @Override  
    public void onSensorChanged(SensorEvent event) {  
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {  
            float x = event.values[0];  
            float y = event.values[1];  
            float z = event.values[2];  
  
            Log.d(TAG, "Accelerometer: x=" + x + ", y=" + y + ", z=" + z);  
        }  
    }  
  
    @Override  
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {  
            switch (accuracy) {  
                case SensorManager.SENSOR_STATUS_UNRELIABLE:  
                    Log.d("Sensor", "Акселерометр: ненадёжные данные");  
                    break;  
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:  
                    Log.d("Sensor", "Акселерометр: высокая точность");  
                    break;  
            }  
        }  
    }  
}
```

## **Accelerometer**
Создан новый модуль "Accelerometer".
Приложение отображает в TextView показания акселерометра (X, Y, Z).
Используя SensorManager и SensorEventListener, показания акселерометра обновляются в реальном времени при вращении устройства.
### Экраны
![2-Accelerometer-1](https://github.com/user-attachments/assets/75a9ea3c-e487-443e-83fa-c8750b3c5448)

![2-Accelerometer-2](https://github.com/user-attachments/assets/a74b1f27-5b3b-471f-bcdf-105418aed4db)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.accelerometer;  
  
import android.content.Context;  
import android.hardware.Sensor;  
import android.hardware.SensorEvent;  
import android.hardware.SensorEventListener;  
import android.hardware.SensorManager;  
import android.os.Bundle;  
import android.util.Log;  
import android.widget.TextView;  
  
import androidx.appcompat.app.AppCompatActivity;  
  
import ru.mirea.bublikov.accelerometer.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity implements SensorEventListener {  
    private SensorManager sensorManager;  
    private Sensor accelerometer;  
    private TextView azimuthTextView;  
    private TextView pitchTextView;  
    private TextView rollTextView;  
  
    private ActivityMainBinding binding;  
    private static final String TAG = MainActivity.class.getSimpleName();  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);  
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);  
        azimuthTextView = binding.textViewAzimuth;  
        pitchTextView = binding.textViewPitch;  
        rollTextView = binding.textViewRoll;  
    }  
  
    @Override  
    protected void onPause() {  
        super.onPause();  
        sensorManager.unregisterListener(this);  
    }  
  
    @Override  
    protected void onResume() {  
        super.onResume();  
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);  
    }  
  
    @Override  
    public void onSensorChanged(SensorEvent event) {  
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {  
            float x = event.values[0];  
            float y = event.values[1];  
            float z = event.values[2];  
            azimuthTextView.setText(String.format("Azimuth: %s", x));  
            pitchTextView.setText(String.format("Pitch: %s", y));  
            rollTextView.setText(String.format("Roll: %s", z));  
        }  
    }  
  
    @Override  
    public void onAccuracyChanged(Sensor sensor, int accuracy) {  
        Log.d(TAG, "onAccuracyChanged: " + sensor.getName() + ", accuracy: " + accuracy);  
    }  
}
```

## **Camera**
Создан новый модуль "Camera".
Приложение позволяет вызвать системную камеру, сделать снимок, сохранить его и отобразить на экране.
Снимок сохраняется во временный файл в приватной папке приложения.
Для безопасной передачи URI файла системной камере используется FileProvider.
Вызов камеры осуществляется через неявный интент (MediaStore.ACTION_IMAGE_CAPTURE) с передачей URI файла (MediaStore.EXTRA_OUTPUT).
Результат съемки обрабатывается с помощью ActivityResultLauncher. При успешном снимке изображение загружается в ImageView.
### Экраны
![3-Camera-1](https://github.com/user-attachments/assets/1c29391a-7f1b-4b65-9a28-8378d4775c4b)

![3-Camera-2](https://github.com/user-attachments/assets/a0b24b5e-f159-4802-b0d6-aca7aed59feb)

![3-Camera-3](https://github.com/user-attachments/assets/ceaf280f-44e8-4cc2-adaf-e36ca56dc6db)

![3-Camera-4](https://github.com/user-attachments/assets/10403f9b-1d70-4661-bb3d-60f96c5b01c2)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.camera;  
  
import static android.Manifest.permission.CAMERA;  
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;  
  
import android.app.Activity;  
import android.content.Intent;  
import android.content.pm.PackageManager;  
import android.net.Uri;  
import android.os.Bundle;  
import android.os.Environment;  
import android.provider.MediaStore;  
import android.util.Log;  
import android.view.View;  
  
import androidx.activity.result.ActivityResult;  
import androidx.activity.result.ActivityResultCallback;  
import androidx.activity.result.ActivityResultLauncher;  
import androidx.activity.result.contract.ActivityResultContracts;  
import androidx.annotation.NonNull;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.app.ActivityCompat;  
import androidx.core.content.ContextCompat;  
import androidx.core.content.FileProvider;  
  
import java.io.File;  
import java.io.IOException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Locale;  
  
import ru.mirea.bublikov.camera.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
    private static final int REQUEST_CODE_PERMISSION = 100;  
    private static final int CAMERA_REQUEST = 0;  
    private boolean isWork = false;  
    private Uri imageUri;  
    private ActivityMainBinding binding;  
  
    private static final String TAG = MainActivity.class.getSimpleName();  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, CAMERA);  
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);  
  
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {  
            Log.d(TAG, "Разрешение камеры получено.");  
            isWork = true;  
        } else {  
            Log.d(TAG, "Запрашиваем необходимые разрешения.");  
            ActivityCompat.requestPermissions(this, new String[]{  
                    CAMERA, WRITE_EXTERNAL_STORAGE},  
                    REQUEST_CODE_PERMISSION);  
        }  
  
        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {  
            @Override  
            public void onActivityResult(ActivityResult result) {  
                if(result.getResultCode() == Activity.RESULT_OK) {  
                    Intent data = result.getData();  
                    binding.imageView.setImageURI(imageUri);  
                }  
            }  
        };  
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(  
            new ActivityResultContracts.StartActivityForResult(),  
            callback);  
  
        binding.imageView.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
                if (isWork) {  
                    try {  
                        File photoFile = createImageFile();  
                        String authorities = getApplicationContext().getPackageName() + ".fileprovider";  
                        imageUri = FileProvider.getUriForFile(MainActivity.this, authorities, photoFile);  
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
                        cameraActivityResultLauncher.launch(cameraIntent);  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        });  
    }  
  
    @Override  
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  
        if (requestCode == REQUEST_CODE_PERMISSION) {  
            Log.d(TAG, "onRequestPermissionsResult вызван. requestCode: " + requestCode);  
  
            boolean cameraPermissionGrantedResult = false;  
            for (int i = 0; i < permissions.length; i++) {  
                if (permissions[i].equals(CAMERA)) {  
                    if (grantResults.length > i && grantResults[i] == PackageManager.PERMISSION_GRANTED) {  
                        cameraPermissionGrantedResult = true;  
                        Log.d(TAG, "Результат запроса камеры: GRANTED");  
                    } else {  
                        Log.d(TAG, "Результат запроса камеры: DENIED");  
                    }  
                    break;  
                }  
            }  
  
            if (cameraPermissionGrantedResult) {  
                Log.d(TAG, "Разрешение камеры получено.");  
                isWork = true;  
            } else {  
                Log.w(TAG, "Разрешение камеры отклонено.");  
                isWork = false;  
            }  
        }  
    }  
  
    private File createImageFile() throws IOException {  
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());  
        String imageFileName = "IMAGE_" + timeStamp + "_";  
  
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);  
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);  
    }  
}
```

## **Audio Record**
Создан новый модуль "AudioRecord", реализующий простой диктофон с записью и воспроизведением аудио.
Используются классы MediaRecorder для записи и MediaPlayer для воспроизведения.
Аудио записывается в файл в приватном каталоге приложения (getExternalFilesDir).
Кнопки "Начать запись" и "Воспроизвести" управляют соответствующими процессами и изменяют свою доступность.
### Экраны
![4-AudioRecord-1](https://github.com/user-attachments/assets/f4bf3107-ebd4-4dff-aff3-ced1c1bdfaec)

![4-AudioRecord-2](https://github.com/user-attachments/assets/a86babc2-f034-499a-a161-632ec43357c7)

![4-AudioRecord-3](https://github.com/user-attachments/assets/955d95fd-ef88-430a-affa-1791c16d40a9)

![4-AudioRecord-4](https://github.com/user-attachments/assets/3723821b-226a-4c5b-8212-ae63d090914e)

![4-AudioRecord-5](https://github.com/user-attachments/assets/3e0a7050-7d35-4fcb-a3c2-1d1da6d21d84)

![4-AudioRecord-6](https://github.com/user-attachments/assets/af70b474-2690-409a-b4fc-a5608f6c00f4)

### Код
#### MainActivity.java
``` java
package ru.mirea.bublikov.audiorecord;  
  
import android.content.pm.PackageManager;  
import android.media.MediaPlayer;  
import android.media.MediaRecorder;  
import android.os.Bundle;  
import android.os.Environment;  
import android.util.Log;  
import android.view.View;  
import android.widget.Button;  
  
import androidx.annotation.NonNull;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.app.ActivityCompat;  
import androidx.core.content.ContextCompat;  
  
import java.io.File;  
import java.io.IOException;  
  
import ru.mirea.bublikov.audiorecord.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
    private ActivityMainBinding binding;  
    private static final int REQUEST_CODE_PERMISSION = 200;  
    private final String TAG = MainActivity.class.getSimpleName();  
    private boolean isWork;  
    private String recordFilePath = null;  
    private Button recordButton = null;  
    private Button playButton = null;  
    private MediaRecorder recorder = null;  
    private MediaPlayer player = null;  
    boolean isStartRecording = true;  
    boolean isStartPlaying = true;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        recordButton = binding.recordButton;  
        playButton = binding.playButton;  
        playButton.setEnabled(false);  
        File musicDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);  
        if (musicDir == null) {  
            Log.e(TAG, "Не удалось получить каталог ExternalFilesDir(Environment.DIRECTORY_MUSIC)");  
        } else {  
            recordFilePath = (new File(musicDir, "audiorecordtest.3gp")).getAbsolutePath();  
            Log.d(TAG, "Путь для записи: " + recordFilePath);  
        }  
  
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);  
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);  
  
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && (musicDir != null)) {  
            Log.d(TAG, "Необходимые разрешения уже получены.");  
            isWork = true;  
        } else {  
            Log.d(TAG, "Запрашиваем необходимые разрешения.");  
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO,  
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);  
        }  
  
        recordButton.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                if(isStartRecording) {  
                    recordButton.setText("Stop recording");  
                    playButton.setEnabled(false);  
                    startRecording();  
                } else {  
                    recordButton.setText("Start recording");  
                    playButton.setEnabled(true);  
                    stopRecording();  
                }  
                isStartRecording = !isStartRecording;  
            }  
        });  
  
        playButton.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                if(isStartPlaying) {  
                    playButton.setText("Stop playing");  
                    recordButton.setEnabled(false);  
                    startPlaying();  
                } else {  
                    playButton.setText("Start playing");  
                    recordButton.setEnabled(true);  
                    stopPlaying();  
                }  
                isStartPlaying = !isStartPlaying;  
            }  
        });  
    }  
  
    private void startRecording() {  
        recorder = new MediaRecorder();  
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  
        recorder.setOutputFile(recordFilePath);  
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
        try {  
            recorder.prepare();  
        } catch (IOException e) {  
            Log.e(TAG, "prepare() failed");  
        }  
        recorder.start();  
    }  
  
    private void stopRecording() {  
        recorder.stop();  
        recorder.release();  
        recorder = null;  
    }  
  
    private void startPlaying() {  
        player = new MediaPlayer();  
        try {  
            player.setDataSource(recordFilePath);  
            player.prepare();  
            player.start();  
        } catch (IOException e) {  
            Log.e(TAG, "prepare() failed");  
        }  
    }  
  
    private void stopPlaying() {  
        player.release();  
        player = null;  
    }  
  
    @Override  
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  
  
        if (requestCode == REQUEST_CODE_PERMISSION) {  
            boolean audioPermissionGranted = false;  
            for (int i = 0; i < permissions.length; i++) {  
                if (permissions[i].equals(android.Manifest.permission.RECORD_AUDIO) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {  
                    audioPermissionGranted = true;  
                    break;  
                }  
            }  
            isWork = audioPermissionGranted && (recordFilePath != null);  
  
            if (isWork) {  
                Log.d(TAG, "Разрешение RECORD_AUDIO получено.");  
                binding.recordButton.setEnabled(true);  
            } else {  
                Log.w(TAG, "Разрешение RECORD_AUDIO отклонено или путь недоступен.");  
                binding.recordButton.setEnabled(false);  
                binding.playButton.setEnabled(false);  
            }  
        }  
    }  
}
```

## **Mirea Project**
В проект "MireaProject" добавлена новая секция "Hardware" с тремя фрагментами, демонстрирующими использование датчиков, камеры и микрофона для решения разных задач.
- В AndroidManifest.xml добавлены необходимые разрешения (RECORD_AUDIO, CAMERA, WRITE_EXTERNAL_STORAGE).
- Создан фрагмент-меню HardwareFragment с кнопками для навигации по экранам.
- Созданы три новых фрагмента:
    - SensorFragment: Использует акселерометр для определения, лежит ли устройство на плоской поверхности, и отображает статус с помощью текста и цвета индикатора.
    - CameraFragment: Позволяет сделать снимок с помощью системной камеры (с использованием FileProvider для сохранения в приватной папке) и добавить текстовую заметку к фото. Использует ActivityResultLauncher для камеры и запроса разрешений.
    - MicrophoneFragment: Использует MediaRecorder для мониторинга амплитуды звука с микрофона и отображает уровень шума в реальном времени с помощью текста и ProgressBar. Использует Handler для периодических обновлений и ActivityResultLauncher для запроса разрешения на микрофон.
- Новые фрагменты добавлены в Navigation Graph и меню Navigation Drawer, обновлена AppBarConfiguration в MainActivity.
- Для всех задач реализован запрос разрешений во время выполнения с помощью ActivityResultLauncher.
### Экраны
![5-MireaProject-1](https://github.com/user-attachments/assets/89a5c6f8-0c41-4a0d-aa6c-564bbce7badf)

![5-MireaProject-2](https://github.com/user-attachments/assets/65f7dc88-cd13-4e6a-9a11-015d544dde34)

#### Sensor Fragment
![5-MireaProject-3](https://github.com/user-attachments/assets/65ebdce1-7f0e-4ef3-b76d-708b352115b6)

![5-MireaProject-4](https://github.com/user-attachments/assets/e322ae9c-8a68-4831-a57c-a0fea22a190b)

#### Camera Fragment
![5-MireaProject-5](https://github.com/user-attachments/assets/260172fb-7293-4d5a-aea5-0d534ca65662)

![5-MireaProject-6](https://github.com/user-attachments/assets/12b1cfa0-b61b-4191-a5b8-8423dd815be0)

![5-MireaProject-7](https://github.com/user-attachments/assets/e3542d8a-1146-47ef-b1c1-f1d92b476cd1)

![5-MireaProject-8](https://github.com/user-attachments/assets/fef90e69-3471-46ce-bc92-55f0c0233ecd)

![5-MireaProject-9](https://github.com/user-attachments/assets/df1a070c-cd16-42e8-8066-5b004d47dab0)

#### Microphone Fragment
![5-MireaProject-10](https://github.com/user-attachments/assets/8c01c0a1-72ed-454f-8f0e-a64fd56c9a3e)

![5-MireaProject-11](https://github.com/user-attachments/assets/efca17f9-e5b8-4e1f-9076-248cd4d48f02)

![5-MireaProject-12](https://github.com/user-attachments/assets/09265039-66d1-417a-9a22-6acedd4a06bb)

### Код
#### SensorFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SensorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textViewStatus;
    private View viewIndicator;

    private static final String TAG = SensorFragment.class.getSimpleName();
    private static final float THRESHOLD_FLAT = 0.5f;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorFragment newInstance(String param1, String param2) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SensorFragment() {
        // Required empty public constructor
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
        View root = inflater.inflate(R.layout.fragment_sensor, container, false);

        textViewStatus = root.findViewById(R.id.textViewFlatStatus);
        viewIndicator = root.findViewById(R.id.viewFlatIndicator);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            boolean isFlat = Math.abs(x) < THRESHOLD_FLAT &&
                    Math.abs(y) < THRESHOLD_FLAT &&
                    Math.abs(z - SensorManager.STANDARD_GRAVITY) < THRESHOLD_FLAT;

            if (isFlat) {
                textViewStatus.setText("Устройство лежит плоско");
                viewIndicator.setBackgroundColor(Color.GREEN);
            } else {
                textViewStatus.setText("Устройство наклонено");
                viewIndicator.setBackgroundColor(Color.RED);
            }
            Log.d(TAG, String.format("Accelerometer: X=%.2f, Y=%.2f, Z=%.2f. IsFlat: %b", x, y, z, isFlat));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
```

#### CameraFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    private ImageView imageViewPhoto;
    private EditText editTextCaption;
    private Button buttonSave;
    private TextView textViewSavedNote;

    private static final String TAG = CameraFragment.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSIONS = 100;

    private Uri imageUri;
    private ActivityResultLauncher<String[]> permissionRequestLauncher;
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean cameraGranted = result.getOrDefault(CAMERA, false);
                    Boolean storageGranted = result.getOrDefault(WRITE_EXTERNAL_STORAGE, false);

                    if (cameraGranted != null && cameraGranted) {
                        Log.d(TAG, "Разрешение камеры получено.");
                        takePhoto();
                    } else {
                        Log.w(TAG, "Разрешение камеры отклонено.");
                    }
                });

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: resultCode = " + result.getResultCode());
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d(TAG, "Снимок сохранен по URI: " + imageUri);
                            imageViewPhoto.setImageURI(imageUri);
                            buttonSave.setEnabled(true);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        imageViewPhoto = root.findViewById(R.id.imageViewNotePhoto);
        editTextCaption = root.findViewById(R.id.editTextNoteCaption);
        buttonSave = root.findViewById(R.id.buttonSaveNote);
        textViewSavedNote = root.findViewById(R.id.textViewSavedNote);

        buttonSave.setEnabled(false);

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndTakePhoto();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        return root;
    }

    private void checkPermissionsAndTakePhoto() {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermissionGranted = ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (cameraPermissionGranted && storagePermissionGranted) {
            Log.d(TAG, "Необходимые разрешения получены.");
            takePhoto();
        } else {
            Log.d(TAG, "Запрашиваем необходимые разрешения.");
            permissionRequestLauncher.launch(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE});
        }
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File photoFile = createImageFile();
            String authorities = requireContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
            Log.d(TAG, "Сгенерирован content:// URI: " + imageUri);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cameraActivityResultLauncher.launch(cameraIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_NOTE_" + timeStamp + "_";

        File storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    private void saveNote() {
        String caption = editTextCaption.getText().toString().trim();

        if (imageUri == null) {
            Toast.makeText(requireContext(), "Сначала сделайте фото!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (caption.isEmpty()) {
            Toast.makeText(requireContext(), "Введите текст заметки!", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedNoteText = "Фото: " + imageUri.getLastPathSegment() + "\nЗаметка: " + caption;
        textViewSavedNote.setText("Последняя заметка:\n" + savedNoteText);

        Toast.makeText(requireContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();

        imageUri = null;
        imageViewPhoto.setImageResource(android.R.drawable.ic_menu_camera);
        editTextCaption.setText("");
        buttonSave.setEnabled(false);
    }
}
```

#### MicrophoneFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import static android.Manifest.permission.RECORD_AUDIO;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MicrophoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MicrophoneFragment extends Fragment {
    private TextView textViewSoundLevel;
    private ProgressBar progressBarSoundLevel;
    private Button buttonStartMonitoring;

    private MediaRecorder recorder = null;
    private Handler handler = new Handler(Looper.getMainLooper());

    private static final String TAG = MicrophoneFragment.class.getSimpleName();
    private static final int REFRESH_INTERVAL = 100;

    private ActivityResultLauncher<String> permissionRequestLauncher;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicrophoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MicrophoneFragment newInstance(String param1, String param2) {
        MicrophoneFragment fragment = new MicrophoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MicrophoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionRequestLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d(TAG, "Разрешение RECORD_AUDIO получено.");
                        startMonitoring();
                    } else {
                        Log.w(TAG, "Разрешение RECORD_AUDIO отклонено.");
                        buttonStartMonitoring.setEnabled(false);
                        buttonStartMonitoring.setText("Нет разрешения");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);

        textViewSoundLevel = root.findViewById(R.id.textViewSoundLevel);
        progressBarSoundLevel = root.findViewById(R.id.progressBarSoundLevel);
        buttonStartMonitoring = root.findViewById(R.id.buttonStartMonitoring);

        textViewSoundLevel.setText("Уровень: N/A");
        progressBarSoundLevel.setProgress(0);

        buttonStartMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder == null) {
                    checkPermissionAndStartMonitoring();
                } else {
                    stopMonitoring();
                }
            }
        });

        return root;
    }

    private void checkPermissionAndStartMonitoring() {
        if (ContextCompat.checkSelfPermission(requireContext(), RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Разрешение RECORD_AUDIO есть.");
            startMonitoring();
        } else {
            Log.d(TAG, "Запрашиваем разрешение RECORD_AUDIO.");
            permissionRequestLauncher.launch(RECORD_AUDIO);
        }
    }

    private void startMonitoring() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        String tempFilePath = requireContext().getCacheDir().getAbsolutePath() + "/temp_audio_monitor.3gp";
        recorder.setOutputFile(tempFilePath);

        try {
            recorder.prepare();
            recorder.start();
            Log.d(TAG, "MediaRecorder для мониторинга запущен.");
            buttonStartMonitoring.setText("Остановить мониторинг");

            updateSoundLevel.run();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            buttonStartMonitoring.setText("Начать мониторинг");
        }
    }

    private void stopMonitoring() {
        Log.d(TAG, "Остановка мониторинга уровня звука.");
        handler.removeCallbacks(updateSoundLevel);

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d(TAG, "MediaRecorder для мониторинга остановлен.");
            buttonStartMonitoring.setText("Начать мониторинг");
            textViewSoundLevel.setText("Уровень: Остановлен");
            progressBarSoundLevel.setProgress(0);

            String tempFilePath = requireContext().getCacheDir().getAbsolutePath() + "/temp_audio_monitor.3gp";
            File tempFile = new File(tempFilePath);
            if (tempFile.exists()) {
                if (tempFile.delete()) {
                    Log.d(TAG, "Временный файл мониторинга удален.");
                }
            }
        } else {
            buttonStartMonitoring.setText("Начать мониторинг");
            textViewSoundLevel.setText("Уровень: Остановлен");
            progressBarSoundLevel.setProgress(0);
        }
    }

    private Runnable updateSoundLevel = new Runnable() {
        @Override
        public void run() {
            if (recorder != null) {
                int amplitude = recorder.getMaxAmplitude();
                textViewSoundLevel.setText("Амплитуда: " + amplitude);
                progressBarSoundLevel.setProgress(amplitude);
                handler.postDelayed(this, REFRESH_INTERVAL);
            } else {
                handler.removeCallbacks(this);
                textViewSoundLevel.setText("Уровень: Остановлен");
                progressBarSoundLevel.setProgress(0);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        stopMonitoring();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSoundLevel);
    }
}
```

#### HardwareFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HardwareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HardwareFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HardwareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HardwareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HardwareFragment newInstance(String param1, String param2) {
        HardwareFragment fragment = new HardwareFragment();
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
        View root = inflater.inflate(R.layout.fragment_hardware, container, false);

        Button buttonSensorTask = root.findViewById(R.id.buttonSensorTask);
        Button buttonCameraTask = root.findViewById(R.id.buttonCameraTask);
        Button buttonMicrophoneTask = root.findViewById(R.id.buttonMicrophoneTask);

        buttonSensorTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_sensorFragment);
            }
        });

        buttonCameraTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_cameraFragment);
            }
        });

        buttonMicrophoneTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_hardwareFragment_to_microphoneFragment);
            }
        });

        return root;
    }
}
```
