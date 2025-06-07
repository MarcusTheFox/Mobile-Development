package ru.mirea.Bublikov.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.Bublikov.mireaproject.databinding.FragmentNetworkDataBinding;

public class NetworkDataFragment extends Fragment {

    private FragmentNetworkDataBinding binding;
    private TextView textViewStatus;
    private TextView textViewData;
    private Button buttonFetch;

    private static final String TAG = "NetworkDataFrag";

    private static final String DATA_URL = "https://jsonplaceholder.typicode.com/posts/1";

    public NetworkDataFragment() {
        // Required empty public constructor
    }

    public static NetworkDataFragment newInstance() {
        return new NetworkDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewStatus = binding.textViewNetworkDataStatus;
        textViewData = binding.textViewNetworkData;
        buttonFetch = binding.buttonFetchData;

        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadDataTask().execute(DATA_URL);
                } else {
                    Toast.makeText(requireContext(), "Нет интернета", Toast.LENGTH_SHORT).show();
                    textViewStatus.setText("Статус: Нет интернета");
                }
            }
        });

        return root;
    }
    
    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewStatus.setText("Статус: Загрузка...");
            textViewData.setText("");
            buttonFetch.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            if (urls == null || urls.length == 0 || urls[0] == null) {
                return "Ошибка: URL не предоставлен.";
            }
            String address = urls[0];
            InputStream inputStream = null;
            HttpURLConnection connection = null;
            String data = "";

            try {
                URL url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.close();

                    data = bos.toString("UTF-8");
                } else {
                    data = "Ошибка HTTP: " + connection.getResponseMessage() + ". Код: " + responseCode;
                }
            } catch (IOException e) {
                data = "Ошибка сети: " + e.getMessage();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Ошибка закрытия потока!", e);
                }
                if (connection != null) connection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "AsyncTask: onPostExecute с результатом: " + result);

            if (result != null && !result.startsWith("Ошибка")) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String title = jsonResponse.optString("title", "N/A");
                    String body = jsonResponse.optString("body", "N/A");
                    String formattedData = "Заголовок:\n" + title + "\n\n" + "Содержание:\n" + body;
                    textViewData.setText(formattedData);
                    textViewStatus.setText("Статус: Загрузка завершена");
                    Toast.makeText(requireContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                textViewStatus.setText("Статус: " + result);
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }

            buttonFetch.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        textViewStatus = null;
        textViewData = null;
        buttonFetch = null;
    }
}