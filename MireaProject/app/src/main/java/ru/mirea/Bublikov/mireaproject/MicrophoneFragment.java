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