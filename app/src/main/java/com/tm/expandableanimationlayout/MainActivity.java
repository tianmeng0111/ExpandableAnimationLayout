package com.tm.expandableanimationlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private SwitchCompat switchCompat;
    private ExpandableAnimationLayout expandableAnimationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        switchCompat = (SwitchCompat) findViewById(R.id.switch_compat);
        expandableAnimationLayout = (ExpandableAnimationLayout) findViewById(R.id.expandable_layout);
        expandableAnimationLayout.setLayout(R.layout.layout_expandable, switchCompat.isChecked());

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    expandableAnimationLayout.setExpand();
                } else {
                    expandableAnimationLayout.setShrink();
                }
            }
        });
    }


}
