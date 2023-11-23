package com.taxtion;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class Utils {

    public static CryptoKeyPair buildCryptoSuite(Client client, String fromAddress) {
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", cryptoSuite.getCryptoKeyPair().getPemKeyStoreFilePath(fromAddress), null);
        return cryptoSuite.getCryptoKeyPair();
    }

    /**
     * timestamp -> LocalDateTime
     * @param timestamp
     * @return
     */
    public  static LocalDateTime timestampDatetime(BigInteger timestamp){
        long longValue = timestamp.longValue();
        Instant instant = Instant.ofEpochSecond(longValue);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * LocalDateTime -> timestamp
     * @param localDateTime
     * @return
     */
    public static BigInteger datetimeToTimestamp(LocalDateTime localDateTime){
        long second = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
        return BigInteger.valueOf(second);
    }

    /**
     * 处理bytes32
     * @param dataString
     * @return
     */
    public static byte[] convertBytes32StringToByteArray(String dataString) {
        // 移除0x
        if (dataString.startsWith("0x")) {
            dataString = dataString.substring(2);
        }


        if (dataString.length() % 64 != 0) {
            throw new IllegalArgumentException("Invalid input string length");
        }

        int numBytes32 = dataString.length() / 64;
        byte[] result = new byte[numBytes32 * 32];

        for (int i = 0; i < numBytes32; i++) {
            String bytes32Hex = dataString.substring(i * 64, (i + 1) * 64);
            byte[] bytes32 = hexStringToByteArray(bytes32Hex);
            System.arraycopy(bytes32, 0, result, i * 32, 32);
        }

        return result;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
