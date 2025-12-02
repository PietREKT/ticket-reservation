insert into app_users (id, username, password, role, password_needs_changing)
values ('8b1a5d5c-5a34-4c9b-b4c1-9e8c7a2f3d61',
        'admin',
        '$2a$10$Ljo4OU1172qr9XHszpAAfeIvVSwhb7Dpvv65KXwJoUehNQJINb8OK',
        'ADMIN',
        false);
-- password evaluates to admin


-- Stations (Polish major stations)
INSERT INTO station (id, description, code, country_code, city, location) VALUES
                                                                              (1, 'Centralna',  'WAWC', 'PL', 'Warszawa', ST_GeomFromText('POINT(21.00365 52.22977)', 4326)),
                                                                              (2, 'Fabryczna',  'LODF', 'PL', 'Łódź',     ST_GeomFromText('POINT(19.46810 51.75945)', 4326)),
                                                                              (3, 'Główny',     'POZG', 'PL', 'Poznań',   ST_GeomFromText('POINT(16.91860 52.40100)', 4326)),
                                                                              (4, 'Główny',     'KRKG', 'PL', 'Kraków',   ST_GeomFromText('POINT(19.94500 50.06790)', 4326)),
                                                                              (5, 'Główny',     'GDN',  'PL', 'Gdańsk',   ST_GeomFromText('POINT(18.64660 54.35200)', 4326)),
                                                                              (6, 'Główny',     'WRO',  'PL', 'Wrocław',  ST_GeomFromText('POINT(17.03610 51.09890)', 4326));

-- Routes
INSERT INTO route (id, name, length, total_time_minutes, active) VALUES
                                                                     (1, 'Warszawa Centralna – Łódź Fabryczna – Poznań Główny', 400.0, 240, TRUE),
                                                                     (2, 'Warszawa Centralna – Kraków Główny',                  300.0, 160, TRUE),
                                                                     (3, 'Gdańsk Główny – Warszawa Centralna – Kraków Główny',  550.0, 320, TRUE),
                                                                     (4, 'Wrocław Główny – Warszawa Centralna (nocny)',         340.0, 360, TRUE);

-- Route stops (ordered along each route)
INSERT INTO route_stop (id, position, station_id, route_id) VALUES
                                                                -- Route 1: WAW -> Łódź -> Poznań
                                                                (1, 0, 1, 1),
                                                                (2, 1, 2, 1),
                                                                (3, 2, 3, 1),

                                                                -- Route 2: WAW -> Kraków
                                                                (4, 0, 1, 2),
                                                                (5, 1, 4, 2),

                                                                -- Route 3: Gdańsk -> WAW -> Kraków
                                                                (6, 0, 5, 3),
                                                                (7, 1, 1, 3),
                                                                (8, 2, 4, 3),

                                                                -- Route 4: Wrocław -> WAW (overnight)
                                                                (9, 0, 6, 4),
                                                                (10, 1, 1, 4);

-- Timetables
-- All times are "clock times"; day_offset on stops handles overnight behaviour.

INSERT INTO timetable (id, departure_time, arrival_time, waiting_time_at_station) VALUES
                                                                                      -- Timetable 1: Morning IC WAW -> Łódź -> Poznań
                                                                                      (1, TIME '08:00', TIME '11:30', 5),

                                                                                      -- Timetable 2: Gdańsk -> WAW -> Kraków (direct day train)
                                                                                      (2, TIME '06:00', TIME '11:30', 10),

                                                                                      -- Timetable 3: WAW -> Kraków (fast IC, used for transfers)
                                                                                      (3, TIME '09:30', TIME '11:00', 5),

                                                                                      -- Timetable 4: Wrocław -> WAW (overnight, arrival next day)
                                                                                      (4, TIME '23:30', TIME '05:30', 5);

-- Operating days for timetables (timetable_days)
-- Timetable 1: Mon–Fri
INSERT INTO timetable_days (timetable_id, operating_days) VALUES
                                                              (1, 'MONDAY'),
                                                              (1, 'TUESDAY'),
                                                              (1, 'WEDNESDAY'),
                                                              (1, 'THURSDAY'),
                                                              (1, 'FRIDAY');

-- Timetable 2: Daily
INSERT INTO timetable_days (timetable_id, operating_days) VALUES
                                                              (2, 'MONDAY'),
                                                              (2, 'TUESDAY'),
                                                              (2, 'WEDNESDAY'),
                                                              (2, 'THURSDAY'),
                                                              (2, 'FRIDAY'),
                                                              (2, 'SATURDAY'),
                                                              (2, 'SUNDAY');

