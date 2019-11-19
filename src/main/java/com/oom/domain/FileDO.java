package com.oom.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 文件上传
 * 
 */

@Data
@NoArgsConstructor
public class FileDO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	// 文件名称
	private String name;

	//文件类型
	private String type;

	// 文件大小
	private Long size;

	//URL地址
	private String url;

	// 加密信封
	private String content;

	//创建时间
	private Date createDate;

	public FileDO(String name, String type, Long size ,String url, String content,  Date createDate) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
		this.url = url;
		this.content = content;
		this.createDate = createDate;
	}

}
