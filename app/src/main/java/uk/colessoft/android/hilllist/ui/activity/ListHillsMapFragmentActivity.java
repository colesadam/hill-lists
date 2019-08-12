package uk.colessoft.android.hilllist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.ui.fragment.HillListFragment;
import uk.colessoft.android.hilllist.ui.fragment.HillDetailFragment;
import uk.colessoft.android.hilllist.ui.fragment.ListHillsMapFragment;

public class ListHillsMapFragmentActivity extends AppCompatActivity implements
        HillListFragment.OnHillSelectedListener,
        ListHillsMapFragment.MapOnHillSelectedListener {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.hills_map_fragment);
    }

    public void onHillSelected(long rowid) {

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
