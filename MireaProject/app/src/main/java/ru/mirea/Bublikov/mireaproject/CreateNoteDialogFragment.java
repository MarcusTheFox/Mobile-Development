package ru.mirea.Bublikov.mireaproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentCreateNoteDialogBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNoteDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNoteDialogFragment extends DialogFragment {

    private FragmentCreateNoteDialogBinding binding;
    private EditText editTextTitle;
    private EditText editTextContent;
    private EditText editTextSecretMessage;
    private Button buttonCancel;
    private Button buttonSave;

    private static final String TAG = CreateNoteDialogFragment.class.getSimpleName();

    private static final int LENGTH_BITS = 16;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateNoteDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNoteDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNoteDialogFragment newInstance(String param1, String param2) {
        CreateNoteDialogFragment fragment = new CreateNoteDialogFragment();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Новая запись");

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNoteDialogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextTitle = binding.editTextNoteTitle;
        editTextContent = binding.editTextNoteContent;
        editTextSecretMessage = binding.editTextSecretMessage;
        buttonCancel = binding.buttonCancel;
        buttonSave = binding.buttonSave;

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Нажата кнопка Отмена");
                dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                String secretMessage = editTextSecretMessage.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(requireContext(), "Заголовок и содержание не могут быть пустыми", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Нажата кнопка Сохранить. Заголовок: " + title + ", Обложка: " + content + ", Секрет: " + secretMessage);

                String stegoContent = null;
                if (!secretMessage.isEmpty()) {
                    stegoContent = hideSecretMessage(content, secretMessage);
                    if (stegoContent == null) {
                        Toast.makeText(requireContext(), "Ошибка: обложка слишком короткая для скрытия секрета!", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Обложка слишком короткая для скрытия секрета.");
                        return;
                    }
                    Log.d(TAG, "Секрет скрыт в обложке. Результат (часть): " + stegoContent.substring(0, Math.min(stegoContent.length(), 50)) + "...");

                } else {
                    stegoContent = content;
                    Log.d(TAG, "Секретное сообщение отсутствует. Сохраняем только обложку.");
                }

                saveNoteToFile(title, stegoContent);

                dismiss();
            }
        });

        return root;
    }

    private String hideSecretMessage(String coverText, String secretMessage) {
        if (coverText == null || secretMessage == null) {
            Log.w(TAG, "hideSecretMessage: coverText or secretMessage is null.");
            return null;
        }

        byte[] secretBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        int secretLength = secretBytes.length;

        byte[] lengthBytes = new byte[] {
                (byte) (secretLength >> 8),
                (byte) secretLength
        };
        
        int totalBitsToHide = LENGTH_BITS + secretLength * 8;

        StringBuilder stegoTextBuilder = new StringBuilder();
        int bitsHiddenCount = 0;

        for (char coverChar : coverText.toCharArray()) {
            if (Character.isLetter(coverChar)) {
                if (bitsHiddenCount < totalBitsToHide) {
                    int bitToHide = getBitToHide(bitsHiddenCount, lengthBytes, secretBytes);

                    if (bitToHide == 0) {
                        stegoTextBuilder.append(Character.toLowerCase(coverChar));
                    } else {
                        stegoTextBuilder.append(Character.toUpperCase(coverChar));
                    }

                    bitsHiddenCount++;

                } else {
                    stegoTextBuilder.append(coverChar);
                }
            } else {
                stegoTextBuilder.append(coverChar);
            }
        }

        if (bitsHiddenCount < totalBitsToHide) {
            Log.w(TAG, "hideSecretMessage: Не хватило букв в обложке (" + bitsHiddenCount + ") для скрытия всех битов (" + totalBitsToHide + ") секрета.");
            return null;
        }

        return stegoTextBuilder.toString();
    }

    private static int getBitToHide(int bitsHiddenCount, byte[] lengthBytes, byte[] secretBytes) {
        int bitToHide;
        if (bitsHiddenCount < LENGTH_BITS) {
            int currentLengthByteIndex = bitsHiddenCount / 8;
            int currentLengthBitIndex = bitsHiddenCount % 8;

            byte currentLengthByte = lengthBytes[currentLengthByteIndex];
            bitToHide = (currentLengthByte >> (7 - currentLengthBitIndex)) & 1;
        } else {
            int secretBitsCount = bitsHiddenCount - LENGTH_BITS;
            int currentSecretByteIndex = secretBitsCount / 8;
            int currentSecretBitIndex = secretBitsCount % 8;

            byte currentSecretByte = secretBytes[currentSecretByteIndex];
            bitToHide = (currentSecretByte >> (7 - currentSecretBitIndex)) & 1;
        }
        return bitToHide;
    }

    private void saveNoteToFile(String title, String stegoContent) {
        String fileName = title.replaceAll("[^a-zA-Z0-9-_.]", "_");

        if (fileName.isEmpty()) {
            fileName = "untitled_note";
        }
        fileName += ".txt";

        FileOutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            outputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

            outputStreamWriter.write(stegoContent);

            Log.d(TAG, "Заметка со скрытым секретом успешно записана: " + fileName);
            Toast.makeText(requireContext(), "Запись \"" + title + "\" сохранена (с секретом)", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "Ошибка записи заметки в файл!", e);
            Toast.makeText(requireContext(), "Ошибка при сохранении записи", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Ошибка при закрытии потока записи заметки!", e);
            }
        }
    }

    public static String extractSecretMessage(String stegoText) {
        if (stegoText == null) {
            Log.w(TAG, "extractSecretMessage: stegoText is null.");
            return null;
        }

        List<Integer> bits = new ArrayList<>();
        for (char stegoChar : stegoText.toCharArray()) {
            if (Character.isLetter(stegoChar)) {
                if (Character.isLowerCase(stegoChar)) {
                    bits.add(0);
                } else {
                    bits.add(1);
                }
            }
        }

        if (bits.size() < LENGTH_BITS) {
            Log.w(TAG, "extractSecretMessage: Недостаточно битов (" + bits.size() + ") для извлечения длины секрета (" + LENGTH_BITS + ").");
            return null;
        }

        int secretLength = 0;
        for (int i = 0; i < LENGTH_BITS; i++) {
            secretLength = (secretLength << 1) | bits.get(i);
        }
        Log.d(TAG, "extractSecretMessage: Извлеченная длина секрета (в байтах): " + secretLength);

        if (secretLength < 0) {
            Log.w(TAG, "extractSecretMessage: Извлечена некорректная длина секрета (отрицательная).");
            return null;
        }

        int totalHiddenBits = LENGTH_BITS + secretLength * 8;

        if (bits.size() < totalHiddenBits) {
            Log.w(TAG, "extractSecretMessage: Недостаточно битов (" + bits.size() + ") для извлечения всего секрета (ожидается " + totalHiddenBits + ").");
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int currentByte = 0;
        int bitCount = 0;

        for (int i = LENGTH_BITS; i < totalHiddenBits; i++) {
            int bit = bits.get(i);
            currentByte = (currentByte << 1) | bit;
            bitCount++;
            if (bitCount == 8) {
                baos.write(currentByte);
                currentByte = 0;
                bitCount = 0;
            }
        }

        byte[] extractedBytes = baos.toByteArray();
        try {
            String secret = new String(extractedBytes, StandardCharsets.UTF_8);
            Log.d(TAG, "extractSecretMessage: Извлечено байт: " + extractedBytes.length + ", Секрет: " + secret);
            return secret;
        } catch (Exception e) {
            Log.e(TAG, "extractSecretMessage: Ошибка при преобразовании байт секрета в строку!", e);
            return null;
        }
    }
}