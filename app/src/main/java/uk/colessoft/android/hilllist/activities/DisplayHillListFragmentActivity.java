package uk.colessoft.android.hilllist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.mvp.HillsAdapter;

public class DisplayHillListFragmentActivity extends AppCompatActivity implements
        HillsAdapter.RecyclerItemViewClick {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hill_list_fragment);
        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("country"));
        getSupportActionBar().setSubtitle(getIntent().getExtras().getString("hilllistType"));
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public void hillClicked(long id) {
        HillDetailFragment fragment = (HillDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hill_detail_fragment);

        if (fragment == null || !fragment.isInLayout()) {
            Intent intent = new Intent(this, HillDetailFragmentActivity.class);
            intent.putExtra("rowid", id);
            startActivity(intent);
        } else {

            //fragment.updateHill(id);
        }
    }
}