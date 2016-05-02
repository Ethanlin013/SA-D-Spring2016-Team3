package controllers;

import play.mvc.Result;

/**
 * Created by dachengwen on 4/24/16.
 */
public interface LoginAPI {
    public Result login(String username, String password);
}
