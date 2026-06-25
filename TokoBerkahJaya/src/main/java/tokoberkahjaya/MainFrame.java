package tokoberkahjaya;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private JLabel lblUserInfo;
    private User currentUser;
    
    private TransaksiPanel transaksiPanel;
    private ManagementPanel managementPanel;  // ← SEKARANG ManagementPanel (bukan ManagementFrame)
    private ManagementUserPanel userPanel;
    private LaporanPenjualanPanel laporanPanel;
    
    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
        showPanel("transaksi");
    }
    
    private void initComponents() {
        setTitle("Dashboard - Toko Berkah Jaya");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        setLayout(new BorderLayout());
        
        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel appTitle = new JLabel("🏪 Toko Berkah Jaya");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);
        
        String roleText = currentUser.isAdmin() ? "👑 Admin" : "🛒 Kasir";
        lblUserInfo = new JLabel(roleText + " | " + currentUser.getNamaLengkap() + 
            " | " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUserInfo.setForeground(new Color(230, 240, 255));
        
        JButton btnLogout = createMenuButton("Logout", new Color(220, 70, 70));
        btnLogout.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(lblUserInfo);
        rightPanel.add(btnLogout);
        
        headerPanel.add(appTitle, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 30, 35));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        
        JLabel logoLabel = new JLabel("📦 TBJ", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(new Color(0, 102, 204));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        JButton btnTransaksi = createSidebarButton("🛒 Transaksi", true);
        btnTransaksi.addActionListener(e -> showPanel("transaksi"));
        
        sidebar.add(logoLabel);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnTransaksi);
        sidebar.add(Box.createVerticalStrut(5));
        
        if (currentUser.isAdmin()) {
            JButton btnBarang = createSidebarButton("📦 Data Barang", false);
            JButton btnCustomer = createSidebarButton("👥 Data Customer", false);
            JButton btnUser = createSidebarButton("👤 Management User", false);
            JButton btnLaporan = createSidebarButton("📊 Laporan", false);
            
            btnBarang.addActionListener(e -> showPanel("barang"));
            btnCustomer.addActionListener(e -> showPanel("customer"));
            btnUser.addActionListener(e -> showPanel("user"));
            btnLaporan.addActionListener(e -> showPanel("laporan"));
            
            sidebar.add(btnBarang);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnCustomer);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnUser);
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btnLaporan);
            sidebar.add(Box.createVerticalStrut(5));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // CONTENT
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(245, 245, 250));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JButton createSidebarButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        
        if (isActive) {
            btn.setBackground(new Color(0, 102, 204));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(45, 45, 50));
            btn.setForeground(new Color(220, 220, 220));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(60, 60, 65)); }
                public void mouseExited(MouseEvent e) { btn.setBackground(new Color(45, 45, 50)); }
            });
        }
        return btn;
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }
    
    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        
        // Validasi akses untuk admin-only
        if (!currentUser.isAdmin()) {
            if (panelName.equals("barang") || panelName.equals("customer") || 
                panelName.equals("user") || panelName.equals("laporan")) {
                JOptionPane.showMessageDialog(this, "Akses ditolak! Hanya untuk Admin.");
                return;
            }
        }
        
        switch (panelName) {
            case "transaksi":
                if (transaksiPanel == null) {
                    transaksiPanel = new TransaksiPanel();
                }
                contentPanel.removeAll();
                contentPanel.add(transaksiPanel, "transaksi");
                cl.show(contentPanel, "transaksi");
                transaksiPanel.refreshData();
                break;
                
            case "barang":
                if (managementPanel == null) {
                    managementPanel = new ManagementPanel();
                }
                contentPanel.removeAll();
                managementPanel.showBarangPanel();
                contentPanel.add(managementPanel, "barang");
                cl.show(contentPanel, "barang");
                break;
                
            case "customer":
                if (managementPanel == null) {
                    managementPanel = new ManagementPanel();
                }
                contentPanel.removeAll();
                managementPanel.showCustomerPanel();
                contentPanel.add(managementPanel, "customer");
                cl.show(contentPanel, "customer");
                break;
                
            case "user":
                if (userPanel == null) {
                    userPanel = new ManagementUserPanel();
                }
                contentPanel.removeAll();
                contentPanel.add(userPanel, "user");
                cl.show(contentPanel, "user");
                break;
                
            case "laporan":
                if (laporanPanel == null) {
                    laporanPanel = new LaporanPenjualanPanel();
                }
                contentPanel.removeAll();
                contentPanel.add(laporanPanel, "laporan");
                cl.show(contentPanel, "laporan");
                break;
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}