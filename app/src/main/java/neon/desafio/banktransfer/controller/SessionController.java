package neon.desafio.banktransfer.controller;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import neon.desafio.banktransfer.model.UserVO;

@EBean(scope = EBean.Scope.Singleton)
public class SessionController {

    private UserVO loggedUser;
    private String sessionToken;

    @AfterInject
    public void initUser() {
        loggedUser = new UserVO("1", "Pedro Pereira", "pedroaugustop10@gmail.com", "11 91111-1111");
    }

    public UserVO getLoggedUser() {
        return loggedUser;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
