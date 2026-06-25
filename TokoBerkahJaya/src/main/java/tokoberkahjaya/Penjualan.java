package tokoberkahjaya;

import java.util.Date;

public class Penjualan {
    private int idJual;
    private Date tglTransaksi;
    private String idCustomer;
    private String idBarang;
    private int jumlahBeli;
    private double totalBayar;
    
    public Penjualan() {}
    
    public Penjualan(int idJual, Date tglTransaksi, String idCustomer, String idBarang, int jumlahBeli, double totalBayar) {
        this.idJual = idJual;
        this.tglTransaksi = tglTransaksi;
        this.idCustomer = idCustomer;
        this.idBarang = idBarang;
        this.jumlahBeli = jumlahBeli;
        this.totalBayar = totalBayar;
    }
    
    public int getIdJual() { return idJual; }
    public void setIdJual(int idJual) { this.idJual = idJual; }
    public Date getTglTransaksi() { return tglTransaksi; }
    public void setTglTransaksi(Date tglTransaksi) { this.tglTransaksi = tglTransaksi; }
    public String getIdCustomer() { return idCustomer; }
    public void setIdCustomer(String idCustomer) { this.idCustomer = idCustomer; }
    public String getIdBarang() { return idBarang; }
    public void setIdBarang(String idBarang) { this.idBarang = idBarang; }
    public int getJumlahBeli() { return jumlahBeli; }
    public void setJumlahBeli(int jumlahBeli) { this.jumlahBeli = jumlahBeli; }
    public double getTotalBayar() { return totalBayar; }
    public void setTotalBayar(double totalBayar) { this.totalBayar = totalBayar; }
}