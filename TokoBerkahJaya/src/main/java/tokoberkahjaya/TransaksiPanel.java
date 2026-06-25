package tokoberkahjaya;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransaksiPanel extends JPanel {
    
    private JComboBox<Customer> cbCustomer;
    private JComboBox<Barang> cbBarang;
    private JTextField tfHargaJual, tfJumlahBeli, tfSubTotal;
    private JButton btnTambahItem, btnResetItem, btnSimpanTransaksi, btnResetForm;
    private JTable tableKeranjang, tableRiwayatCustomer, tableDetailItem;
    private DefaultTableModel tableKeranjangModel, tableRiwayatCustomerModel, tableDetailItemModel;
    private JLabel lblTotalAkhirValue, lblTransaksiId;
    
    // Komponen Pembayaran
    private JComboBox<String> cbMetodePembayaran;
    private JTextField tfUangDibayar;
    private JLabel lblKembalian;
    private JButton btnHitungKembalian;
    
    private CustomerDAO customerDAO;
    private BarangDAO barangDAO;
    private List<KeranjangItem> keranjangList;
    private String currentTransaksiId;
    
    public TransaksiPanel() {
        customerDAO = new CustomerDAO();
        barangDAO = new BarangDAO();
        keranjangList = new ArrayList<>();
        currentTransaksiId = generateTransaksiId();
        
        initComponents();
        loadCustomers();
        loadBarang();
        setupListeners();
    }
    
    private String generateTransaksiId() {
        return "TRX" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    }
    
    public void refreshData() {
        loadBarang();
        resetSemua();
        if (cbCustomer.getSelectedItem() != null) {
            loadRiwayatCustomer((Customer) cbCustomer.getSelectedItem());
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 248, 255));
        
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(new Color(240, 248, 255));
        
        // ==================== HEADER ====================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Toko Berkah Jaya");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 204));
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setOpaque(false);
        rightHeader.add(new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        
        lblTransaksiId = new JLabel("Transaksi #: " + currentTransaksiId);
        lblTransaksiId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTransaksiId.setForeground(new Color(0, 150, 0));
        rightHeader.add(lblTransaksiId);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        
        // ==================== FORM INPUT ====================
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Customer
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Pilih Customer:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        cbCustomer = new JComboBox<>();
        cbCustomer.setPreferredSize(new Dimension(350, 35));
        formPanel.add(cbCustomer, gbc);
        
        // Barang
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Pilih Barang:"), gbc);
        gbc.gridx = 1;
        cbBarang = new JComboBox<>();
        cbBarang.setPreferredSize(new Dimension(250, 35));
        formPanel.add(cbBarang, gbc);
        
        // Harga
        gbc.gridx = 2;
        formPanel.add(new JLabel("Harga:"), gbc);
        gbc.gridx = 3;
        tfHargaJual = new JTextField();
        tfHargaJual.setEditable(false);
        tfHargaJual.setBackground(new Color(245, 245, 245));
        tfHargaJual.setPreferredSize(new Dimension(150, 35));
        formPanel.add(tfHargaJual, gbc);
        
        // Jumlah
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Jumlah:"), gbc);
        gbc.gridx = 1;
        tfJumlahBeli = new JTextField();
        tfJumlahBeli.setPreferredSize(new Dimension(120, 35));
        formPanel.add(tfJumlahBeli, gbc);
        
        // Subtotal
        gbc.gridx = 2;
        formPanel.add(new JLabel("Subtotal:"), gbc);
        gbc.gridx = 3;
        tfSubTotal = new JTextField();
        tfSubTotal.setEditable(false);
        tfSubTotal.setBackground(new Color(255, 255, 220));
        tfSubTotal.setForeground(new Color(0, 100, 0));
        tfSubTotal.setPreferredSize(new Dimension(150, 35));
        formPanel.add(tfSubTotal, gbc);
        
        // Tombol Item
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        JPanel itemButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnTambahItem = createButton("➕ Tambah ke Keranjang", new Color(0, 150, 0));
        btnResetItem = createButton("🗑️ Reset Item", new Color(200, 100, 0));
        itemButtonPanel.add(btnTambahItem);
        itemButtonPanel.add(btnResetItem);
        formPanel.add(itemButtonPanel, gbc);
        
        formWrapper.add(formPanel, BorderLayout.NORTH);
        
        // ==================== KERANJANG ====================
        JPanel keranjangPanel = new JPanel(new BorderLayout());
        keranjangPanel.setBorder(BorderFactory.createTitledBorder("🛒 Keranjang Belanja"));
        
        String[] keranjangColumns = {"Kode", "Nama Barang", "Harga", "Jumlah", "Subtotal"};
        tableKeranjangModel = new DefaultTableModel(keranjangColumns, 0);
        tableKeranjang = new JTable(tableKeranjangModel);
        tableKeranjang.setRowHeight(30);
        JScrollPane scrollKeranjang = new JScrollPane(tableKeranjang);
        scrollKeranjang.setPreferredSize(new Dimension(0, 120));
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        totalPanel.setBackground(new Color(255, 255, 220));
        totalPanel.add(new JLabel("TOTAL AKHIR:"));
        lblTotalAkhirValue = new JLabel("Rp 0");
        lblTotalAkhirValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalAkhirValue.setForeground(new Color(0, 150, 0));
        totalPanel.add(lblTotalAkhirValue);
        
        keranjangPanel.add(scrollKeranjang, BorderLayout.CENTER);
        keranjangPanel.add(totalPanel, BorderLayout.SOUTH);
        
        // ==================== PEMBAYARAN ====================
        JPanel pembayaranPanel = new JPanel(new GridBagLayout());
        pembayaranPanel.setBorder(BorderFactory.createTitledBorder("💰 Pembayaran"));
        pembayaranPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.insets = new Insets(8, 8, 8, 8);
        gbcPay.fill = GridBagConstraints.HORIZONTAL;
        
        gbcPay.gridx = 0; gbcPay.gridy = 0;
        pembayaranPanel.add(new JLabel("Metode Pembayaran:"), gbcPay);
        gbcPay.gridx = 1;
        cbMetodePembayaran = new JComboBox<>(new String[]{"Cash", "Debit", "QRIS"});
        cbMetodePembayaran.setPreferredSize(new Dimension(150, 30));
        pembayaranPanel.add(cbMetodePembayaran, gbcPay);
        
        gbcPay.gridx = 0; gbcPay.gridy = 1;
        pembayaranPanel.add(new JLabel("Uang Dibayar:"), gbcPay);
        gbcPay.gridx = 1;
        tfUangDibayar = new JTextField();
        tfUangDibayar.setPreferredSize(new Dimension(150, 30));
        tfUangDibayar.setEnabled(false);
        pembayaranPanel.add(tfUangDibayar, gbcPay);
        
        gbcPay.gridx = 2;
        btnHitungKembalian = createButton("Hitung", new Color(0, 102, 204));
        btnHitungKembalian.setEnabled(false);
        pembayaranPanel.add(btnHitungKembalian, gbcPay);
        
        gbcPay.gridx = 0; gbcPay.gridy = 2;
        pembayaranPanel.add(new JLabel("Kembalian:"), gbcPay);
        gbcPay.gridx = 1; gbcPay.gridwidth = 2;
        lblKembalian = new JLabel("Rp 0");
        lblKembalian.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblKembalian.setForeground(new Color(0, 150, 0));
        pembayaranPanel.add(lblKembalian, gbcPay);
        
        // ==================== TOMBOL TRANSAKSI ====================
        JPanel transaksiButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSimpanTransaksi = createButton("✅ Simpan Transaksi", new Color(0, 102, 204));
        btnResetForm = createButton("🔄 Reset Semua", new Color(150, 150, 150));
        transaksiButtonPanel.add(btnSimpanTransaksi);
        transaksiButtonPanel.add(btnResetForm);
        
        // ==================== RIWAYAT PEMBELIAN CUSTOMER ====================
        JPanel riwayatCustomerPanel = new JPanel(new BorderLayout());
        riwayatCustomerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📋 Riwayat Pembelian Customer (Klik untuk lihat detail)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(0, 102, 204)
        ));
        
        String[] riwayatColumns = {"ID Transaksi", "Tanggal", "Total Item", "Total Bayar", "Metode"};
        tableRiwayatCustomerModel = new DefaultTableModel(riwayatColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableRiwayatCustomer = new JTable(tableRiwayatCustomerModel);
        tableRiwayatCustomer.setRowHeight(28);
        tableRiwayatCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableRiwayatCustomer.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollRiwayatCustomer = new JScrollPane(tableRiwayatCustomer);
        scrollRiwayatCustomer.setPreferredSize(new Dimension(0, 150));
        riwayatCustomerPanel.add(scrollRiwayatCustomer, BorderLayout.CENTER);
        
        // ==================== DETAIL ITEM ====================
        JPanel detailItemPanel = new JPanel(new BorderLayout());
        detailItemPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📄 Detail Item yang Dibeli",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(0, 102, 204)
        ));
        
        String[] detailColumns = {"ID Barang", "Nama Barang", "Harga", "Jumlah", "Subtotal"};
        tableDetailItemModel = new DefaultTableModel(detailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableDetailItem = new JTable(tableDetailItemModel);
        tableDetailItem.setRowHeight(28);
        tableDetailItem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableDetailItem.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollDetailItem = new JScrollPane(tableDetailItem);
        scrollDetailItem.setPreferredSize(new Dimension(0, 120));
        detailItemPanel.add(scrollDetailItem, BorderLayout.CENTER);
        
        // ==================== ASSEMBLE ====================
        JPanel historyPanel = new JPanel(new BorderLayout(0, 5));
        historyPanel.add(riwayatCustomerPanel, BorderLayout.CENTER);
        historyPanel.add(detailItemPanel, BorderLayout.SOUTH);
        
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createVerticalStrut(5));
        mainContainer.add(formWrapper);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(keranjangPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(pembayaranPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(transaksiButtonPanel);
        mainContainer.add(Box.createVerticalStrut(10));
        mainContainer.add(historyPanel);
        
        JScrollPane mainScroll = new JScrollPane(mainContainer);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        add(mainScroll, BorderLayout.CENTER);
    }
    
    private void loadCustomers() {
        cbCustomer.removeAllItems();
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers != null) {
            for (Customer c : customers) {
                cbCustomer.addItem(c);
            }
        }
    }
    
    private void loadBarang() {
        cbBarang.removeAllItems();
        List<Barang> barangList = barangDAO.getAllBarang();
        if (barangList != null) {
            for (Barang b : barangList) {
                cbBarang.addItem(b);
            }
        }
    }
    
    private void loadRiwayatCustomer(Customer customer) {
        tableRiwayatCustomerModel.setRowCount(0);
        tableDetailItemModel.setRowCount(0);
        
        if (customer == null) return;
        
        String sql = "SELECT id_transaksi, DATE_FORMAT(tgl_transaksi, '%Y-%m-%d %H:%i') as tgl, " +
                     "total_item, total_bayar, metode_pembayaran " +
                     "FROM tb_transaksi_header " +
                     "WHERE id_customer = ? " +
                     "ORDER BY tgl_transaksi DESC";
        
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, customer.getIdCustomer());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tableRiwayatCustomerModel.addRow(new Object[]{
                    rs.getString("id_transaksi"),
                    rs.getString("tgl"),
                    rs.getInt("total_item"),
                    formatRupiah(rs.getDouble("total_bayar")),
                    rs.getString("metode_pembayaran")
                });
            }
            
            if (tableRiwayatCustomerModel.getRowCount() == 0) {
                tableRiwayatCustomerModel.addRow(new Object[]{"-", "Belum ada transaksi", "-", "-", "-"});
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadDetailTransaksi(String idTransaksi) {
        tableDetailItemModel.setRowCount(0);
        
        String sql = "SELECT d.id_barang, b.nama_barang, d.subtotal / d.jumlah_beli as harga_satuan, " +
                     "d.jumlah_beli, d.subtotal " +
                     "FROM tb_transaksi_detail d " +
                     "JOIN tb_barang b ON d.id_barang = b.id_barang " +
                     "WHERE d.id_transaksi = ?";
        
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, idTransaksi);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tableDetailItemModel.addRow(new Object[]{
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    formatRupiah(rs.getDouble("harga_satuan")),
                    rs.getInt("jumlah_beli"),
                    formatRupiah(rs.getDouble("subtotal"))
                });
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setupListeners() {
        // Ketika customer dipilih, load riwayat pembelian customer tersebut
        cbCustomer.addActionListener(e -> {
            Customer customer = (Customer) cbCustomer.getSelectedItem();
            if (customer != null && customer.getIdCustomer() != null && !customer.getIdCustomer().isEmpty()) {
                loadRiwayatCustomer(customer);
            }
        });
        
        // Ketika transaksi di klik, load detail item
        tableRiwayatCustomer.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableRiwayatCustomer.getSelectedRow();
                if (row >= 0 && tableRiwayatCustomerModel.getValueAt(row, 0) != null) {
                    String idTransaksi = tableRiwayatCustomerModel.getValueAt(row, 0).toString();
                    if (!idTransaksi.equals("-")) {
                        loadDetailTransaksi(idTransaksi);
                    }
                }
            }
        });
        
        cbBarang.addActionListener(e -> {
            Barang b = (Barang) cbBarang.getSelectedItem();
            if (b != null) tfHargaJual.setText(formatRupiah(b.getHargaJual()));
            hitungSubTotal();
        });
        
        tfJumlahBeli.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { hitungSubTotal(); }
        });
        
        cbMetodePembayaran.addActionListener(e -> {
            boolean isCash = cbMetodePembayaran.getSelectedItem().equals("Cash");
            tfUangDibayar.setEnabled(isCash);
            btnHitungKembalian.setEnabled(isCash);
            if (!isCash) {
                tfUangDibayar.setText("");
                lblKembalian.setText("Rp 0");
            }
        });
        
        btnHitungKembalian.addActionListener(e -> hitungKembalian());
        btnTambahItem.addActionListener(e -> tambahKeKeranjang());
        btnResetItem.addActionListener(e -> resetItemForm());
        btnSimpanTransaksi.addActionListener(e -> simpanTransaksi());
        btnResetForm.addActionListener(e -> resetSemua());
        
        tableKeranjang.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tableKeranjang.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        keranjangList.remove(row);
                        refreshKeranjangTable();
                        updateTotalAkhir();
                    }
                }
            }
        });
    }
    
    private void hitungSubTotal() {
        Barang b = (Barang) cbBarang.getSelectedItem();
        if (b != null) {
            try {
                int jml = Integer.parseInt(tfJumlahBeli.getText().trim());
                tfSubTotal.setText(formatRupiah(b.getHargaJual() * (jml > 0 ? jml : 0)));
            } catch (NumberFormatException e) { tfSubTotal.setText(formatRupiah(0)); }
        }
    }
    
    private void hitungKembalian() {
        try {
            double total = parseRupiah(lblTotalAkhirValue.getText());
            double uangDibayar = Double.parseDouble(tfUangDibayar.getText().trim());
            if (uangDibayar < total) {
                JOptionPane.showMessageDialog(this, "Uang dibayar kurang dari total belanja!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                lblKembalian.setText("Rp 0");
            } else {
                double kembalian = uangDibayar - total;
                lblKembalian.setText(formatRupiah(kembalian));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal uang yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tambahKeKeranjang() {
        Customer customer = (Customer) cbCustomer.getSelectedItem();
        Barang barang = (Barang) cbBarang.getSelectedItem();
        if (customer == null || barang == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer dan barang!");
            return;
        }
        int jumlah;
        try { jumlah = Integer.parseInt(tfJumlahBeli.getText().trim()); } 
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Jumlah harus angka!"); return; }
        if (jumlah <= 0) { JOptionPane.showMessageDialog(this, "Jumlah harus > 0!"); return; }
        if (barang.getStok() < jumlah) { JOptionPane.showMessageDialog(this, "Stok tidak cukup!"); return; }
        
        for (KeranjangItem item : keranjangList) {
            if (item.idBarang.equals(barang.getIdBarang())) {
                item.jumlah += jumlah;
                item.subTotal = item.harga * item.jumlah;
                refreshKeranjangTable();
                updateTotalAkhir();
                resetItemForm();
                return;
            }
        }
        keranjangList.add(new KeranjangItem(barang.getIdBarang(), barang.getNamaBarang(), barang.getHargaJual(), jumlah));
        refreshKeranjangTable();
        updateTotalAkhir();
        resetItemForm();
    }
    
    private void refreshKeranjangTable() {
        tableKeranjangModel.setRowCount(0);
        for (KeranjangItem item : keranjangList) {
            tableKeranjangModel.addRow(new Object[]{item.idBarang, item.namaBarang, formatRupiah(item.harga), item.jumlah, formatRupiah(item.subTotal)});
        }
    }
    
    private void updateTotalAkhir() {
        double total = 0;
        for (KeranjangItem item : keranjangList) total += item.subTotal;
        lblTotalAkhirValue.setText(formatRupiah(total));
    }
    
    private void resetItemForm() {
        if (cbBarang.getItemCount() > 0) cbBarang.setSelectedIndex(0);
        tfJumlahBeli.setText("");
        tfSubTotal.setText("");
        tfJumlahBeli.requestFocus();
    }
    
    private void resetSemua() {
        keranjangList.clear();
        refreshKeranjangTable();
        updateTotalAkhir();
        resetItemForm();
        currentTransaksiId = generateTransaksiId();
        lblTransaksiId.setText("Transaksi #: " + currentTransaksiId);
        if (cbCustomer.getItemCount() > 0) cbCustomer.setSelectedIndex(0);
        tfUangDibayar.setText("");
        lblKembalian.setText("Rp 0");
        cbMetodePembayaran.setSelectedIndex(0);
        tableRiwayatCustomerModel.setRowCount(0);
        tableDetailItemModel.setRowCount(0);
    }
    
    private double parseRupiah(String rupiah) {
        return Double.parseDouble(rupiah.replace("Rp", "").replace(".", "").replace(",", "").trim());
    }
    
    private void simpanTransaksi() {
        Customer customer = (Customer) cbCustomer.getSelectedItem();
        if (customer == null || keranjangList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih customer dan tambahkan barang!");
            return;
        }
        
        double total = 0;
        for (KeranjangItem item : keranjangList) total += item.subTotal;
        
        String metode = (String) cbMetodePembayaran.getSelectedItem();
        
        if (metode.equals("Cash")) {
            try {
                double uangDibayar = Double.parseDouble(tfUangDibayar.getText().trim());
                if (uangDibayar < total) {
                    JOptionPane.showMessageDialog(this, "Uang dibayar kurang dari total belanja!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Masukkan nominal uang yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "📋 KONFIRMASI TRANSAKSI\n\n" +
            "Customer    : " + customer.getNamaCustomer() + "\n" +
            "Total       : " + formatRupiah(total) + "\n" +
            "Metode Bayar: " + metode + "\n" +
            (metode.equals("Cash") ? "Kembalian   : " + lblKembalian.getText() + "\n" : "") +
            "\nSimpan transaksi ini?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            String sqlHeader = "INSERT INTO tb_transaksi_header (id_transaksi, tgl_transaksi, id_customer, total_item, total_bayar, metode_pembayaran) VALUES (?, NOW(), ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHeader)) {
                pstmt.setString(1, currentTransaksiId);
                pstmt.setString(2, customer.getIdCustomer());
                pstmt.setInt(3, keranjangList.size());
                pstmt.setDouble(4, total);
                pstmt.setString(5, metode);
                pstmt.executeUpdate();
            }
            
            String sqlDetail = "INSERT INTO tb_transaksi_detail (id_transaksi, id_barang, jumlah_beli, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDetail)) {
                for (KeranjangItem item : keranjangList) {
                    pstmt.setString(1, currentTransaksiId);
                    pstmt.setString(2, item.idBarang);
                    pstmt.setInt(3, item.jumlah);
                    pstmt.setDouble(4, item.subTotal);
                    pstmt.executeUpdate();
                }
            }
            
            String sqlUpdateStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateStok)) {
                for (KeranjangItem item : keranjangList) {
                    pstmt.setInt(1, item.jumlah);
                    pstmt.setString(2, item.idBarang);
                    pstmt.executeUpdate();
                }
            }
            
            conn.commit();
            
            String pesanSukses = "✅ TRANSAKSI BERHASIL!\n\n" +
                "ID Transaksi: " + currentTransaksiId + "\n" +
                "Total: " + formatRupiah(total) + "\n" +
                "Metode: " + metode;
            
            if (metode.equals("Cash")) {
                pesanSukses += "\nKembalian: " + lblKembalian.getText();
            }
            
            JOptionPane.showMessageDialog(this, pesanSukses, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh riwayat customer
            loadRiwayatCustomer(customer);
            loadBarang();
            resetSemua();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String formatRupiah(double angka) {
        return String.format("Rp %,.0f", angka);
    }
    
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return btn;
    }
    
    class KeranjangItem {
        String idBarang, namaBarang;
        double harga, subTotal;
        int jumlah;
        KeranjangItem(String id, String nama, double harga, int jml) {
            this.idBarang = id; this.namaBarang = nama; this.harga = harga; this.jumlah = jml; this.subTotal = harga * jml;
        }
    }
}