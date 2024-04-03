# Kotlin entity-component-system

Простая реализация ECS системы для игр, написанных на Kotlin. Легковесная, поддерживает весь базовый функционал ECS-систем.

## Features

### Несколько типов поддерживаемых компонентов

Компоненты, применяемые к сущностям, разделены на два типа компонентов - обычные (EcsComponent) и неповторяемые (SingletonEcsComponent). Контекст гарантирует, что неповторяемые компоненты могут присутствовать лишь в одном экземпляре одновременно.

Неповторяемые компоненты могут быть полезны при необходимости обработки уникальных для контекста сущностей, например, флага игрока или информации о начале игровой сессии.

### Отдельный тип событий

События выделены как отдельный тип внутри контекста - Event. Таким образом, они хранятся отдельно от данных компонентов сущностей, занимают меньший объем памяти и обрабатываются быстрее компонентов.

### Автоматическая синхронизация состояния сущностей и контекста

При изменении сущности, созданной внутри контекста, ее изменения автоматически синхронизируются с контекстом. Например, удалив компонент в объекте сущности, он будет автоматически удален и из контекста.

### Немодифицируемость исполняемых систем

Системы, созданные во время инициализации контекста не могут быть изменены в будущем, во время выполнения контекста.

## Недостающий функционал (TODO-list)

### Вероятные ошибки при синхронизации изменений состояния сущности

Процесс синхронизации сущности не является в полной мере атомарным. При возникновении ошибки на одном из его шагов, уже выполненные шаги не будут отменены. Например, в случае возникновения ошибки во время удаления компонента из сущности, есть шанс на то, что он будет отвязан от сущности, но останется внутри контекста.

### Необходимость разделения интерфейса EcsContext

Интерфейс EcsContext является перегруженным. Его можно заменить на композицию нескольких access-интерфейсов.

## Примеры

### Создание контекста, сущности и компонентов

```kotlin
val context = EcsContextBuilder()
    .appendSystem(InputHandlingSystem()) // Appending some systems.
    .appendSystem(MovingSystem())        // This systems will be executed in append order
    .appendSystem(RenderingSystem())
    // Also you can create custom entity while building context. This entity will be created on
    // EcsContextBuild::build() call
    .appendEntity("render_state_entity") { createdEcsEntity, createdEcsContext ->
        // Any logic with created entity
        createdEcsEntity.appendComponent(RenderingStateComponent())
        createdEcsEntity.appendComponent(ImportantComponent())
    }
    .build()

println(context.getAllEntities().size) // Output: 1. This context already contains render_state_entity
println(context.getComponentTypes().size) // Output: 2. RenderingStateComponent, ImportantComponent

val entity = context.createEntity()
entity.appendComponent(PositionComponent())
entity.appendComponent(HealthComponent())
entity.appendComponent(ImportantComponent())

println(context.getAllEntities().size) // Output: 2. This context contains one new entity.
println(context.getComponentTypes().size) // Output: 2. Position, Health, RenderingState, and Important components.
```
