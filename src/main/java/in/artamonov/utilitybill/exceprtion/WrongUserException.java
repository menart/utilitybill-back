package in.artamonov.utilitybill.exceprtion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class WrongUserException extends AuthException {
    public WrongUserException() {
        super("wrong user name or password", "WRONG_USER_AUTHENTICATION");
    }
}
