package sg.com.conversant.swiftplayer.rxjava.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Xiaodong on 2017/7/28.
 */

public class Rating implements Serializable {

    @SerializedName("max")
    private float max;

    @SerializedName("average")
    private float average;

    @SerializedName("min")
    private float min;

    @SerializedName("stars")
    private String stars;

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "{max = " + max + ", average = " + average + ", min = " + min +
                ", stars = " + stars + "}";
    }
}
