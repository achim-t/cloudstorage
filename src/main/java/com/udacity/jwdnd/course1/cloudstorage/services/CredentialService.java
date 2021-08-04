package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(User user) {
        List<Credential> credentials = credentialMapper.getByUser(user.getUserId());
        return credentials.stream().map(this::decrypt).collect(Collectors.toList());
    }

    private Credential encrypt(Credential credential) {
        credential.setKey(generateKey());
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    private String generateKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    private Credential decrypt(Credential credential) {
        credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    public int addCredential(Credential credential, Integer userid) {
        return credentialMapper.insert(encrypt(credential), userid);
    }

    public void deleteAllCredentials(Integer userId) {
        for (Credential credential : credentialMapper.getByUser(userId)) {
            credentialMapper.delete(credential.getCredentialid());
        }
    }

    public boolean updateCredential(Credential credential, Integer userId) {
        Credential credentialDB = credentialMapper.findOne(credential.getCredentialid());
        if (credentialDB == null) return false;
        if (credentialDB.getUserid() != userId) return false;

        credentialMapper.update(encrypt(credential));
        return true;
    }

    public boolean delete(Integer credentialid, Integer userId) {
        Credential credentialDB = credentialMapper.findOne(credentialid);
        if (credentialDB == null) return false;
        if (credentialDB.getUserid() != userId) return false;

        credentialMapper.delete(credentialid);
        return true;
    }
}
