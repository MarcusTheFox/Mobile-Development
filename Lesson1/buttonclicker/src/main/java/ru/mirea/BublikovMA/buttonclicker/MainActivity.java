package ru.mirea.BublikovMA.buttonclicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tvOut;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);

        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOut.setText("Мой номер по списку № 4 (по журналу)");
            }
        };

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }

    public void onItIsNotMeClicked(View view) {
        checkBox.setChecked(!checkBox.isChecked());
        String str = checkBox.isChecked() ?
                    "Мой номер по списку № 4 (по журналу)" :
                    "Это не я сделал";
        tvOut.setText(str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}