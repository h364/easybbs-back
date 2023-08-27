package com.easybbs.component;

import com.easybbs.constants.Constants;
import com.easybbs.dto.SysSettingDto;
import com.easybbs.entity.po.ForumArticle;
import com.easybbs.utils.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public void saveHotArticle(ForumArticle forumArticle) {
        redisUtils.setex(Constants.REDIS_KEY_HOT_ARTICLE + forumArticle.getArticleId(), forumArticle, Constants.ONE_HOUR);
    }

    public ForumArticle getHotArticle(String articleId) {
        ForumArticle forumArticle = (ForumArticle) redisUtils.get(Constants.REDIS_KEY_HOT_ARTICLE + articleId);
        if (forumArticle == null) {
            return null;
        }
        return forumArticle;
    }

    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    public SysSettingDto getSysSetting() {
        return (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
    }
}
