# Lab2 — ООП-приложение на Java: система диспетчеризации автопарка

## 1) Постановка задачи
Приложение моделирует диспетчеризацию заявок на перевозки в городском автопарке.

### Функции приложения
- загрузка данных автопарка из CSV-файла;
- хранение и подбор транспорта под заявку;
- выбор стратегии построения маршрута (обычный / эко);
- расчёт маршрута и оценка энергозатрат;
- логирование ключевых действий (загрузка данных, назначение транспорта).

### Формат входных/выходных данных
**Вход:**
- CSV-файл `transport_units.csv`:
  - `type,id,capacity,battery_kwh,x,y`
  - `type`: `bus | cargo_van | electric_bus`
- параметры заявки (`DeliveryRequest`):
  - `requestId`, `from`, `to`, `cargoWeightKg`, `passengers`, `priority`.

**Выход:**
- `DispatchResult`:
  - id заявки,
  - id назначенного транспорта,
  - рассчитанный маршрут (`mode`, `distanceKm`, `expectedEnergy`).

### Ограничения данных
- координаты: `[0..1000]`;
- масса груза заявки: `[0..10000]` кг;
- пассажиры заявки: `[0..120]`;
- `TransportUnit.maxLoadKg`: `[0..50000]`;
- `TransportUnit.maxPassengers`: `[0..200]`;
- батарея электробуса: `[50..700]` кВт⋅ч.

## 2) ООП-модель (>= 8 классов)
Классы и интерфейсы проекта:
1. `TransportUnit` (абстрактный базовый класс)
2. `Bus`
3. `CargoVan`
4. `ElectricBus`
5. `DeliveryRequest`
6. `Route`
7. `Location`
8. `Fleet`
9. `DispatchService`
10. `DispatchResult`
11. `CsvTransportDataLoader`
12. `RouteStrategyFactory`
13. `ShortestRouteStrategy`
14. `EcoRouteStrategy`
15. `Priority` (enum)
16. `ElectricPowered` (interface)
17. `GeoTrackable` (interface)

### Принципы ООП
- **Инкапсуляция:** валидация и скрытие полей в `TransportUnit`, `DeliveryRequest`, `Route`, `Location`.
- **Наследование (простое):** `Bus`, `CargoVan` наследуют `TransportUnit`; `ElectricBus` наследует `Bus`.
- **Множественное наследование (через интерфейсы):** `ElectricBus implements ElectricPowered, GeoTrackable`.
- **Полиморфизм:** `estimateEnergyForKm` переопределяется в разных типах транспорта; стратегии маршрута реализуют `RouteStrategy`.
- **Абстракция:** абстрактный `TransportUnit`, интерфейсы `RouteStrategy`, `TransportDataLoader`.

### Связи
- **Ассоциация:** `DispatchService` использует `Fleet` и `RouteStrategyFactory`.
- **Агрегация:** `Fleet` агрегирует коллекцию `TransportUnit`.
- **Композиция (данные результата):** `DispatchResult` содержит `Route`.

## 3) Паттерны проектирования
Использованы 2 совместимых паттерна:
1. **Strategy** (`RouteStrategy`, `ShortestRouteStrategy`, `EcoRouteStrategy`) — выбор алгоритма расчёта маршрута.
2. **Factory Method / Simple Factory** (`RouteStrategyFactory`) — создание нужной стратегии по приоритету заявки.

### Почему эти паттерны подходят
- Диспетчеризация требует сменяемых алгоритмов маршрутизации.
- Фабрика упрощает выбор стратегии и изолирует клиентский код (`DispatchService`) от деталей создания.
- Паттерны естественно сочетаются: фабрика создаёт конкретную стратегию, сервис её применяет.

## 4) Логирование
- Логирование выполняется через `java.util.logging.Logger` в:
  - `CsvTransportDataLoader` (загрузка каждой единицы транспорта),
  - `DispatchService` (назначение транспорта и стратегия).

## 5) Unit-тестирование
Покрыты позитивные/негативные/граничные сценарии:
- `DeliveryRequestTest` — корректность валидации и границ.
- `CsvTransportDataLoaderTest` — загрузка корректного CSV и ошибка при неизвестном типе.
- `DispatchServiceTest` — выбор стратегии ECO/STANDARD, ошибка при невозможности назначения, граница по пассажирам.

### Запуск
```bash
cd lab2
mvn test
```

### Запуск демо
```bash
cd lab2
mvn -q -DskipTests package
java -cp target/lab2-1.0-SNAPSHOT.jar com.example.lab2.App src/main/resources/transport_units.csv
```
