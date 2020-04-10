package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.models.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class HelloWorldController {
    @Autowired
    OfficeDao officeDao;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        Collection<Office> offices = officeDao.list();
        for (Office office : offices) {
            System.out.println(office);
        }

        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", "MediCare");
        return mav;
    }
}