package ra.appambekar.models;

import android.os.Parcel;
import android.os.Parcelable;


import ra.appambekar.helpers.CloudinaryHelper;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class AppScreen implements Parcelable {

    private String title;
    private String image;
    private int index;

    public AppScreen() { }
    public AppScreen(Parcel inParcel) {
        String[] data = new String[2];
        inParcel.readStringArray(data);
        title = data[0];
        image = data[1];

        index = inParcel.readInt();
    }

    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getIndex() { return index; }

    public String getImageURL() { return CloudinaryHelper.getInstance().getBaseImagesURL() + image; }
    public String getThumbnailURL(int thumbHeight) { return CloudinaryHelper.getInstance().getHeightTransformURL(thumbHeight) + image; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeStringArray(new String[] { title, image });
        outParcel.writeInt(index);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AppScreen createFromParcel(Parcel in) {
            return new AppScreen(in);
        }
        public AppScreen[] newArray(int size) {
            return new AppScreen[size];
        }
    };
}
