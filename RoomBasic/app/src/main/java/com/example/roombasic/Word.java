package com.example.roombasic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity tag 讓套件知道，這裡是一個表的實體 schema，並宣告出各欄位的存取子，用來給叫用者進行資料操作
@Entity
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english_word")
    private String word;

    @ColumnInfo(name = "chinese_meaning")
    private String chineseMeaning;

    @ColumnInfo(name = "is_Ch_View", defaultValue = "0")
    @NonNull
    private Boolean isChView;

    //建構子， id 是自動生成的主鍵，所以不用傳
    public Word(String word, String chineseMeaning) {
        this.word = word;
        this.chineseMeaning = chineseMeaning;

        //這裡也很特別，一定要有填值到，沒有預設值，不填 dao 就會掛
        //原因應該是這裡宣告是用 Boolean，它應該是個物件，所以可為 null
        this.isChView = false;
    }

    //以下定義各欄位存取子

    public int getId() {
        return id;
    }

    //理論上 id 是自動生成，但這個 set 套件會使用它，所以要有
    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getChineseMeaning() {
        return chineseMeaning;
    }

    public void setChineseMeaning(String chineseMeaning) {
        this.chineseMeaning = chineseMeaning;
    }

    public Boolean getChView() {
        return isChView;
    }

    public void setChView(Boolean chView) {
        isChView = chView;
    }
}
