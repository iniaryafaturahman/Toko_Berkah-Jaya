package tokoberkahjaya;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LaporanPenjualanPanel extends JPanel {
    
    private JComboBox<String> cbFilterType;
    private JTextField tfSearch;
    private JButton btnSearch, btnRefresh, btnPrint, btnDelete;
    private JTable tableRingkasan, tableDetail;
    private DefaultTableModel tableRingkasanModel, tableDetailModel;
    private JLabel lblTotalTransaksi, lblTotalPendapatan, lblTotalItem;
    
    public LaporanPenjualanPanel() {
        initComponents();
        loadLaporan();
        setupListeners();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 250));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("📊 Laporan Penjualan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        
        JLabel subtitleLabel = new JLabel("Detail Transaksi dan Ringkasan Penjualan");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        filterPanel.add(new JLabel("Filter:"));
        cbFilterType = new JComboBox<>(new String[]{"Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"});
        cbFilterType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbFilterType.setPreferredSize(new Dimension(130, 30));
        filterPanel.add(cbFilterType);
        
        filterPanel.add(new JLabel("Cari ID:"));
        tfSearch = new JTextField(15);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfSearch.setPreferredSize(new Dimension(150, 30));
        filterPanel.add(tfSearch);
        
        btnSearch = createButton("🔍 Cari", new Color(0, 102, 204));
        btnRefresh = createButton("🔄 Refresh", new Color(100, 100, 200));
        btnPrint = createButton("🖨️ Print", new Color(0, 150, 0));
        btnDelete = createButton("🗑️ Hapus", new Color(200, 50, 50));
        
        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);
        filterPanel.add(btnPrint);
        filterPanel.add(btnDelete);
        
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JPanel card1 = createSummaryCard("📋 Total Transaksi", "0", new Color(52, 152, 219));
        JPanel card2 = createSummaryCard("💰 Total Pendapatan", "Rp 0", new Color(46, 204, 113));
        JPanel card3 = createSummaryCard("📦 Total Item", "0", new Color(155, 89, 182));
        
        summaryPanel.add(card1);
        summaryPanel.add(card2);
        summaryPanel.add(card3);
        
        lblTotalTransaksi = (JLabel) ((JPanel) card1.getComponent(1)).getComponent(0);
        lblTotalPendapatan = (JLabel) ((JPanel) card2.getComponent(1)).getComponent(0);
        lblTotalItem = (JLabel) ((JPanel) card3.getComponent(1)).getComponent(0);
        
        JPanel ringkasanPanel = new JPanel(new BorderLayout());
        ringkasanPanel.setBackground(Color.WHITE);
        ringkasanPanel.setBorder(BorderFactory.createTitledBorder("📋 Ringkasan Transaksi"));
        
        String[] ringkasanColumns = {"ID Transaksi", "Tanggal", "Customer", "Jumlah Item", "Total Bayar"};
        tableRingkasanModel = new DefaultTableModel(ringkasanColumns, 0);
        tableRingkasan = new JTable(tableRingkasanModel);
        tableRingkasan.setRowHeight(30);
        JScrollPane scrollRingkasan = new JScrollPane(tableRingkasan);
        scrollRingkasan.setPreferredSize(new Dimension(0, 280));
        ringkasanPanel.add(scrollRingkasan, BorderLayout.CENTER);
        
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createTitledBorder("📄 Detail Transaksi"));
        
        String[] detailColumns = {"ID Transaksi", "ID Barang", "Nama Barang", "Harga", "Jumlah", "Subtotal"};
        tableDetailModel = new DefaultTableModel(detailColumns, 0);
        tableDetail = new JTable(tableDetailModel);
        tableDetail.setRowHeight(28);
        JScrollPane scrollDetail = new JScrollPane(tableDetail);
        scrollDetail.setPreferredSize(new Dimension(0, 180));
        detailPanel.add(scrollDetail, BorderLayout.CENTER);
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(ringkasanPanel, BorderLayout.CENTER);
        centerPanel.add(detailPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(Color.GRAY);
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(Color.WHITE);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        return card;
    }
    
    private void loadLaporan() {
        loadRingkasanTransaksi();
        updateSummary();
    }
    
    private void loadRingkasanTransaksi() {
        tableRingkasanModel.setRowCount(0);
        String filter = (String) cbFilterType.getSelectedItem();
        String searchId = tfSearch.getText().trim();
        
        String sql = "SELECT h.id_transaksi, DATE_FORMAT(h.tgl_transaksi, '%Y-%m-%d %H:%i') as tgl, " +
                     "c.nama_customer, h.total_item, h.total_bayar " +
                     "FROM tb_transaksi_header h " +
                     "JOIN tb_customer c ON h.id_customer = c.id_customer WHERE 1=1 ";
        
        if (filter.equals("Hari Ini")) sql += "AND DATE(h.tgl_transaksi) = CURDATE() ";
        else if (filter.equals("Minggu Ini")) sql += "AND h.tgl_transaksi >= DATE_SUB(NOW(), INTERVAL 7 DAY) ";
        else if (filter.equals("Bulan Ini")) sql += "AND h.tgl_transaksi >= DATE_SUB(NOW(), INTERVAL 30 DAY) ";
        if (!searchId.isEmpty()) sql += "AND h.id_transaksi LIKE '%" + searchId + "%' ";
        sql += "ORDER BY h.tgl_transaksi DESC";
        
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableRingkasanModel.addRow(new Object[]{
                    rs.getString("id_transaksi"), rs.getString("tgl"),
                    rs.getString("nama_customer"), rs.getInt("total_item"),
                    formatRupiah(rs.getDouble("total_bayar"))
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void loadDetailTransaksi(String idTransaksi) {
        tableDetailModel.setRowCount(0);
        String sql = "SELECT d.id_transaksi, b.id_barang, b.nama_barang, b.harga_jual, " +
                     "d.jumlah_beli, d.subtotal FROM tb_transaksi_detail d " +
                     "JOIN tb_barang b ON d.id_barang = b.id_barang WHERE d.id_transaksi = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, idTransaksi);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableDetailModel.addRow(new Object[]{
                    rs.getString("id_transaksi"), rs.getString("id_barang"),
                    rs.getString("nama_barang"), formatRupiah(rs.getDouble("harga_jual")),
                    rs.getInt("jumlah_beli"), formatRupiah(rs.getDouble("subtotal"))
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void updateSummary() {
        String sql = "SELECT COUNT(*) as total, SUM(total_bayar) as pendapatan, SUM(total_item) as item FROM tb_transaksi_header";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                lblTotalTransaksi.setText(String.valueOf(rs.getInt("total")));
                lblTotalPendapatan.setText(formatRupiah(rs.getDouble("pendapatan")));
                lblTotalItem.setText(String.valueOf(rs.getInt("item")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void hapusTransaksi() {
        int row = tableRingkasan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!");
            return;
        }
        String idTransaksi = tableRingkasanModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus transaksi " + idTransaksi + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement("DELETE FROM tb_transaksi_detail WHERE id_transaksi = ?")) {
                pstmt.setString(1, idTransaksi);
                pstmt.executeUpdate();
                try (PreparedStatement pstmt2 = DatabaseConnection.getConnection().prepareStatement("DELETE FROM tb_transaksi_header WHERE id_transaksi = ?")) {
                    pstmt2.setString(1, idTransaksi);
                    pstmt2.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
                loadLaporan();
                tableDetailModel.setRowCount(0);
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    private void setupListeners() {
        tableRingkasan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableRingkasan.getSelectedRow();
                if (row >= 0) loadDetailTransaksi(tableRingkasanModel.getValueAt(row, 0).toString());
            }
        });
        btnSearch.addActionListener(e -> loadRingkasanTransaksi());
        btnRefresh.addActionListener(e -> { cbFilterType.setSelectedIndex(0); tfSearch.setText(""); loadLaporan(); tableDetailModel.setRowCount(0); });
        btnPrint.addActionListener(e -> printLaporan());
        btnDelete.addActionListener(e -> hapusTransaksi());
    }
    
    private void printLaporan() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== TOKO BERKAH JAYA ==========\n");
        sb.append("         LAPORAN PENJUALAN\n");
        sb.append("     ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())).append("\n\n");
        sb.append("ID Transaksi | Tanggal | Customer | Total\n");
        for (int i = 0; i < tableRingkasanModel.getRowCount(); i++) {
            sb.append(tableRingkasanModel.getValueAt(i, 0)).append(" | ")
              .append(tableRingkasanModel.getValueAt(i, 1)).append(" | ")
              .append(tableRingkasanModel.getValueAt(i, 2)).append(" | ")
              .append(tableRingkasanModel.getValueAt(i, 4)).append("\n");
        }
        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Preview Laporan", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String formatRupiah(double angka) {
        return String.format("Rp %,.0f", angka);
    }
    
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}