-- Timetable 3: Mon–Sat
INSERT INTO timetable_days (timetable_id, operating_days) VALUES
                                                              (3, 'MONDAY'),
                                                              (3, 'TUESDAY'),
                                                              (3, 'WEDNESDAY'),
                                                              (3, 'THURSDAY'),
                                                              (3, 'FRIDAY'),
                                                              (3, 'SATURDAY');

-- Timetable 4: Fri/Sat nights
INSERT INTO timetable_days (timetable_id, operating_days) VALUES
                                                              (4, 'FRIDAY'),
                                                              (4, 'SATURDAY');

-- Timetable 1: Route 1 (WAW 08:00 -> Łódź 09:30 -> Poznań 11:30), same day (day_offset = 0)
INSERT INTO timetable_stop (id, routestop_id, timetable_id, arrival_time, departure_time, day_offset) VALUES
                                                                                                          -- Warszawa Centralna (origin)
                                                                                                          (1, 1, 1, NULL, TIME '08:00', 0),
                                                                                                          -- Łódź Fabryczna
                                                                                                          (2, 2, 1, TIME '09:30', TIME '09:35', 0),
                                                                                                          -- Poznań Główny (terminus)
                                                                                                          (3, 3, 1, TIME '11:30', NULL, 0);

-- Timetable 2: Route 3 (Gdańsk 06:00 -> WAW 09:00 -> Kraków 11:30), same day
INSERT INTO timetable_stop (id, routestop_id, timetable_id, arrival_time, departure_time, day_offset) VALUES
                                                                                                          -- Gdańsk Główny (origin)
                                                                                                          (4, 6, 2, NULL, TIME '06:00', 0),
                                                                                                          -- Warszawa Centralna (intermediate)
                                                                                                          (5, 7, 2, TIME '09:00', TIME '09:10', 0),
                                                                                                          -- Kraków Główny (terminus)
                                                                                                          (6, 8, 2, TIME '11:30', NULL, 0);

-- Timetable 3: Route 2 (WAW 09:30 -> Kraków 11:00), same day
INSERT INTO timetable_stop (id, routestop_id, timetable_id, arrival_time, departure_time, day_offset) VALUES
                                                                                                          -- Warszawa Centralna (origin)
                                                                                                          (7, 4, 3, NULL, TIME '09:30', 0),
                                                                                                          -- Kraków Główny (terminus)
                                                                                                          (8, 5, 3, TIME '11:00', NULL, 0);

-- Timetable 4: Route 4 (Wrocław 23:30 -> WAW 05:30 next day)
INSERT INTO timetable_stop (id, routestop_id, timetable_id, arrival_time, departure_time, day_offset) VALUES
                                                                                                          -- Wrocław Główny (origin, day 0)
                                                                                                          (9,  9, 4, NULL, TIME '23:30', 0),
                                                                                                          -- Warszawa Centralna (arrival next day, day_offset = 1)
                                                                                                          (10, 10, 4, TIME '05:30', NULL, 1);

-- User, który jest jednocześnie pasażerem
INSERT INTO app_users (id, username, password, role, password_needs_changing)
VALUES ('11111111-1111-1111-1111-111111111111',
        'jan.kowalski',
        '$2a$10$Ljo4OU1172qr9XHszpAAfeIvVSwhb7Dpvv65KXwJoUehNQJINb8OK', -- hasło: admin
        'PASSENGER',
        false);

INSERT INTO passenger (id, first_name, last_name, email, birth_date, document_number)
VALUES ('11111111-1111-1111-1111-111111111111',
        'Jan',
        'Kowalski',
        'jan.kowalski@example.com',
        DATE '1990-01-01',
        'ABC123456');

-- Pociągi (tabela: pociag, zgodnie z TrainEntity)
INSERT INTO pociag (id, model, numer_pociagu, numer_linii, predkosc, ilosc_wagonow)
VALUES
    (1, 'ED250', 'IC1001', '1', 200, 2),
    (2, 'ED160', 'IC2001', '2', 160, 1);

-- Wagony (tabela: wagon) powiązane z pociągami powyżej
INSERT INTO wagon (id, numer, miejsca_ogolnie, miejsca_wolne, klasa, pociag_id)
VALUES
    (1, 'W1', 80, 80, '2', 1),
    (2, 'W2', 60, 60, '1', 1),
    (3, 'W3', 80, 80, '2', 2);