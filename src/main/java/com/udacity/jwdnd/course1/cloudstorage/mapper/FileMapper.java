package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE USERID=#{userid}")
    List<File> getFilesByUser(Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES (#{file.filename}, #{file.contenttype}, #{file.filesize}, #{file.filedata}, #{userid})")
    int insert(File file, Integer userid);

    @Select("SELECT * FROM FILES WHERE fileId=#{fileId}")
    File findOne(Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId=#{fileId}")
    void delete(Integer fileId);
}
