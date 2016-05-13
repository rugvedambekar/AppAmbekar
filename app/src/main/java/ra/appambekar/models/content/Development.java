package ra.appambekar.models.content;

import ra.appambekar.R;

/**
 * Created by rugvedambekar on 2016-04-21.
 */
public enum Development {
    Google(R.drawable.google_logo, R.string.a_google),
    Android(R.drawable.android_logo, R.string.a_android),
    OpenSource(R.drawable.github_logo, R.string.a_opensource),
    Extension(R.drawable.firebase_logo, R.string.a_extend),
    Analytics(R.drawable.google_analytics_logo, R.string.a_analytics),
    Ship(R.drawable.google_play_logo, R.string.a_ship);

    public int IconId, ContentId;

    private Development(int icon, int content) {
        IconId = icon;
        ContentId = content;
    }
}
