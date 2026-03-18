package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.presentation.dto.JwtTokenDTO;
import com.padel.padel_global_score.presentation.dto.LoginDTO;
import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.security.CustomUserDetails;
import com.padel.padel_global_score.security.jwt.JwtFilter;
import com.padel.padel_global_score.security.jwt.TokenProvider;
import com.padel.padel_global_score.service.GrupoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final GrupoService grupoService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> authorize(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.username(),
                loginDTO.password()
        );
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = tokenProvider.createToken(authentication);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return ResponseEntity.status(200)
                .headers(httpHeaders)
                .body(new SuccessResponse(200, new JwtTokenDTO(jwt)));
    }


    @GetMapping("validate")
    public ResponseEntity<SuccessResponse> validateToken(@CookieValue(name = "token", required = false) String token) {
        boolean isValid = token != null && tokenProvider.validateToken(token);
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, isValid));
    }

    @GetMapping("/me/groups")
    public ResponseEntity<SuccessResponse> getMyGroups(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, grupoService.getByAdminId(userDetails.getUserId())));
    }

}
