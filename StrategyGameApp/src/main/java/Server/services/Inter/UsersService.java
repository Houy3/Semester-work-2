package Server.services.Inter;

import Protocol.MessageValues.User.UserProfileData;
import Protocol.MessageValues.User.UserUpdateForm;
import Server.DB.exceptions.DBException;
import Server.DB.exceptions.NotFoundException;
import Server.DB.exceptions.NotUniqueException;
import Server.DB.exceptions.NullException;
import Server.models.UserDB;
import Server.models.validators.ValidatorException;
import Server.services.ServiceWithDB;
import Server.services.exceptions.ServiceException;
import Server.services.exceptions.UserAlreadyLoginException;

public interface UsersService extends ServiceWithDB {

    void register(UserDB user) throws DBException, ServiceException, NotUniqueException, NullException, ValidatorException;

    void login(UserDB userDB) throws ServiceException, DBException, NullException, NotFoundException, UserAlreadyLoginException;

    void logout(UserDB userDB);

    void update(UserUpdateForm form, UserDB user) throws DBException, ServiceException, NotUniqueException, NotFoundException, NullException, ValidatorException;

    UserProfileData getProfileData(UserDB user);

}
