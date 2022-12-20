package Server.services.Inter;

import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.DB.models.UserDB;
import Server.DB.models.validators.ValidatorException;
import Server.services.Service;
import Server.services.exceptions.ServiceException;
import Server.services.exceptions.UserAlreadyLoginException;

public interface UsersService extends Service {

    void register(UserDB user) throws DBException, ServiceException, NotUniqueException, NullException, ValidatorException;

    void login(UserDB userDB) throws ServiceException, DBException, NullException, NotFoundException, UserAlreadyLoginException;

    void logout(UserDB userDB);
}
