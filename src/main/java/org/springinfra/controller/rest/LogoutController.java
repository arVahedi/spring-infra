package org.springinfra.controller.rest;

import examples.model.dto.view.TokenAuthenticationView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springinfra.assets.ResponseTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(BaseRestController.API_PATH_PREFIX_V1 + "/logout")
@Slf4j
@Tag(name = "Logout API", description = "Logout API")
@RequiredArgsConstructor
@Valid
public class LogoutController extends BaseRestController {

    private final ClearSiteDataHeaderWriter clearSiteDataWriter =
            new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL);

    private final List<LogoutHandler> logoutHandlers;

    @Operation(summary = "Logout user", description = "Logout an authenticated user")
    @GetMapping
//    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> authenticate(HttpServletRequest request,
                                                                                  HttpServletResponse response,
                                                                                  Authentication authentication) {

        // 1) Clear browser data (cookies, cache, storage) via response header
        this.clearSiteDataWriter.writeHeaders(request, response);

        // 2) Invalidate session if present (no-op in stateless setups)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 3) Clear authentication for this request thread
        SecurityContextHolder.clearContext();

        // 4) Optional: delegate to your identity provider module
        // Replace this with whatever "logout customization" actually means in your project:
        // - revoke refresh token
        // - blacklist access token
        // - trigger RP-initiated logout redirect URL building, etc.
        this.logoutHandlers.forEach(logoutHandler -> logoutHandler.logout(request, response, authentication));

        // 5) Equivalent of logoutSuccessUrl("/")
        // For an API, 204 is usually better; but you asked to keep redirect.
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }
}
