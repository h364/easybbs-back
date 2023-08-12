package com.easybbs.mappers;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户消息 数据库操作接口
 */
public interface UserMessageMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据MessageId更新
     */
    Integer updateByMessageId(@Param("bean") T t, @Param("messageId") Integer messageId);


    /**
     * 根据MessageId删除
     */
    Integer deleteByMessageId(@Param("messageId") Integer messageId);


    /**
     * 根据MessageId获取对象
     */
    T selectByMessageId(@Param("messageId") Integer messageId);


    /**
     * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType更新
     */
    Integer updateByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("bean") T t, @Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);


    /**
     * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType删除
     */
    Integer deleteByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);


    /**
     * 根据ArticleIdAndCommentIdAndSendUserIdAndMessageType获取对象
     */
    T selectByArticleIdAndCommentIdAndSendUserIdAndMessageType(@Param("articleId") String articleId, @Param("commentId") Integer commentId, @Param("sendUserId") String sendUserId, @Param("messageType") Integer messageType);

    List<Map> selectUserMessageCount(@Param("userId") String userId);

    void updateMessageStatusBatch(@Param("receivedUserId") String receivedUserId, @Param("messageType") Integer messageType, @Param("status") Integer status);
}
