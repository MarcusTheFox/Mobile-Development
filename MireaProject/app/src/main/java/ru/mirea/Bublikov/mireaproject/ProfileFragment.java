package ru.mirea.Bublikov.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfilePrefs";

    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_COLOR = "color";

    private static final String TAG = ProfileFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadProfileData();

        binding.buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return root;
    }

    private void saveProfileData() {
        String name = binding.editTextProfileName.getText().toString();
        String ageStr = binding.editTextProfileAge.getText().toString();
        String color = binding.editTextProfileColor.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_AGE, ageStr);
        editor.putString(KEY_COLOR, color);

        editor.apply();

        Log.d(TAG, "Профиль сохранен: Имя=" + name + ", Возраст=" + ageStr + ", Цвет=" + color);
        Toast.makeText(requireContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        String name = sharedPreferences.getString(KEY_NAME, "");
        String ageStr = sharedPreferences.getString(KEY_AGE, "");
        String color = sharedPreferences.getString(KEY_COLOR, "");

        binding.editTextProfileName.setText(name);
        binding.editTextProfileAge.setText(ageStr);
        binding.editTextProfileColor.setText(color);

        Log.d(TAG, "Профиль загружен: Имя=" + name + ", Возраст=" + ageStr + ", Цвет=" + color);
        Toast.makeText(requireContext(), "Профиль загружен", Toast.LENGTH_SHORT).show();
    }
}