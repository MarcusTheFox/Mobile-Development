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
 * create an instance of this fragment.
 */
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