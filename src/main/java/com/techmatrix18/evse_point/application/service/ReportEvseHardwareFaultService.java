package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.ReportEvseHardwareFaultCommand;
import com.techmatrix18.evse_point.application.port.in.ReportEvseHardwareFaultUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReportEvseHardwareFaultService
 * Реализация бизнес-логики обработки аварийных сигналов оборудования (перевод в статус FAULTED)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class ReportEvseHardwareFaultService implements ReportEvseHardwareFaultUseCase {

    private final EvsePointRepository evseRepository;

    public ReportEvseHardwareFaultService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional // Атомарно изолирует транзакцию изменения статуса и активирует Optimistic Locking
    public void reportFault(ReportEvseHardwareFaultCommand command) {
        // Извлекаем доменную модель зарядной точки (EVSE) через выходной порт
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // Вызываем доменный бизнес-метод Rich Model для перевода в аварийное состояние
        // Меняет статус на EvseStatus.FAULTED и обновляетUpdatedAt
        evse.logFault();

        // Фиксируем изменения в репозитории постоянного хранения
        evseRepository.save(evse);

        // Логирование кода ошибки (command.errorCode()) для мониторинга
        // Здесь вы можете интегрировать отправку алертов технической поддержке через Output Port (например, Telegram, SMS или Slack)
    }
}

