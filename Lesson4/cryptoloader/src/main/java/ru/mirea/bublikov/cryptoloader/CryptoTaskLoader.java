package ru.mirea.bublikov.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTaskLoader extends AsyncTaskLoader<String> {

    private final String TAG = CryptoTaskLoader.class.getSimpleName();
    private byte[] encryptedText;
    private byte[] secretKeyEncoded;

    public static final String ARG_WORD = "word";
    public static final String ARG_SECRET_KEY = "secret_key";

    public CryptoTaskLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            encryptedText = args.getByteArray(ARG_WORD);
            secretKeyEncoded = args.getByteArray(ARG_SECRET_KEY);
        }
        Log.d(TAG, "CryptoTaskLoader создан");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground: Начата дешифровка...");
        SystemClock.sleep(5000);

        SecretKey originalKey = new SecretKeySpec(secretKeyEncoded, 0, secretKeyEncoded.length, "AES");
        Log.d(TAG, "loadInBackground: Ключ восстановлен.");

        try {
            String decryptedText = decryptMsg(encryptedText, originalKey);
            Log.d(TAG, "loadInBackground: Дешифровка успешна. Результат: " + decryptedText);
            return decryptedText;
        } catch (RuntimeException e) {
            Log.e(TAG, "loadInBackground: Ошибка при дешифровании!", e);
            return "Ошибка дешифрования: " + e.getMessage();
        }
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            byte[] decryptedBytes = cipher.doFinal(cipherText);
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
