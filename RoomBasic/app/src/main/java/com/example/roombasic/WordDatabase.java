package com.example.roombasic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//entities 可以多個 class 達成多表，version 碼用來 migration 時的基準
//這個 tag 讓套件會幫我們進行資料庫相關中介碼的操作，我們這裡作的事就很簡單，決定參數和介面的宣告，剩下的交給套件
//有關 migration => https://developer.android.com/training/data-storage/room/migrating-db-versions#groovy
//Auto migration 已試過可行，但每個版本要需要 export 出自己的 schema json，不然沒辦法運作，所以要 auto 最好一開始就決定而且 exportSchema 打開
//打開的話 gradle 要設置輸出的位置放這些 schema json
@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

    //singleton pattern 實作，避免呼叫出多個 WordDatabase 實體
    private static WordDatabase instance;

    //使用同步方法，保證無法多緒同時運行來確保更安全
    public synchronized static WordDatabase getDatabase(Context context){
        if(instance == null){
            //啟用資料庫，那個 name 就是檔名，檔案在 /data/data/com.example.roombasic/databases/ 夾底下
            //可以複制出來用 sqlite browser 打開(無加密)
            instance = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "word_database")
                    //.allowMainThreadQueries()         //不好的寫法，用 AsyncTask 取代了
                    //.fallbackToDestructiveMigration() //不好的寫法，資料會全清空
                    .addMigrations(Migration_1_2)       //接下來就是一直 , 追加新的 migration 就是傳多個參數
                    .build();
        }

        return instance;
    }

    //表示支援這個 entity 的 data access object 介面
    public abstract WordDao getWordDao();

    //多個 entity 時，就往下追加…

    //實作自已定義的 migration 物件

    private static final Migration Migration_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //sqlite 沒有 bool
            //這裡實際的實驗中，直接寫 not null default 沒有用，還得配合 Word 的屬性宣告才能生效，兩者缺一不可
            database.execSQL("alter table word add column is_Ch_View integer not null default 0");
        }
    };

    //多個 migration 時，就往下追加

}
