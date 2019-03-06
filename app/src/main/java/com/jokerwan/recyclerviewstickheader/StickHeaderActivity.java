package com.jokerwan.recyclerviewstickheader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class StickHeaderActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private StickyHeaderLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_header);
        layout = findViewById(R.id.sh_layout);
        recycler = findViewById(R.id.recycler);

        layout.setShowStickItemPosition(5);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new MyAdapter());

    }
}
