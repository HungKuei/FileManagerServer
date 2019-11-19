package com.oom.service;

import com.oom.domain.FileDO;

import java.util.List;

/**
 * 文件上传
 * 
 * @author tzy
 * @email tang86100@163.com
 * @date 2018-06-25 16:26:05
 */
public interface FileService {

	FileDO get(Long id);

	List<FileDO> list(Integer page, Integer limit);

	Long count();

	int save(FileDO file);

	boolean remove(Long id);
}
