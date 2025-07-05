package ru.otus.services;

import ru.otus.crm.service.DbServiceAppUserImpl;

public class UserAuthServiceImpl implements UserAuthService {

    private final DbServiceAppUserImpl userDao;

    public UserAuthServiceImpl(DbServiceAppUserImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
