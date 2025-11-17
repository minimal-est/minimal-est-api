package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Notification", description = "사용자 알림 관련 API")
public class NotificationController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "내 알림 조회", description = "현재 사용자의 알림 목록을 조회합니다. (인증 필수)")
    public ResponseEntity<?> findUserNotifications(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {

        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 표시합니다. (인증 필수)")
    public ResponseEntity<?> markAsRead(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ) {
        return null;
    }
}
