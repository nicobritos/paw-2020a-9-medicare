package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.models.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;


@Controller
public class HelloWorldController {

    @Autowired
    StaffService staffService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        Collection<Staff> staffList = this.staffService.list();
        if(staffList == null){
            staffList=new ArrayList<>(0);
        }
        mav.addObject("staff", staffList);
        return mav;
    }
}