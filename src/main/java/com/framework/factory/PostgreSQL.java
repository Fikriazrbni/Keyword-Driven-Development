package com.framework.factory;
import com.framework.services.MyConfig;

import java.sql.*;


public class PostgreSQL {
    static Connection conn = null;

    public static void connectionPostgreSQL() {
        String url = "jdbc:postgresql://10.27.62.94:5558/qcermati";
        String user = "cermati";
        String password = "uatcermati";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void disconnectionPostgreSQL(){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void contohGetData() throws SQLException {
        ResultSet rs = null;
        connectionPostgreSQL();
        String sql = "select * from loan.creation_loans WHERE application_number IN (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "L000006WYSC7OXCPE");
        //int rowsUpdated = pstmt.executeUpdate();
        rs = pstmt.executeQuery();
        while (rs.next()) {
            String getConsumer_id = rs.getString("consumer_id");
            String getLoan_state = rs.getString("loan_state");
            System.out.println(getConsumer_id);
            System.out.println(getLoan_state);
        }
        System.out.println();
    }

    public static void updateDateLoanDetail() throws SQLException {
        String sql = "UPDATE loan.creation_loans SET paid_at = ? WHERE application_number IN (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        String putPaidDate = MyConfig.mapSaveData.get("savePaidDate") + " 00:00:00";
        pstmt.setTimestamp(1, Timestamp.valueOf(putPaidDate));
        pstmt.setString(2, MyConfig.mapSaveData.get("saveNoAplikasi"));
        System.out.println(pstmt);
        int rowsUpdated = pstmt.executeUpdate();
    }

    public static void updateDateCardStatus() throws SQLException {
        String sql = "update loan.consumer_banners cb set paid_at = ? where cb.created_at = (select max(cb2.created_at) from loan.consumer_banners cb2 where cb2.consumer_id = (select id from consumer.consumers c where c.main_saving_account_number = ?) and cb2.banner_id ='e8594526-31dd-4e8f-ac9b-881201d37ae2')";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        String putPaidDate = MyConfig.mapSaveData.get("savePaidDate") + " 00:00:00";
        pstmt.setTimestamp(1, Timestamp.valueOf(putPaidDate));
        pstmt.setString(2, MyConfig.mapSaveData.get("saveNoBluAcc"));
        System.out.println(pstmt);
        int rowsUpdated = pstmt.executeUpdate();
    }

    public static void wlUpdateCreatedAt() throws SQLException {
        String sql = "update loan.creation_loans cl set created_at = now() - interval '10 day' where application_number in (?) and loan_state in('REJECTED', 'DECLINE')";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveNoAplikasi"));
        System.out.println(pstmt);
        int rowsUpdated = pstmt.executeUpdate();
    }

    public static void wlgetloan_stateFromCreation_loans() throws SQLException {
        ResultSet rs = null;
        String sql = "select * from loan.creation_loans cl where cl.application_number in (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveNoAplikasi"));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            String getloan_state = rs.getString("loan_state");
            System.out.println(getloan_state);
            MyConfig.mapSaveData.put("saveloan_state",getloan_state );
        }
    }

    public static void wlgetConsumerIdFromCreation_loans() throws SQLException {
        ResultSet rs = null;
        String sql = "select * from loan.creation_loans cl where cl.application_number in (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveNoAplikasi"));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            String getConsumer_id = rs.getString("consumer_id");
            System.out.println(getConsumer_id);
            MyConfig.mapSaveData.put("saveConsumerId",getConsumer_id );
        }
    }


    public static void wlDeleteRowFromConsumerLoans() throws SQLException {
        String sql = "delete from loan.consumer_loans cl where cl.consumer_id = (?) ::uuid";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveConsumerId"));
        System.out.println(pstmt);
        int deleteResult = pstmt.executeUpdate();
    }

    public static void wlDeleteRowFromConsumerBanner() throws SQLException {
        String sql= "delete from loan.consumer_banners cb where cb.consumer_id = (?) ::uuid and cb.banner_id='260d314c-c162-4de6-892f-6bbadefaa9a4'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveConsumerId"));
        System.out.println(pstmt);
        int deleteResult = pstmt.executeUpdate();
    }

    public static void wlInsertIntoTableFromConsumerLoan() throws SQLException {
        String sql= "insert into loan.consumer_loans (id, consumer_id, created_at, updated_at, blocked_until, loan_state) values (UUID(?), UUID(?), now(), now(), null, 'OFFERING')";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveConsumerId"));
        pstmt.setString(2, MyConfig.mapSaveData.get("saveConsumerId"));
        System.out.println(pstmt);
        int insertResult = pstmt.executeUpdate();
    }

    public static void wlInsertIntoTableFromConsumerBanner() throws SQLException {
        String sql= "insert into loan.consumer_banners (id, created_at, updated_at, consumer_id, banner_id, started_at, expired_at, title, title_en, \"body\", body_en, subtitle, subtitle_en, blocked_until) values (UUID(?), now(), now(), UUID(?), '260d314c-c162-4de6-892f-6bbadefaa9a4', now(), '2023-11-11 14:03:08.287 +0700', 'bluExtraCash', 'bluExtraCash', 'Pinjaman untuk berbagai kebutuhan dengan proses yang cepat dan simpel', 'With blu, make borrowing money clear and simple', 'Dapatkan pinjaman hingga Rp 10 juta', 'Get an easy-access personal loan up to Rp 10 millions', null);";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, MyConfig.mapSaveData.get("saveConsumerId"));
        pstmt.setString(2, MyConfig.mapSaveData.get("saveConsumerId"));
        System.out.println(pstmt);
        int insertResult = pstmt.executeUpdate();
    }
}
