package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

//RecyclerView 的管理器，告訴 RecyclerView 如何填充數據

//RecyclerView 它可以在捲動時回收掉看不到的部份，但它就需要靠這個 adapter 來決定資料怎麼再填成可視元件

//ListAdapter 自帶內建一個 List<Word>
//可以避免使用 Recycler.Adapter 時在 CURD 需要知道明確的變更位置才能作到部份變更而非全面重刷

public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {

    private Boolean isCardView;

    private WordViewModel wordViewModel;

    public MyAdapter(Boolean isCardView, WordViewModel wordViewModel) {
        //ListAdapter 特有的，需提供比對差異化的方式
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                //先對是不是同一個 item (比對 key)
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                //再比資料內容是不是一樣
                return oldItem.getWord().equals(newItem.getWord()) &&
                        oldItem.getChineseMeaning().equals(newItem.getChineseMeaning()) &&
                        Objects.equals(oldItem.getChView(), newItem.getChView());

                //return false;
            }
        });

        this.isCardView = isCardView;
        this.wordViewModel = wordViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //這裡就是開放一個插口，讓RD可以接到自己的定義的元件上
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //控制著使用不同版型
        View itemView;
        if (isCardView) {
            itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.cell_normal, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //這裡就是開放一個插口，可以讓RD作資訊的綁定，資料被依序的丟過來，而這裡決定資料該怎麼和自定元件產生畫面
        //注意，由於顯示元件都是會被回收再用的，所以一定要作到每一個位置都有初始化的行為

        Word word = getItem(position);

        holder.itemView.setVisibility(View.VISIBLE);

        holder.textSn.setText(String.valueOf(word.getId()));
        holder.textEn.setText(word.getWord());
        holder.textCh.setText(word.getChineseMeaning());

        //依照設定值決定是否顯現
        if (word.getChView()) {
            holder.textCh.setVisibility(View.VISIBLE);
        } else {
            holder.textCh.setVisibility(View.GONE);
        }

        // 這裡綁定操作為的部份，由於 RecycleView 會一直呼叫這個綁定，所以被教程的說這是有效率上的浪費
        // 為了這個，他把下面綁定的行為都改到上面 onCreateViewHolder 去作
        // 但這就造成每個 item 的 switch 事件發生時，是不知道相對應的 word 是誰，為了解決這個問題
        // 它又引用了 holder.itemView.setTag(key i,obj) 把物件透過 tag 導給外部可用 getTag 調出來得到這個 word
        // 這裡我就不打算跟進，因為這讓可讀性變差了，而效能是否差那麼多我個人存疑

        //如果版型上有 switch 就增加額外的動作設定
        if (holder.switchCh != null) {
            //先消失它的事件，以免對它作動作時又引發其它行為
            holder.switchCh.setOnCheckedChangeListener(null);

            //將資料值給控件
            holder.switchCh.setChecked(word.getChView());

            //當介面有操作時，值就要反映回資料庫，所以需要有操作資料的路徑
            //這裡就需要考慮，是透過 view model 還是透過 repo 物件來作
            //本案例是透過 view model 來間接操作資料，所以建構時是傳 view model
            holder.switchCh.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                //這裡也一樣有更新的問題，一樣要拿一個 temp 去刷db，不然就會造成 observer 失敗
                Word temp = new Word(word.getWord(), word.getChineseMeaning());
                temp.setId(word.getId());
                temp.setChView(isChecked);
                wordViewModel.UpdateWords(temp);
            });
        }

        //這裡讓 item 點擊時可以發生呼叫網頁的行為
        holder.itemView.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q=" + holder.textEn.getText());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    //教程說這裡要加 static 來避免 leaking，我不是很確定原理
    //這個 holder 看起來是實作 adapter 的一部份, adapter 在 onCreateViewHolder 事件時要走這個 holder
    //而 holder 的主要功能看來就是提供 view 上控件的連結
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textSn, textEn, textCh;
        Switch switchCh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //在給定的 view 中連接控件，注意這裡可能是找不到的
            textSn = itemView.findViewById(R.id.textViewSn);
            textEn = itemView.findViewById(R.id.textViewEn);
            textCh = itemView.findViewById(R.id.textViewCh);
            switchCh = itemView.findViewById(R.id.switchCh);
        }
    }
}
