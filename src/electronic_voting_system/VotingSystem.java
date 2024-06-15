package electronic_voting_system;

import java.text.SimpleDateFormat;
import java.util.*;

public class VotingSystem {
    private static List<User> users;
    private static List<Voting> votings;

    public static void main(String[] args) {
        users = DataStorage.loadUsers();
        votings = DataStorage.loadVotings();

        if (users == null) {
            users = new ArrayList<>();
        }
        if (votings == null) {
            votings = new ArrayList<>();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в систему электронного голосования!");
        System.out.print("1. Войти\n2. Зарегистрироваться\nВыберите опцию: ");
        int option = Integer.parseInt(scanner.nextLine());

        if (option == 1) {
            login(scanner);
        } else if (option == 2) {
            register(scanner);
        } else {
            System.out.println("Неверный выбор.");
        }

        scanner.close();
    }

    private static void login(Scanner scanner) {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User currentUser = authenticate(login, password);

        if (currentUser != null) {
            System.out.println("Успешный вход. Ваша роль: " + currentUser.getRole());
            switch (currentUser.getRole()) {
                case "Admin":
                    adminMenu(scanner);
                    break;
                case "CIC":
                    cicMenu(scanner);
                    break;
                case "Candidate":
                    candidateMenu(scanner);
                    break;
                case "User":
                    userMenu(scanner);
                    break;
                default:
                    System.out.println("Неизвестная роль: " + currentUser.getRole());
            }
        } else {
            System.out.println("Неверные логин или пароль.");
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите дату рождения (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();

        User newUser = new User(login, password, "User", fullName, birthDate);
        users.add(newUser);
        DataStorage.saveUsers(users);
        System.out.println("Регистрация завершена. Теперь вы можете войти.");
    }

    private static User authenticate(String login, String password) {
        for (User user : users) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private static void adminMenu(Scanner scanner) {
        System.out.println("Админ меню:");
        System.out.println("1. Просмотр пользователей");
        System.out.println("2. Удаление пользователя");
        System.out.println("3. Просмотр ЦИК");
        System.out.println("4. Удаление ЦИК");
        System.out.println("5. Создание ЦИК");
        System.out.println("6. Просмотр кандидатов");
        System.out.println("7. Удаление кандидата");
        System.out.print("Выберите опцию: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                viewUsers();
                break;
            case 2:
                deleteUser(scanner);
                break;
            case 3:
                viewCICs();
                break;
            case 4:
                deleteCIC(scanner);
                break;
            case 5:
                createCIC(scanner);
                break;
            case 6:
                viewCandidates();
                break;
            case 7:
                deleteCandidate(scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void viewUsers() {
        for (User user : users) {
            System.out.println(user.getLogin() + " - " + user.getRole());
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.print("Введите логин пользователя для удаления: ");
        String login = scanner.nextLine();
        users.removeIf(user -> user.getLogin().equals(login));
        DataStorage.saveUsers(users);
        System.out.println("Пользователь удален.");
    }

    private static void viewCICs() {
        for (User user : users) {
            if (user.getRole().equals("CIC")) {
                System.out.println(user.getLogin() + " - " + user.getRole());
            }
        }
    }

    private static void deleteCIC(Scanner scanner) {
        System.out.print("Введите логин ЦИК для удаления: ");
        String login = scanner.nextLine();
        users.removeIf(user -> user.getLogin().equals(login) && user.getRole().equals("CIC"));
        DataStorage.saveUsers(users);
        System.out.println("ЦИК удален.");
    }

    private static void createCIC(Scanner scanner) {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите дату рождения (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();

        User newUser = new User(login, password, "CIC", fullName, birthDate);
        users.add(newUser);
        DataStorage.saveUsers(users);
        System.out.println("ЦИК создан.");
    }

    private static void cicMenu(Scanner scanner) {
        System.out.println("Меню ЦИК:");
        System.out.println("1. Создание голосования");
        System.out.println("2. Добавление кандидатов");
        System.out.print("Выберите опцию: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                createVoting(scanner);
                break;
            case 2:
                addCandidate(scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void createVoting(Scanner scanner) {
        System.out.print("Введите название голосования: ");
        String title = scanner.nextLine();
        System.out.print("Введите дату окончания (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date endDate;
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Voting voting = new Voting(title, endDate, new ArrayList<>());
        votings.add(voting);
        DataStorage.saveVotings(votings);
        System.out.println("Голосование создано.");
    }

    private static void addCandidate(Scanner scanner) {
    System.out.print("Введите название голосования: ");
    String votingTitle = scanner.nextLine();
    Voting voting = votings.stream()
            .filter(v -> v.getTitle().equals(votingTitle))
            .findFirst()
            .orElse(null);

    if (voting == null) {
        System.out.println("Голосование не найдено.");
        return;
    }

    System.out.print("Введите имя кандидата: ");
    String name = scanner.nextLine();
    System.out.print("Введите описание кандидата: ");
    String description = scanner.nextLine();

    Candidate candidate = new Candidate(name, description);
    voting.getCandidates().add(candidate); // Добавляем кандидата в список кандидатов голосования
    DataStorage.saveVotings(votings); // Сохраняем изменения в хранилище

    System.out.println("Кандидат добавлен в голосование: " + votingTitle);
}

    private static void viewCandidates() {
        for (Voting voting : votings) {
            System.out.println("Голосование: " + voting.getTitle());
            System.out.println("Кандидаты:");
            for (Candidate candidate : voting.getCandidates()) {
                System.out.println("- " + candidate.getName());
            }
            System.out.println();
        }
    }

    private static void deleteCandidate(Scanner scanner) {
        System.out.print("Введите название голосования: ");
        String votingTitle = scanner.nextLine();
        System.out.print("Введите имя кандидата для удаления: ");
        String candidateName = scanner.nextLine();

        Voting voting = votings.stream()
                .filter(v -> v.getTitle().equals(votingTitle))
                .findFirst()
                .orElse(null);

        if (voting == null) {
            System.out.println("Голосование не найдено.");
            return;
        }

        voting.getCandidates().removeIf(candidate -> candidate.getName().equals(candidateName));
        DataStorage.saveVotings(votings);
        System.out.println("Кандидат удален.");
    }

    private static void candidateMenu(Scanner scanner) {
        System.out.println("Меню кандидата:");
        System.out.println("1. Заполнение данных о себе");
        System.out.println("2. Просмотр результатов предыдущего голосования");
        System.out.println("3. Просмотр всех голосований, в которых участвовал");
        System.out.print("Выберите опцию: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                updateCandidateInfo(scanner);
                break;
            case 2:
                viewPreviousResults();
                break;
            case 3:
                viewParticipatedVotings();
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void updateCandidateInfo(Scanner scanner) {
        System.out.println("Функция обновления данных о кандидате.");
    }

    private static void viewPreviousResults() {
        System.out.println("Функция просмотра результатов предыдущего голосования.");
    }

    private static void viewParticipatedVotings() {
        // Здесь можно реализовать вывод всех голосований, в которых участвовал текущий кандидат
        System.out.println("Функция просмотра всех голосований, в которых участвовал кандидат.");
    }

    private static void userMenu(Scanner scanner) {
        System.out.println("Меню пользователя:");
        System.out.println("1. Просмотр доступных голосований");
        System.out.println("2. Проголосовать");
        System.out.print("Выберите опцию: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                viewAvailableVotings();
                break;
            case 2:
                vote(scanner);
                break;
            default:
                System.out.println("Неверный выбор.");
        }
    }

    private static void vote(Scanner scanner) {
        System.out.print("Введите название голосования: ");
        String votingTitle = scanner.nextLine();
        Voting voting = votings.stream()
                .filter(v -> v.getTitle().equals(votingTitle))
                .findFirst()
                .orElse(null);

        if (voting == null) {
            System.out.println("Голосование не найдено.");
            return;
        }

        if (hasUserVoted(voting)) {
            System.out.println("Вы уже проголосовали в этом голосовании.");
            return;
        }

        System.out.println("Список кандидатов:");
        for (int i = 0; i < voting.getCandidates().size(); i++) {
            System.out.println((i + 1) + ". " + voting.getCandidates().get(i).getName());
        }

        System.out.print("Выберите кандидата (номер): ");
        int candidateIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (candidateIndex < 0 || candidateIndex >= voting.getCandidates().size()) {
            System.out.println("Неверный номер кандидата.");
            return;
        }
    }

    private static void viewAvailableVotings() {
        if (votings.isEmpty()) {
            System.out.println("Нет доступных голосований.");
            return;
        }
    }

    private static boolean hasUserVoted(Voting voting) {
    return voting.getCandidates().stream().anyMatch(candidate -> candidate.hasVotedBy("user"));
}
}
