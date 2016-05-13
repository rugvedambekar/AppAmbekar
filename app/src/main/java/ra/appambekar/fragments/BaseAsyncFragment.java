package ra.appambekar.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by rugvedambekar on 16-05-08.
 */
public class BaseAsyncFragment extends Fragment {

    private AsyncTask<Void, Void, Void> mBackgroundTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            BaseAsyncFragment.this.doInBackground();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BaseAsyncFragment.this.onPostExecute();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBackgroundTask.execute();
    }

    protected void doInBackground() { }
    protected void onPostExecute() { }

}
