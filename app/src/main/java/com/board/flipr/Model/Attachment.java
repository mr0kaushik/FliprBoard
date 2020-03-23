package com.board.flipr.Model;

public class Attachment {
    String attachment_text;
    String link;

    public Attachment() {
    }

    public Attachment(String attachment_text, String link) {
        this.attachment_text = attachment_text;
        this.link = link;
    }

    public String getAttachment_text() {
        return attachment_text;
    }

    public void setAttachment_text(String attachment_text) {
        this.attachment_text = attachment_text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
