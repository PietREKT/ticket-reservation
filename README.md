# Forum wędkarskie
## Szczegóły projektu:
#### Zasady:
1. W Jirze na tablicy zadań należy oznaczać nad czym pracujesz. **NIE PRACUJ NAD TYM SAMYM CO INNA OSOBA**
2. Implementacja funkcjonalności na frontendzie powinna odbyć się __po__ implementacji tejże na backendzie. Minimum potrzebne do rozpoczęcia pracy to ustalenie pól DTO przekazywanych przez backend.
---
#### Struktura projektu:
```
forum-wedkarskie:
|   README.md
|   .env
|   .env.example
└── frontend
└── forum-backend
└── Zasoby
```
Folder `Zasoby` zawiera zasoby przydatne do organizacji pracy. 
---
#### Organizacja pracy nad frontendem:
```
frontend:
└── src
    └── components
    └── resources
    └── stores
    └── utils
```
* Components - folder w którym powinny być umieszczane komponenty aplikacji. Każdy widok powinien mieć swój własny folder. Komponentów można używać pomiędzy różnymi widokami.
* Resources - statyczne pliki potrzebne do obsługi innych skryptów - np. pliki JSON z tłumaczeniami
* Stores - folder na magazyny pinia. Jedyne miejsce w którym możesz wykonywać żądania do API
* Utils - folder na klasy inicjalizujące lub inne skrypty bez widoku

#### Przydatne linki:
* [API Docs (wymaga uruchomionego backendu)](http://localhost:8080/swagger-ui/index.html#/)
* [Jira](https://piotrstoinski.atlassian.net/jira/software/projects/IN/boards/3)
* [Dokumentacja Vue](https://vuejs.org/guide/introduction)
* [Dokumentacja Pinia](https://pinia.vuejs.org/core-concepts)
* [Dokumentacja Vue Router](https://router.vuejs.org/guide)


#### Uruchamianie aplikacji
1. Otworzyć folder 'forum-backend' w IDE wspierającym Javę
2. Uruchomić projekt

3. Otworzyć folder 'frontend' w IDE/konsoli
4. W konsoli/terminalu IDE wpisać `npm run dev`

## Obsługa gita:

#### Zasady:
1. Nie używać brancha `main`!
2. Wszystkie feature'y powinny być implementowane na osobnych branchach
3. Gałęzie będą merge'owane przeze mnie (Piotrka). **TY NIC NIE MERGUJESZ**
---
#### Zasady nazywania commitów:
1. Wiadomości powinny zaczynać się od słowa kluczowego `FIX:` lub `FEAT:`
2. Wiadomości powinny zawierać nazwę zadania na Jirze, po dwukropku
3. Ewentualne szczegóły zamieścić po znaku `-` po nazwie zadania
##### Przykładowe wiadomości:
- `FEAT: Logowanie`
- `FIX: Logowanie - brak danych w żądaniu HTTP`
---
#### Przed implementacją nowej funkcjonalności należy w kolejności:
1. Pobrać aktualną wersję repozytorium:\
`$ git pull https://github.com/PietREKT/forum-wedkarskie`
2. Utworzyć nowego brancha:  
`$ git branch <nazwa>`\
Nazwy branchy powinny nawiązywać do feature'ów, np:\
`$ git branch backend_login` \
`$ git branch frontend_login`
3. Przełączyć się na nowego brancha:\
`$ git checkout <nazwa>` 
4. Upewnić się, że znajdujecie się na odpowiednim branchu. Po wykonaniu polecenia\
`$ git branch` \
wasz obecny branch zostanie oznaczony symoblem '**\***'
5. Programować
___
#### Po implementacji nowej funkcjonalności należy w kolejności:
1. Upewnić się, że znajdujemy się na branchu docelowym:\
`$ git branch` 
2. Wykonać commit: \
`$ git commit -m "<wiadomość>"`
3. Wypchnąć zmiany na zdalne repozytorium: \
`$ git push -u origin <branch>`
4. **NIE MERGE'UJ BRANCHY**
