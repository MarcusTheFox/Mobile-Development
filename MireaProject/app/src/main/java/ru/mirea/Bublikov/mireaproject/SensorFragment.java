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