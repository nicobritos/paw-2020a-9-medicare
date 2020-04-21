package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.LocalityService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.StaffSpecialtyService;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.StaffSpecialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class MedicListController {
    @Autowired
    StaffService staffService;

    @Autowired
    StaffSpecialtyService specialityService;

    @Autowired
    LocalityService localityService;

    @RequestMapping(value = "/mediclist")
    public ModelAndView medicsList(@RequestParam(value = "name",required = false)String name, @RequestParam(value = "specialties",required = false) String specialties, @RequestParam(value = "localities",required = false) String localities){
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

        Set<Locality> searchedLocalities = new HashSet<>();

        if (localities != null) {
            // split strings to get all specialties used in search
            // and create the search parameter
            for (String s : localities.split(",")) {
                try {
                    Locality locality = new Locality();
                    locality.setId(Integer.parseInt(s));
                    searchedLocalities.add(locality);
                } catch (NumberFormatException e) {
                }
            }
        }

        if(name != null) {
            String[] words = name.split(" ");
            staffList = new HashSet<>();
            for (String word : words) {
                staffList.addAll(this.staffService.findBy(word, null, null, searchedSpecialties, searchedLocalities));
                staffList.addAll(this.staffService.findBy(null, word, null, searchedSpecialties, searchedLocalities));
            }
        } else{
            staffList = this.staffService.findBy(null, null, null, searchedSpecialties, searchedLocalities);
        }

        Collection<StaffSpecialty> specialtiesList = this.specialityService.list();
        Collection<Locality> localitiesList = this.localityService.list();

        // pass objects to model and view
        mav.addObject("staff", staffList);
        mav.addObject("specialties",specialtiesList);
        mav.addObject("localities",localitiesList);
        mav.addObject("name", name);
        mav.addObject("selSpeciality", specialties);
        mav.addObject("selLocality", localities);


        return mav;
    }
}