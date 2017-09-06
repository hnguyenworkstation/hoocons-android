package com.hoocons.hoocons_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.hoocons.hoocons_android.Adapters.GeocoderAdapter;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapBoxPlaceSearchActivity extends BaseActivity {
    @BindView(R.id.query)
    AutoCompleteTextView autocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_box_place_search);
        ButterKnife.bind(this);

        // Custom adapter
        GeocoderAdapter adapter = new GeocoderAdapter(this);
        autocomplete.setAdapter(adapter);
    }
}
