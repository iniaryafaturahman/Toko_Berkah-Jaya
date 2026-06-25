package tokoberkahjaya;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {
    
    // Konversi teks kategori ke angka
    private int konversiKategoriKeAngka(String kategoriTeks) {
        switch (kategoriTeks) {
            case "Sembako": return 1;
            case "Makanan": return 2;
            case "Minuman": return 3;
            case "Sabun": return 4;
            case "Rokok": return 5;
            default: return 5;
        }
    }
    
    // Konversi angka kategori ke teks
    private String konversiKategoriKeTeks(int idKategori) {
        switch (idKategori) {
            case 1: return "Sembako";
            case 2: return "Makanan";
            case 3: return "Minuman";
            case 4: return "Sabun";
            case 5: return "Rokok";
            default: return "Lainnya";
        }
    }
    
    public boolean insertBarang(Barang barang) {
        String sql = "INSERT INTO tb_barang (id_barang, kategori, nama_barang, satuan, harga_jual, stok) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, barang.getIdBarang());
            // Konversi idKategori (int) ke teks untuk disimpan ke database
            pstmt.setString(2, konversiKategoriKeTeks(barang.getIdKategori()));
            pstmt.setString(3, barang.getNamaBarang());
            pstmt.setString(4, barang.getSatuan());
            pstmt.setDouble(5, barang.getHargaJual());
            pstmt.setInt(6, barang.getStok());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Barang> getAllBarang() {
        List<Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM tb_barang ORDER BY id_barang";
        
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Konversi kategori teks dari database ke idKategori (int)
                int idKategori = konversiKategoriKeAngka(rs.getString("kategori"));
                Barang barang = new Barang(
                    rs.getString("id_barang"),
                    idKategori,
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok")
                );
                barangList.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return barangList;
    }
    
    public Barang getBarangById(String id) {
        String sql = "SELECT * FROM tb_barang WHERE id_barang = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int idKategori = konversiKategoriKeAngka(rs.getString("kategori"));
                return new Barang(
                    rs.getString("id_barang"),
                    idKategori,
                    rs.getString("nama_barang"),
                    rs.getString("satuan"),
                    rs.getDouble("harga_jual"),
                    rs.getInt("stok")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateBarang(Barang barang) {
        String sql = "UPDATE tb_barang SET kategori=?, nama_barang=?, satuan=?, harga_jual=?, stok=? WHERE id_barang=?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, konversiKategoriKeTeks(barang.getIdKategori()));
            pstmt.setString(2, barang.getNamaBarang());
            pstmt.setString(3, barang.getSatuan());
            pstmt.setDouble(4, barang.getHargaJual());
            pstmt.setInt(5, barang.getStok());
            pstmt.setString(6, barang.getIdBarang());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteBarang(String idBarang) {
        String sql = "DELETE FROM tb_barang WHERE id_barang = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, idBarang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStok(String idBarang, int jumlahBeli) {
        String sql = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, jumlahBeli);
            pstmt.setString(2, idBarang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getStok(String idBarang) {
        String sql = "SELECT stok FROM tb_barang WHERE id_barang = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, idBarang);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stok");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}