package sg.com.conversant.swiftplayer.rxjava.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Xiaodong on 2017/7/28.
 */

public class Subject implements Serializable {

    @SerializedName("rating")
    private Rating rating;

    @SerializedName("genres")
    private String[] genres;

    @SerializedName("title")
    private String title;

    @SerializedName("casts")
    private List<Cast> casts;

    @SerializedName("collect_count")
    private int collectCount;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("subtype")
    private String subtype;

    @SerializedName("directors")
    private List<Cast> directors;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public List<Cast> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Cast> directors) {
        this.directors = directors;
    }

    private String arrayToString(String[] arrays) {
        String str = "";
        for (int i = 0; i < arrays.length; i++) {
            if (i != arrays.length - 1) {
                str = str + arrays[i] + ", ";
            } else {
                str = str + arrays[i];
            }
        }
        return str;
    }

    private String objectToString(List<Cast> casts) {
        String str = "";
        if (casts == null || casts.size() == 0) {
            return str;
        }
        for (int i = 0; i < casts.size(); i++) {
            if (i != casts.size() - 1) {
                str = str + casts.get(i).toString() + ", ";
            } else {
                str = str + casts.get(i).toString();
            }
        }
        return str;
    }

    @Override
    public String toString() {
        return "{rating = " + rating.toString() + ", genres = {" + arrayToString(genres) + "}, title = " +
                title + ", casts = {" + objectToString(casts) + "}, collect_count = " + collectCount +
                ", original_title = " + originalTitle + ", subtype = " + subtype + ", directors = {" +
                objectToString(directors) + "}}";
    }
}
