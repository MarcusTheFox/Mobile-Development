package ru.mirea.bublikov.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SuperheroDao {
    @Query("SELECT * FROM superhero")
    List<Superhero> getAll();

    @Query("SELECT * FROM superhero WHERE id = :id")
    Superhero getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Superhero... superheroes);

    @Update
    void update(Superhero... superheroes);

    @Delete
    void delete(Superhero... superheroes);

    @Query("DELETE FROM superhero")
    void deleteAll();
}