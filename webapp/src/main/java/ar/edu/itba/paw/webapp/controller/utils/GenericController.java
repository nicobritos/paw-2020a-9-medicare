package ar.edu.itba.paw.webapp.controller.utils;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.utils.JsonResponse.JsonResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class GenericController {
    private final static String GENERIC_ERROR_MESSAGE = "Un error ha ocurrido en nuestro servidor. Intente mas tarde.";

    @Autowired
    private UserService userService;

    protected Optional<User> getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            return this.userService.findByUsername(((org.springframework.security.core.userdetails.User) principal).getUsername());
        }
        return Optional.empty();
    }

    protected JsonResponse formatJsonResponse(Supplier<Object> supplier) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            jsonResponse.setData(supplier.get());
        } catch (MediCareException e) {
            jsonResponse.setStatus(new JsonResponseStatus(e.getMessages()));
        } catch (Exception e) {
            jsonResponse.setStatus(new JsonResponseStatus(GENERIC_ERROR_MESSAGE));
        }
        return jsonResponse;
    }

    protected List<String> getErrorMessages(List<ObjectError> objectErrors) {
        return objectErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
    }
}
