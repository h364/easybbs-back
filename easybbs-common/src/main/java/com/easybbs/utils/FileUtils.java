package com.easybbs.utils;

import com.easybbs.constants.Constants;
import com.easybbs.dto.FileUploadDto;
import com.easybbs.entity.config.AppConfig;
import com.easybbs.entity.enums.DateTimePatternEnum;
import com.easybbs.entity.enums.FileUploadTypeEnum;
import com.easybbs.exception.BusinessException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@Component
public class FileUtils {
    @Resource
    private AppConfig appConfig;

    public FileUploadDto uploadFile2Local(MultipartFile file, String folder, FileUploadTypeEnum typeEnum) {
        try {
            FileUploadDto fileUploadDto = new FileUploadDto();
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = StringTools.getFileSuffix(originalFilename);
            if (originalFilename.length() > Constants.LENGTH_30) {
                originalFilename = StringTools.getFileName(originalFilename).substring(0, Constants.LENGTH_20) + fileSuffix;
            }
            if (!ArrayUtils.contains(typeEnum.getSuffixArray(), fileSuffix)) {
                throw new BusinessException("文件类型不正确");
            }
            String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
            String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder + folder + month + "/");
            String fileName = StringTools.getRandomString(Constants.LENGTH_15) + fileSuffix;
            File targetFile = new File(targetFileFolder.getPath() + "/" + fileName);
            String localPath = month + "/" + fileName;

            if(typeEnum == FileUploadTypeEnum.AVATAR) {
                targetFileFolder = new File(baseFolder + Constants.FILE_FOLDER_AVATAR_NAME);
                targetFile = new File(targetFileFolder.getPath() + "/" + folder + Constants.AVATAR_SUFFIX);
                localPath = folder + Constants.AVATAR_SUFFIX;
            }
            if(!targetFileFolder.exists()) {
                targetFileFolder.mkdirs();
            }
            file.transferTo(targetFile);

            //压缩图片
            if(typeEnum == FileUploadTypeEnum.COMMENT_IMAGE) {
                String thumbnailName = targetFile.getName().replace(".", "_.");
                File thumbnail = new File(targetFile.getParent() + "/" + thumbnailName);
                Boolean thumbnailCreated = ImageUtils.createThumbnail(targetFile, Constants.LENGTH_200, Constants.LENGTH_200, thumbnail);
                if(!thumbnailCreated) {
                    org.apache.commons.io.FileUtils.copyFile(targetFile, thumbnail);
                }
            }else if(typeEnum == FileUploadTypeEnum.AVATAR || typeEnum == FileUploadTypeEnum.ARTICLE_COVER) {
                ImageUtils.createThumbnail(targetFile, Constants.LENGTH_200, Constants.LENGTH_200, targetFile);
            }
            fileUploadDto.setLocalPath(localPath);
            fileUploadDto.setOriginalFileName(originalFilename);

            return fileUploadDto;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
    }
}
