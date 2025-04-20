# Отчёт 3

## **Favorite Book**
Создан новый модуль `FavoriteBook` с двумя экранами `MainActivity` и `ShareActivity`. 
`MainActivity` отображает начальное сообщение, затем название любимой книги и цитату пользователя, введенные на втором экране. По кнопке "Открыть экран ввода данных" происходит переход на `ShareActivity`.
`ShareActivity` отображает любимую книгу и цитату разработчика. Предоставляет поля ввода для пользователя, чтобы ввести название своей любимой книги и цитату. Кнопка "Отправить данные" передает введенную пользователем информацию обратно на `MainActivity`.
### Экраны
![1-FavoriteBook-1](https://github.com/user-attachments/assets/7ac64c91-492f-44d6-9657-1e450d24ae14)

![1-FavoriteBook-2](https://github.com/user-attachments/assets/7cd3aadf-e5d9-4193-b5d0-61ad8c9813ec)

![1-FavoriteBook-3](https://github.com/user-attachments/assets/050b1a6a-f67f-449d-b348-8d65279a6ac9)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.favoritebook;  
  
import android.app.Activity;  
import android.content.Intent;  
import android.os.Bundle;  
import android.view.View;  
import android.widget.TextView;  
  
import androidx.activity.result.ActivityResult;  
import androidx.activity.result.ActivityResultCallback;  
import androidx.activity.result.ActivityResultLauncher;  
import androidx.activity.result.contract.ActivityResultContracts;  
import androidx.appcompat.app.AppCompatActivity;  
  
public class MainActivity extends AppCompatActivity {  
    private ActivityResultLauncher<Intent> activityResultLauncher;  
    static final String BOOK_NAME_KEY = "book_name";  
    static final String QUOTES_KEY = "quotes_name";  
    static final String USER_MESSAGE = "MESSAGE";  
    private TextView textViewUserBook;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        textViewUserBook = findViewById(R.id.textView);  
  
        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {  
            @Override  
            public void onActivityResult(ActivityResult result) {  
                if (result.getResultCode() == Activity.RESULT_OK) {  
                    Intent data = result.getData();  
                    String userBook = data.getStringExtra(USER_MESSAGE);  
                    textViewUserBook.setText(userBook);  
                }  
            }  
        };  
        activityResultLauncher = registerForActivityResult(  
                new ActivityResultContracts.StartActivityForResult(),  
                callback);  
    }  
  
    public void getInfoAboutBook(View view) {  
        Intent intent = new Intent(this, ShareActivity.class);  
  
        String developerBook = "Алхимик";  
        String developerQuote = "Главное — нельзя бояться, что ничего не выйдет";  
  
        intent.putExtra(BOOK_NAME_KEY, developerBook);  
        intent.putExtra(QUOTES_KEY, developerQuote);  
        activityResultLauncher.launch(intent);  
    }  
}
```
#### ShareActivity.java
``` java
package ru.mirea.Bublikov.favoritebook;  
  
import android.app.Activity;  
import android.content.Intent;  
import android.os.Bundle;  
import android.view.View;  
import android.widget.EditText;  
import android.widget.TextView;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class ShareActivity extends AppCompatActivity {  
  
    private TextView textViewDevBook;  
    private TextView textViewDevQuote;  
    private EditText editTextBookName;  
    private EditText editTextQuote;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_share);  
  
        textViewDevBook = findViewById(R.id.textView2);  
        textViewDevQuote = findViewById(R.id.textView3);  
        editTextBookName = findViewById(R.id.editTextText);  
        editTextQuote = findViewById(R.id.editTextText2);  
  
        Bundle extras = getIntent().getExtras();  
        if (extras != null) {  
            String bookName = extras.getString(MainActivity.BOOK_NAME_KEY);  
            String quotesName = extras.getString(MainActivity.QUOTES_KEY);  
            textViewDevBook.setText(String.format("Моя любимая книга:\n%s", bookName));  
            textViewDevQuote.setText(String.format("Цитата:\n%s", quotesName));  
        }  
    }  
  
    public void sendDataToMainActivity(View view) {  
        String userBookName = editTextBookName.getText().toString();  
        String userQuote = editTextQuote.getText().toString();  
  
        String userMessage = String.format("Название Вашей любимой книги: %s. Цитата: %s",  
                userBookName, userQuote);  
  
        Intent data = new Intent();  
        data.putExtra(MainActivity.USER_MESSAGE, userMessage);  
        setResult(Activity.RESULT_OK, data);  
        finish();  
    }  
}
```

## **System Intents App**
Создан новый модуль `SystemIntentsApp`, демонстрирующий использование системных интентов. Приложение содержит три кнопки для вызова системных приложений:
- **Позвонить**: открывает приложение "Phone" с предустановленным номером телефона.
- **Открыть браузер**: открывает браузер и загружает указанный веб-сайт.
- **Открыть карту**: открывает приложение "Maps" и отображает карту с заданными географическими координатами.
### Экраны
![2-SystemIntentsApp-1](https://github.com/user-attachments/assets/474532f2-737c-46ce-8ce0-162ad14ab1f9)

![2-SystemIntentsApp-2](https://github.com/user-attachments/assets/f9a5aebb-9c59-4d63-a1b7-2de5bfa5b3ee)

![2-SystemIntentsApp-3](https://github.com/user-attachments/assets/c7cccdf9-4f87-4320-bc02-5b88d8c9628b)

![2-SystemIntentsApp-4](https://github.com/user-attachments/assets/47650972-804b-479d-a63b-c8ccbdf3cb41)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.systemintentsapp;  
  
import android.content.Intent;  
import android.net.Uri;  
import android.os.Bundle;  
import android.view.View;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
  
public class MainActivity extends AppCompatActivity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
    }  
  
    public void onClickCall(View view) {  
        Intent intent = new Intent(Intent.ACTION_DIAL);  
        intent.setData(Uri.parse("tel:89811112233"));  
        startActivity(intent);  
    }  
  
    public void onClickOpenBrowser(View view) {  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setData(Uri.parse("http://developer.android.com"));  
        startActivity(intent);  
    }  
  
    public void onClickOpenMaps(View view) {  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setData(Uri.parse("geo:55.749479,37.613944"));  
        startActivity(intent);  
    }  
}
```

