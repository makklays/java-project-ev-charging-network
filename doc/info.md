
# Схема взаимодействия (Runtime Dependency Injection)

```
[ Контроллер: UserController ]
     │
     ▼ вызывает интерфейс
[ Входящий порт: UpdateProfileUseCase ]
     │
     ▼ реализован классом
[ Сервис: UpdateProfileService ]
     │
     ▼ вызывает доменный интерфейс (порт)
[ Порт БД: UserRepositoryPort ]
     │
============ Скрытая магия Spring (DI) =================
     │ В рантайме Spring подставляет сюда реальный бин:
     ▼
[ Адаптер: UserRepositoryAdapter ] (Именно здесь он работает!)
     │
     ▼ вызывает JPA инструмент
[ JpaUserRepository ] ➡️ [ СУБД PostgreSQL ]
```

