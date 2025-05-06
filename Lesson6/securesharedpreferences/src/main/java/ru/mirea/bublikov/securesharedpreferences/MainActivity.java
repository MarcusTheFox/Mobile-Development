package ru.mirea.bublikov.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.bublikov.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences secureSharedPreferences;
    private static final String SECURE_PREFS_NAME = "secure";
    private static final String KEY_POET_NAME = "poet_name";

    private static final String DEFAULT_POET_NAME = "Гера Шипов";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    SECURE_PREFS_NAME,
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            secureSharedPreferences.edit().putString(KEY_POET_NAME, DEFAULT_POET_NAME).apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        String poetName = secureSharedPreferences.getString(KEY_POET_NAME, "[Имя поэта]");
        binding.textViewPoetName.setText(poetName);
        binding.imageViewPoet.setImageResource(R.drawable.gera_chipov);

    }
}