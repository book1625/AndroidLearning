package com.example.datapaging;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Button btnGen, btnClr;

    private StudentDatabase studentDatabase;

    private StudentDao studentDao;

    private StudentAdapter pageAdapter;

    private LiveData<PagedList<Student>> allStudentsPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGen = findViewById(R.id.buttonGen);
        btnClr = findViewById(R.id.buttonClr);
        pageAdapter = new StudentAdapter();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(pageAdapter);

        studentDatabase = StudentDatabase.getInstance(this);
        studentDao = studentDatabase.getStudentDao();

        //PageList 基本上就是內建動態加載，而 LiveData 則是包裝了可觀察層
        //資料在觀察發現有異動，就 submit 給 adapter 轉給畫面
        //而 adapter 決定那些資料的比對方式以決定誰該刷新畫面，再透過 view holder
        //將 資料 => itemview 可視控件 產生對應，而 itemview 就歸 recycler view 管
        //當 getAll 取得所資料的參考，它不是一個 LiveData 而是 DataSource，然後透過下面的 builder
        //DataSource 被這個 builder 內置到 LiveData 包裝裡，這一動和先前的作法不同
        allStudentsPages = new LivePagedListBuilder<>(studentDao.getAll(), 5).build();
        allStudentsPages.observe(this, students -> {
            pageAdapter.submitList(students);

            //特殊功能，這裡可以在 page 加載時發動，就可以看到 page 依次的把資料載進來的 log
            students.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int i, int i1) {
                    Log.d("mylog", "students:" + String.valueOf(i) + "=>" + String.valueOf(i1));
                }

                @Override
                public void onInserted(int i, int i1) {
                }

                @Override
                public void onRemoved(int i, int i1) {
                }
            });
        });

        btnGen.setOnClickListener(view -> {
            for (int i = 0; i < 10; i++) {
                Student student = new Student();
                student.setNumber(i);
                studentDao.insertStudents(student);
            }
        });

        btnClr.setOnClickListener(view -> {
            studentDao.deleteAll();
        });
    }
}