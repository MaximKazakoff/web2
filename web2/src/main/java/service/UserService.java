package service;

import model.User;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class UserService {

    private static UserService userService;

    public static UserService getUserService() {
        if (userService == null) {
            synchronized (UserService.class) {
                if (userService == null) {
                    userService = new UserService();
                }
            }
        }
        return userService;
    }

    private UserService() {
    }

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());


    public List<User> getAllUsers() {
        System.out.println("getAllUsers Результат: " + Arrays.toString(dataBase.values().stream().map(User::toString).toArray()));
        System.out.println(" ");
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        System.out.println("getUserById Параметр: " + id);
        System.out.println("getUserById Результат: " + dataBase.get(id));
        System.out.println("getUserById dataBase: " + Arrays.toString(dataBase.values().stream().map(User::toString).toArray()));
        System.out.println(" ");
        return dataBase.get(id);
    }

    public boolean addUser(User user) {

        System.out.println("addUser Параметры вызова: " + user.toString());

        user.setId(maxId.getAndIncrement());
        System.out.println("addUser После изменения: " + user.toString());
        /*
        1.	Карта пользователей не должна содержать пользователей с одинаковым id
        2. При добавлении пользователя обязательна проверка на наличие такого пользователя в базе
         */
        System.out.println("Получить юзера по id:");
        System.out.println("addUser getUserById Результат: " + getUserById(user.getId()));
        System.out.println("Проверка юзера в базе зарегистрированных:");
        System.out.println("addUser isExistsThisUser Результат: " + !isExistsThisUser(user));
        System.out.println(" ");
        if (getUserById(user.getId()) == null & !isExistsThisUser(user)) {
            return dataBase.put(user.getId(), user) == null;
        } else {
            return false;
        }
    }

    public void deleteAllUser() {
        System.out.println("deleteAllUser До: " + Arrays.toString(dataBase.values().stream().map(User::toString).toArray()));

        dataBase.clear();
        System.out.println("deleteAllUser После: " + Arrays.toString(dataBase.values().stream().map(User::toString).toArray()));
        System.out.println(" ");
    }

    public boolean isExistsThisUser(User user) {
        System.out.println("isExistsThisUser Параметры вызова: " + user.toString());
        System.out.println("isExistsThisUser Параметры вызова dataBase: " + Arrays.toString(dataBase.values().stream().map(User::toString).toArray()));
        boolean result = dataBase.values()
                .stream()
                .map(v -> Arrays.asList(v.getEmail(), v.getPassword()))
                .collect(Collectors.toList())
                .stream()
                .anyMatch(el -> el.equals(Arrays.asList(user.getEmail(), user.getPassword())));
        System.out.println("isExistsThisUser: " + result);
        System.out.println(" ");
        return result;
    }


    public List<User> getAllAuth() {
        System.out.println("getAllAuth: " + new ArrayList<>(authMap.values()));
        System.out.println(" ");
        return new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        System.out.println("authUser Параметры вызова: " + user.toString());
        System.out.println("Проверка юзера в базе зарегистрированных:");
        System.out.println("authUser isExistsThisUser(user): " + isExistsThisUser(user));
        System.out.println(" ");
        /*
        3. Нельзя залогинить пользователя с отсутствующей регистрацией.
         */
        if (isExistsThisUser(user)) {
            User newUser = dataBase
                    .values()
                    .stream()
                    .filter(el -> el.getEmail().equals(user.getEmail()) && el.getPassword().equals(user.getPassword()))
                    .collect(Collectors.toList())
                    .get(0);
            return authMap.putIfAbsent(newUser.getId(), newUser) == null;
        } else {
            return false;
        }
    }

    public void logoutAllUsers() {
        System.out.println("logoutAllUsers До: " + Arrays.toString(authMap.values().stream().map(User::toString).toArray()));

        authMap.clear();
        System.out.println("logoutAllUsers После: " + Arrays.toString(authMap.values().stream().map(User::toString).toArray()));
        System.out.println(" ");
    }

    public boolean isUserAuthById(Long id) {
        System.out.println("isUserAuthById параметр: " + id);
        System.out.println("authMap.containsKey(id): " + authMap.containsKey(id));
        System.out.println(" ");
        return authMap.containsKey(id);
    }
}
