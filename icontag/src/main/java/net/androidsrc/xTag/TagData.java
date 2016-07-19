package net.androidsrc.xTag;

/**
 * Created by aman on 17/7/16.
 */
public class TagData {
    private int imgId;
    private String displayName;

    public TagData(String displayName, int imgId) {
        this.displayName = displayName;
        this.imgId = imgId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getImgId() {
        return imgId;
    }
}
