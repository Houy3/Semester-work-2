package Server.DB.models.validators;

public interface Validator<T> {


    void check(T object) throws IllegalArgumentException, ValidatorException;
}
