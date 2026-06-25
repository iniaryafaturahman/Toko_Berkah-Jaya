package tokoberkahjaya;

public class Barang {
    private String idBarang;
    private int idKategori;
    private String namaBarang;
    private String satuan;
    private double hargaJual;
    private int stok;
    
    public Barang(String idBarang, int idKategori, String namaBarang, String satuan, double hargaJual, int stok) {
        this.idBarang = idBarang;
        this.idKategori = idKategori;
        this.namaBarang = namaBarang;
        this.satuan = satuan;
        this.hargaJual = hargaJual;
        this.stok = stok;
    }
    
    public String getIdBarang() { return idBarang; }
    public void setIdBarang(String idBarang) { this.idBarang = idBarang; }
    public int getIdKategori() { return idKategori; }
    public void setIdKategori(int idKategori) { this.idKategori = idKategori; }
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public double getHargaJual() { return hargaJual; }
    public void setHargaJual(double hargaJual) { this.hargaJual = hargaJual; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    
    // Method untuk konversi kategori angka ke teks
    public String getKategoriTeks() {
        switch (idKategori) {
            case 1: return "Sembako";
            case 2: return "Makanan";
            case 3: return "Minuman";
            case 4: return "Sabun";
            case 5: return "Rokok";
            default: return "Lainnya";
        }
    }
    
    @Override
    public String toString() {
        return idBarang + " - " + namaBarang + " (Stok: " + stok + ")";
    }
}