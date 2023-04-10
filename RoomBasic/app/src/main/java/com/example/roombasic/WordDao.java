package com.example.roombasic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Dao tag 讓套件幫我們依各種行為 tag 寫好了操作 DB 所需要的真實碼
//對這個介面的方法作 Go To -> Implementation 就可到看到一個自動生成中間檔
//所以我們作的事情依然只有宣告
@Dao
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updateWords(Word... words);

    @Delete
    void deleteWords(Word... words);

    @Query("delete from Word")
    void deleteAllWords();

    @Query("select * from Word order by id desc")
    LiveData<List<Word>> getAllWords();
}
