package com.wyf.codenotes.widget;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.wyf.codenotes.R;

import java.util.ArrayList;
import java.util.List;

public class ListWidget extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_widget);
    }


    private List<String> bleList = new ArrayList<>();

    /**
     * adapter的几种简单写法
     */
    private void adapter(){

        //item只有一个文本框是动态的. 数据源非String类型
        ArrayAdapter adapter1 = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, bleList){
            @Nullable
            @Override
            public Object getItem(int position) {
                 String  data = "这是显示的数据";
                return data;
            }
        } ;

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"F1","F2","F3","F4","F5","F6","F7","F8",});
    }
}
