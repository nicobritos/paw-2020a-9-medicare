package ar.edu.itba.paw.webapp.controller.utils;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class GenericController {
    private final static String GENERIC_ERROR_MESSAGE = "Un error ha ocurrido en nuestro servidor. Intente mas tarde.";
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;

    protected boolean isStaff() {
        return this.getUser().filter(value -> this.userService.isStaff(value)).isPresent();
    }

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

    protected JsonResponse formatJsonResponse(BindingResult errors, Supplier<Object> supplier) {
        JsonResponse jsonResponse = new JsonResponse();
        if (errors.hasErrors()) {
            Collection<String> messages = new LinkedList<>();
            for (ObjectError objectError : errors.getAllErrors()) {
                String message = null;
                for (String code : objectError.getCodes()) {
                    message = this.messageSource.getMessage(code, null, null, LocaleContextHolder.getLocale());
                    if (message != null) break;
                }
                if (message == null)
                    message = objectError.getDefaultMessage();
                messages.add(message);
            }
            jsonResponse.setStatus(new JsonResponseStatus(messages));
            return jsonResponse;
        }

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
