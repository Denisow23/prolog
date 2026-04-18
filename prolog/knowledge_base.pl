% ==============================================
% 1) ДЕРЕВО СЕМЕЙНЫХ ОТНОШЕНИЙ (глубина >= 8)
% ==============================================

% gender
male(ilya).
male(pavel).
male(aleksei).
male(dmitriy).
male(nikolay).
male(sergey).
male(viktor).
male(andrey).
male(roman).
male(denis).
male(egor).

female(maria).
female(elena).
female(olga).
female(irina).
female(tatiana).
female(anna).
female(sofia).
female(lidia).
female(natalia).
female(karina).
female(yana).

% parent(Родитель, Ребенок)
parent(ilya, pavel).
parent(maria, pavel).

parent(pavel, aleksei).
parent(elena, aleksei).

parent(aleksei, dmitriy).
parent(olga, dmitriy).

parent(dmitriy, nikolay).
parent(irina, nikolay).

parent(nikolay, sergey).
parent(tatiana, sergey).

parent(sergey, viktor).
parent(anna, viktor).

parent(viktor, andrey).
parent(sofia, andrey).

parent(andrey, roman).
parent(lidia, roman).

parent(roman, denis).
parent(natalia, denis).

% дополнительные ветви для сиблингов
parent(pavel, karina).
parent(elena, karina).
parent(aleksei, egor).
parent(olga, egor).
parent(andrey, yana).
parent(lidia, yana).

% -------- 10 правил (включая рекурсивные) --------

father(X, Y) :- male(X), parent(X, Y).
mother(X, Y) :- female(X), parent(X, Y).

child(X, Y) :- parent(Y, X).
son(X, Y) :- male(X), child(X, Y).
daughter(X, Y) :- female(X), child(X, Y).

sibling(X, Y) :-
    parent(P, X),
    parent(P, Y),
    X \= Y.

brother(X, Y) :- male(X), sibling(X, Y).
sister(X, Y) :- female(X), sibling(X, Y).

grandparent(X, Y) :- parent(X, Z), parent(Z, Y).

% рекурсивный предок
ancestor(X, Y) :- parent(X, Y).
ancestor(X, Y) :- parent(X, Z), ancestor(Z, Y).

% рекурсивный потомок
descendant(X, Y) :- ancestor(Y, X).

% ==============================================
% 2) СЕМАНТИЧЕСКАЯ СЕТЬ (предметная область: транспорт)
% ширина 3-4, глубина 7-8
% ==============================================

% Иерархия классов (instance_of / is_a)
instance_of(tesla_model3, electric_sedan).
instance_of(volvo_fh16, heavy_truck).
instance_of(yamaha_r1, sport_motorcycle).

is_a(electric_sedan, sedan).
is_a(sedan, passenger_car).
is_a(passenger_car, road_vehicle).
is_a(road_vehicle, land_transport).
is_a(land_transport, transport).
is_a(transport, physical_object).
is_a(physical_object, entity).

is_a(heavy_truck, truck).
is_a(truck, cargo_vehicle).
is_a(cargo_vehicle, road_vehicle).

is_a(sport_motorcycle, motorcycle).
is_a(motorcycle, two_wheeler).
is_a(two_wheeler, road_vehicle).

% свойства классов и экземпляров
property(transport, can_move).
property(land_transport, uses_land_routes).
property(road_vehicle, uses_roads).
property(passenger_car, carries_passengers).
property(cargo_vehicle, carries_cargo).
property(sedan, has_4_doors).
property(electric_sedan, uses_electricity).
property(truck, has_large_load_capacity).
property(motorcycle, has_handlebar_control).
property(sport_motorcycle, has_high_speed).

% пользовательские характеристики экземпляров
property(tesla_model3, has_autopilot).
property(volvo_fh16, has_sleeping_cabin).
property(yamaha_r1, has_racing_mode).

% перекрестные связи (cross-links) между ветками сети
% cross_relation(Класс1, Класс2, ТипСвязи)
cross_relation(electric_sedan, charging_station, uses_infrastructure).
cross_relation(heavy_truck, logistics_hub, serves).
cross_relation(truck, warehouse, serves).
cross_relation(sport_motorcycle, race_track, used_on).
cross_relation(sedan, car_sharing_service, used_in).
cross_relation(cargo_vehicle, passenger_car, alternative_for).

% -------- 9 правил для сети и вывода --------

% экземпляр принадлежит классу напрямую
member_of(Entity, Class) :- instance_of(Entity, Class).

% экземпляр принадлежит суперклассам класса (наследование вверх)
member_of(Entity, SuperClass) :-
    instance_of(Entity, Class),
    subclass_of(Class, SuperClass).

% рекурсивный вывод отношения подкласса
subclass_of(Class, Super) :- is_a(Class, Super).
subclass_of(Class, Super) :- is_a(Class, Mid), subclass_of(Mid, Super).

% наследование свойств для класса
class_property(Class, Property) :- property(Class, Property).
class_property(Class, Property) :-
    subclass_of(Class, Super),
    property(Super, Property).

% итоговое свойство сущности (собственное или унаследованное)
has_property(Entity, Property) :- property(Entity, Property).
has_property(Entity, Property) :-
    member_of(Entity, Class),
    class_property(Class, Property).

% относится ли объект к транспорту
is_transport(Entity) :- member_of(Entity, transport).

% пригодность для дальних поездок (пример логического вывода)
suitable_for_long_trip(Entity) :-
    has_property(Entity, can_move),
    has_property(Entity, uses_land_routes).

% экологичный транспорт (например, электрический)
eco_friendly(Entity) :- has_property(Entity, uses_electricity).

% перекрестная связь для класса с учетом наследования
class_cross_relation(Class, Other, Relation) :- cross_relation(Class, Other, Relation).
class_cross_relation(Class, Other, Relation) :-
    subclass_of(Class, Super),
    cross_relation(Super, Other, Relation).
class_cross_relation(Class, Other, Relation) :-
    subclass_of(Other, SuperOther),
    cross_relation(Class, SuperOther, Relation).

% перекрестная связь для сущности (экземпляра)
entity_cross_relation(Entity, Other, Relation) :-
    member_of(Entity, Class),
    class_cross_relation(Class, Other, Relation).

% симметричный поиск перекрестных связей между двумя сущностями
entities_related(Entity1, Entity2, Relation) :-
    member_of(Entity1, Class1),
    member_of(Entity2, Class2),
    class_cross_relation(Class1, Class2, Relation).
entities_related(Entity1, Entity2, Relation) :-
    member_of(Entity1, Class1),
    member_of(Entity2, Class2),
    class_cross_relation(Class2, Class1, Relation).


% ==============================================
% Примеры запросов
% ==============================================
% Семейное дерево:
% ?- ancestor(ilya, roman).
% ?- descendant(denis, aleksei).
% ?- sibling(aleksei, karina).
% ?- grandparent(pavel, dmitriy).
% ?- brother(egor, dmitriy).
%
% Семантическая сеть:
% ?- has_property(tesla_model3, uses_roads).
% ?- has_property(volvo_fh16, carries_cargo).
% ?- member_of(yamaha_r1, transport).
% ?- suitable_for_long_trip(tesla_model3).
% ?- eco_friendly(tesla_model3).
% ?- entity_cross_relation(tesla_model3, charging_station, uses_infrastructure).
% ?- entity_cross_relation(volvo_fh16, warehouse, serves).
% ?- entities_related(yamaha_r1, tesla_model3, Relation).
