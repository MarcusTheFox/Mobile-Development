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