package uk.colessoft.android.hilllist.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import uk.colessoft.android.hilllist.R;

public class WelshHillsActivity extends AppCompatActivity {

    private Dialog descDialog;
    private static final int ID_DESCDIALOG = 1;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case (ID_DESCDIALOG): {
                descDialog = new Dialog(this);
                descDialog.setContentView(R.layout.hill_group_descriptions);
                ViewGroup parent = (ViewGroup) descDialog
                        .findViewById(R.id.groups_layout);
                LayoutInflater inflater = descDialog.getLayoutInflater();
                inflater.inflate(R.layout.hewitts_description, parent);
                inflater.inflate(R.layout.nuttalls_description, parent, true);
                inflater.inflate(R.layout.marilyns_description, parent);


            }
        }
        return descDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        // TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog);
        dialog.setTitle("Main Welsh Hill Groups");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.groups_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home: {
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(this, Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            }
            case (R.id.menu_descriptions): {

                showDialog(ID_DESCDIALOG);

                return true;

            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welsh_hills_lists);

        View hewitts = findViewById(R.id.welsh_hewitts);
        View nuttalls = findViewById(R.id.welsh_nuttalls);
        View marilyns = findViewById(R.id.welsh_marilyns);
        View allWelsh = findViewById(R.id.allwelsh);

        allWelsh.setOnClickListener(v -> showHills(null, "All Welsh Hills"));
        hewitts.setOnClickListener(v -> showHills("Hew", "Welsh Hewitts"));
        nuttalls.setOnClickListener(v -> showHills("N", "Welsh Nuttalls"));
        marilyns.setOnClickListener(v -> showHills("Ma", "Welsh Marilyns"));

    }

    private void showHills(String hilltype, String hilllistType) {
        Intent intent = new Intent(WelshHillsActivity.this,
                DisplayHillListFragmentActivity.class);
        intent.putExtra("hilllistType", hilllistType);
        intent.putExtra("hilltype", hilltype);
        intent.putExtra("country", Main.WALES);
        startActivity(intent);
    }

}
