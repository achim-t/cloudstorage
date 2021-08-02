package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE USERID=#{userid}")
    List<Note> getNotesByUser(Integer userid);

    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

    @Insert("INSERT INTO NOTES (userid, notetitle, notedescription) VALUES(#{userId}, #{noteTitle}, #{noteText})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);


//    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
//    @Options(useGeneratedKeys = true, keyProperty = "userId")
//    int insert(User user);
}
