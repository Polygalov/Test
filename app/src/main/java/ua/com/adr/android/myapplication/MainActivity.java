package ua.com.adr.android.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import ua.com.adr.android.myapplication.db.Hyperlinks;

public class MainActivity extends AppCompatActivity {


    private SectionsPageAdapter mSectionsPageAdapter;
    CustomRecyclerViewAdapter mCustomRecyclerViewAdapter;
    private HyperlinksViewModel mHyperlinksViewModel;
    private ViewPager mViewPager;
    ArrayAdapter<?> adapter;
    ActionReceiver receiver;
    String url, nameCrud;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mCustomRecyclerViewAdapter = new CustomRecyclerViewAdapter(this);
        mHyperlinksViewModel = ViewModelProviders.of(this).get(HyperlinksViewModel.class);
        mHyperlinksViewModel.getAllPosts().observe(this, links -> mCustomRecyclerViewAdapter.setData(links));

        adapter = ArrayAdapter.createFromResource(this, R.array.sortlist, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        receiver = new ActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ua.com.adr.android.myapplication.action.LINKS");
        registerReceiver(receiver, filter);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFragment1(), getString(R.string.test));
        adapter.addFragment(new TabFragment2(), getString(R.string.history));
        viewPager.setAdapter(adapter);
    }

    public class ActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            nameCrud = intent.getStringExtra("ua.com.adr.android.broadcast.Update");
            url = intent.getStringExtra("URL");
            status = intent.getIntExtra("Status", 0);

            if(nameCrud.equals("DELETE")) {
                mHyperlinksViewModel.deletePost(new Hyperlinks(url,
                        status, System.currentTimeMillis()));
            } else if(nameCrud.equals("UPDATE")) {
                mHyperlinksViewModel.updatePost(new Hyperlinks(url,
                        status, System.currentTimeMillis()));
            } else if(nameCrud.equals("INSERT")) {
                mHyperlinksViewModel.savePost(new Hyperlinks(url,
                        status, System.currentTimeMillis()));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}