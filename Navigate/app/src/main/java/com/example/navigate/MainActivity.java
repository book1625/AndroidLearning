package com.example.navigate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //教程的這個叫法，已經找不到該 controller
        //NavController controller = Navigation.findNavController(this, R.id.fragmentHost);

        //參考下列連接，這樣的呼叫才有辦法找到
        //https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentHost);
        NavController controller = navHostFragment.getNavController();

        //把 controller 設置給畫面，這時就可以看到各 fragment 的 label 出現，而且有 back(不能點)
        NavigationUI.setupActionBarWithNavController(this, controller);
    }

    //這裡支援左上方的 back up 箭頭行為
    @Override
    public boolean onSupportNavigateUp() {
        //預設的就沒用
        //return super.onSupportNavigateUp();

        //改成用 NavController 的返回功能，由於這個動作不是透過指定的 action 走回，所以 action 上的動畫是不會出現的
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentHost);
        NavController controller = navHostFragment.getNavController();
        return controller.navigateUp();
    }
}