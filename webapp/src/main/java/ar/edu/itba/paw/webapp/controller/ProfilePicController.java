package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequestMapping("/profilePics")
public class ProfilePicController extends GenericController {

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
            updatedUser.setPicture(pic.getBytes());
            this.userService.update(updatedUser);
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
        byte[] pic= user.get().getPicture();
        ResponseEntity<byte[]> res;
        HttpHeaders headers = new HttpHeaders();
        //if there is a pic return the
        if(pic!=null){
            headers.add("content-type",this.acceptedImageType);
            res = new ResponseEntity<byte[]>(pic,headers,HttpStatus.OK);
        }
        //else return a common pic
        else{
            try{
                InputStream in = context.getResourceAsStream(this.defaultImagePath);
                headers.add("content-type",this.defaultImageType);
                byte[] bytepic = IOUtils.toByteArray(in);
                res = new ResponseEntity<byte[]>(bytepic,headers,HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return res;
    }

    //receives UserId returns profile pic
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfilePic(){
        ResponseEntity<byte[]> res;
        HttpHeaders headers = new HttpHeaders();
        //if there is a pic return the
        try{
            InputStream in = context.getResourceAsStream(this.defaultImagePath);
            headers.add("content-type",this.defaultImageType);
            byte[] bytepic = IOUtils.toByteArray(in);
            res = new ResponseEntity<byte[]>(bytepic,headers,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }
}
