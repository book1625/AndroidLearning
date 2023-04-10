package com.example.roombasic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WordViewModel wordViewModel;

    MyAdapter myAdapterNormal, myAdapterCard;

    RecyclerView recyclerView;

    Switch switchTheme;

    Button btnInsert, btnDelete, btnUpdate, btnClean;

    SearchView search;

    TextProvider textProvider = new TextProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        //controls
        recyclerView = findViewById(R.id.recycler);
        btnInsert = findViewById(R.id.buttonInsert);
        btnDelete = findViewById(R.id.buttonDelete);
        btnUpdate = findViewById(R.id.buttonUpdate);
        btnClean = findViewById(R.id.buttonClean);
        switchTheme = findViewById(R.id.switchTheme);

        //insert
        btnInsert.setOnClickListener(view -> {
            Pair<String, String> pair = textProvider.GetRandText();
            Word word1 = new Word(pair.first, pair.second);
            wordViewModel.InsertWords(word1);
            resetSearch();
        });

        //clear
        btnClean.setOnClickListener(view -> {
            wordViewModel.CleanWords();
            resetSearch();
        });

        //update
        btnUpdate.setOnClickListener(view -> {
            List<Word> words = wordViewModel.GetCurrentWords();
            if ((words != null ? words.size() : 0) == 0) return;

            //這裡有個很重要的概念，當前能拿到的 word 物件都是和 db 連動，而且暫時顯示畫面上
            //如果直接改這個物件的屬性，則 db 也不會改，畫面也不會變，但資料卻變了
            //如果直接改這個物件屬性，又拿它去改 db ，則連動事件發生，但比對時卻兩手資料相同，造成畫面無法更新!!
            //所以一定得再建一個新的用來操作db，而舊的 word 得保持原樣
            Word src = words.get(0);
            Word word = new Word("Brooks", "就是我本人 !!");
            word.setId(src.getId());

            wordViewModel.UpdateWords(word);
            resetSearch();
        });

        //delete
        btnDelete.setOnClickListener(view -> {
            List<Word> words = wordViewModel.GetCurrentWords();
            if ((words != null ? words.size() : 0) == 0) return;

            Word word = words.get(0);

            wordViewModel.DeleteWords(word);
            resetSearch();
        });

        //指定是一維的列表 LinearLayoutManager, 如果是二維用 GridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adapter
        myAdapterCard = new MyAdapter(true, wordViewModel);
        myAdapterNormal = new MyAdapter(false, wordViewModel);
        recyclerView.setAdapter(myAdapterNormal);
        switchTheme.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                recyclerView.setAdapter(myAdapterCard);
            } else {
                recyclerView.setAdapter(myAdapterNormal);
            }
        });

        //支援滑動刪除
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //這裡如果要實作拖動改變順序，由於拖動時事件發生，如果修改資料以對應拖動，但資料又是非同步，無法立刻同步
                //這時快速的拖動就會造成資料的錯亂，所以一般作法都需要先進到拖動模式，然後只拖動畫面上的資料，等到完成才同步到資料庫
                //這裡就先不作了…
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word tarWord = wordViewModel.GetCurrentWords().get(position);
                wordViewModel.DeleteWords(tarWord);

                //這裡等於利用了 snackbar 的制造間接的保留了刪掉的 word，得到了可以加回來的機會
                Snackbar.make(getParent(), findViewById(R.id.main_layout), "Undo ??", Snackbar.LENGTH_LONG)
                        .setAction("Restore Word", view -> wordViewModel.InsertWords(tarWord)
                        ).show();
            }

            //這裡自己算圖示的拖動位移，用來達成拖動時出現刪除圖示
            //目前畫出來的效果和教程有些不同，主要是那個 icon 的圖層似乎不是在最底層，但目前不確定該怎麼處理
            Drawable icon = ContextCompat.getDrawable(getApplicationContext() , R.drawable.baseline_delete_forever_24);
            Drawable backGround = new ColorDrawable(Color.GRAY);
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
                int iconLeft, iconRight, iconTop, iconBottom;
                int backLeft, backRight, backTop, backBottom;

                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + iconMargin;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if(dX > 0){
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int)dX;
                    backGround.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft+ icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                }else if (dX < 0){
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int)dX;
                    backGround.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                }else{
                    backGround.setBounds(0,0,0,0);
                    icon.setBounds(0,0,0,0);
                }
                backGround.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(recyclerView);

        //幫 recycler view 加上 item 的分隔裝飾
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //data observe 當資料有變時，送給資料喬接器，並引發通知，這裡兩個都要送
        wordViewModel.GetAllWords().observe(this, words -> {
            List<Word> currentWords = wordViewModel.GetCurrentWords();
            myAdapterCard.submitList(currentWords);
            myAdapterNormal.submitList(currentWords);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        //這個是右上 menu 的初始，沒有寫是不會出現的，這裡放 main 上 是所有頁都出來
        // 如果是 fragment 的話就要各自作，而要要 set HasOptionsMenu 屬性 true 才會出現
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //避免太長擠標題，但直設不是什麼好方法
        search = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        search.setMaxWidth(500);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //動態的篩選結果

                //變動 vide model 的篩選字，記下來用
                wordViewModel.setSearchPattern(s);

                //動態依篩選字，決定轉送到 adapter 的資料，這用來控制要真的顯示的資訊
                //stream 的用法 就和 linq 的概念很接近
                List<Word> filterWords = wordViewModel.GetCurrentWords();

                //當資料有變時，送給資料喬接器，並引發通知，這裡兩個都要送
                myAdapterCard.submitList(filterWords);
                myAdapterNormal.submitList(filterWords);

                //表示事件不需要再往下傳，處理到這就好
                return true;
            }
        });

        return true;
    }

    private void resetSearch() {
        wordViewModel.setSearchPattern("");     //clear model data
        search.setQuery("", false);             //clear the text
        search.setIconified(true);              //close the search editor and make search icon again
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否清空所有資料");
                builder.setPositiveButton("ya~", (dialogInterface, i) -> {
                    wordViewModel.CleanWords();
                });
                builder.setNegativeButton("no!!", (dialogInterface, i) -> {
                    //nothing....
                });
                builder.create();
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Note 這裡將操作資料的碼都移到 view model，只剩畫面事件響應，操作 view model 所給的行為
}