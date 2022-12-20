package Server.services.Impl;

import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.DB.models.UserDB;
import Server.DB.models.encryptors.PasswordEncryptor;
import Server.DB.models.validators.EmailValidator;
import Server.DB.models.validators.NicknameValidator;
import Server.DB.models.validators.PasswordValidator;
import Server.DB.models.validators.ValidatorException;
import Server.DB.repositories.Inter.UsersRepository;
import Server.services.Inter.UsersService;
import Server.services.exceptions.ServiceException;
import Server.services.ServiceImpl;
import Server.services.exceptions.UserAlreadyLoginException;

import java.util.HashSet;
import java.util.Set;

public class UsersServiceImpl extends ServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final Set<UserDB> activeUsers;

    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final NicknameValidator nicknameValidator;

    private final PasswordEncryptor passwordEncryptor;


    public UsersServiceImpl(UsersRepository usersRepository,
                            EmailValidator emailValidator,
                            PasswordValidator passwordValidator,
                            NicknameValidator nicknameValidator) {
        super(usersRepository);
        this.usersRepository = usersRepository;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.nicknameValidator = nicknameValidator;
        this.passwordEncryptor = new PasswordEncryptor();

        this.activeUsers = new HashSet<>();
    }

    @Override
    public void register(UserDB user) throws DBException, ServiceException, NotUniqueException, NullException, ValidatorException {
        emailValidator.check(user.getEmail());
        passwordValidator.check(user.getPassword());
        nicknameValidator.check(user.getNickname());
        user.setPasswordHash(passwordEncryptor.encrypt(user.getPassword()));
        user.setPassword(null);

        super.add(user);
    }

    @Override
    public void login(UserDB user) throws ServiceException, DBException, NullException, NotFoundException, UserAlreadyLoginException {
        user.setPasswordHash(passwordEncryptor.encrypt(user.getPassword()));
        this.usersRepository.selectUserByEmailAndPasswordHash(user);
        if (activeUsers.contains(user)) {
            throw new UserAlreadyLoginException();
        }
        activeUsers.add(user);
    }

    @Override
    public void logout(UserDB userDB) {
        activeUsers.remove(userDB);
    }
}
