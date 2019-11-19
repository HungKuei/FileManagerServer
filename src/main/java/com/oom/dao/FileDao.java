package com.oom.dao;


import com.oom.domain.FileDO;

import java.util.List;

/**
 * 文件上传
 */
public interface FileDao {

	FileDO get(Long id);
	
	List<FileDO> list(Integer currentPage, Integer linesize);

	Long count();
	
	int save(FileDO file);
	
	int remove(Long id);
}
