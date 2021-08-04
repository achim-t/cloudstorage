package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public List<Note> getNotes(User user) {
        return noteMapper.getNotesByUser(user.getUserId());
//        return noteMapper.getAllNotes();
    }


    public int addNote(Note note, Integer userid) {
        return noteMapper.insert(note, userid);
    }

    public void deleteAllNotes(Integer userId) {
        noteMapper.delete(userId);
    }

    public void updateNote(Note note) {
        noteMapper.update(note);
    }

    public void delete(Integer noteid) {
        noteMapper.delete(noteid);
    }
}
