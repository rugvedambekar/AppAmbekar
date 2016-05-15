package ra.appambekar.utilities.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.ArrayList;

import ra.appambekar.AmbekarApplication;

/**
 * Created by rugvedambekar on 16-05-14.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static ArrayList<Responder> mResponders = new ArrayList<>();

    public static void RegisterResponder(Responder responder) { mResponders.add(responder); }
    public static void RemoveResponder(Responder responder) { mResponders.remove(responder); }
    public static void RemoveAllResponders() { mResponders.clear(); }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("NetworkChangeReceiver", "NETWORK CHANGE DETECTED :: " + (AmbekarApplication.hasActiveConnection() ? "ACTIVE" : "DEAD"));
        if (mResponders.isEmpty()) return;

        boolean hasConnection = AmbekarApplication.hasActiveConnection();
        for (Responder responder : mResponders) {
            if (hasConnection) responder.onNetworkAvailable();
            else responder.onNetworkUnavailable();
        }
    }

    public interface Responder {
        void onNetworkAvailable();
        void onNetworkUnavailable();
    }
}
