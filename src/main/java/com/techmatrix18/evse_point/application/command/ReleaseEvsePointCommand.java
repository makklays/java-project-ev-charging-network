package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * ReleaseEvsePointCommand
 * Телеметрическая команда, фиксирующая отключение кабеля от машины и освобождение порта
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record ReleaseEvsePointCommand(
        Long evseId
) {
    public ReleaseEvsePointCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required to release the charging point");
    }
}

