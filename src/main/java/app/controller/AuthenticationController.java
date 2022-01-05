package app.controller;

import app.security.jwt.JwtAuthUtil;
import app.security.model.AuthenticationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Api(value = "Authentication Controller", description = "authentication with json web token")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtAuthUtil tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtAuthUtil jwtAuthUtil) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = jwtAuthUtil;
    }

    @ApiOperation(value = "return json web token", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 400, message = "client error"),
            @ApiResponse(code = 500, message = "server error"),
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticationUserAndGetJwt(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));;

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateJwt(authentication);
        if (token != null || !token.isEmpty()) {
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