## **Simple Fragment App**
Создан новый модуль `SimpleFragmentApp`, демонстрирующий работу с фрагментами и адаптацию под разные ориентации экрана. 
- Созданы два фрагмента: `FirstFragment` и `SecondFragment`, каждый со своим цветом фона.
- Реализована динамическая замена фрагментов в `MainActivity` для портретной ориентации с помощью кнопок.
- Создана отдельная разметка activity_main.xml для альбомной ориентации.

### Экраны
![3-SimpleFragmentApp-1](https://github.com/user-attachments/assets/88fc8ff0-891e-4052-9a9a-c10e02bce79a)

![3-SimpleFragmentApp-2](https://github.com/user-attachments/assets/f52db4da-5fe6-4895-b7fb-252e6ca15ef9)

![3-SimpleFragmentApp-3](https://github.com/user-attachments/assets/55e666ad-d4bf-40e2-9d7c-cf49401b9998)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.simplefragmentapp;  
  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button;  
  
import androidx.activity.EdgeToEdge;  
import androidx.appcompat.app.AppCompatActivity;  
import androidx.core.graphics.Insets;  
import androidx.core.view.ViewCompat;  
import androidx.core.view.WindowInsetsCompat;  
import androidx.fragment.app.Fragment;  
import androidx.fragment.app.FragmentManager;  
import androidx.fragment.app.FragmentTransaction;  
  
public class MainActivity extends AppCompatActivity {  
    private Fragment fragment1, fragment2;  
    private FragmentManager fragmentManager;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        EdgeToEdge.enable(this);  
        setContentView(R.layout.activity_main);  
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {  
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());  
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);  
            return insets;  
        });  
  
        fragment1 = new FirstFragment();  
        fragment2 = new SecondFragment();  
        fragmentManager = getSupportFragmentManager();  
    }  
  
    public void OnClickBtnFirstFragment(View v) {  
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,  
                fragment1).commit();  
    }  
  
    public void OnClickBtnSecondFragment(View v) {  
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView,  
                fragment2).commit();  
    }  
}
```

## **Mirea Project**
Создано новое приложение `MireaProject` на основе шаблона `Navigation Drawer Activity`.
- Созданы два новых фрагмента: `DataFragment` для отображения информации об отрасли (с Material You стилизацией) и `WebViewFragment` для реализации простого браузера на основе WebView.
- В Navigation Drawer добавлены пункты меню для `DataFragment` и `WebViewFragment`, связанные с соответствующими фрагментами через `Navigation Component`.
- Обновлена конфигурация `Navigation Component` в `MainActivity` для включения новых фрагментов в навигацию.
- В разметке content_main.xml `ConstraintLayout` заменен на `NestedScrollView` для обеспечения возможности прокрутки контента фрагментов.
- Приложение позволяет переключаться между фрагментами через `Navigation Drawer`, отображая заданный контент в каждом фрагменте, включая веб-страницу в `WebViewFragment` и информацию об отрасли в `DataFragment`.

### Экраны
![4-MireaProject-1](https://github.com/user-attachments/assets/1df2af21-e59b-4c99-b980-3abd09a1120e)

![4-MireaProject-2](https://github.com/user-attachments/assets/64359025-be18-4d0c-adaf-e20d8e954f33)

![4-MireaProject-3](https://github.com/user-attachments/assets/ec3993cb-920a-45e4-8b0d-23fce2c9661b)

![4-MireaProject-4](https://github.com/user-attachments/assets/dd951a9d-3ac3-44fa-9dd3-b07f61b78895)

### Код
#### MainActivity.java
``` java
package ru.mirea.Bublikov.mireaproject;  
  
