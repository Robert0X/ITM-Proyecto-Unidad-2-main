import java.util.ArrayList;

public class Administrator extends User {
    private ArrayList<Permission> permissions;
    private boolean isSuperAdmin;

    public Administrator(Profile profile, String username, String password, ArrayList<Permission> permissions,
            boolean isSuperAdmin) {
        super(profile, username, password);
        this.permissions = permissions;
        this.isSuperAdmin = isSuperAdmin;
    }

    // Getters y setters

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }
}
