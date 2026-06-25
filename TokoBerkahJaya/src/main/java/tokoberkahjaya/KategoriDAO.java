package tokoberkahjaya;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    
    public List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_kategori ORDER BY nama_kategori";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Kategori(rs.getInt("id_kategori"), rs.getString("nama_kategori")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public boolean insertKategori(String namaKategori) {
        String sql = "INSERT INTO tb_kategori (nama_kategori) VALUES (?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, namaKategori);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteKategori(int idKategori) {
        String sql = "DELETE FROM tb_kategori WHERE id_kategori = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idKategori);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getIdKategoriFromName(String namaKategori) {
        String sql = "SELECT id_kategori FROM tb_kategori WHERE nama_kategori = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, namaKategori);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_kategori");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}   