package app.controller;

import app.model.User;
import app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Api(value = "Registration Controller", description = "registration users")
public class RegistrationController {

    private static final String SAVE_CONTROLLER = "app.controller";

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "save user to database", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "client error"),
            @ApiResponse(code = 500, message = "server error"),
    })
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody User user) {
        user = userService.saveUser(user);
        User userFromDatabase = userService.getUserById(user.getUser_id());
        if (userFromDatabase != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
