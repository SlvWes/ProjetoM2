package br.com.pssi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    public static String hashSenha(String senhaPura) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senhaPura.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            // Força o retorno em minúsculo para bater perfeitamente com o banco
            return hexString.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro crítico: Algoritmo SHA-256 indisponível.", e);
        }
    }
}