import android.os.Bundle;  
import android.view.View;  
import android.view.Menu;  
  
import com.google.android.material.snackbar.Snackbar;  
import com.google.android.material.navigation.NavigationView;  
  
import androidx.navigation.NavController;  
import androidx.navigation.Navigation;  
import androidx.navigation.ui.AppBarConfiguration;  
import androidx.navigation.ui.NavigationUI;  
import androidx.drawerlayout.widget.DrawerLayout;  
import androidx.appcompat.app.AppCompatActivity;  
  
import ru.mirea.Bublikov.mireaproject.databinding.ActivityMainBinding;  
  
public class MainActivity extends AppCompatActivity {  
  
    private AppBarConfiguration mAppBarConfiguration;  
    private ActivityMainBinding binding;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
  
        binding = ActivityMainBinding.inflate(getLayoutInflater());  
        setContentView(binding.getRoot());  
  
        setSupportActionBar(binding.appBarMain.toolbar);  
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View view) {  
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)  
                        .setAction("Action", null)  
                        .setAnchorView(R.id.fab).show();  
            }  
        });  
        DrawerLayout drawer = binding.drawerLayout;  
        NavigationView navigationView = binding.navView;  
        // Passing each menu ID as a set of Ids because each  
        // menu should be considered as top level destinations.        mAppBarConfiguration = new AppBarConfiguration.Builder(  
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,  
                R.id.dataFragment, R.id.webViewFragment)  
                .setOpenableLayout(drawer)  
                .build();  
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);  
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);  
        NavigationUI.setupWithNavController(navigationView, navController);  
    }  
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
    }  
  
    @Override  
    public boolean onSupportNavigateUp() {  
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);  
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)  
                || super.onSupportNavigateUp();  
    }  
}
```

#### DataFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;  
  
import android.os.Bundle;  
  
import androidx.fragment.app.Fragment;  
  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
  
/**  
 * A simple {@link Fragment} subclass.  
 * Use the {@link DataFragment#newInstance} factory method to  
 * create an instance of this fragment. */public class DataFragment extends Fragment {  
  
    // TODO: Rename parameter arguments, choose names that match  
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER  
    private static final String ARG_PARAM1 = "param1";  
    private static final String ARG_PARAM2 = "param2";  
  
    // TODO: Rename and change types of parameters  
    private String mParam1;  
    private String mParam2;  
  
    public DataFragment() {  
        // Required empty public constructor  
    }  
  
    /**  
     * Use this factory method to create a new instance of     * this fragment using the provided parameters.     *     * @param param1 Parameter 1.  
     * @param param2 Parameter 2.  
     * @return A new instance of fragment DataFragment.  
     */    // TODO: Rename and change types and number of parameters  
    public static DataFragment newInstance(String param1, String param2) {  
        DataFragment fragment = new DataFragment();  
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
        // Inflate the layout for this fragment  
        return inflater.inflate(R.layout.fragment_data, container, false);  
    }  
}
```

#### WebViewFragment.java
``` java
package ru.mirea.Bublikov.mireaproject;  
  
import android.os.Bundle;  
  
import androidx.fragment.app.Fragment;  
  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.webkit.WebView;  
import android.webkit.WebViewClient;  
  
/**  
 * A simple {@link Fragment} subclass.  
 * Use the {@link WebViewFragment#newInstance} factory method to  
 * create an instance of this fragment. */public class WebViewFragment extends Fragment {  
  
    // TODO: Rename parameter arguments, choose names that match  
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER  
    private static final String ARG_PARAM1 = "param1";  
    private static final String ARG_PARAM2 = "param2";  
  
    // TODO: Rename and change types of parameters  
    private String mParam1;  
    private String mParam2;  
  
    private WebView webView;  
  
    public WebViewFragment() {  
        // Required empty public constructor  
    }  
  
    /**  
     * Use this factory method to create a new instance of     * this fragment using the provided parameters.     *     * @param param1 Parameter 1.  
     * @param param2 Parameter 2.  
     * @return A new instance of fragment WebViewFragment.  
     */    // TODO: Rename and change types and number of parameters  
    public static WebViewFragment newInstance(String param1, String param2) {  
        WebViewFragment fragment = new WebViewFragment();  
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
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);  
  
        webView = view.findViewById(R.id.webView);  
        webView.getSettings().setJavaScriptEnabled(true);  
        webView.setWebViewClient(new WebViewClient());  
        webView.loadUrl("https://google.com");  
  
        return view;  
    }  
}
```
