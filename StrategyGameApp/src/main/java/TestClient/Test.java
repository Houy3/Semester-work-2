package TestClient;

import Protocol.HighLevelMessageManager;
import Protocol.Message;
import Protocol.MessageManager;
import Protocol.MessageValues.User.UserLoginForm;
import Protocol.MessageValues.User.UserRegistrationForm;
import Protocol.exceptions.BadResponseException;
import Protocol.exceptions.MismatchedClassException;

import java.io.IOException;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws BadResponseException {


        try {
            HighLevelMessageManager messageManager = new HighLevelMessageManager();

            Socket socket =  new Socket("localhost", 8888);

            System.out.println(Message.class);


            UserRegistrationForm userRegistrationForm  = new UserRegistrationForm(
                    "naursdsduz@gmail.com",
                    "nauruz0304",
                    "asdasdasd"
            );

            Message register = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_REGISTER, userRegistrationForm),
                    socket
                    );


            UserLoginForm user = new UserLoginForm("ompopjfpjpo@mail.ru", "987h98u98g98g9");

            Message message = MessageManager.sendMessage(
                    new Message(MessageManager.MessageType.USER_LOGIN, user),
                    socket
            );
            MessageManager.sendMessage(register, socket);
            MessageManager.sendMessage(message, socket);

            System.out.println(MessageManager.sendMessage(register, socket));

            System.out.println(register.type());
         //   System.out.println(MessageManager.readMessage(socket.getInputStream()));


        } catch (IOException | MismatchedClassException e) {
            System.out.println(e.getMessage());
        }

    }
}
