package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillListFragment;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.ListHillsMapFragment;

public class ListHillsMapFragmentActivity extends AppCompatActivity implements
        HillListFragment.OnHillSelectedListener,
        ListHillsMapFragment.MapOnHillSelectedListener {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hills_map_fragment);
    }

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

    public void mapOnHillSelected(int rowid) {
        HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        if (fragment != null && fragment.isInLayout()) {
            fragment.updateHill(rowid);
        }
    }
}
