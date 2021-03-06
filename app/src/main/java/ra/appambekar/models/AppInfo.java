package ra.appambekar.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ra.appambekar.helpers.ImageCloudHelper;

import static ra.appambekar.helpers.FirebaseHelper.*;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class AppInfo {

    private boolean showcase;
    private String title;
    private String logo;
    private String summary;
    private String playId;

    @JsonIgnoreProperties
    private String mAppExt;

    public AppInfo() { }

    public String getTitle() { return title; }
    public String getLogo() { return logo; }
    public String getSummary() { return summary; }
    public String getPlayId() { return playId; }
    public boolean getShowcase() { return showcase; }

    public void setAppFirebaseEXT(String ext) { mAppExt = ext; }

    public String getLogoURL() { return ImageCloudHelper.getInstance().getBaseImagesURL() + logo; }
    public String getScreensEXT() { return FireChild.AppProjects.extension() + "/" + mAppExt + FireChild.Screens.extension(); }
    public String getGooglePlayLink() { return playId.isEmpty() ? null : "https://play.google.com/store/apps/details?id=" + playId; }

}
