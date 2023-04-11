package com.example.roombasic;

//操作數據的倉庫，這裡其實就是為了把操作數據進行一定程度的封裝
//外部其實就不知道這裡到底是操作了誰，是資料庫還是 api 還是寫檔
//另外，也把非同步的部份都隱藏了起來，避免呼叫的 view model 過度的涉入資料行為細節

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {

    //這個變數其實非常重要，因為它透過 LiveData 內部的實作，作到自動同步資料庫的內容
    //所以可以視它為 db 檔在記憶體中的分身，而且自動同步
    //透過 DAO 發動語法變更 db 資料時，LiveData 就會自己隨之變化，所以需要持有這個物件，而且不可隨便換
    private final LiveData<List<Word>> allWordLive;

    private final WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase db = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = db.getWordDao();
        allWordLive = wordDao.getAllWords();
    }

    //取得全部資料，特別方法，因為取的是和資料庫連動的 LiveData 物件
    public LiveData<List<Word>> GetAllWordLive() {
        return allWordLive;
    }

    //Note : 由於操作 DB 是被限定不可以在主線程上運行(除非下達 allowMainThreadQueries())，所以要對 DB 操作進行
    //非同步工作的設計，這裡使用了 AsyncTask 抽像類來實作需要的非同步工作，它其實很像 .net 的 background worker
    //可以傳參數，可以在發動前後指定 callback，可以作進度回報等行為
    //但每個方法都要宣告類別...
    //另外 AsyncTask 看起來已不是官方建議用的，因為 ctor 的部份都出現了過期警告
    //https://stackoverflow.com/questions/58767733/the-asynctask-api-is-deprecated-in-android-11-what-are-the-alternatives
    //不確定上面的討論中，何者是較為合適的方案，之後再研究

    public void InsertWords(Word... words){
        new InsertAsyncTask(wordDao).execute(words);
    }

    public void UpdateWords(Word... words){
        new UpdateAsyncTask(wordDao).execute(words);
    }

    public void DeleteWords(Word... words){
        new DeleteAsyncTask(wordDao).execute(words);
    }

    public void CleanWords(){
        new CleanAsyncTask(wordDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Word, Void, Void>{
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Word, Void, Void>{
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    //這裡由於不用參數，所以 template arg 傳了 Void
    private static class CleanAsyncTask extends AsyncTask<Void, Void, Void>{
        private WordDao wordDao;

        public CleanAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        //注意這裡 Void... 的寫法
        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
