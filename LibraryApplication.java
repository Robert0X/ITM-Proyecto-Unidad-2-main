import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class LibraryApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();
    private static List<Author> authors = new ArrayList<>();

    public static void main(String[] args) {
        initializeData(); // Inicializar datos de prueba
        showLoginMenu();
    }

    private static void showLoginMenu() {
        while (true) {
            System.out.println("1. Login");
            System.out.println("0. Exit");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    login();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = findUserByUsername(username);

        if (user == null || !user.checkPassword(password)) {
            System.out.println("Invalid username or password");
            return;
        }

        if (user instanceof Client) {
            showClientMenu((Client) user);
        } else if (user instanceof Administrator) {
            showAdministratorMenu((Administrator) user);
        }
    }

    private static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static void showClientMenu(Client client) {
        System.out.println("Welcome, " + client.getUsername());
        System.out.println("1. View available books");
        System.out.println("2. View your transactions");
        System.out.println("0. Logout");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                listAvailableBooks();
                break;
            case 2:
                listTransactionsByClient(client);
                break;
            case 0:
                showLoginMenu();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private static void showAdministratorMenu(Administrator admin) {
        System.out.println("Welcome, " + admin.getUsername());
        System.out.println("1. Manage books");
        System.out.println("2. Manage clients");
        System.out.println("3. Manage authors");
        System.out.println("4. Manage transactions");
        System.out.println("0. Logout");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                showBookMenu(admin);
                break;
            case 2:
                showClientMenu(admin);
                break;
            case 3:
                showAuthorMenu(admin);
                break;
            case 4:
                showTransactionMenu(admin);
                break;
            case 0:
                showLoginMenu();
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    private static void showBookMenu(Administrator admin) {
        if (!admin.hasPermission(Permission.READ)) {
            System.out.println("You don't have permission to manage books.");
            return;
        }

        while (true) {
            System.out.println("Book Management Menu");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. List All Books");
            System.out.println("0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    if (admin.hasPermission(Permission.WRITE)) {
                        addBook();
                    } else {
                        System.out.println("You don't have permission to add books.");
                    }
                    break;
                case 2:
                    if (admin.hasPermission(Permission.WRITE)) {
                        updateBook();
                    } else {
                        System.out.println("You don't have permission to update books.");
                    }
                    break;
                case 3:
                    if (admin.hasPermission(Permission.DELETE)) {
                        deleteBook();
                    } else {
                        System.out.println("You don't have permission to delete books.");
                    }
                    break;
                case 4:
                    if (admin.hasPermission(Permission.READ)) {
                        listAllBooks();
                    } else {
                        System.out.println("You don't have permission to list books.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void addBook() {
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author ID: ");
        String authorId = scanner.nextLine();
        Author author = AuthorRepository.getAuthorById(authorId);
        if (author == null) {
            System.out.println("Author not found");
            return;
        }
        Book newBook = new Book(isbn, title, author, new Date());
        BookRepository.createBook(newBook);
        System.out.println("Book added successfully!");
    }

    private static void updateBook() {
        System.out.print("Enter book ISBN to update: ");
        String isbn = scanner.nextLine();
        Book existingBook = BookRepository.getBookByIsbn(isbn);
        if (existingBook == null) {
            System.out.println("Book not found");
            return;
        }
        System.out.print("Enter updated title: ");
        String updatedTitle = scanner.nextLine();
        existingBook.setTitle(updatedTitle);
        BookRepository.updateBook(isbn, existingBook);
        System.out.println("Book updated successfully!");
    }

    private static void deleteBook() {
        System.out.print("Enter book ISBN to delete: ");
        String isbn = scanner.nextLine();
        Book book = BookRepository.getBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Cannot delete book because it is borrowed");
            return;
        }
        BookRepository.deleteBook(book);
        System.out.println("Book deleted successfully!");
    }

    private static void listAllBooks() {
        List<Book> allBooks = BookRepository.getAllBooks();
        for (Book book : allBooks) {
            System.out.println("ISBN: " + book.getIsbn() + ", Title: " + book.getTitle() + ", Author: "
                    + book.getAuthor().getProfile().getName());
        }
    }

    private static void showClientMenu(Administrator admin) {
        if (!admin.hasPermission(Permission.READ)) {
            System.out.println("You don't have permission to manage clients.");
            return;
        }

        while (true) {
            System.out.println("Client Management Menu");
            System.out.println("1. Add Client");
            System.out.println("2. Update Client");
            System.out.println("3. Delete Client");
            System.out.println("4. List All Clients");
            System.out.println("0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    if (admin.hasPermission(Permission.WRITE)) {
                        addClient();
                    } else {
                        System.out.println("You don't have permission to add clients.");
                    }
                    break;
                case 2:
                    if (admin.hasPermission(Permission.WRITE)) {
                        updateClient();
                    } else {
                        System.out.println("You don't have permission to update clients.");
                    }
                    break;
                case 3:
                    if (admin.hasPermission(Permission.DELETE)) {
                        deleteClient();
                    } else {
                        System.out.println("You don't have permission to delete clients.");
                    }
                    break;
                case 4:
                    if (admin.hasPermission(Permission.READ)) {
                        listAllClients();
                    } else {
                        System.out.println("You don't have permission to list clients.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static Date parseStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            return null;
        }
    }

    private static void addClient() {
        System.out.println("Enter client first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter client last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter birth date (dd/mm/yyyy): ");
        String birthStr = scanner.nextLine();
        Date birthDate = LibraryApplication.parseStringToDate(birthStr);
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        Profile profile = new Profile(firstName, lastName, birthDate);
        Client client = new Client(profile, username, password);
        ClientRepository.createClient(client);
        System.out.println("Client added successfully!");
    }

    private static void updateClient() {
        System.out.print("Enter client ID to update: ");
        String id = scanner.nextLine();
        Client existingClient = ClientRepository.getClient(id);
        if (existingClient == null) {
            System.out.println("Client not found");
            return;
        }
        System.out.print("Enter updated name: ");
        String updatedName = scanner.nextLine();
        System.out.print("Enter updated last name: ");
        String updatedLastName = scanner.nextLine();
        System.out.print("Enter updated birth date (dd/mm/yyyy): ");
        String updatedBirthStr = scanner.nextLine();
        Date updatedBirthDate = parseStringToDate(updatedBirthStr);
        Profile updatedProfile = new Profile(updatedName, updatedLastName, updatedBirthDate);
        existingClient.setProfile(updatedProfile);
        ClientRepository.updateClient(id, existingClient);
        System.out.println("Client updated successfully!");
    }

    private static void deleteClient() {
        System.out.print("Enter client ID to delete: ");
        String id = scanner.nextLine();
        Client client = ClientRepository.getClient(id);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }
        if (!client.getBorrowedBooks().isEmpty()) {
            System.out.println("Cannot delete client because they have borrowed books");
            return;
        }
        ClientRepository.deleteClient(client);
        System.out.println("Client deleted successfully!");
    }

    private static void listAllClients() {
        List<Client> allClients = ClientRepository.getAllClients();
        for (Client client : allClients) {
            System.out.println("ID: " + client.getId() + ", Name: " + client.getProfile().getName() + ", Last Name: "
                    + client.getProfile().getLastName() + ", Birth Date: " + client.getProfile().getBirthdate());
        }
    }

    private static void showAuthorMenu(Administrator admin) {
        if (!admin.hasPermission(Permission.READ)) {
            System.out.println("You don't have permission to manage authors.");
            return;
        }

        while (true) {
            System.out.println("Author Management Menu");
            System.out.println("1. Add Author");
            System.out.println("2. Update Author");
            System.out.println("3. Delete Author");
            System.out.println("4. List All Authors");
            System.out.println("0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    if (admin.hasPermission(Permission.WRITE)) {
                        addAuthor();
                    } else {
                        System.out.println("You don't have permission to add authors.");
                    }
                    break;
                case 2:
                    if (admin.hasPermission(Permission.WRITE)) {
                        updateAuthor();
                    } else {
                        System.out.println("You don't have permission to update authors.");
                    }
                    break;
                case 3:
                    if (admin.hasPermission(Permission.DELETE)) {
                        deleteAuthor();
                    } else {
                        System.out.println("You don't have permission to delete authors.");
                    }
                    break;
                case 4:
                    if (admin.hasPermission(Permission.READ)) {
                        listAllAuthors();
                    } else {
                        System.out.println("You don't have permission to list authors.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void addAuthor() {
        System.out.println("Enter author first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter author last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter birth date (dd/mm/yyyy): ");
        String birthStr = scanner.nextLine();
        Date birthDate = parseStringToDate(birthStr);
        Profile profile = new Profile(firstName, lastName, birthDate);
        Author author = new Author(profile);
        AuthorRepository.createAuthor(author);
        System.out.println("Author added successfully!");
    }

    private static void updateAuthor() {
        System.out.print("Enter author ID to update: ");
        String id = scanner.nextLine();
        Author existingAuthor = AuthorRepository.getAuthorById(id);
        if (existingAuthor == null) {
            System.out.println("Author not found");
            return;
        }
        System.out.print("Enter updated first name: ");
        String updatedFirstName = scanner.nextLine();
        System.out.print("Enter updated last name: ");
        String updatedLastName = scanner.nextLine();
        System.out.print("Enter updated birth date (dd/mm/yyyy): ");
        String updatedBirthStr = scanner.nextLine();
        Date updatedBirthDate = parseStringToDate(updatedBirthStr);
        Profile updatedProfile = new Profile(updatedFirstName, updatedLastName, updatedBirthDate);
        existingAuthor.setProfile(updatedProfile);
        AuthorRepository.updateAuthor(id, existingAuthor);
        System.out.println("Author updated successfully!");
    }

    private static void deleteAuthor() {
        System.out.print("Enter author ID to delete: ");
        String id = scanner.nextLine();
        Author author = AuthorRepository.getAuthorById(id);
        if (author == null) {
            System.out.println("Author not found");
            return;
        }
        if (!author.getBooks().isEmpty()) {
            System.out.println("Cannot delete author because they have assigned books!");
            return;
        }
        AuthorRepository.deleteAuthor(author);
    }

    private static void listAllAuthors() {
        List<Author> allAuthors = AuthorRepository.getAllAuthors();
        for (Author author : allAuthors) {
            System.out.println(author.getProfile().getName());
        }
    }

    private static void showTransactionMenu(Administrator admin) {
        if (!admin.hasPermission(Permission.READ)) {
            System.out.println("You don't have permission to manage transactions.");
            return;
        }

        while (true) {
            System.out.println("Transaction Management Menu");
            System.out.println("1. Add Transaction");
            System.out.println("2. Update Transaction");
            System.out.println("3. Delete Transaction");
            System.out.println("4. List All Transactions");
            System.out.println("0. Back");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    if (admin.hasPermission(Permission.WRITE)) {
                        addTransaction();
                    } else {
                        System.out.println("You don't have permission to add transactions.");
                    }
                    break;
                case 2:
                    if (admin.hasPermission(Permission.WRITE)) {
                        updateTransaction();
                    } else {
                        System.out.println("You don't have permission to update transactions.");
                    }
                    break;
                case 3:
                    if (admin.hasPermission(Permission.DELETE)) {
                        deleteTransaction();
                    } else {
                        System.out.println("You don't have permission to delete transactions.");
                    }
                    break;
                case 4:
                    if (admin.hasPermission(Permission.READ)) {
                        listAllTransactions();
                    } else {
                        System.out.println("You don't have permission to list transactions.");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void addTransaction() {
        System.out.print("Enter client ID: ");
        String clientId = scanner.nextLine();
        Client client = ClientRepository.getClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }
        System.out.print("Enter book ISBN: ");
        String bookIsbn = scanner.nextLine();
        Book book = BookRepository.getBookByIsbn(bookIsbn);
        if (book == null) {
            System.out.println("Book not found");
            return;
        }
        TransactionType transactionType = getTransactionType();
        if (transactionType == null) {
            return;
        }
        Transaction transaction = new Transaction(transactionType, client, book);
        TransactionRepository.logTransaction(transaction);
        System.out.println("Transaction added successfully!");
    }

    private static TransactionType getTransactionType() {
        System.out.println("Select transaction type:");
        System.out.println("1. Borrow");
        System.out.println("2. Return");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                return TransactionType.BORROW;
            case 2:
                return TransactionType.RETURN;
            default:
                System.out.println("Invalid option");
                return null;
        }
    }

    private static void updateTransaction() {
        System.out.print("Enter transaction ID to update: ");
        String id = scanner.nextLine();
        Transaction existingTransaction = TransactionRepository.getTransactionById(id);
        if (existingTransaction == null) {
            System.out.println("Transaction not found");
            return;
        }
        System.out.print("Enter updated client ID: ");
        String updatedClientId = scanner.nextLine();
        Client updatedClient = ClientRepository.getClient(updatedClientId);
        if (updatedClient == null) {
            System.out.println("Client not found");
            return;
        }
        System.out.print("Enter updated book ISBN: ");
        String updatedBookIsbn = scanner.nextLine();
        Book updatedBook = BookRepository.getBookByIsbn(updatedBookIsbn);
        if (updatedBook == null) {
            System.out.println("Book not found");
            return;
        }
        TransactionType updatedTransactionType = getTransactionType();
        if (updatedTransactionType == null) {
            return;
        }
        Transaction updatedTransaction = new Transaction(updatedTransactionType, updatedClient, updatedBook);
        TransactionRepository.updateTransaction(id, updatedTransaction);
        System.out.println("Transaction updated successfully!");
    }

    private static void deleteTransaction() {
        System.out.print("Enter transaction ID to delete: ");
        String id = scanner.nextLine();
        Transaction transaction = TransactionRepository.getTransactionById(id);
        if (transaction == null) {
            System.out.println("Transaction not found");
            return;
        }
        TransactionRepository.deleteTransaction(transaction);
        System.out.println("Transaction deleted successfully!");
    }

    private static void listAllTransactions() {
        List<Transaction> allTransactions = TransactionRepository.getAllTransactions();
        for (Transaction transaction : allTransactions) {
            System.out.println("ID: " + transaction.getId() + ", Type: " + transaction.getType() + ", Client: "
                    + transaction.getClient().getProfile().getName() + ", Book: " + transaction.getBook().getTitle()
                    + ", Date: " + transaction.getDate());
        }
    }

    private static void listAvailableBooks() {
        System.out.println("Available books:");
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println(book.getTitle() + " by " + book.getAuthor().getProfile().getName());
            }
        }
    }

    private static void listTransactionsByClient(Client client) {
        System.out.println("Your transactions:");
        // Implementar lógica para obtener y mostrar las transacciones del cliente
    }

    private static void initializeData() {
        // Crear algunos usuarios de ejemplo
        Profile profile1 = new Profile("Juan", "Perez", new Date());
        Client client1 = new Client(profile1, "jperez", "password123");
        users.add(client1);

        Profile profile2 = new Profile("Maria", "Gomez", new Date());
        Administrator admin1 = new Administrator(profile2, "mgomez", "admin123", new ArrayList<>(), false);
        users.add(admin1);

        // Crear algunos autores y libros de ejemplo
        Author author1 = new Author(new Profile("Gabriel", "García Márquez", new Date()));
        Author author2 = new Author(new Profile("Isabel", "Allende", new Date()));
        authors.add(author1);
        authors.add(author2);

        Book book1 = new Book("9780007491426", "Cien años de soledad", author1, new Date());
        Book book2 = new Book("9780571267277", "La casa de los espíritus", author2, new Date());
        books.add(book1);
        books.add(book2);

        // Agregar más datos según sea necesario
    }
}