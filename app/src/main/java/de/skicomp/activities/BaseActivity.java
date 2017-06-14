package de.skicomp.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleError(Response response) {
        if (response == null) {
            Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            return;
        }

        String errorMessage = "";
        try {
            errorMessage = response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), response.code() + " / " + errorMessage, Toast.LENGTH_SHORT).show();
    }

}
