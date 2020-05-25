package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.GenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Optional;

@Controller
public class LandingController extends GenericController {
    @Autowired
    StaffService staffService;

    @Autowired
    StaffSpecialtyService specialtyService;

    @Autowired
    LocalityService localityService;

    @RequestMapping("/")
    public ModelAndView landingPage() {
        final ModelAndView mav = new ModelAndView("landing");
        Optional<User> user = getUser();
        if (user.isPresent()) {
            if (!user.get().getVerified()) {
                return new ModelAndView("redirect:/verifyEmail");
            } else if (this.isStaff()) {
                return new ModelAndView("redirect:/staff/home");
            }
        }
        Collection<StaffSpecialty> specialtiesList = this.specialtyService.list();
        Collection<Locality> localitiesList = this.localityService.list();

        // pass objects to model and view
        mav.addObject("user", user);
        if (user.isPresent() && isStaff()) {
            mav.addObject("staffs", staffService.findByUser(user.get()));
        }
        mav.addObject("specialties", specialtiesList);
        mav.addObject("localities", localitiesList);
        return mav;
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        Optional<User> userOptional = getUser();
        if (!userOptional.isPresent()) {
            return new ModelAndView("redirect:/");
        }
        if (!userOptional.get().getVerified()) {
            return new ModelAndView("redirect:/verifyEmail");
        }
        if (isStaff()) {
            return new ModelAndView("redirect:/staff/home");
        } else {
            return new ModelAndView("redirect:/patient/home");
        }
    }
}
