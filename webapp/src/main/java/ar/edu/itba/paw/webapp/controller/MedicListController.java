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

import java.util.ArrayList;
import java.util.Collection;


@Controller
public class MedicListController {

    @Autowired
    StaffService staffService;

    @Autowired
    StaffSpecialtyService specialityService;

    @RequestMapping(value = "/")
    public ModelAndView medicsList(@RequestParam(value = "name",required = false)String name,@RequestParam(value = "specialties",required = false) String specialties){
        //get modelandview from index.jsp
        final ModelAndView mav = new ModelAndView("index");
        //staff variable that will be passed to the jsp
        Collection<Staff> staffList;
        //check if specialties parameter was set
        if(specialties!=null){
            // create a list that will be passed to search
            ArrayList<StaffSpecialty> searchedSpecialties = new ArrayList<>();
            // split strings to get all specialties used in search
            // and create the search parameter
            for(String s:specialties.split(",")){
                try{
                    StaffSpecialty specialty = new StaffSpecialty();
                    specialty.setId(Integer.parseInt(s));
                    searchedSpecialties.add(specialty);
                }catch (NumberFormatException e){
                }
            }
            //if name is not a parameter search only specialties else search for both
            if(name!=null){
                staffList = this.staffService.findByNameAndStaffSpecialties(name,searchedSpecialties);
            }else{
                staffList = this.staffService.findByStaffSpecialties(searchedSpecialties);
            }
        }
        else{
            if(name != null){
                staffList = this.staffService.findByName(name);
            }else{
                staffList = this.staffService.list();
            }
        }

        // make sure jsp doesnt receive null, instead receive empty list
        if(staffList == null){
            staffList = new ArrayList<>(0);
        }

        //get all specialties and make sure its at least an empty list
        Collection<StaffSpecialty> specialtiesList = this.specialityService.list();
        if(specialtiesList == null){
            staffList = new ArrayList<>(0);
        }

        // pass objects to model and view
        mav.addObject("staff", staffList);
        mav.addObject("specialties",specialtiesList);

        return mav;
    }
}