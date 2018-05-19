package com.sinntalker.sinntest20180503_yy.Fragment.health;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.Fragment.Device.AddDeviceActivity;
import com.sinntalker.sinntest20180503_yy.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements View.OnClickListener{

    private Button healthDocument_btn;
    private Button addDevice_btn;

    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        healthDocument_btn = view.findViewById(R.id.healthDocument_btn);
        addDevice_btn = view.findViewById(R.id.addDevice_btn);

        healthDocument_btn.setOnClickListener(this);
        addDevice_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == healthDocument_btn) {
            Toast.makeText(getContext(), "立即查看", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), HealthDocuActivity.class));
//            Intent intent = new Intent(getContext(), HealthDocuActivity.class);
//            startActivity(intent);
        }
        if(v == addDevice_btn) {
            Toast.makeText(getContext(), "添加设备", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), AddDeviceActivity.class));
//            Intent intent = new Intent(mContext,BluetoothActivity.class);
//            startActivity(intent);
        }
    }

}
