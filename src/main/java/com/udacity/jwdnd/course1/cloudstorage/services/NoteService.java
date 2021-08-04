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
    }

    public int addNote(Note note, Integer userid) {
        return noteMapper.insert(note, userid);
    }

    public void deleteAllNotes(Integer userId) {
        for (Note note : noteMapper.getNotesByUser(userId)) {
            noteMapper.delete(note.getNoteid());
        }
    }

    public void updateNote(Note note, Integer userId) {
        if (noteMapper.findOne(note.getNoteid()).getUserid() == userId)
            noteMapper.update(note);
    }

    public void delete(Integer noteid, Integer userId) {
        if (noteMapper.findOne(noteid).getUserid() == userId)
            noteMapper.delete(noteid);
    }
}
