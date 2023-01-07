package com.tmi.util;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

@Service
public class Encrypt {
    private static final Random random = new Random(); // 임시 salt값

    /**
     * @param password
     * @return
     * salt value를 랜덤으로 생성
     * salt value를 password와 결합하고, 결합된 값의 hash를 계산한다
     * salt와 hash가 연결되어 64비트로 인코딩된 문자열로 return
     */
    public String encryptPassword(String password){
        try{
            // Generate a random salt value
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Concatenate the password and salt
            byte[] passwordAndSalt = new byte[password.length() + salt.length];
            System.arraycopy(password.getBytes("UTF-8"), 0, passwordAndSalt, 0, password.length());
            System.arraycopy(salt, 0, passwordAndSalt, password.length(), salt.length);

            // Compute the hash of the password and salt
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(passwordAndSalt);

            // Contenate the salt and hash and return the result as a base64-encoded string
            byte[] saltAndHash = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
            System.arraycopy(hash, 0, saltAndHash, salt.length, hash.length);
            return Base64.getEncoder().encodeToString(saltAndHash);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        // 암호 확인 프로세스
        // 입력받은 암호와 솔트값을 연결하고, 해시를 계산
        // 계산된 해시와 저장된 해시를 비교, 둘이 같다면 올바른 암호
        // 더 안전한 salt value를 위해서 java.security.SecureRandom class를 사용하는 방향 고려
    }
}
