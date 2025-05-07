package ru.mirea.bublikov.employeedb;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DATABASE_NAME = "superheroes_database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity onCreate");

        AppDatabase db = App.getInstance().getDatabase();
        SuperheroDao superheroDao = db.superheroDao();

        Superhero superhero1 = new Superhero("Spider-Man",
                "Wall-crawling, Spider-Sense, Super Strength");
        Superhero superhero2 = new Superhero("Iron Man",
                "Powered Armor Suit, Genius-Level Intellect");

        Log.d(TAG, "Созданы объекты супергероев: " + superhero1 + ", " + superhero2);

        superheroDao.deleteAll();
        Log.d(TAG, "Таблица Superhero очищена.");

        superheroDao.insert(superhero1, superhero2);
        Log.d(TAG, "Супергерои вставлены в базу.");

        List<Superhero> allSuperheroes = superheroDao.getAll();
        Log.d(TAG, "Загружены все супергерои:");
        for (Superhero hero : allSuperheroes) {
            Log.d(TAG, hero.toString());
        }

        Superhero firstSuperhero = superheroDao.getById(1);

        if (firstSuperhero != null) {
            Log.d(TAG, "Загружен супергерой по ID=1: " + firstSuperhero.toString());
            firstSuperhero.superpower = "Enhanced Spider-Sense";

            superheroDao.update(firstSuperhero);
            Log.d(TAG, "Супергерой с ID=1 обновлен: " + firstSuperhero.toString());

            Superhero updatedSuperhero = superheroDao.getById(1);
            Log.d(TAG, "Супергерой с ID=1 после обновления: " + updatedSuperhero.toString());

        } else {
            Log.w(TAG, "Супергерой с ID=1 не найден после вставки.");
        }

        List<Superhero> finalSuperheroes = superheroDao.getAll();
        Log.d(TAG, "Все супергерои после всех операций:");
        if (finalSuperheroes.isEmpty()) {
            Log.d(TAG, "Таблица пуста.");
        } else {
            for (Superhero hero : finalSuperheroes) {
                Log.d(TAG, hero.toString());
            }
        }

        db.close();
        App.getInstance().deleteDatabase();
    }
}