package com.pratap.endlessrecyclerview;

import java.io.Serializable;

/**
 * Created by praful on 8/2/17.
 */
public class GalleryPojo implements Serializable {


    String imageComment, image, date_time, uploaded_by;

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public GalleryPojo(String image, String imageComment) {
        this.imageComment = imageComment;
        this.image = image;
    }

    public GalleryPojo(String imageComment, String image, String date_time, String uploaded_by) {
        this.imageComment = imageComment;
        this.image = image;
        this.date_time = date_time;
        this.uploaded_by = uploaded_by;
    }

    public GalleryPojo() {
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
