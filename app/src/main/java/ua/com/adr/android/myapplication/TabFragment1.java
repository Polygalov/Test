package ua.com.adr.android.myapplication;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TabFragment1 extends Fragment {
    Button ok;
    EditText etUrl;
    HyperlinksViewModel mHyperlinksViewModel;
    public static final String LINKS_ACTION = "ua.com.adr.android.test_b.action.PICTURES";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1,container,false);
        mHyperlinksViewModel = ViewModelProviders.of(this).get(HyperlinksViewModel.class);
        ok = (Button) v.findViewById(R.id.button_ok);
        etUrl = (EditText) v.findViewById(R.id.et_url);
        ok.setOnClickListener(v1 -> {
            String locUrl = etUrl.getText().toString();
            if(locUrl.isEmpty()){
                Toast.makeText(getActivity(),"Введите ссылку", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("ua.com.adr.android.test_b", "ua.com.adr.android.test_b.MainActivity");
                intent.putExtra("url", locUrl);
                intent.putExtra("from", "okButton");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Intent urlIntent = new Intent();
                urlIntent.setAction(LINKS_ACTION);
                urlIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                urlIntent.putExtra("ua.com.adr.android.broadcast.Url", locUrl);
                urlIntent.putExtra("ua.com.adr.android.broadcast.From", "okButton");
                urlIntent.putExtra("ua.com.adr.android.broadcast.Status", 1);
                getActivity().sendBroadcast(urlIntent);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
