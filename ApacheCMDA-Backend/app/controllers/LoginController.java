package controllers;

import play.mvc.Result;

/**
 * Created by dachengwen on 4/24/16.
 */
public class LoginController {
    protected LoginAPI loginAPI;

    //cannot access the default constructor!
    private LoginController() {

    }

    protected LoginController(LoginAPI loginAPI) {
        this.loginAPI = loginAPI;
    }
    public Result login(String username, String password) {
        return loginAPI.login(username, password);
    }
}
