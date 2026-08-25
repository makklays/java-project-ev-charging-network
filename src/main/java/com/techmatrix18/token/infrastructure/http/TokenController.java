package com.techmatrix18.token.infrastructure.http;

import com.techmatrix18.token.application.command.IssueTokenCommand;
import com.techmatrix18.token.application.command.RefreshTokenCommand;
import com.techmatrix18.token.application.command.RevokeTokenCommand;
import com.techmatrix18.token.application.port.in.IssueTokenUseCase;
import com.techmatrix18.token.application.port.in.RefreshTokenUseCase;
import com.techmatrix18.token.application.port.in.RevokeTokenUseCase;
import com.techmatrix18.token.domain.Token;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TokenController
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

@RestController
@RequestMapping("/api/v1/auth")
public class TokenController {

    private final IssueTokenUseCase issueTokenUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RevokeTokenUseCase revokeTokenUseCase;

    // Spring автоматически внедрит три раздельных сервиса через их интерфейсы-порты
    public TokenController(IssueTokenUseCase issueTokenUseCase,
                           RefreshTokenUseCase refreshTokenUseCase,
                           RevokeTokenUseCase revokeTokenUseCase) {
        this.issueTokenUseCase = issueTokenUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.revokeTokenUseCase = revokeTokenUseCase;
    }

    /**
     * POST /api/v1/auth/tokens (Вход / Создание сессии)
     */
    @PostMapping("/tokens")
    public ResponseEntity<TokenResponse> issueTokens(@Valid @RequestBody IssueTokenRequest request) {
        // Сюда код зайдет ТОЛЬКО если все аннотации в request успешны.
        // Клиент гарантированно получит 400 Bad Request, если данные неверны.
        IssueTokenCommand command = request.toCommand();

        Token token = issueTokenUseCase.issueTokens(command);
        return new ResponseEntity<>(TokenResponse.fromDomain(token), HttpStatus.CREATED);
    }

    /**
     * POST /api/v1/auth/refresh (Ротация токенов)
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenCommand command = request.toCommand();
        Token token = refreshTokenUseCase.refreshTokens(command);
        return ResponseEntity.ok(TokenResponse.fromDomain(token));
    }

    /**
     * POST /api/v1/auth/logout (Разлогин / Отзыв токена)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@Valid @RequestBody RevokeTokenRequest request) {
        RevokeTokenCommand command = request.toCommand();
        revokeTokenUseCase.revokeToken(command);
        return ResponseEntity.noContent().build(); // Возвращаем HTTP 204 No Content
    }
}

