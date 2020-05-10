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
public class ProfilePicController extends GenericController {
    @Autowired
    private PictureService pictureService;
    @Autowired
    UserService userService;
    @Autowired
    ServletContext context;

    //TODO:correct path

    private String defaultImagePath = "/img/defaultProfilePic.svg";
    private String defaultImageType = "image/svg+xml";
    private String acceptedImageType = "image/jpeg";

    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public ResponseEntity<String> setProfilePic(@RequestParam MultipartFile pic, HttpServletRequest req){
        //get current user
        Optional<User> user = getUser();
        if(!user.isPresent()) {
            //TODO:revise status code
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //check its a valid pic
        if(pic==null||!pic.getContentType().equals(this.acceptedImageType)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //update user
        User updatedUser = user.get();
        try{
            Picture picture = new Picture();
            picture.setData(pic.getBytes());
            picture.setName(pic.getOriginalFilename());
            picture.setSize(pic.getSize());
            picture.setMimeType(pic.getContentType());
            this.userService.setProfile(updatedUser, picture);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //receives UserId returns profile pic
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfilePic(@PathVariable("id") Integer id){
        //get user
        Optional<User> user  = this.userService.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //get user picture
        ResponseEntity<byte[]> res;
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type",this.acceptedImageType);
        //if there is a pic return the
        byte[] pic;
        if (user.get().getProfileId() != null) {
            Optional<Picture> picture = this.pictureService.findById(user.get().getProfileId());
            pic = picture.map(Picture::getData).orElse(null);
        } else {
            pic = null;
        }

        if (pic != null) {
            res = new ResponseEntity<>(pic, headers, HttpStatus.OK);
        }
        //else return a common pic
        else{
            try{
                InputStream in = context.getResourceAsStream(this.defaultImagePath);
                headers.add("content-type",this.defaultImageType);
                res = new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return res;
    }
}
