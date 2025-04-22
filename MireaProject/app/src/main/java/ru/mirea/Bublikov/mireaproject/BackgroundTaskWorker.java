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
