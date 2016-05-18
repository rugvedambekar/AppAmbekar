package ra.appambekar.utilities.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.models.events.NetworkEvent;

/**
 * Created by rugvedambekar on 16-05-14.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d("NetworkChangeReceiver", "NETWORK CHANGE DETECTED :: " + (AmbekarApplication.hasActiveConnection() ? "ACTIVE" : "DEAD"));
        EventBus.getDefault().post(new NetworkEvent(AmbekarApplication.hasActiveConnection()));
    }

}
