package com.techmatrix18.token.application.port.in;

import com.techmatrix18.token.application.command.IssueTokenCommand;
import com.techmatrix18.token.domain.Token;

/**
 * Входящий порт (Use Case) исключительно для сценария выпуска токенов.
 * Выполняет единственную бизнес-задачу (Single Responsibility).
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 23.08.2026
 */

public interface IssueTokenUseCase {

    // Создает и сохраняет новую сессию безопасности для авторизованного пользователя.
    Token issueTokens(IssueTokenCommand command);
}

