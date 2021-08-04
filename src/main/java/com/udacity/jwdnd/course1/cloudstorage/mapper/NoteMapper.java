package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE USERID=#{userid}")
    List<Note> getNotesByUser(Integer userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{note.notetitle}, #{note.notedescription}, #{userid})")
    int insert(Note note, Integer userid);

    @Select("SELECT * FROM NOTES WHERE noteid=#{noteid}")
    Note findOne(Integer noteid);

    @Update("UPDATE NOTES SET notetitle=#{notetitle}, notedescription=#{notedescription} WHERE noteid=#{noteid}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteid}")
    void delete(Integer noteid);
}
