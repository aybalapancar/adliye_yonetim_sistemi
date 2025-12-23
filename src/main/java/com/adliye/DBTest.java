package com.adliye;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✔ Veritabanına başarılı şekilde bağlanıldı!");
        } else {
            System.out.println("❌ Bağlantı başarısız! Bilgileri kontrol et!");
        }
    }
}
