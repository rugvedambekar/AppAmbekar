package ra.appambekar.models;

import ra.appambekar.helpers.ImageCloudHelper;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class ContactCard {

    private String pic;
    private String address;
    private String email;
    private String phone;

    public ContactCard() { }

    public String getPic() { return pic; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public String getPicURL() { return ImageCloudHelper.getInstance().getBaseImagesURL() + pic; }
}
