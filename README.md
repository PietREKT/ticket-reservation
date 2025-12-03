# Ticket Reservation – backend systemu rezerwacji biletów kolejowych

Spring Bootowy backend do wyszukiwania połączeń kolejowych, zarządzania rozkładami jazdy oraz sprzedaży biletów z rezerwacją miejsc.  
Projekt jest oparty o **PostgreSQL + PostGIS**, **JWT** oraz **Java 21**.

---

## Struktura projektu
```
ticket-reservation/
├── README.md
└── tickets-backend/
    ├── pom.xml
    ├── .env.example
    ├── compose.yaml
    └── src/
        ├── main/
        │   ├── java/org/piet/ticketsbackend/
        │   │   ├── connectionfinder/
        │   │   ├── geo/
        │   │   ├── globals/
        │   │   ├── passengers/
        │   │   ├── routes/
        │   │   ├── stations/
        │   │   ├── timetables/
        │   │   ├── trains/
        │   │   ├── tickets/
        │   │   ├── users/
        │   │   └── wagons/
        │   └── resources/
        │       ├── application.yaml
        │       ├── schema-postgres.sql
        │       ├── data-postgres.sql
        │       └── i18n/
        └── test/
```

---

## Stos technologiczny

- **Java 21**
- **Spring Boot 4.x**
- **PostgreSQL + PostGIS**
- **JWT / BCrypt**
- **Spring Security, JPA, Validation**
- **Docker, Maven**

---

## Kluczowe funkcjonalności

### Użytkownicy i bezpieczeństwo
- Rejestracja i logowanie  
- Tokeny JWT  
- Zmiana hasła  
- Rola `ADMIN` i `PASSENGER`  
- Endpoint `/me`  

---

### Stacje, trasy i rozkłady
- Dodawanie i zarządzanie stacjami  
- Tworzenie tras i przystanków  
- Rozkłady jazdy z obsługą dni tygodnia  

---

### Wyszukiwanie połączeń
Endpoint:  
`GET /api/connections/find`  
Obsługa parametrów DEPART_AFTER / ARRIVE_BEFORE, maksymalnych przesiadek, minimalnego czasu na przesiadkę.

---

### Bilety i rezerwacje miejsc
- Zakup biletu  
- Anulowanie biletu  
- Walidacja miejsc i unikalność rezerwacji  

---

### Pasażerowie
- CRUD pasażerów  
- Powiązanie z użytkownikami  

---

### Geo
- Liczenie dystansu (JTS + Geodesy)  
- Wyznaczanie czasu przejazdu  

---

## Uruchomienie

### 1. Utwórz `.env`

```
cp .env.example .env
```

### 2. Uruchom bazę danych

```
docker compose up -d
```

### 3. Start backendu

```
mvn spring-boot:run
```

---

## Testy

```
mvn test
```

---

### [Repozytorium na githubie](https://github.com/PietREKT/ticket-reservation)