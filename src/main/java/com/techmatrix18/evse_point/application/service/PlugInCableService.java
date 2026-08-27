package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.PlugInCableCommand;
import com.techmatrix18.evse_point.application.port.in.PlugInCableUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PlugInCableService
 * Реализация бизнес-логики обработки события подключения кабеля (перевод в статус PREPARING)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class PlugInCableService implements PlugInCableUseCase {

    private final EvsePointRepository evseRepository;

    public PlugInCableService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional // Гарантирует атомарность изменения статуса оборудования и контроль Optimistic Locking
    public void plugInCable(PlugInCableCommand command) {
        // 1. Извлекаем доменную модель зарядной точки (EVSE) из репозитория через выходной порт
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // 2. Делегируем изменение состояния внутренней бизнес-логике Rich Model
        // Метод переведет статус в EvseStatus.PREPARING и зафиксирует время updatedAt
        evse.prepare();

        // 3. Сохраняем обновленный объект обратно в базу данных
        evseRepository.save(evse);
    }
}

