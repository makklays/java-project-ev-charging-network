package com.techmatrix18.evse_point.application.service;

import com.techmatrix18.evse_point.application.command.ReleaseEvsePointCommand;
import com.techmatrix18.evse_point.application.port.in.ReleaseEvsePointUseCase;
import com.techmatrix18.evse_point.application.port.out.EvsePointRepository;
import com.techmatrix18.evse_point.domain.EvsePoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReleaseEvsePointService
 * Реализация бизнес-логики освобождения зарядного порта (перевод в статус AVAILABLE)
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

@Service
public class ReleaseEvsePointService implements ReleaseEvsePointUseCase {

    private final EvsePointRepository evseRepository;

    public ReleaseEvsePointService(EvsePointRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    @Override
    @Transactional // Обеспечивает атомарность операции и инкремент технического поля version для Optimistic Locking
    public void releaseEvsePoint(ReleaseEvsePointCommand command) {
        // Извлекаем доменную модель зарядной точки (EVSE) из репозитория через выходной порт
        EvsePoint evse = evseRepository.findById(command.evseId())
                .orElseThrow(() -> new IllegalArgumentException("EVSE point not found with ID: " + command.evseId()));

        // Делегируем изменение состояния внутренней бизнес-логике Rich Model
        // Метод вернет статус в EvseStatus.AVAILABLE и обновит временную метку updatedAt
        evse.release();

        // Сохраняем обновленный объект в базу данных через выходной порт
        evseRepository.save(evse);
    }
}

