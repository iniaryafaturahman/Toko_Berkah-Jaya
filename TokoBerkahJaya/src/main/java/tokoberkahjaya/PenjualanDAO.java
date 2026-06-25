package tokoberkahjaya;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenjualanDAO {
    
    public boolean savePenjualan(Penjualan penjualan) {
        String sql = "INSERT INTO tb_penjualan (tgl_transaksi, id_customer, id_barang, jumlah_beli, total_bayar) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setDate(1, new Date(penjualan.getTglTransaksi().getTime()));
            pstmt.setString(2, penjualan.getIdCustomer());
            pstmt.setString(3, penjualan.getIdBarang());
            pstmt.setInt(4, penjualan.getJumlahBeli());
            pstmt.setDouble(5, penjualan.getTotalBayar());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Object[]> getAllPenjualanWithDetail() {
        List<Object[]> penjualanList = new ArrayList<>();
        String sql = "SELECT p.id_jual, p.tgl_transaksi, c.nama_customer, b.nama_barang, p.jumlah_beli, p.total_bayar " +
                    "FROM tb_penjualan p " +
                    "JOIN tb_customer c ON p.id_customer = c.id_customer " +
                    "JOIN tb_barang b ON p.id_barang = b.id_barang " +
                    "ORDER BY p.tgl_transaksi DESC";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_jual"),
                    rs.getDate("tgl_transaksi"),
                    rs.getString("nama_customer"),
                    rs.getString("nama_barang"),
                    rs.getInt("jumlah_beli"),
                    rs.getDouble("total_bayar")
                };
                penjualanList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return penjualanList;
    }
}