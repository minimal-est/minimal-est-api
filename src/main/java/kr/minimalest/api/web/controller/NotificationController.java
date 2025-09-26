package kr.minimalest.api.web.controller;

import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> findUserNotifications(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {

        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return null;
    }
}
