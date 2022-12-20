package Server.DB.models.encryptors;

public interface Encryptor<T> {

    String encrypt(T object);

}
