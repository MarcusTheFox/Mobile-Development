# Отчёт 1

## **Ход работы**
___
В процессе выполнения практической работы было создано 3 модуля: **layouttype**, **control_lesson1**, **buttonclicker**. 
Проверка проходила на эмуляторе **Pixel 6 API 33**.
### **Модуль "Layout Type"**
___
#### Linear Layout
![Linear Layout](https://github.com/user-attachments/assets/c13a3bde-78a0-4544-a6c1-ee6da9e5730a)

#### Table Layout
![Table Layout](https://github.com/user-attachments/assets/ead1befd-3fbe-4760-b13c-4b79cf50e798)

#### Constraint Layout
![Constraint Layout](https://github.com/user-attachments/assets/28ad4ffa-d795-415f-895e-4e13942c0f9a)


### **Модуль "Control Lesson 1"**
---
#### activity_main.xml
![control lesson](https://github.com/user-attachments/assets/85bd4c5b-2396-4cbf-8c19-bb2c0b592648)

#### activity_second.xml
##### Портретная ориентация экрана
![Портретная ориентация](https://github.com/user-attachments/assets/a9167fda-9498-4013-8778-99ff4dad2942)

##### Горизонтальная ориентация экрана
![Горизонтальная ориентация](https://github.com/user-attachments/assets/fbb28a38-0e55-4856-bb96-d83b7ad1e60c)

### **Модуль "Button Сlicker"**
---
![Pasted image 20250405181054](https://github.com/user-attachments/assets/2e181c5c-9306-4e4e-9dd4-c765db85aa37)

![Pasted image 20250405181121](https://github.com/user-attachments/assets/e35f5d2d-e33b-48af-ae97-b18e0621a3f3)

##### Код в MainActivity.java
``` java
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
```
