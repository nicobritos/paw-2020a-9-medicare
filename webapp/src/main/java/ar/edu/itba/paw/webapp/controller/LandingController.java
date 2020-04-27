package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class LandingController extends GenericController{
    @Autowired
    StaffService staffService;

    @Autowired
    StaffSpecialtyService specialtyService;

    @Autowired
    LocalityService localityService;

    @RequestMapping("/")
    public ModelAndView landingPage(){
        final ModelAndView mav = new ModelAndView("landing");
        Collection<StaffSpecialty> specialtiesList = this.specialtyService.list();
        Collection<Locality> localitiesList = this.localityService.list();

        // pass objects to model and view
        mav.addObject("specialties",specialtiesList);
        mav.addObject("localities",localitiesList);
        return mav;
    }
}
