import static org.junit.Assert.*;
import org.junit.Test;

public class LibraryApplicationTest {

    @Test
    public void testShowAuthorMenu_noPermission() {
        Administrator admin = new Administrator();
        admin.setPermission(Permission.NONE);
        LibraryApplication.showAuthorMenu(admin);
        // Assert output shows no permission message
        assertTrue(outContent.toString().contains("You don't have permission to manage authors."));
    }

    @Test
    public void testShowAuthorMenu_withReadPermission() {
        Administrator admin = new Administrator();
        admin.setPermission(Permission.READ);
        LibraryApplication.showAuthorMenu(admin);
        // Assert output shows author list
        assertTrue(outContent.toString().contains("List of Authors:"));
    }

    @Test
    public void testShowAuthorMenu_withWritePermission() {
        Administrator admin = new Administrator();
        admin.setPermission(Permission.WRITE);
        LibraryApplication.showAuthorMenu(admin);
        // Assert add author workflow succeeds
        assertTrue(outContent.toString().contains("Author added successfully!"));
    }

    @Test
    public void testShowAuthorMenu_withDeletePermission() {
        Administrator admin = new Administrator();
        admin.setPermission(Permission.DELETE);
        LibraryApplication.showAuthorMenu(admin);
        // Assert delete author workflow succeeds
        assertTrue(outContent.toString().contains("Author deleted successfully!"));
    }

}
