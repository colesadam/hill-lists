package uk.colessoft.android.hilllist.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.LocationAwareFragment;

public class LocationAwareFragmentActivity extends AppCompatActivity
        implements LocationAwareFragment.OnHillSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_location_layout);

        Fragment locationFragment = new LocationAwareFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.test, locationFragment).commit();
    }

    @Override
    public void onHillSelected(int rowid) {
        HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        if (fragment == null || !fragment.isInLayout()) {
            Intent intent = new Intent(this, HillDetailFragmentActivity.class);
            intent.putExtra("rowid", rowid);
            startActivity(intent);
        } else {
            fragment.updateHill(rowid);
        }
    }
}
