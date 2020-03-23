package com.board.flipr.Model;

import com.board.flipr.Interfaces.CardItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardListCard implements Serializable{
    @SerializedName("card_title")
    @Expose
    private String title;
    @SerializedName("card_description")
    @Expose
    private String description;
    @SerializedName("card_color")
    @Expose
    private Integer color;
    @SerializedName("check_boxes")
    @Expose
    private List<CheckBox> checkBoxItems;
    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments;
    @SerializedName("due_time")
    @Expose
    private LocalDateTime dueTime;


    public BoardListCard() {
    }

    public BoardListCard(String title, Integer color) {
        this.title = title;
        this.color = color;
    }

    public BoardListCard(String title, String description, Integer color, List<CheckBox> checkBoxItems, List<Attachment> attachments, LocalDateTime dueTime) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.checkBoxItems = checkBoxItems;
        this.attachments = attachments;
        this.dueTime = dueTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public List<CheckBox> getCheckBoxItems() {
        return checkBoxItems;
    }

    public void setCheckBoxItems(List<CheckBox> checkBoxItems) {
        this.checkBoxItems = checkBoxItems;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }


    public int getCheckedBoxesCount() {
        return (checkBoxItems != null) ? checkBoxItems.size() : 0;
    }

    public void addCheckBox(CheckBox checkBox) {
        if (checkBoxItems == null) {
            checkBoxItems = new ArrayList<>();
        }
        checkBoxItems.add(checkBox);
    }


    public int getCheckedCount() {
        int count = 0;
        if (checkBoxItems != null && checkBoxItems.size() > 0) {
            for (CheckBox box : checkBoxItems) {
                if (box.getCheck()) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getAttachmentCount() {
        return (attachments != null) ? attachments.size() : 0;
    }

    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(attachment);
    }

    public String getCheckedString() {
        if (getCheckedCount() > 0 && getCheckedBoxesCount() > 0) {
            return getCheckedCount() + "/" + getCheckedBoxesCount();
        }
        return null;
    }


}


