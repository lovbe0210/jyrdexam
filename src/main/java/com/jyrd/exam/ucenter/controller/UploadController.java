package com.jyrd.exam.ucenter.controller;

import com.jyrd.exam.base.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/ucenter")
public class UploadController {
    @Value("${fastDFS.host}")
    private String fdfsHost;

    @PostMapping("fileUpload")
    public ResponseVo fileUpload(@RequestParam("file") MultipartFile file){
        String imageUrl = fdfsHost;
        if(file == null){
            return new ResponseVo(false,"文件非法或不存在");
        }

        try {
            String confFile = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(confFile);
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getTrackerServer();
            StorageClient storageClient=new StorageClient(trackerServer,null);

            String orginalFilename=file.getOriginalFilename();
            String extString = StringUtils.substringAfterLast(orginalFilename,".");

            // 注意这里使用重载方法--使用字节数组装载数据，而不能在网页直接使用文件路径，
            // 因为网页上传的只有文件名本身而不包含在磁盘中的具体位置
            String[] upload_file = storageClient.upload_file(file.getBytes(), extString, null);
            for (int i = 0; i < upload_file.length; i++) {
                String s = upload_file[i];
                imageUrl += "/" + s;
            }
            return new ResponseVo(true,"上传成功",imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        return new ResponseVo(false,"文件上传失败，请稍后重试");
    }
}
