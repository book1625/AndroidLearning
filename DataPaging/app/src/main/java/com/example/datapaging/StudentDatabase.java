package com.example.datapaging;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class}, version = 1, exportSchema = false)
public abstract class StudentDatabase extends RoomDatabase {

    private static StudentDatabase instance;

    public static synchronized StudentDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, StudentDatabase.class, "students_database")
                    .allowMainThreadQueries() //這裡我偷懶了… 沒有寫非同步，所以就省了 repo 這個類，直接操作 dao
                    .build();

        return instance;
    }

    abstract StudentDao getStudentDao();
}
