package com.oom.dao.impl;

import com.oom.dao.FileDao;
import com.oom.domain.FileDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileDaoImpl implements FileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public FileDO get(Long id) {
        String sql = "SELECT * FROM file_info WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(FileDO.class));
    }

    @Override
    public List<FileDO> list(Integer currentPage, Integer linesize) {
        String sql = "SELECT * FROM file_info LIMIT ?,?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, currentPage, linesize);
        //List list = jdbcTemplate.queryForList(sql, new Integer[]{currentPage, linesize}, FileDO.class);
        List<FileDO> list = new ArrayList<>();
        while (result.next()){
            FileDO file = new FileDO();
            file.setName(result.getString("name"));
            file.setId(result.getLong("id"));
            file.setType(result.getString("type"));
            file.setSize(result.getLong("size"));
            file.setUrl(result.getString("url"));
            file.setContent(result.getString("content"));
            file.setCreateDate(result.getDate("create_date"));
            list.add(file);
        }
        return list;
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(*) FROM file_info";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public int save(FileDO file) {
        String sql = "INSERT INTO file_info (name, type, size, url, content, create_date) VALUES(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{file.getName(), file.getType(), file.getSize(), file.getUrl(), file.getContent(), file.getCreateDate()});
    }

    @Override
    public int remove(Long id) {
        String sql = "DELETE FROM file_info WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
