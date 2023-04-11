package com.example.start;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.ContextParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AndroidManifest.xml app 最主要的系統描述檔

        //MainActivity.java 存在 java 夾，需以 pkg 名稱的方式分層放置
        //而描述畫面的 activicty_main.xml 則放在 res/layout 底下
        //res/drawable res/mipmap 主要放圖
        //res/values 下放置各種不同用途的 key-value 檔

        // R.layout.activity_main 是什麼呢？ activity_main.xml 在 compile 之後，會生成一個不重複的 id 編號在 R.layout 這個 class 底下
        // 而 setContentView 是 Activity 提供的 function，讓 Activity 跟 xml 的 layout 可以透過 R 底下生成的 id 綁在一起。
        setContentView(R.layout.activity_main);

        // 註冊按鍵讓它可以發動另一個 activity，按了可以發現它就堆疊到 main_activity 上面，當按下系統 back 鍵時
        // 當按下系統 back 鍵時，這個最上層的 another_activity 就結束，又回到 main_activity 上
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAnother();
            }
        });

        //到這裡為止 activity 的使用很像 windows 中的 window 物件
        //各 activity 可以完全視為獨立的 UI 緒，差別只在 windows 可以同時呈現多個，而且可以各自操作
        //但 android 則是堆疊各 activity，只有最上層可以操作到 ，比較像流程

        //gradle 是建置管理套件，可以 config 來達成各種建置有關的需要
        //發動特定功能也靠它 => enable data binding
        //相依某些套件 => dependencies
        //gradle 檔有手動變更時，需要按 sync 同步一下才會生效

        //ConstraintLayout 不是 android 原生的，它是靠 gradle 導入下列實作才有的
        //implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
        //它就像 WPF 的 W,H auto, H,V aline, Margin 這些概念

        //https://www.geeksforgeeks.org/difference-between-a-fragment-and-an-activity-in-android/
        //Fragment 是因為平板而產生的中間物
        //基本思維是希望依據螢幕大小在 Activity 嵌入一到多個 Fragment
        //把 app 開發從 Activity（依賴螢幕大小）的維度降到 Fragment（跟螢幕大小無關）的維度
        //大的螢幕可以放二個 Fragment，而小的螢幕就放一個
        //結果，利用 fragment 加上 navigation 的方式，反而成為取代多個activity流程式設計
        //它們是靠導入下列實作才有的
        //implementation 'androidx.navigation:navigation-fragment:2.5.3'
        //implementation 'androidx.navigation:navigation-ui:2.5.3'
        //Fragment 一個很棒的優點就是他就像疊積木一樣可以一層一層新增，也可以刪除、置換
        //使用起來甚至比 Activity 更自由，這些操作都是透過 FragmentManager 來完成
        //多個 Fragment 是基於一個 activity 來運作的， activity 本身是不能支援多窗口的
        //也就是說 android 的 activity 堆疊概念是固定不改的
        //所以是從一個 activity 上演化出看起來多窗的 FragmentActivity 類別


        // Context vs activity
        //https://medium.com/@banmarkovic/what-is-context-in-android-and-which-one-should-you-use-e1a8c6529652

        //Context 其實就是個定義基本行為的抽像類(這裡以 getResources 方法為例)，activity 就是一個 Context 來的
        this.getResources();

        //Application context 和 UI 無關，生命週期和 application 一致
        Context con = getApplicationContext();
        con.getResources();

        //其實 Application 也一樣就是一個 Context 來的
        Application app = getApplication();
        app.getResources();

        //注意!!! 由於生命週期的關系，如果到底亂存 activity context 的參考，將造成GC無法真的把 activity 記憶體回收，造成 leaking

        //View 具有和 UI 事件響應和介面操作行為，從它的行為和繼承者來判斷，它就是 WPF 的 Control 類，一切控件之祖
        //可以看到下面兩個控件都具有 performClick() 方法，而且這些控件在事件響應時都會把 view 給傳入，這就像 WPF 的 object sender 參數
        Button btn = findViewById(R.id.button);
        TextView tv = findViewById(R.id.textView);
        //btn.performClick();
        tv.performClick();

        //有關設置控件大小的 dp 值，文字大小的 sp 值
        //https://developer.android.com/training/multiscreen/screendensities
        //px = dp * (dpi / 160)  以 160 為基值去換算以當前dpi來算時，1dp 需要的 pixel 是多少
        //開發者不太需要去管理 dpi 造成的影響, 固定的 dp 值，在不同的 dpi 上，會換算成對應的像素，所以視覺上的大小就差不多
    }

    private void goToAnother()
    {
        Intent intent = new Intent(this , AnotherActivity.class);
        startActivity(intent);
    }
}