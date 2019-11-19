package com.oom.controller;

import com.oom.config.FileServerConfiger;
import com.oom.domain.FileDO;
import com.oom.service.FileService;
import com.oom.utils.DateUtil;
import com.oom.utils.FileType;
import com.oom.utils.FileUtil;
import com.oom.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 文件上传下载
 */
 
@Controller
@RequestMapping("/api")
public class FileController {
	@Autowired
	private FileService fileService;

	@Autowired
	private FileServerConfiger fileServerConfiger;

	
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Integer page, Integer limit){
		//查询列表数据
		List<FileDO> fileList = fileService.list(page, limit);
		Long total = fileService.count();
		return R.ok(fileList, total);
	}

	/**
	 * 删除
	 */
	@PostMapping( "/del")
	@ResponseBody
	public R remove(@RequestParam Long id){
        FileDO fileDO = fileService.get(id);
        if (fileDO == null){
        	return R.error("文件不存在");
		}
		if (!fileService.remove(id)){
        	return R.error("删除失败，请重试");
		}
		String filePath = fileDO.getUrl() + "/" + fileDO.getContent() +"." + fileDO.getType();
		if (!FileUtil.deleteFile(filePath)){
			return R.error("删除失败");
		}
		return R.ok("删除成功");
	}

	/**
	 * 上传文件
	 */
	@ResponseBody
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		//如需获取其他表单信息，可通过request.getParameter("paramName")来获取
		//例：String paramName = request.getParameter("paramName");
		//以下为从MultipartFile中获取文件信息并做相关处理
		String fileName = file.getOriginalFilename();
		Long fileSize = file.getSize();
		String newFileName = FileUtil.renameToUUID(fileName);
		String filePath = fileServerConfiger.getPathDirectory()+DateUtil.getNowDate();
		String fileType = FileType.fileType(fileName);
		if (fileType.equals("0")){
			return R.error("文件名为空");
		}
		if (fileType.equals("1")){
			return R.error("不支持当前文件类型的上传");
		}
		FileDO sysFile = new FileDO(FileUtil.getFileNameNoEx(fileName), fileType, fileSize, filePath, FileUtil.getFileNameNoEx(newFileName), new Date());
		try {
			FileUtil.uploadFile(file.getBytes(), filePath, newFileName);
		} catch (Exception e) {
			return R.error();
		}
		if (fileService.save(sysFile) > 0) {
			return R.ok().put("fileName",sysFile.getUrl());
		}
		return R.error();
	}

	/**
	 * 下载文件
	 */
	@ResponseBody
	@GetMapping("/download/{id}")
	public void download(@PathVariable("id") Long id, HttpServletResponse response) {
		FileDO fileDO = fileService.get(id);
		String filePath = fileDO.getUrl();
		String fileName = fileDO.getName();
		filePath += "/" + fileDO.getContent() + "." + fileDO.getType();
		fileName += "." + fileDO.getType();
        try {
            FileUtil.downloadFile(filePath,fileName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
