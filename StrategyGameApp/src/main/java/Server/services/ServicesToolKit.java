package Server.services;

import Server.DB.models.validators.EmailValidator;
import Server.DB.models.validators.NicknameValidator;
import Server.DB.models.validators.PasswordValidator;
import Server.DB.repositories.Impl.UsersRepositoryImpl;
import Server.DB.repositories.RepositoryImpl;
import Server.app.ServerApp;
import Server.services.Impl.UsersServiceImpl;
import Server.services.Inter.UsersService;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class ServicesToolKit {


    private final DataSource dataSource;

    private final Service service;
    private final UsersService usersService;


    public ServicesToolKit(DataSource dataSource) {
        this.dataSource = dataSource;

        service = new ServiceImpl(new RepositoryImpl(dataSource));


        Properties properties = new Properties();
        try {
            properties.load(ServerApp.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        EmailValidator emailValidator = new EmailValidator(properties.getProperty("email.regexp"));
        PasswordValidator passwordValidator = new PasswordValidator(
                properties.getProperty("password.regexp"),
                Integer.parseInt(properties.getProperty("password.minLength")),
                Integer.parseInt(properties.getProperty("password.maxLength"))
        );
        NicknameValidator nicknameValidator = new NicknameValidator(properties.getProperty("nickname.regexp"));

        usersService = new UsersServiceImpl(
                new UsersRepositoryImpl(dataSource),
                emailValidator,
                passwordValidator,
                nicknameValidator);
    }

    public Service getMainService() {
        return service;
    }

    public UsersService getUsersService() {
        return usersService;
    }
}
