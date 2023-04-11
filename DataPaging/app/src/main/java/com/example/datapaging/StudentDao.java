package com.example.datapaging;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StudentDao {

    @Insert
    void insertStudents(Student... students);

    @Query("delete from students")
    void deleteAll();

    @Query("select * from students order by id")
    DataSource.Factory<Integer, Student> getAll();
}
