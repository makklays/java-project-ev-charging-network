package com.techmatrix18.evse_point.application.command;

import java.util.Objects;

/**
 * PlugInCableCommand
 * Телеметрическая команда (из IoT/Kafka), фиксирующая подключение зарядного кабеля к электромобилю
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 27.08.2026
 */

public record PlugInCableCommand(
        Long evseId
) {
    public PlugInCableCommand {
        Objects.requireNonNull(evseId, "EVSE ID is required to register a cable plug-in event");
    }
}

