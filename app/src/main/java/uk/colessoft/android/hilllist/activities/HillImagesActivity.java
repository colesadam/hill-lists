package uk.colessoft.android.hilllist.activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.database.HillsDatabaseHelper;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.HillImageFragment;
import uk.colessoft.android.hilllist.fragments.HillImagesFragment;
import uk.colessoft.android.hilllist.model.Hill;

public class HillImagesActivity extends AppCompatActivity implements HillImagesFragment.OnFragmentInteractionListener {

    private DbHelper dbHelper;
    private HillImagesFragment imagesFragment;
    private String IMAGES_FRAGMENT = "1";
    private String IMAGE_FRAGMENT = "2";
    private Hill retrievedHill;
    private long hillId;
    private HillDetailFragment detailFragment;
    private HillImageFragment hillImageFragment;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("hillId", hillId);
        outState.putCharSequence("title", getSupportActionBar().getTitle());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSupportActionBar().setTitle(savedInstanceState.getCharSequence("title"));
        hillId = savedInstanceState.getLong("hillId");

        dbHelper = HillsDatabaseHelper.getInstance(this);
        imagesFragment = (HillImagesFragment) getSupportFragmentManager().findFragmentByTag(IMAGES_FRAGMENT);
        if (findViewById(R.id.hill_image_fragment) != null) {
            GetHillAsyncTask task = new GetHillAsyncTask();
            task.execute(hillId);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hill_images);

        if (findViewById(R.id.content_hill_images) != null) {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                return;
            }

            imagesFragment = new HillImagesFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            imagesFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_hill_images, imagesFragment, IMAGES_FRAGMENT).commit();

        }

        dbHelper = HillsDatabaseHelper.getInstance(this);

        GetHillAsyncTask getHillAsyncTask = new GetHillAsyncTask();
        hillId = getIntent().getExtras().getLong("hillId");

        getHillAsyncTask.execute(hillId);

    }

    @Override
    public void onFragmentInteraction(String uuid) {

        hillImageFragment = HillImageFragment.newInstance(uuid);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (findViewById(R.id.hill_image_fragment) != null)
            transaction.replace(R.id.hill_image_fragment, hillImageFragment, IMAGE_FRAGMENT);
        else
            transaction.replace(R.id.content_hill_images, hillImageFragment, IMAGE_FRAGMENT);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();

            GetHillAsyncTask getHillAsyncTask = new GetHillAsyncTask();
            dbHelper = HillsDatabaseHelper.getInstance(this);
            imagesFragment = (HillImagesFragment) getSupportFragmentManager().findFragmentByTag(IMAGES_FRAGMENT);
            getHillAsyncTask.execute(hillId);

        } else {
            super.onBackPressed();
        }
    }

    private class GetHillAsyncTask extends AsyncTask<Long, Void, Hill> {
        @Override
        protected void onPostExecute(Hill hill) {
            getSupportActionBar().setTitle("Images of " + hill.getHillname());
            imagesFragment.getImages(hill);
        }

        @Override
        protected Hill doInBackground(Long... params) {
            System.out.println("Called with hill id:" + params[0]);
            return dbHelper.getHill(params[0]);

        }
    }

}
