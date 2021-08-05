package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public List<File> getFiles(User user) {
        return fileMapper.getFilesByUser(user.getUserId());
    }

    public int addFile(MultipartFile multipartFile, Integer userid) throws IOException {
        File file = new File();
        try {
            file.setContenttype(multipartFile.getContentType());
            file.setFiledata(multipartFile.getBytes());
            file.setFilename(multipartFile.getOriginalFilename());
            file.setFilesize(Long.toString(multipartFile.getSize()));
        } catch (IOException e) {
            throw e;
        }
        return fileMapper.insert(file, userid);
    }

    public void deleteAllFiles(Integer userId) {
        for (File file : fileMapper.getFilesByUser(userId)) {
            fileMapper.delete(file.getFileId());
        }
    }

    public boolean delete(Integer fileid, Integer userId) {
        File fileDB = fileMapper.findOne(fileid);
        if (fileDB == null) return false;
        if (fileDB.getUserid() != userId) return false;

        fileMapper.delete(fileid);
        return true;
    }

    public File get(Integer fileId, Integer userId) {
        File fileDB = fileMapper.findOne(fileId);
        if (fileDB == null) return null;
        if (fileDB.getUserid() == userId) return fileDB;
        return null;
    }

}