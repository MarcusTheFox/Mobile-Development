package ru.mirea.Bublikov.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentFileManagerBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileManagerFragment extends Fragment {

    private FragmentFileManagerBinding binding;
    private TextView textViewInfo;
    private FloatingActionButton fabAddNote;

    private EditText editTextFileNameToExtract;
    private Button buttonExtractSecret;

    private static final String TAG = FileManagerFragment.class.getSimpleName();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FileManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileManagerFragment newInstance(String param1, String param2) {
        FileManagerFragment fragment = new FileManagerFragment();
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
        binding = FragmentFileManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewInfo = binding.textViewFileManagerInfo;
        fabAddNote = binding.fabAddNote;

        editTextFileNameToExtract = binding.editTextFileNameToExtract;
        buttonExtractSecret = binding.buttonExtractSecret;

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateNoteDialog();
            }
        });

        buttonExtractSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractSecret();
            }
        });

        return root;
    }

    private void extractSecret() {
        String fileName = editTextFileNameToExtract.getText().toString().trim();

        if (fileName.isEmpty()) {
            Toast.makeText(requireContext(), "Введите имя файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.toLowerCase().endsWith(".txt")) {
            fileName += ".txt";
        }

        File file = new File(requireContext().getFilesDir(), fileName);

        if (!file.exists()) {
            Log.w(TAG, "Файл не найден для извлечения: " + file.getAbsolutePath());
            Toast.makeText(requireContext(), "Файл \"" + fileName + "\" не найден.", Toast.LENGTH_SHORT).show();
            return;
        }

        String stegoContent = readFileContent(file);

        if (stegoContent == null) {
            Toast.makeText(requireContext(), "Ошибка чтения файла или файл пуст.", Toast.LENGTH_SHORT).show();
            return;
        }

        String extractedSecret = CreateNoteDialogFragment.extractSecretMessage(stegoContent);

        if (extractedSecret != null) {
            Toast.makeText(requireContext(), "Извлеченный секрет:\n" + extractedSecret, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Извлечен секрет: " + extractedSecret);
            textViewInfo.setText("Извлеченный секрет:\n" + extractedSecret);
        } else {
            Toast.makeText(requireContext(), "Не удалось извлечь секрет.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Метод извлечения секрета вернул null.");
        }
    }

    private String readFileContent(File file) {
        FileInputStream fin = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            fin = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fin, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            Log.d(TAG, "Файл успешно прочитан: " + file.getName());
            return stringBuilder.toString();

        } catch (IOException e) {
            Log.e(TAG, "Ошибка чтения файла: " + file.getName(), e);
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
                if (fin != null) fin.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при закрытии потоков чтения!", e);
            }
        }
    }

    private void showCreateNoteDialog() {
        CreateNoteDialogFragment dialogFragment = new CreateNoteDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "CreateNoteDialog");
    }
}