package controllers;

/**
 * Created by stain on 11/6/2015.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import play.api.mvc.Request;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import play.data.*;
import util.APICall;
import util.Constants;
import util.APICall.ResponseType;
import play.Logger;
import models.User;

abstract class FilePath {
  abstract String generatePath(Http.MultipartFormData.FilePart image);
}
//concrete class to create coresponding avatar file path
class Avatar extends FilePath {
    String generatePath(Http.MultipartFormData.FilePart image) {
        String fileName = image.getFilename();
        String contentType = image.getContentType();
        java.io.File file = image.getFile();
        String ext = FilenameUtils.getExtension(fileName);
        String imgPathToSave = "public/images/" + "image_" + UUID.randomUUID() + "." + ext;
        boolean success = new File("images").mkdirs();
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            FileUtils.writeByteArrayToFile(new File(imgPathToSave), bytes);
            imgPathToSave = "/" + imgPathToSave;
        } catch (IOException e) {
            imgPathToSave = "/public/images/service.jpeg";
        }
        return imgPathToSave;
    }
}

//concrete class to create default avatarfilepath
class DefaultAvatar extends FilePath  {
    String generatePath(Http.MultipartFormData.FilePart image) {
        String imgPathToSave = "/public/images/service.jpeg";
        return imgPathToSave;
    }
}

//factory class
class AvatarFactory {
    FilePath getAvatar(Http.MultipartFormData.FilePart image) {
        if (image != null) {
            return new Avatar();
        } else {
            return new DefaultAvatar();
        }
    }
}

public class SignupController  extends Controller {
    final static Form<User> f_user = Form.form(User.class);

    public static Result signUp() {
        return ok(signup.render());
    }

    public static Result register() {
        Form<User> form = f_user.bindFromRequest();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("avatar");
        String imgPathToSave = "";
        //call factory
        AvatarFactory avatarFactory = new AvatarFactory();
        FilePath filepath = avatarFactory.getAvatar(image);
        imgPathToSave = filepath.generatePath(image);
        imgPathToSave = imgPathToSave.replaceFirst("public", "assets");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jnode = mapper.createObjectNode();
        jnode.put("email", form.field("email").value());
        jnode.put("password", form.field("password").value());
        jnode.put("username", form.field("username").value());
        jnode.put("avatar", imgPathToSave);
        JsonNode usernode = User.register(jnode);
        if (usernode == null || usernode.has("error")) {
            Logger.debug("Register Failed!");
            flash("error", "Register Failed! Please check your information");
            return redirect(routes.SignupController.signUp());
        }
        Logger.debug("New user created");
        flash("success", "New user created! Please log in.");
        return redirect(routes.Application.login());
    }



}
