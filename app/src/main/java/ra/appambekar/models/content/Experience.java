package ra.appambekar.models.content;

import ra.appambekar.R;

/**
 * Created by rugvedambekar on 2016-03-03.
 */
public enum Experience {
    Applications(R.string.xp_applications, R.string.xp_applications_tag, R.array.xp_applications_list, R.array.xp_applications_imgs),
    Verticals(R.string.xp_verticals, R.string.xp_verticals_tag, R.array.xp_verticals_list, R.array.xp_verticals_imgs),
    Entities(R.string.xp_entities, R.string.xp_entities_tag, R.array.xp_entities_list, R.array.xp_entities_imgs),
    Organizations(R.string.xp_organizations, R.string.xp_organizations_tag, R.array.xp_organizations_list, R.array.xp_organizations_imgs);

    public int TitleId, TadId;
    public int ListId, ImgListId;

    private Experience(int title, int tag, int list, int imgList) {
        TitleId = title;
        TadId = tag;
        ListId = list;
        ImgListId = imgList;
    }
}
