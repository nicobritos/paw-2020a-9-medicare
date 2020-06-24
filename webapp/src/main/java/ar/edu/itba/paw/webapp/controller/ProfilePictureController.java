package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.PictureService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequestMapping("/profilePics")
public class ProfilePictureController extends GenericController {
    @Autowired
    private PictureService pictureService;
    @Autowired
    UserService userService;
    @Autowired
    ServletContext context;

    private final String defaultImagePath = "/img/defaultProfilePic.svg";
    private final String defaultImageType = "image/svg+xml";

    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<String> setProfilePic(@RequestParam MultipartFile pic, HttpServletRequest req) {
        //get current user
        Optional<User> user = this.getUser();
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //check its a valid pic
        if (pic == null || !pic.getContentType().contains("image")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //update user
        User updatedUser = user.get();
        try {
            Picture picture = new Picture();
            picture.setData(pic.getBytes());
            picture.setName(pic.getOriginalFilename());
            picture.setSize(pic.getSize());
            picture.setMimeType(pic.getContentType());
            this.userService.setProfile(updatedUser, picture);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //receives pictureId returns profile pic
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfilePic(@PathVariable("id") Integer id) {
        Optional<Picture> optPic = this.pictureService.findById(id);
        ResponseEntity<byte[]> res;
        HttpHeaders headers = new HttpHeaders();
        byte[] pic = null;
        if (optPic.isPresent()) {
            pic = optPic.get().getData();
        }
        if (pic != null) {
            headers.add("content-type", optPic.get().getMimeType());
            res = new ResponseEntity<>(pic, headers, HttpStatus.OK);
        }
        //else return a common pic
        else {
            try {
                InputStream in = this.context.getResourceAsStream(this.defaultImagePath);
                headers.add("content-type", this.defaultImageType);
                res = new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return res;
    }

    //returns default image
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfilePic() {
        ResponseEntity<byte[]> res;
        HttpHeaders headers = new HttpHeaders();
        //if there is a pic return the
        try {
            InputStream in = this.context.getResourceAsStream(this.defaultImagePath);
            headers.add("content-type", this.defaultImageType);
            byte[] bytepic = IOUtils.toByteArray(in);
            res = new ResponseEntity<byte[]>(bytepic, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }
}
