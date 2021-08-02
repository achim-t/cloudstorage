package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE USERID={userid}")
    List<Note> getNotesByUser(Integer userid);

    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

}
