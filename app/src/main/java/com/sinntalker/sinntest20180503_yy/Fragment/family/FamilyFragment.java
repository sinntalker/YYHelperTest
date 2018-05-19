package com.sinntalker.sinntest20180503_yy.Fragment.family;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sinntalker.sinntest20180503_yy.R;


public class FamilyFragment extends Fragment {

    private Button addRelation_btn;

    public FamilyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

        //添加好友图标大小
        Button addFamilyMember_btn = view.findViewById(R.id.addRelationShip_button);
        Drawable family = getResources().getDrawable(R.drawable.family);
        family.setBounds(0, 0, 75, 75);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        addFamilyMember_btn.setCompoundDrawables(family, null, null, null);//只放左边

        addRelation_btn = view.findViewById(R.id.addRelationShip_button);

        addRelation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "添加家人", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), AddFamilyMembersActivity.class));
            }
        });

        return view;
    }


}
