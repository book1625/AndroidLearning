package com.example.roombasic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.stream.Collectors;

public class WordViewModel extends AndroidViewModel {

    WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        searchPattern = "";
    }

    private String searchPattern;

    public String getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(String searchPattern) {
        this.searchPattern = searchPattern;
    }

    public LiveData<List<Word>> GetAllWords() {
        return wordRepository.GetAllWordLive();
    }

    public List<Word> GetCurrentWords()
    {
        if(searchPattern.isEmpty())
            return wordRepository.GetAllWordLive().getValue();

        return wordRepository.GetAllWordLive().getValue().stream().filter(
                p->p.getWord().toLowerCase().contains(searchPattern.toLowerCase()))
                .collect(Collectors.toList());
    }

    //Note 以下四個接口讓 view 可以操作資料，但實際上，就只是轉去操作資料倉庫

    public void InsertWords(Word... words) {
        wordRepository.InsertWords(words);
    }

    public void UpdateWords(Word... words) {
        wordRepository.UpdateWords(words);
    }

    public void DeleteWords(Word... words) {
        wordRepository.DeleteWords(words);
    }

    public void CleanWords() {
        wordRepository.CleanWords();
    }
}
