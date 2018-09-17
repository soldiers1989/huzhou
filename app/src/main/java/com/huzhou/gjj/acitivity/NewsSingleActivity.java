package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.News;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;




@ContentView(R.layout.activity_news_single)
public class NewsSingleActivity extends BaseAppCompatActivity {

   @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.fabu_time)
    private TextView fabu_time;
    @ViewInject(R.id.contetent)
    private TextView contetent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        News information = (News) getIntent()
                .getSerializableExtra("news_data");
        title.setText(information.getInfoTitle());
        fabu_time.setText(information.getInputDate());
        contetent.setText(information.getInfo());
    }
}
