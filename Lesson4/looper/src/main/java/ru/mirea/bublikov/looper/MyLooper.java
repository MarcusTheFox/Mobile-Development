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
