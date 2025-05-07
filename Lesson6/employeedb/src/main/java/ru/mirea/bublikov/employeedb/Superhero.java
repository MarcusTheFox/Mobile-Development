package ru.mirea.bublikov.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Superhero {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String superpower;

    public Superhero() {
    }

    public Superhero(String name, String superpower) {
        this.name = name;
        this.superpower = superpower;
    }

    @Override
    public String toString() {
        return "Superhero{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", superpower='" + superpower + '\'' +
                '}';
    }
}