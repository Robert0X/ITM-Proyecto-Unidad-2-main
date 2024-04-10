import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private Profile profile;
    private String username;
    private String password;

    public User(Profile profile, String username, String password) {
        this.profile = profile;
        this.username = username;
        this.setPassword(password); // Uso del setter para hashear la contraseña
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        // Hashear la contraseña usando SHA-256 y almacenar el hash
        this.password = hashString(password);
    }

    public boolean checkPassword(String password) {
        // Comparar el hash de la contraseña ingresada con el hash almacenado
        return this.password.equals(hashString(password));
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
