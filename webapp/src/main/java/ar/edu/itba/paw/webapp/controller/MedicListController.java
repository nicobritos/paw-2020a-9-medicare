package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Controller
public class MedicListController {
    @Autowired
    StaffService staffService;

    @Autowired
    StaffSpecialtyService specialityService;

    @RequestMapping(value = "/mediclist")
    public ModelAndView medicsList(@RequestParam(value = "name",required = false)String name, @RequestParam(value = "surname",required = false)String surname, @RequestParam(value = "specialties",required = false) String specialties){
        //get modelandview from index.jsp
        final ModelAndView mav = new ModelAndView("index");
        //staff variable that will be passed to the jsp
        Collection<Staff> staffList;

        Set<StaffSpecialty> searchedSpecialties = new HashSet<>();

        if (specialties != null) {
            // split strings to get all specialties used in search
            // and create the search parameter
            for (String s : specialties.split(",")) {
                try {
                    StaffSpecialty specialty = new StaffSpecialty();
                    specialty.setId(Integer.parseInt(s));
                    searchedSpecialties.add(specialty);
                } catch (NumberFormatException e) {
                }
            }
        }
        staffList = this.staffService.findBy(name, surname, null, searchedSpecialties);
        Collection<StaffSpecialty> specialtiesList = this.specialityService.list();

        // pass objects to model and view
        mav.addObject("staff", staffList);
        mav.addObject("specialties",specialtiesList);

        return mav;
    }
}