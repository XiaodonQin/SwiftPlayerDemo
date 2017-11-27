package sg.com.conversant.swiftplayer.rxjava.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Xiaodong on 2017/7/28.
 */

public class Cast implements Serializable {

    @SerializedName("alt")
    private String alt;//详情网址

    @SerializedName("avatars")
    private Avatars avatars;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Avatars getAvatars() {
        return avatars;
    }

    public void setAvatars(Avatars avatars) {
        this.avatars = avatars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{alt = " + alt + ", avatars = " + avatars.toString() +
                ", name = " + name + ", id = " + id + "}";
    }
}
