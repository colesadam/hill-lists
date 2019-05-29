package uk.colessoft.android.hilllist.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.fragments.HillDetailFragment;
import uk.colessoft.android.hilllist.fragments.HillImageFragment;
import uk.colessoft.android.hilllist.fragments.HillImagesFragment;
import uk.colessoft.android.hilllist.model.HillDetail;

public class HillImagesActivity extends AppCompatActivity implements HillImagesFragment.OnFragmentInteractionListener {

    @Inject
    BritishHillsDatasource dbHelper;
    private HillImagesFragment imagesFragment;
    private String IMAGES_FRAGMENT = "1";
    private String IMAGE_FRAGMENT = "2";
    private HillDetail retrievedHillDetail;
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

        ((BHApplication) getApplication()).getDbComponent().inject(this);
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

        ((BHApplication) getApplication()).getDbComponent().inject(this);
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
            ((BHApplication)getApplication()).getDbComponent().inject(this);
            imagesFragment = (HillImagesFragment) getSupportFragmentManager().findFragmentByTag(IMAGES_FRAGMENT);
            getHillAsyncTask.execute(hillId);

        } else {
            super.onBackPressed();
        }
    }

    private class GetHillAsyncTask extends AsyncTask<Long, Void, HillDetail> {
        @Override
        protected void onPostExecute(HillDetail hillDetail) {
            getSupportActionBar().setTitle("Images of " + hillDetail.getHill().getHillname());
            imagesFragment.getImages(hillDetail);
        }

        @Override
        protected HillDetail doInBackground(Long... params) {
            System.out.println("Called with hill id:" + params[0]);
            return dbHelper.getHill(params[0]);

        }
    }

}
