package com.easybbs.entity.vo;

import com.easybbs.entity.vo.ForumArticleVO;

public class ForumArticleDetailVO {
    private ForumArticleVO forumArticle;
    private ForumArticleAttachmentVO attachment;
    private Boolean haveLike = false;

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }

    public ForumArticleVO getForumArticle() {
        return forumArticle;
    }

    public void setForumArticle(ForumArticleVO forumArticle) {
        this.forumArticle = forumArticle;
    }

    public ForumArticleAttachmentVO getAttachment() {
        return attachment;
    }

    public void setAttachment(ForumArticleAttachmentVO attachment) {
        this.attachment = attachment;
    }
}
