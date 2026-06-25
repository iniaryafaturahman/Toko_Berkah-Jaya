package tokoberkahjaya;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ManagementUserPanel extends JPanel {
    private JTextField tfUsername, tfNamaLengkap;
    private JPasswordField pfPassword;
    private JComboBox<String> cbRole;
    private JButton btnSave, btnUpdate, btnDelete, btnClear, btnRefresh;
    private JTable tableUser;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private String selectedUsername = null;
    
    public ManagementUserPanel() {
        userDAO = new UserDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 250));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        tfUsername = new JTextField(15);
        formPanel.add(tfUsername, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        pfPassword = new JPasswordField(15);
        formPanel.add(pfPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1;
        tfNamaLengkap = new JTextField(20);
        formPanel.add(tfNamaLengkap, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        cbRole = new JComboBox<>(new String[]{"admin", "kasir"});
        formPanel.add(cbRole, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = createButton("➕ Tambah", new Color(0, 150, 0));
        btnUpdate = createButton("✏️ Update", new Color(0, 102, 204));
        btnDelete = createButton("🗑️ Hapus", new Color(200, 50, 50));
        btnClear = createButton("🧹 Clear", new Color(150, 150, 150));
        btnRefresh = createButton("🔄 Refresh", new Color(100, 100, 200));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        formPanel.add(buttonPanel, gbc);
        
        String[] columns = {"Username", "Nama Lengkap", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        tableUser = new JTable(tableModel);
        tableUser.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tableUser);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Daftar User"));
        
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        btnSave.addActionListener(e -> saveUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadUserTable());
        
        tableUser.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableUser.getSelectedRow();
                if (row >= 0) {
                    selectedUsername = tableModel.getValueAt(row, 0).toString();
                    tfUsername.setText(selectedUsername);
                    tfUsername.setEditable(false);
                    tfNamaLengkap.setText(tableModel.getValueAt(row, 1).toString());
                    cbRole.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    pfPassword.setText("");
                }
            }
        });
        
        loadUserTable();
    }
    
    private void loadUserTable() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getUsername(), u.getNamaLengkap(), u.getRole()});
        }
    }
    
    private void saveUser() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password harus diisi!");
            return;
        }
        User user = new User(username, password, tfNamaLengkap.getText().trim(), (String) cbRole.getSelectedItem());
        if (userDAO.insertUser(user)) {
            JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
            loadUserTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal! Username mungkin sudah ada.");
        }
    }
    
    private void updateUser() {
        if (selectedUsername == null) {
            JOptionPane.showMessageDialog(this, "Pilih user yang akan diupdate!");
            return;
        }
        String password = new String(pfPassword.getPassword()).trim();
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password harus diisi!");
            return;
        }
        User user = new User(selectedUsername, password, tfNamaLengkap.getText().trim(), (String) cbRole.getSelectedItem());
        userDAO.updateUser(user);
        loadUserTable();
        clearForm();
    }
    
    private void deleteUser() {
        if (selectedUsername == null) return;
        if (selectedUsername.equals("admin")) {
            JOptionPane.showMessageDialog(this, "User admin tidak dapat dihapus!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Hapus user " + selectedUsername + "?") == JOptionPane.YES_OPTION) {
            userDAO.deleteUser(selectedUsername);
            loadUserTable();
            clearForm();
        }
    }
    
    private void clearForm() {
        tfUsername.setText("");
        tfUsername.setEditable(true);
        pfPassword.setText("");
        tfNamaLengkap.setText("");
        cbRole.setSelectedIndex(0);
        selectedUsername = null;
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