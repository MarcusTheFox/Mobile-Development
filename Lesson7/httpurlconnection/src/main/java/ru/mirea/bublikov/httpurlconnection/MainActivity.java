package ru.mirea.bublikov.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import ru.mirea.bublikov.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String IP_INFO_URL = "https://ipinfo.io/json";
    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1/forecast?current_weather=true&latitude=%f&longitude=%f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGetInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (connectivityManager != null) {
                    networkinfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkinfo != null && networkinfo.isConnected()) {
                    new DownloadInfoTask().execute(IP_INFO_URL);
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                    binding.textViewStatus.setText("Статус: Нет интернета");
                }
            }
        });
    }

    private class DownloadInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.textViewStatus.setText("Статус: Загрузка...");
            binding.textViewIP.setText("IP: ");
            binding.textViewCity.setText("Город: ");
            binding.textViewRegion.setText("Регион: ");
            binding.textViewWeather.setText("Погода: ");
            binding.buttonGetInfo.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String ipInfoJson = downloadIpInfo(urls[0]);
                JSONObject response = new JSONObject(ipInfoJson);

                String ip = response.optString("ip", "N/A");
                String city = response.optString("city", "N/A");
                String region = response.optString("region", "N/A");
                String loc = response.optString("loc", null);

                StringBuilder resultBuilder = new StringBuilder();
                resultBuilder.append("IP: ").append(ip).append("\n");
                resultBuilder.append("Город: ").append(city).append("\n");
                resultBuilder.append("Регион: ").append(region).append("\n");

                if (!loc.isEmpty()) {
                    String[] latLon = loc.split(",");
                    if (latLon.length == 2) {
                        double latitude = Double.parseDouble(latLon[0]);
                        double longitude = Double.parseDouble(latLon[1]);

                        String weatherUrl = String.format(Locale.ENGLISH, WEATHER_BASE_URL, latitude, longitude);

                        String weatherJson = downloadIpInfo(weatherUrl);

                        JSONObject weatherResponse = new JSONObject(weatherJson);
                        JSONObject currentWeather = weatherResponse.optJSONObject("current_weather");

                        if (currentWeather != null) {
                            double temperature = currentWeather.optDouble("temperature", Double.NaN);

                            if (!Double.isNaN(temperature)) {
                                resultBuilder.append("Погода: ").append(temperature).append("°C");
                            }
                        }
                    }
                }

                return resultBuilder.toString();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String[] lines = result.split("\n");
            for (String line : lines) {
                if (line.startsWith("IP: ")) {
                    binding.textViewIP.setText(line);
                } else if (line.startsWith("Город: ")) {
                    binding.textViewCity.setText(line);
                } else if (line.startsWith("Регион: ")) {
                    binding.textViewRegion.setText(line);
                } else if (line.startsWith("Погода: ")) {
                    binding.textViewWeather.setText(line);
                } else if (line.startsWith("Ошибка")) {
                    binding.textViewStatus.setText("Статус: " + line);
                }
            }

            if (!result.startsWith("Ошибка")) {
                binding.textViewStatus.setText("Статус: Загрузка завершена");
            }

            binding.buttonGetInfo.setEnabled(true);
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error Code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }
}