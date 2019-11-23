package oom.controller;

import com.alibaba.fastjson.JSONObject;
import com.oom.model.FileDO;
import com.oom.utils.AESEncUtil;
import com.oom.utils.FileUtil;
import com.oom.utils.R;
import com.oom.utils.RsaSignature;
import oom.config.FileClientConfiger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

@Controller
@RequestMapping("/api")
public class FileClientController {

    @Autowired
    private FileClientConfiger fileClientConfiger;

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam Integer page, Integer limit) throws Exception {
        HttpGet get = new HttpGet(fileClientConfiger.getListServerUrl()+page+"/"+limit);
        // 生成随机密钥
        String sid = AESEncUtil.getSecretKey();
        // 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
        String signature = RsaSignature.rsaEncrypt(sid, fileClientConfiger.getServerPublicKey());
        get.setHeader("X-SID", sid);
        get.setHeader("X-Signature", signature);
        //执行提交
        HttpResponse response = httpClient.execute(get);
        HttpEntity resultEntity = response.getEntity();
        if (resultEntity == null){
            return R.error("无响应结果");
        }
        String result = EntityUtils.toString(resultEntity, Charset.forName("UTF-8"));
        return JSONObject.parseObject(result, R.class);
    }

    @ResponseBody
    @PostMapping("/del")
    public R del(@RequestParam String id) throws Exception {
        HttpPost post = new HttpPost(fileClientConfiger.getDeleteServerUrl()+id);
        // 生成随机密钥
        String sid = AESEncUtil.getSecretKey();
        // 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
        String signature = RsaSignature.rsaEncrypt(sid, fileClientConfiger.getServerPublicKey());
        post.setHeader("X-SID", sid);
        post.setHeader("X-Signature", signature);
        //执行提交
        HttpResponse response = httpClient.execute(post);
        HttpEntity resultEntity = response.getEntity();
        if (resultEntity == null){
            return R.error("无响应结果");
        }
        String result = EntityUtils.toString(resultEntity, Charset.forName("UTF-8"));
        return JSONObject.parseObject(result, R.class);
    }



    @PostMapping("/upload")
    @ResponseBody
    public R uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        HttpPost post = new HttpPost(fileClientConfiger.getUploadServerUrl());
        // 生成随机密钥
        String sid = AESEncUtil.getSecretKey();
        // 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
        String signature = RsaSignature.rsaEncrypt(sid, fileClientConfiger.getServerPublicKey());
        post.setHeader("X-SID", sid);
        post.setHeader("X-Signature", signature);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("utf-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
        //绑定文件参数，传入文件流和contenttype
        String fileName = file.getOriginalFilename();
        builder.addBinaryBody("file",file.getInputStream(), ContentType.MULTIPART_FORM_DATA,fileName);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        //执行提交
        HttpResponse response = httpClient.execute(post);
        HttpEntity resultEntity = response.getEntity();
        if (resultEntity == null){
            return R.error("无响应结果");
        }
        String result = EntityUtils.toString(resultEntity, Charset.forName("UTF-8"));
        return JSONObject.parseObject(result, R.class);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        // Ras私钥解密
        HttpGet get = new HttpGet(fileClientConfiger.getDownloadServerUrl()+id);
        // 生成随机密钥
        String sid = AESEncUtil.getSecretKey();
        // 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
        String signature = RsaSignature.rsaEncrypt(sid, fileClientConfiger.getServerPublicKey());
        get.setHeader("X-SID", sid);
        get.setHeader("X-Signature", signature);
        HttpResponse httpResponse = httpClient.execute(get);
        HttpEntity resultEntity = httpResponse.getEntity();
        if (resultEntity == null){
            return;
        }
        String result = EntityUtils.toString(resultEntity, Charset.forName("UTF-8"));
        FileDO fileDO = JSONObject.parseObject(result, FileDO.class);
        String filePath = fileDO.getUrl();
        String fileName = fileDO.getName();
        filePath += id + "." + fileDO.getType();
        fileName += "." + fileDO.getType();
        String content = fileDO.getContent();
        String decrypt = RsaSignature.rsaDecrypt(content, fileClientConfiger.getServerPrivateKey());
        byte[] bytes = FileUtil.getFileByte(filePath, response);
        // AES 解密
        byte[] data = AESEncUtil.decrypt(bytes, decrypt);
        try {
            FileUtil.downloadFile(data,fileName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
