package com.easybbs.controller;

import com.easybbs.anotation.GlobalInterceptor;
import com.easybbs.constants.Constants;
import com.easybbs.entity.config.WebConfig;
import com.easybbs.entity.enums.ResponseCodeEnum;
import com.easybbs.entity.vo.ResponseVO;
import com.easybbs.exception.BusinessException;
import com.easybbs.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/file")
public class FileController extends ABaseController {

    @Resource
    private WebConfig webConfig;

    @RequestMapping("/uploadImage")
    @GlobalInterceptor(checkParams = true, checkLogin = true)
    public ResponseVO uploadImage(MultipartFile file) {
        if (file == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String filename = file.getOriginalFilename();
        String fileNameSuffix = StringTools.getFileSuffix(filename);
        if (!ArrayUtils.contains(Constants.IMAGE_SUFFIX, fileNameSuffix)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String path = copyFile(file);
        HashMap<String, String> fileMap = new HashMap<>();
        fileMap.put("fileName", path);
        return getSuccessResponseVO(fileMap);
    }

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    public void getImage(HttpServletResponse response,
                         @PathVariable("imageFolder") String imageFolder,
                         @PathVariable("imageName") String imageName) {
        readImage(response, imageFolder, imageName);
    }

    @RequestMapping("/getAvatar/{userId}")
    public void getAvatar(HttpServletResponse response, @PathVariable("userId") String userId) {
        String avatarFolderName = Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NAME;
        String avatarPath = webConfig.getProjectFolder() + avatarFolderName + userId + Constants.AVATAR_SUFFIX;
        File avatarFolder = new File(avatarFolderName);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdirs();
        }
        File file = new File(avatarPath);
        String imageName = userId + Constants.AVATAR_SUFFIX;
        if (!file.exists()) {
            imageName = Constants.AVATAR_DEFAULT;
        }
        readImage(response, Constants.FILE_FOLDER_AVATAR_NAME, imageName);
    }

    private String copyFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileSuffix = StringTools.getFileSuffix(filename);
            String fileRealName = StringTools.getRandomString(Constants.LENGTH_30) + fileSuffix;
            String folderPath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_TEMP;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File uploadFile = new File(folderPath + "/" + fileRealName);
            file.transferTo(uploadFile);
            return Constants.FILE_FOLDER_TEMP2 + "/" + fileRealName;
        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
    }

    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        ServletOutputStream sos = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
                return;
            }
            String imageSuffix = StringTools.getFileSuffix(imageName);
            String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFolder + "/" + imageName;
            if (Constants.FILE_FOLDER_TEMP2.equals(imageFolder)) {
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + "/" + imageName;
            } else if (imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)) {
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + imageName;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            imageSuffix = imageSuffix.replace(".", "");
            if (!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)) {
                response.setHeader("Cache-Control", "max-age=2592000");
            }
            response.setContentType("image/" + imageSuffix);
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                baos.write(ch);
            }
            sos.write(baos.toByteArray());
        } catch (Exception e) {
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    throw new BusinessException(ResponseCodeEnum.CODE_500);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new BusinessException(ResponseCodeEnum.CODE_500);
                }
            }
        }
    }
}
