package com.oom.service.impl;

import com.oom.dao.FileDao;
import com.oom.model.FileDO;
import com.oom.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private FileDao fileDao;
	
	@Override
	public FileDO get(String id){
		return fileDao.get(id);
	}
	
	@Override
	public List<FileDO> list(Integer page, Integer limit){
		Integer currentPage = (page-1)*limit;
		Integer linesize = page*limit;
		return fileDao.list(currentPage, linesize);
	}
	
	@Override
	public Long count(){
		return fileDao.count();
	}
	
	@Override
	public int save(FileDO file){
		return fileDao.save(file);
	}
	
	@Override
	public boolean remove(String id){
		return fileDao.remove(id) > 0;
	}

}
