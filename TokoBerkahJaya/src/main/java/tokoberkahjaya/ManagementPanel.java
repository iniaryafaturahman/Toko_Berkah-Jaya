package tokoberkahjaya;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ManagementPanel extends JPanel {
    
    private JTabbedPane tabbedPane;
    
    // Komponen Barang
    private JTextField tfIdBarang, tfNamaBarang, tfSatuan, tfHargaJual, tfStok;
    private JComboBox<String> cbKategori;
    private JButton btnBarangSave, btnBarangUpdate, btnBarangDelete, btnBarangClear, btnBarangRefresh;
    private JTable tableBarang;
    private DefaultTableModel tableBarangModel;
    private BarangDAO barangDAO;
    private String selectedBarangId = null;
    
    // Komponen Customer
    private JTextField tfIdCustomer, tfNamaCustomer, tfAlamat, tfTelepon;
    private JButton btnCustomerSave, btnCustomerUpdate, btnCustomerDelete, btnCustomerClear, btnRefreshCustomer;
    private JTable tableCustomer;
    private DefaultTableModel tableCustomerModel;
    private CustomerDAO customerDAO;
    private String selectedCustomerId = null;
    
    public ManagementPanel() {
        barangDAO = new BarangDAO();
        customerDAO = new CustomerDAO();
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📦 Data Barang", createBarangPanel());
        tabbedPane.addTab("👥 Data Customer", createCustomerPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    public void showBarangPanel() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0);
            loadBarangTable();
        }
    }
    
    public void showCustomerPanel() {
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(1);
            loadCustomerTable();
        }
    }
    
    // ==================== VALIDASI ====================
    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z\\s]+");
    }
    
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d+");
    }
    
    // ==================== KATEGORI ====================
    private void loadKategori() {
        cbKategori.removeAllItems();
        KategoriDAO kategoriDAO = new KategoriDAO();
        List<Kategori> list = kategoriDAO.getAllKategori();
        for (Kategori k : list) {
            cbKategori.addItem(k.getNamaKategori());
        }
    }
    
    private void tambahKategori() {
        String namaKategori = JOptionPane.showInputDialog(this, 
            "Masukkan nama kategori baru:", 
            "Tambah Kategori", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (namaKategori != null && !namaKategori.trim().isEmpty()) {
            KategoriDAO kategoriDAO = new KategoriDAO();
            if (kategoriDAO.insertKategori(namaKategori.trim())) {
                JOptionPane.showMessageDialog(this, "✅ Kategori berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadKategori();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal menambahkan kategori! Mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private int getIdKategoriFromName(String namaKategori) {
        KategoriDAO kategoriDAO = new KategoriDAO();
        return kategoriDAO.getIdKategoriFromName(namaKategori);
    }
    
    private String getNamaKategoriFromId(int idKategori) {
        KategoriDAO kategoriDAO = new KategoriDAO();
        List<Kategori> list = kategoriDAO.getAllKategori();
        for (Kategori k : list) {
            if (k.getIdKategori() == idKategori) {
                return k.getNamaKategori();
            }
        }
        return "Lainnya";
    }
    
    // ==================== PANEL BARANG ====================
    private JPanel createBarangPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID Barang
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblIdBarang = new JLabel("ID Barang:");
        lblIdBarang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblIdBarang, gbc);
        gbc.gridx = 1;
        tfIdBarang = new JTextField(15);
        tfIdBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfIdBarang, gbc);
        
        // Kategori (DENGAN TOMBOL +)
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblKategori = new JLabel("Kategori:");
        lblKategori.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblKategori, gbc);
        gbc.gridx = 1;
        
        JPanel kategoriPanel = new JPanel(new BorderLayout(5, 0));
        kategoriPanel.setOpaque(false);
        
        cbKategori = new JComboBox<>();
        cbKategori.setPreferredSize(new Dimension(180, 30));
        cbKategori.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        kategoriPanel.add(cbKategori, BorderLayout.CENTER);
        
        JButton btnTambahKategori = new JButton("+");
        btnTambahKategori.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTambahKategori.setBackground(new Color(0, 150, 0));
        btnTambahKategori.setForeground(Color.WHITE);
        btnTambahKategori.setFocusPainted(false);
        btnTambahKategori.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambahKategori.setPreferredSize(new Dimension(35, 30));
        btnTambahKategori.addActionListener(e -> tambahKategori());
        kategoriPanel.add(btnTambahKategori, BorderLayout.EAST);
        
        formPanel.add(kategoriPanel, gbc);
        
        // Nama Barang
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblNamaBarang = new JLabel("Nama Barang:");
        lblNamaBarang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblNamaBarang, gbc);
        gbc.gridx = 1;
        tfNamaBarang = new JTextField(20);
        tfNamaBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfNamaBarang, gbc);
        
        // Satuan
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblSatuan = new JLabel("Satuan:");
        lblSatuan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblSatuan, gbc);
        gbc.gridx = 1;
        tfSatuan = new JTextField(10);
        tfSatuan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfSatuan, gbc);
        
        // Harga Jual
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblHargaJual = new JLabel("Harga Jual:");
        lblHargaJual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblHargaJual, gbc);
        gbc.gridx = 1;
        tfHargaJual = new JTextField(15);
        tfHargaJual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfHargaJual, gbc);
        
        // Stok
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblStok = new JLabel("Stok:");
        lblStok.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblStok, gbc);
        gbc.gridx = 1;
        tfStok = new JTextField(10);
        tfStok.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfStok, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnBarangSave = createButton("💾 Simpan", new Color(0, 150, 0));
        btnBarangUpdate = createButton("✏️ Update", new Color(0, 102, 204));
        btnBarangDelete = createButton("🗑️ Hapus", new Color(200, 50, 50));
        btnBarangClear = createButton("🧹 Clear", new Color(150, 150, 150));
        btnBarangRefresh = createButton("🔄 Refresh", new Color(100, 100, 200));
        
        buttonPanel.add(btnBarangSave);
        buttonPanel.add(btnBarangUpdate);
        buttonPanel.add(btnBarangDelete);
        buttonPanel.add(btnBarangClear);
        buttonPanel.add(btnBarangRefresh);
        formPanel.add(buttonPanel, gbc);
        
        // Table Barang
        String[] columns = {"ID Barang", "Kategori", "Nama Barang", "Satuan", "Harga Jual", "Stok"};
        tableBarangModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableBarang = new JTable(tableBarangModel);
        tableBarang.setRowHeight(30);
        tableBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableBarang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableBarang.getTableHeader().setBackground(new Color(240, 240, 240));
        tableBarang.setSelectionBackground(new Color(200, 220, 255));
        
        // Set column widths
        tableBarang.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableBarang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableBarang.getColumnModel().getColumn(2).setPreferredWidth(180);
        tableBarang.getColumnModel().getColumn(3).setPreferredWidth(70);
        tableBarang.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableBarang.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📋 Daftar Barang", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(0, 102, 204)
        ));
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Event Listeners Barang
        btnBarangSave.addActionListener(e -> saveBarang());
        btnBarangUpdate.addActionListener(e -> updateBarang());
        btnBarangDelete.addActionListener(e -> deleteBarang());
        btnBarangClear.addActionListener(e -> clearBarangForm());
        btnBarangRefresh.addActionListener(e -> {
            loadBarangTable();
            JOptionPane.showMessageDialog(this, "Data barang telah di-refresh!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        
        tableBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableBarang.getSelectedRow();
                if (row >= 0 && tableBarangModel.getValueAt(row, 0) != null) {
                    selectedBarangId = tableBarangModel.getValueAt(row, 0).toString();
                    tfIdBarang.setText(selectedBarangId);
                    tfIdBarang.setEditable(false);
                    cbKategori.setSelectedItem(tableBarangModel.getValueAt(row, 1).toString());
                    tfNamaBarang.setText(tableBarangModel.getValueAt(row, 2).toString());
                    tfSatuan.setText(tableBarangModel.getValueAt(row, 3).toString());
                    tfHargaJual.setText(tableBarangModel.getValueAt(row, 4).toString());
                    tfStok.setText(tableBarangModel.getValueAt(row, 5).toString());
                }
            }
        });
        
        // Load data kategori dan barang
        loadKategori();
        loadBarangTable();
        
        return mainPanel;
    }
    
    private void loadBarangTable() {
        tableBarangModel.setRowCount(0);
        
        System.out.println("=== LOAD BARANG TABLE ===");
        
        if (DatabaseConnection.getConnection() == null) {
            System.out.println("❌ Koneksi database GAGAL!");
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!\nPastikan MySQL menyala.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Barang> list = barangDAO.getAllBarang();
        
        if (list != null && !list.isEmpty()) {
            for (Barang b : list) {
                String namaKategori = getNamaKategoriFromId(b.getIdKategori());
                tableBarangModel.addRow(new Object[]{
                    b.getIdBarang(), 
                    namaKategori, 
                    b.getNamaBarang(),
                    b.getSatuan(), 
                    b.getHargaJual(), 
                    b.getStok()
                });
                System.out.println("✅ Added: " + b.getIdBarang() + " - " + b.getNamaBarang());
            }
        } else {
            System.out.println("⚠️ TIDAK ADA DATA BARANG!");
            tableBarangModel.addRow(new Object[]{"-", "Tidak ada data", "-", "-", "-", "-"});
        }
        
        System.out.println("📊 Total rows: " + tableBarangModel.getRowCount());
        System.out.println("=========================");
        
        tableBarang.revalidate();
        tableBarang.repaint();
    }
    
    private void saveBarang() {
        try {
            String idBarang = tfIdBarang.getText().trim();
            if (idBarang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID Barang harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                tfIdBarang.requestFocus();
                return;
            }
            
            // Cek apakah ID sudah ada
            Barang existing = barangDAO.getBarangById(idBarang);
            if (existing != null) {
                JOptionPane.showMessageDialog(this, 
                    "❌ ID Barang '" + idBarang + "' sudah ada!\nGunakan ID yang berbeda.", 
                    "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
                tfIdBarang.requestFocus();
                return;
            }
            
            if (tfNamaBarang.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama Barang harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                tfNamaBarang.requestFocus();
                return;
            }
            
            String namaKategori = (String) cbKategori.getSelectedItem();
            int idKategori = getIdKategoriFromName(namaKategori);
            
            Barang b = new Barang(
                idBarang,
                idKategori,
                tfNamaBarang.getText().trim(),
                tfSatuan.getText().trim(),
                Double.parseDouble(tfHargaJual.getText().trim()),
                Integer.parseInt(tfStok.getText().trim())
            );
            
            if (barangDAO.insertBarang(b)) {
                JOptionPane.showMessageDialog(this, "✅ Barang berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangTable();
                clearBarangForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal menambahkan barang!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Harga dan Stok harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBarang() {
        if (selectedBarangId == null) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang akan diupdate dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String namaKategori = (String) cbKategori.getSelectedItem();
            int idKategori = getIdKategoriFromName(namaKategori);
            
            Barang b = new Barang(
                tfIdBarang.getText().trim(),
                idKategori,
                tfNamaBarang.getText().trim(),
                tfSatuan.getText().trim(),
                Double.parseDouble(tfHargaJual.getText().trim()),
                Integer.parseInt(tfStok.getText().trim())
            );
            
            if (barangDAO.updateBarang(b)) {
                JOptionPane.showMessageDialog(this, "✅ Barang berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangTable();
                clearBarangForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal mengupdate barang!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteBarang() {
        if (selectedBarangId == null) {
            JOptionPane.showMessageDialog(this, "Pilih barang yang akan dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin akan menghapus barang " + selectedBarangId + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (barangDAO.deleteBarang(selectedBarangId)) {
                JOptionPane.showMessageDialog(this, "✅ Barang berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBarangTable();
                clearBarangForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal menghapus barang!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearBarangForm() {
        tfIdBarang.setText("");
        tfIdBarang.setEditable(true);
        tfNamaBarang.setText("");
        tfSatuan.setText("");
        tfHargaJual.setText("");
        tfStok.setText("");
        if (cbKategori.getItemCount() > 0) cbKategori.setSelectedIndex(0);
        selectedBarangId = null;
        tfIdBarang.requestFocus();
    }
    
    // ==================== PANEL CUSTOMER ====================
    private JPanel createCustomerPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID Customer
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblIdCustomer = new JLabel("ID Customer:");
        lblIdCustomer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblIdCustomer, gbc);
        gbc.gridx = 1;
        tfIdCustomer = new JTextField(15);
        tfIdCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfIdCustomer, gbc);
        
        // Nama Customer (dengan validasi)
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNamaCustomer = new JLabel("Nama Customer:");
        lblNamaCustomer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblNamaCustomer, gbc);
        gbc.gridx = 1;
        tfNamaCustomer = new JTextField(20);
        tfNamaCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfNamaCustomer.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = tfNamaCustomer.getText();
                if (!text.isEmpty() && !isValidName(text)) {
                    tfNamaCustomer.setForeground(Color.RED);
                } else {
                    tfNamaCustomer.setForeground(Color.BLACK);
                }
            }
        });
        formPanel.add(tfNamaCustomer, gbc);
        
        // Alamat
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblAlamat, gbc);
        gbc.gridx = 1;
        tfAlamat = new JTextField(30);
        tfAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(tfAlamat, gbc);
        
        // Telepon (dengan validasi)
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblTelepon = new JLabel("Telepon:");
        lblTelepon.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblTelepon, gbc);
        gbc.gridx = 1;
        tfTelepon = new JTextField(15);
        tfTelepon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfTelepon.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = tfTelepon.getText();
                if (!text.isEmpty() && !isValidPhone(text)) {
                    tfTelepon.setForeground(Color.RED);
                } else {
                    tfTelepon.setForeground(Color.BLACK);
                }
            }
        });
        formPanel.add(tfTelepon, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnCustomerSave = createButton("➕ Tambah Customer", new Color(0, 150, 0));
        btnCustomerUpdate = createButton("✏️ Update Customer", new Color(0, 102, 204));
        btnCustomerDelete = createButton("🗑️ Hapus Customer", new Color(200, 50, 50));
        btnCustomerClear = createButton("🧹 Clear Form", new Color(150, 150, 150));
        btnRefreshCustomer = createButton("🔄 Refresh", new Color(100, 100, 200));
        
        buttonPanel.add(btnCustomerSave);
        buttonPanel.add(btnCustomerUpdate);
        buttonPanel.add(btnCustomerDelete);
        buttonPanel.add(btnCustomerClear);
        buttonPanel.add(btnRefreshCustomer);
        formPanel.add(buttonPanel, gbc);
        
        // Table Customer
        String[] columns = {"ID Customer", "Nama Customer", "Alamat", "Telepon"};
        tableCustomerModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableCustomer = new JTable(tableCustomerModel);
        tableCustomer.setRowHeight(30);
        tableCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableCustomer.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableCustomer.getTableHeader().setBackground(new Color(240, 240, 240));
        tableCustomer.setSelectionBackground(new Color(200, 220, 255));
        
        JScrollPane scrollPane = new JScrollPane(tableCustomer);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "📋 Daftar Customer", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(0, 102, 204)
        ));
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Event Listeners Customer
        btnCustomerSave.addActionListener(e -> saveCustomer());
        btnCustomerUpdate.addActionListener(e -> updateCustomer());
        btnCustomerDelete.addActionListener(e -> deleteCustomer());
        btnCustomerClear.addActionListener(e -> clearCustomerForm());
        btnRefreshCustomer.addActionListener(e -> {
            loadCustomerTable();
            JOptionPane.showMessageDialog(this, "Data customer telah di-refresh!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        
        tableCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCustomer.getSelectedRow();
                if (row >= 0) {
                    selectedCustomerId = tableCustomerModel.getValueAt(row, 0).toString();
                    tfIdCustomer.setText(selectedCustomerId);
                    tfIdCustomer.setEditable(false);
                    tfNamaCustomer.setText(tableCustomerModel.getValueAt(row, 1).toString());
                    tfAlamat.setText(tableCustomerModel.getValueAt(row, 2).toString());
                    tfTelepon.setText(tableCustomerModel.getValueAt(row, 3).toString());
                    // Reset warna
                    tfNamaCustomer.setForeground(Color.BLACK);
                    tfTelepon.setForeground(Color.BLACK);
                }
            }
        });
        
        loadCustomerTable();
        return mainPanel;
    }
    
    private void loadCustomerTable() {
        tableCustomerModel.setRowCount(0);
        List<Customer> list = customerDAO.getAllCustomers();
        if (list != null && !list.isEmpty()) {
            for (Customer c : list) {
                tableCustomerModel.addRow(new Object[]{
                    c.getIdCustomer(), 
                    c.getNamaCustomer(), 
                    c.getAlamat(), 
                    c.getTelepon()
                });
            }
        } else {
            tableCustomerModel.addRow(new Object[]{"-", "Tidak ada data", "-", "-"});
        }
        System.out.println("Customer table loaded: " + tableCustomerModel.getRowCount() + " rows");
    }
    
    private void saveCustomer() {
        // Validasi ID Customer
        if (tfIdCustomer.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Customer harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfIdCustomer.requestFocus();
            return;
        }
        
        // Validasi Nama Customer
        String nama = tfNamaCustomer.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Customer harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfNamaCustomer.requestFocus();
            return;
        }
        
        if (!isValidName(nama)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Nama Customer hanya boleh mengandung HURUF dan SPASI!\n\nContoh: Andi Wijaya", 
                "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            tfNamaCustomer.requestFocus();
            tfNamaCustomer.setForeground(Color.RED);
            return;
        }
        
        // Validasi Telepon (opsional)
        String telepon = tfTelepon.getText().trim();
        if (!telepon.isEmpty() && !isValidPhone(telepon)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Nomor Telepon hanya boleh mengandung ANGKA 0-9!\n\nContoh: 08123456789", 
                "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            tfTelepon.requestFocus();
            tfTelepon.setForeground(Color.RED);
            return;
        }
        
        // Reset warna
        tfNamaCustomer.setForeground(Color.BLACK);
        tfTelepon.setForeground(Color.BLACK);
        
        Customer customer = new Customer(
            tfIdCustomer.getText().trim(),
            nama,
            tfAlamat.getText().trim(),
            telepon
        );
        
        if (customerDAO.insertCustomer(customer)) {
            JOptionPane.showMessageDialog(this, "✅ Customer berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadCustomerTable();
            clearCustomerForm();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Gagal! ID Customer mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCustomer() {
        if (selectedCustomerId == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nama = tfNamaCustomer.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Customer harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfNamaCustomer.requestFocus();
            return;
        }
        
        if (!isValidName(nama)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Nama Customer hanya boleh mengandung HURUF dan SPASI!", 
                "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            tfNamaCustomer.requestFocus();
            tfNamaCustomer.setForeground(Color.RED);
            return;
        }
        
        String telepon = tfTelepon.getText().trim();
        if (!telepon.isEmpty() && !isValidPhone(telepon)) {
            JOptionPane.showMessageDialog(this, 
                "❌ Nomor Telepon hanya boleh mengandung ANGKA 0-9!", 
                "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            tfTelepon.requestFocus();
            tfTelepon.setForeground(Color.RED);
            return;
        }
        
        tfNamaCustomer.setForeground(Color.BLACK);
        tfTelepon.setForeground(Color.BLACK);
        
        Customer customer = new Customer(
            tfIdCustomer.getText().trim(),
            nama,
            tfAlamat.getText().trim(),
            telepon
        );
        
        if (customerDAO.updateCustomer(customer)) {
            JOptionPane.showMessageDialog(this, "✅ Customer berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadCustomerTable();
            clearCustomerForm();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Gagal mengupdate customer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        if (selectedCustomerId == null) {
            JOptionPane.showMessageDialog(this, "Pilih customer dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin hapus customer: " + selectedCustomerId + " - " + tfNamaCustomer.getText() + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (customerDAO.deleteCustomer(selectedCustomerId)) {
                JOptionPane.showMessageDialog(this, "✅ Customer berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerTable();
                clearCustomerForm();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal menghapus customer!\nMungkin customer sudah memiliki transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearCustomerForm() {
        tfIdCustomer.setText("");
        tfIdCustomer.setEditable(true);
        tfNamaCustomer.setText("");
        tfAlamat.setText("");
        tfTelepon.setText("");
        selectedCustomerId = null;
        tfIdCustomer.requestFocus();
        tfNamaCustomer.setForeground(Color.BLACK);
        tfTelepon.setForeground(Color.BLACK);
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