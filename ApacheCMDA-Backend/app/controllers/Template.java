package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import util.Common;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;


@Named
@Singleton
public abstract class Template extends Controller {
     abstract Result initialize(JsonNode jsonNode);
     abstract JsonNode getJson();
     public final Result addItem() {
        return initialize(getJson());
     }

}