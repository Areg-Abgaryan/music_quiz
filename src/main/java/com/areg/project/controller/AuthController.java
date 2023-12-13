/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.controller;

import com.areg.project.EndpointConstants;
import com.areg.project.manager.AuthManager;
import com.areg.project.manager.UserManager;
import com.areg.project.model.dto.RefreshTokenRequestDTO;
import com.areg.project.model.dto.RefreshTokenResponseDTO;
import com.areg.project.model.dto.TokenDTO;
import com.areg.project.model.dto.UserDTO;
import com.areg.project.model.dto.UserLoginRequestDTO;
import com.areg.project.model.dto.UserLoginResponseDTO;
import com.areg.project.model.entity.RefreshTokenEntity;
import com.areg.project.model.entity.UserEntity;
import com.areg.project.repository.RefreshTokenRepository;
import com.areg.project.security.jwt.JwtProvider;
import com.areg.project.service.jpa.UserService;
import org.apache.commons.lang3.StringUtils;
import io.jsonwebtoken.JwtException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import com.areg.project.logging.QuizLogLevel;
import com.areg.project.logging.QuizLogMachine;
import com.areg.project.security.shiro.ShiroConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InvalidObjectException;
import java.util.Optional;

//  FIXME !! Test all this functionality , REST, db creation, all the flow
@RestController
@RequestMapping(EndpointConstants.API)
public class AuthController {

    private final QuizLogMachine logMachine = new QuizLogMachine(AuthController.class);
    private final JwtProvider jwtProvider;
    private final UserManager userManager;
    private final UserService userService;
    private final AuthManager authManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthController(JwtProvider jwtProvider, UserManager userManager, UserService userService,
                          AuthManager authManager, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProvider = jwtProvider;
        this.userManager = userManager;
        this.userService = userService;
        this.authManager = authManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping(EndpointConstants.SIGNUP)
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        final Subject currentUser = SecurityUtils.getSubject();
        logMachine.log(QuizLogLevel.INFO, "Successfully signed up during the session : "
                + ShiroConfig.getSessionId(currentUser));
        return ResponseEntity.ok(userManager.signUp(userDTO));
    }

    @PostMapping(EndpointConstants.LOGIN)
    @ResponseBody
    //  FIXME !! Refactor this
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO loginDto) {
        try {
            final String username = loginDto.getUsername();
            final String password = loginDto.getPassword();

            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                throw new InvalidObjectException("Invalid request arguments");
            }

            final var token = new UsernamePasswordToken(username, password);
            final Subject currentUser = SecurityUtils.getSubject();

            final UserDTO userDTO = userManager.findUserByUsername(username);
            currentUser.login(token);

            // Generate JWT token
            final var jwtToken = new TokenDTO(jwtProvider.createToken(username));

            //  Generate refresh token
            final RefreshTokenResponseDTO refreshToken = authManager.createRefreshToken(userDTO);

            final var loginOutputDTO = new UserLoginResponseDTO(
                    userDTO.getFirstName(), userDTO.getLastName(), jwtToken, refreshToken);

            logMachine.log(QuizLogLevel.INFO, "Successfully logged in during the session : "
                    + ShiroConfig.getSessionId(currentUser));
            return ResponseEntity.ok(loginOutputDTO);

        } catch (org.springframework.security.core.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username provided");
        } catch (org.apache.shiro.authc.AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid password provided");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }

    @PostMapping(EndpointConstants.LOGOUT)
    @ResponseBody
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization") String jwtToken) {
        try {
            if (jwtProvider.isTokenValid(jwtToken)) {
                final Subject currentUser = SecurityUtils.getSubject();
                currentUser.logout();
                logMachine.log(QuizLogLevel.INFO, "Successfully logged out during the session : "
                        + ShiroConfig.getSessionId(currentUser));
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //  When JWT is expired, we call this.
    //  Will refresh the JWT token via refresh token (if it's valid)
    @PostMapping(EndpointConstants.REFRESH_TOKEN)
    //  FIXME !! Refactor this
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        final UserEntity userEntity = userService.findUserById(refreshTokenRequestDTO.getUserExternalId());
        final Optional<RefreshTokenEntity> token = refreshTokenRepository.
                findByUserEntityAndRefreshToken(userEntity, refreshTokenRequestDTO.getRefreshToken());

        //  Has neither JWT token, nor refresh token (expired)
        if (token.isEmpty() || authManager.verifyRefreshTokenExpiration(token.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in !");
        }

        final UserEntity user = token.get().getUserEntity();

        //  Generate new JWT Token
        final var jwtToken = new TokenDTO(jwtProvider.createToken(user.getUsername()));
        return ResponseEntity.ok(jwtToken);
    }
}
