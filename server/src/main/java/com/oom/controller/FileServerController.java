package com.oom.controller;

import com.oom.config.FileServerConfiger;
import com.oom.model.FileDO;
import com.oom.service.FileService;
import com.oom.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 文件上传下载
 */
 
@Controller
public class FileServerController {
	@Autowired
	private FileService fileService;

	@Autowired
	private FileServerConfiger fileServerConfiger;

	@ResponseBody
	@GetMapping("/list/{page}/{limit}")
	public R list(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit){
		//查询列表数据
		List<FileDO> fileList = fileService.list(page, limit);
		Long total = fileService.count();
		return R.ok(fileList, total);
	}

	/**
	 * 删除
	 */
	@PostMapping( "/del/{id}")
	@ResponseBody
	public R remove(@PathVariable String id){
        FileDO fileDO = fileService.get(id);
        if (fileDO == null){
        	return R.error("文件不存在");
		}
		if (!fileService.remove(id)){
        	return R.error("删除失败，请重试");
		}
		String filePath = fileDO.getUrl() + fileDO.getId() +"." + fileDO.getType();
		if (!FileUtil.deleteFile(filePath)){
			return R.error("删除失败");
		}
		return R.ok("删除成功");
	}

	/**
	 * 上传文件
	 */
	@Transactional
	@ResponseBody
	@PostMapping("/uploadFile")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		String fileName = file.getOriginalFilename();
		Long fileSize = file.getSize();
		String newFileName = FileUtil.renameToUUID(fileName);
		String filePath = fileServerConfiger.getPathDirectory()+DateUtil.getNowDate()+"/";
		String fileType = FileType.fileType(fileName);
		if (fileType.equals("0")){
			return R.error("文件名为空");
		}
		if (fileType.equals("1")){
			return R.error("不支持当前文件类型的上传");
		}
		// 生成随机密钥
		String secretKey = AESEncUtil.getSecretKey();
		// 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
		String rsaMessage = RsaSignature.rsaEncrypt(secretKey, fileServerConfiger.getServerPublicKey());
		FileDO sysFile = new FileDO(FileUtil.getFileNameNoEx(newFileName),FileUtil.getFileNameNoEx(fileName), fileType, fileSize, filePath, rsaMessage, new Date());
		if (fileService.save(sysFile) != 1) {
			return R.error();
		}
		// AES对称加密
		byte[] encrypt = AESEncUtil.encrypt(file.getBytes(), secretKey);
		FileUtil.uploadFile(encrypt, filePath, newFileName);
		return R.ok().put("id",sysFile.getId());
	}

	/**
	 * 下载文件
	 */
	@ResponseBody
	@GetMapping("/download/{id}")
	public FileDO download(@PathVariable("id") String id) {
		return fileService.get(id);
	}
}
