package tokoberkahjaya;

public class User {
    private String username;
    private String password;
    private String namaLengkap;
    private String role;
    
    public User(String username, String password, String namaLengkap, String role) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.role = role;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getRole() { return role; }
    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
    public boolean isKasir() { return "kasir".equalsIgnoreCase(role); }
}