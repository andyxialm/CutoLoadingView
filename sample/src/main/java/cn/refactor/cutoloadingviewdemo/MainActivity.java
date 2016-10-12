package cn.refactor.cutoloadingviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Create by andy (https://github.com/andyxialm)
 * Create time: 16/10/11 14:33
 * Description : Sample activity (sample images from UnSplash)
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setupViews() {

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.scroll_refresh:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RefreshFragment.newInstance(RefreshFragment.TYPE_PULL_TO_REFRESH)).commit();
                        break;
                    case R.id.regular_refresh:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RefreshFragment.newInstance(RefreshFragment.TYPE_REGULAR_REFRESH)).commit();
                        break;
                    case R.id.menu_sample:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SampleFragment.newInstance()).commit();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RefreshFragment.newInstance(RefreshFragment.TYPE_PULL_TO_REFRESH)).commit();
    }

}
