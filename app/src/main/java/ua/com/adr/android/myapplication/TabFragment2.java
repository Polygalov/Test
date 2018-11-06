package ua.com.adr.android.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;


public class TabFragment2 extends Fragment implements CustomRecyclerViewAdapter.ItemClickListener{

    CustomRecyclerViewAdapter adapter;
    private HyperlinksViewModel mHyperlinksViewModel;
    ArrayAdapter<?> spinnerAdapter;
    public static final String LINKS_ACTION = "ua.com.adr.android.test_b.action.PICTURES";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        setHasOptionsMenu(true);

        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sortlist, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        adapter = new CustomRecyclerViewAdapter(getActivity());
        mHyperlinksViewModel = ViewModelProviders.of(this).get(HyperlinksViewModel.class);
        mHyperlinksViewModel.getAllPosts().observe(this, links -> adapter.setData(links));


        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.rvAnimals);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
                    case R.id.sortByDate:
                        sortByDate();
                        break;
                    case R.id.sortByStatus:
                        sortByStatus();
                        break;
                    default:
                        break;
                }
        return true;
    }

    private void sortByDate() {
        mHyperlinksViewModel.getAllPostsByDate().observe(this, links -> adapter.setData(links));
    }

    private void sortByStatus() {
        mHyperlinksViewModel.getAllPostsByStatus().observe(this, links -> adapter.setData(links));
    }


    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("ua.com.adr.android.test_b", "ua.com.adr.android.test_b.MainActivity");
        intent.putExtra("url", adapter.getItemUrl(position));
        intent.putExtra("from", "history");
        intent.putExtra("status", adapter.getItemStatus(position));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Intent urlIntent = new Intent();
        urlIntent.setAction(LINKS_ACTION);
        urlIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        urlIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        urlIntent.putExtra("ua.com.adr.android.broadcast.Url", adapter.getItemUrl(position));
        urlIntent.putExtra("ua.com.adr.android.broadcast.From", "history");
        urlIntent.putExtra("ua.com.adr.android.broadcast.Status", adapter.getItemStatus(position));

        getActivity().sendBroadcast(urlIntent);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                InputMethodManager mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mImm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mImm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }
        }
    }

}
