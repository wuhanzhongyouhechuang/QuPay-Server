package com.ntnikka.modules.apply.controller;

import com.google.common.collect.Lists;
import com.ntnikka.utils.R;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.service.MediaTypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author luk
 * @create 2018-03-27 15:28
 * @desc 文件处理
 **/

@Controller
@RequestMapping("/upload")
public class FileController {

    @RequestMapping(value = "/ToUploadFile", method = {RequestMethod.POST})
    @ResponseBody
    public Integer ToUploadFile(@RequestParam("firstfile") MultipartFile firstfile,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST");
        // ------------------------------------------------------------------------------------------------------------
        String originalFilename = null;
        try {
            if (firstfile.isEmpty()) {
                return 0;
            } else {
                originalFilename = firstfile.getOriginalFilename();
                long size = firstfile.getSize();
                String contentType = firstfile.getContentType();
                System.out.println("原始文件全路径名: " + originalFilename);
                System.out.println("文件大小:" + size + "KB");
                System.out.println("文件类型:" + contentType);
                System.out.println("========================================");
            }
            // ------------------------------------------------------------------------------------------------------------
            /*
             * 1. 获取项目的全路径
             */
            String root = request.getServletContext().getRealPath("/files");
            System.out.println("文件根路径:" + root);

            String filename = firstfile.getOriginalFilename();
            /*
             * 获取文件名，最后一个"\"后面的名字
             */
            int index = filename.lastIndexOf("\\");
            if (index != -1) {
                filename = filename.substring(index + 1);
            }
            /*
             * 生成唯一的文件名
             */
            String savename = UUID.randomUUID() + "_" + filename;
            /*
             * 1. 根据文件名获取hashcode
             */
            int hCode = filename.hashCode();
            // 将hashcode转化为无符号整数基数为16的整数参数的字符串
            String hex = Integer.toHexString(hCode);
            /*
             * 2. 根据字符串生成文件目录
             */
            File dirFile = new File(root, hex.charAt(0) + "/" + hex.charAt(1));
            dirFile.mkdirs();
            /*
             * 4. 生成文件
             */
            File destFile = new File(dirFile, savename);
            String courseFile = destFile.getCanonicalPath();
            // 将文件保存到服务器相应的目录位置
            firstfile.transferTo(destFile);
            System.out.println("文件存储目标路径:" + courseFile);
            ///////////////////////////////////////////////////////
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

     /*
    文件上传
     */

    /**
     * luk
     * 支持多文件上传
     * this demo for upload
     * other programmer can refer to this demo
     *
     * @param request
     * @return 例：{"msg":"success","code":0,"filelist":["C:/home/personal/upload/阿里巴巴Java开发手册（正式版）.pdf","C:/home/personal/upload/测试记录.txt","C:/home/personal/upload/公司发票信息.png"]}
     * @throws Exception
     */
    @RequestMapping(value = "/uploadfiles", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public R uploadfiles(HttpServletRequest request) throws Exception {
        String uploadUrl = "C:/home";
        File dirupload = new File(uploadUrl);
        if (!dirupload.exists()) {
            dirupload.mkdirs();
        }

        String filePath = uploadUrl + "/personal/upload/";
        String browsePath = uploadUrl + "/personal/upload/";
        System.out.println("webRootPath---->" + uploadUrl);
        System.out.println("存放路径---->" + filePath);
        System.out.println("浏览路径---->" + browsePath);


        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //文件集合
        List pathList = Lists.newArrayList();
        MultipartHttpServletRequest mu = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> map = mu.getMultiFileMap();
        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {
            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                if (multipartFile.getSize() > 1048576 * 20) {
                    return R.error("文件不能超过20M");
                } else {
                    multipartFile.transferTo(new File(filePath
                            + multipartFile.getOriginalFilename()));
                    pathList.add(browsePath + multipartFile.getOriginalFilename());
                }
            }
        }
//        String path= (String) pathList.get(0);
//        String filepath=path.split("/contfiles")[1];
//        filepath=file_RootPath+filepath;
        return R.ok().put("filelist", pathList);  //R函数请改成你们自己的基础返回对象封装函数
    }


}
