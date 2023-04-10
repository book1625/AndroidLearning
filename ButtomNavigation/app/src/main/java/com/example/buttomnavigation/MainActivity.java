package com.example.buttomnavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// 這個案子用來展示如何從空模版中一步步創出具有 bottom navigation 功能的 app 而非直接套用現成模版

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部列元件
        BottomNavigationView bottomNaviView = findViewById(R.id.bottomNavigationView);

        //從 fragment host 元件內提取出 navigation 用的控制器
        //NavController naviController = Navigation.findNavController(this, R.id.navFragmentHost); //這個不能用
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navFragmentHost);
        NavController naviController = navHostFragment.getNavController();

        //這是目前官方比較建議使用的 nav 方式，從 nav 檔中提取設置，設給實際的 nav 控件
        //這裡有一個要非常注意的地方，就是每個 fragment 在 nav 檔中的 id 必須和 BottomNavigationView 上每個 item 的 id 要一樣
        //底層是靠這個 id 比對要去那個 fragment 的 !!!!!
        //官方文件中，提到的 toolbar ，這裡應該被 AppCompatActivity 所自帶了，所以在設置 NavigationUI，才分成兩動把相關元件連上
        //https://developer.android.com/guide/navigation/navigation-ui?hl=zh-tw#java

        //AppBarConfiguration configuration = new AppBarConfiguration.Builder(naviController.getGraph()).build(); //使用 nav graph ，會出現 backup 箭頭
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNaviView.getMenu()).build(); //使用 item 清單，沒有 backup
        //AppBarConfiguration configuration = new AppBarConfiguration.Builder(R.id.first_fragment, R.id.second_fragment, R.id.third_fragment).build(); //完全自己決定，沒有 backup

        NavigationUI.setupActionBarWithNavController(this, naviController, configuration);
        NavigationUI.setupWithNavController(bottomNaviView, naviController);
    }
}