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