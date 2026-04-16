# ConnectCore - Social Media REST API
Backend aplikacji inspirowanej serwisami społecznościowymi, umożliwiającej zarządzanie użytkownikami, postami oraz interakcjami (follow, wiadomości, powiadomienia).
Projekt skupia się na dobrych praktykach backendowych, architekturze aplikacji oraz wydajnej pracy z bazą danych.

## Funkcjonalności
- rejestracja użytkowników
- tworzenie, edycja i usuwanie postów
- feed użytkownika (posty obserwowanych osób)
- system follow / unfollow
- wiadomości między użytkownikami
- system powiadomień
- podstawowa obsługa mediów (model + relacje)
- paginacja i sortowanie danych

## Technologie
- Java 21
- Spring Boot
- Spring Data JPA + Hibernate
- Spring Security
- PostgreSQL
- Flyway (migracje bazy danych)
- Swagger / OpenAPI
- JUnit + Mockito
- DTO mapping
- Lombok

## Architektura
Projekt oparty jest o architekturę warstwową:
- controller - obsługa HTTP (REST API)
- service - logika biznesowa
- repository - dostęp do danych
- dto - komunikacja między warstwami
- mapper - mapowanie encji do DTO

Dodatkowo:
- centralna obsługa wyjątków (@RestControllerAdvice)
- walidacja danych (@Valid)
- separacja modeli domenowych i API


## Aspekty techniczne
- eliminacja problemu N+1 (m.in. JOIN FETCH)
- wykorzystanie FetchType LAZY/EAGER
- soft delete (@SQLDelete, @Where)
- optymalizacja zapytań i indeksów w bazie danych
- paginacja (Pageable)
- customowe wyjątki (400, 403, 404, 409)

## Bezpieczeństwo
- Spring Security (UserDetails, role)
- haszowanie haseł (BCrypt)

## Testy
Projekt zawiera testy jednostkowe dla warstwy serwisowej:
- testowanie logiki biznesowej
- mockowanie repozytoriów
- walidacja scenariuszy błędów
Uruchomienie:
mvn test

## Uruchomienie lokalne (bez Dockera)
#### 1. Wymagania
- Java 21
- PostgreSQL
#### 2. Konfiguracja bazy danych
Aplikacja korzysta z PostgreSQL.
Należy utworzyć bazę danych oraz uzupełnić dane dostępowe w pliku application.yml.
Przykładowa konfiguracja:
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/connectcore
    username: your_user
    password: your_password
Można również użyć własnej konfiguracji bazy danych - migracje zostaną wykonane automatycznie przez Flyway.

#### 3. Uruchomienie
mvn spring-boot:run

#### 4. Uruchomienie (Docker)
```bash
docker-compose up --build
```bash



## Status
Projekt w trakcie rozwoju - kolejne funkcjonalności i optymalizacje są w trakcie implementacji.
