package com.zcc.highmyopia.controller;

import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.util.ThrowUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName ResourceController
 * @Description
 * @Author aigao
 * @Date 2024/12/29 11:12
 * @Version 1.0
 */
@Slf4j
@Api(tags = "系统静态资源访问管理")
@RestController
public class ResourceController {
    @Value("${hospital.filePath}")
    private String targetPath;

    @Value("${hospital.pdf2ImgPath}")
    private String PDFToImgRelativePath;

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> getImage(@PathVariable String filename) {
        log.info("查询资源：{}", filename);
        // 创建一个资源对象，指向E盘的项目目录
        ThrowUtils.throwIf(StringUtils.isBlank(filename), ResultCode.PATHVARIABLE_NULL);
        Path filepath = Paths.get(targetPath, filename);
        File file = filepath.toFile();
        //File file = new File(targetPath + "/" + filename);
        log.info("请求的资源filePath:{}",targetPath + "/" + filename);
        ThrowUtils.throwIf(!file.exists(), new BusinessException(ResultCode.RESOURCE_NOT_FOUND));
        Resource resource = new FileSystemResource(file);
        log.info("1");
        // 根据文件的扩展名推断MIME类型
        String contentType = null;
        try{
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new BusinessException(ResultCode.FILE_FORMAT_ERROR);
        }
        if (contentType == null) {
            // 如果无法推断MIME类型，则默认设置为通用二进制类型
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        log.info("2");
        // 创建响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        log.info("3");
        // 返回资源和正确的Content-Type
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
