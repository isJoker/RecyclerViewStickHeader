# RecyclerViewStickHeader
实现RecyclerView中指定任意item悬浮置顶



效果图如下：

![image](https://github.com/isJoker/RecyclerViewStickHeader/blob/master/gif/stick_header.gif)


 <font size=10 face="黑体">使用方式如下</font>

----------
布局
```
<com.jokerwan.recyclerviewstickheader.StickyHeaderLayout
        android:id="@+id/sh_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.jokerwan.recyclerviewstickheader.StickyHeaderLayout>
```

----------
代码
```
public class StickHeaderActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private StickyHeaderLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_header);
        layout = findViewById(R.id.sh_layout);
        recycler = findViewById(R.id.recycler);

        //设置RecyclerView中需要悬浮置顶的item的position
        layout.setShowStickItemPosition(5);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new MyAdapter());

    }
}
```
