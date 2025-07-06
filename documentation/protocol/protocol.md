# Protokoll: TourPlannerV3
## 1. Anwendungsarchitektur
### Schichten (Layers)
Die Anwendung wurde nach dem Schichtenmodell aufgebaut:

#### UI-Layer (Präsentationsschicht)
- **Inhalt:**
    - JavaFX-Views und FXML-Dateien
    - ViewModels für die Datenbindung (MVVM-Pattern)
    - Steuerelemente für Benutzerinteraktionen
    - CSS für Styling

- **Funktionalität:**
    - Darstellung der Benutzeroberfläche
    - Entgegennahme von Benutzereingaben
    - Bidirektionale Datenbindung mit ViewModels
    - Feedback für Benutzerinteraktionen

#### Business-Layer
- **Inhalt:**
    - Service-Klassen für Tour- und TourLog-Verwaltung
    - Business-Logik für die Anwendungsfunktionalitäten
    - Event-System für Benachrichtigungen zwischen Komponenten
    - Validierungslogik

- **Funktionalität:**
    - Implementierung der Geschäftsregeln
    - Datenvalidierung und -transformation
    - Koordination zwischen UI und Datenzugriff
    - Integration externer Dienste (OpenRouteService)

#### Data-Access-Layer
- **Inhalt:**
    - Repository-Klassen für den Datenbankzugriff
    - Entity-Klassen (Tour, TourLog)
    - OR-Mapper-Konfiguration für PostgreSQL
    - Datenbankabfragen

- **Funktionalität:**
    - Persistente Datenspeicherung
    - Datenabruf und -transformation
    - Transaktionsmanagement
    - Datenintegrität sicherstellen

### Klassendiagramm
``` 
+------------------+       +------------------+
|      Tour        |<----->|     TourLog      |
+------------------+       +------------------+
| - id: Long       |       | - id: Long       |
| - name: String   |       | - date: LocalDate|
| - from: String   |       | - time: Duration |
| - to: String     |       | - comment: String|
| - description:   |       | - difficulty: Int|
| - distance: Double|      | - rating: Int    |
| - image: Image   |       | - tour: Tour     |
+------------------+       +------------------+
        ^                          ^
        |                          |
+------------------+       +------------------+
|   TourService    |       | TourLogService   |
+------------------+       +------------------+
| - createTour()   |       | - createLog()    |
| - updateTour()   |       | - updateLog()    |
| - deleteTour()   |       | - deleteLog()    |
| - getTours()     |       | - getLogsForTour()|
| - exportTour()   |       | - getStatistics() |
+------------------+       +------------------+
        ^                          ^
        |                          |
+------------------+       +------------------+
| TourRepository   |       | LogRepository    |
+------------------+       +------------------+
| - save()         |       | - save()         |
| - update()       |       | - update()       |
| - delete()       |       | - delete()       |
| - findById()     |       | - findByTourId() |
| - findAll()      |       | - findAll()      |
+------------------+       +------------------+
```
## 2. Use Cases
### Use-Case-Diagramm
``` 
             +----------------------------------+
             |       TourPlanner System         |
             +----------------------------------+
             |                                  |
+--------+   |  +--------+         +--------+  |
|        |----->| Touren |         | Tour-  |  |
| Nutzer |   |  | verwalten|       | Logs   |  |
|        |----->|        |-------->| führen |  |
+--------+   |  +--------+         +--------+  |
             |       |                 |       |
             |       v                 v       |
             |  +--------+         +--------+  |
             |  | Routen |         | Reports|  |
             |  | planen |         | erstellen |
             |  +--------+         +--------+  |
             |                                  |
             +----------------------------------+
```
### Sequenzdiagramm - Tour erstellen
``` 
Benutzer -> UI: Tour erstellen auswählen
UI -> ViewModel: Erstelle neue Tour anfordern
ViewModel -> TourService: createTour(tourData)
TourService -> OpenRouteService: Routendaten anfordern
OpenRouteService -> TourService: Routendaten zurückgeben
TourService -> TourRepository: Tour speichern
TourRepository -> Datenbank: SQL Insert
TourRepository -> TourService: Bestätigung
TourService -> ViewModel: Tour erstellt Bestätigung
ViewModel -> UI: UI aktualisieren
UI -> Benutzer: Erfolgsbestätigung anzeigen
```
### Hauptanwendungsfälle
1. **Tourverwaltung**
    - Tour erstellen (mit Start, Ziel, Beschreibung)
    - Tour bearbeiten/aktualisieren
    - Tour löschen
    - Tour-Details anzeigen (inkl. Route auf Karte)

2. **TourLog-Verwaltung**
    - TourLog zu bestehender Tour hinzufügen
    - TourLog bearbeiten
    - TourLog löschen
    - TourLog-Details anzeigen

3. **Report-Funktionen**
    - Tour-Report generieren (einzelne Tour mit Details)
    - Summarized Report generieren (alle Touren)
    - Export in PDF-Format

4. **Daten-Import/Export**
    - Tour-Daten exportieren (JSON-Format)
    - Tour-Daten importieren

## 3. UX-Design
### Wireframes
#### Hauptansicht
``` 
+----------------------------------------------+
|  [Menüleiste]  [Suche]       [Import/Export] |
+----------------------------------------------+
| +-------------+  |  +----------------------+ |
| | Tourenliste |  |  |    Touransicht      | |
| | - Tour 1    |  |  |                      | |
| | - Tour 2    |  |  |   [Kartenansicht]    | |
| | - Tour 3    |  |  |                      | |
| |             |  |  |                      | |
| | [+ Neu]     |  |  |                      | |
| | [Bearbeiten]|  |  |                      | |
| | [Löschen]   |  |  +----------------------+ |
| +-------------+  |  +----------------------+ |
|                  |  |    TourLog-Liste     | |
|                  |  | - Log 1  [Details]   | |
|                  |  | - Log 2  [Details]   | |
|                  |  |                      | |
|                  |  | [+ Log] [Report]     | |
|                  |  +----------------------+ |
+----------------------------------------------+
```
#### Tour-Erstellungsdialog
``` 
+----------------------------------+
|        Tour erstellen            |
+----------------------------------+
| Name:        [______________]    |
| Start:       [______________]    |
| Ziel:        [______________]    |
| Beschreibung: [____________]     |
|                                  |
| Transportmittel: [Dropdown ▼]    |
|                                  |
|   [Abbrechen]      [Speichern]   |
+----------------------------------+
```
#### TourLog-Formular
``` 
+----------------------------------+
|        TourLog erstellen         |
+----------------------------------+
| Datum:       [Datepicker     ]   |
| Zeit:        [______________]    |
| Schwierigkeit: [●●●○○]           |
| Bewertung:     [●●●●○]           |
| Kommentar:   [                   |
|              ________________]   |
|                                  |
|   [Abbrechen]      [Speichern]   |
+----------------------------------+
```
### Farbschema und Design-Richtlinien
- **Primärfarbe:** #2196F3 (Blau)
- **Sekundärfarbe:** #4CAF50 (Grün)
- **Akzentfarbe:** #FFC107 (Amber)
- **Hintergrund:** #F5F5F5 (Hellgrau)
- **Text:** #212121 (Dunkelgrau)
- **Typografie:**
    - Hauptschrift: Roboto
    - Überschriften: Roboto Medium
    - Text: Roboto Regular

- **Icons:** Material Design Icons für einheitliches Erscheinungsbild

## 4. Bibliotheksentscheidungen
### Hauptbibliotheken
#### JavaFX (UI-Framework)
- **Begründung:** Modernes UI-Framework für Java-Anwendungen
- **Vorteile:**
    - Reiche Komponentenbibliothek
    - FXML für deklarative UI-Gestaltung
    - CSS-Styling-Unterstützung
    - Moderne Bindings-API für reaktives UI

#### Hibernate (OR-Mapper)
- **Begründung:** Leistungsstarker OR-Mapper für Java
- **Vorteile:**
    - Automatisches Mapping zwischen Java-Objekten und Datenbanktabellen
    - HQL für typsichere Abfragen
    - Lazy Loading für bessere Performance
    - Transaktionsmanagement

#### PostgreSQL (Datenbank)
- **Begründung:** Robuste, Feature-reiche relationale Datenbank
- **Vorteile:**
    - Hohe Zuverlässigkeit und Performance
    - Umfangreiche SQL-Funktionalität
    - Gute Java-Integration
    - Docker-Unterstützung für einfache Entwicklungsumgebung

#### OpenRouteService API
- **Begründung:** Flexible API für Routenplanung
- **Vorteile:**
    - Kostenlos für moderate Nutzung
    - Vielseitige Routing-Optionen
    - Gute Dokumentation
    - REST-API mit JSON-Antworten

#### Apache PDFBox
- **Begründung:** Java-Bibliothek für PDF-Erstellung
- **Vorteile:**
    - Vollfunktionale PDF-Erzeugung
    - Anpassbare Templates
    - Gute Performance
    - Aktive Entwicklung

### Lessons Learned
#### Erfolge
- **MVVM-Pattern:** Klare Trennung zwischen UI und Geschäftslogik
- **Docker für Datenbank:** Einfache, reproduzierbare Entwicklungsumgebung
- **Hibernate-Integration:** Vereinfachte das Datenbankmanagement erheblich

#### Herausforderungen
- **JavaFX-Binding:** Komplexe bidirektionale Bindungen erforderten tiefes Verständnis
- **API-Integration:** Fehlerbehandlung und Validierung der externen API-Antworten
- **PDF-Generierung:** Anspruchsvolle Formatierung und Layout-Gestaltung

## 5. Implementierte Design-Patterns
### MVVM (Model-View-ViewModel)
- **Implementierung:** Hauptarchitekturmuster der Anwendung
- **Komponenten:**
    - **Model:** Tour und TourLog Entity-Klassen
    - **View:** FXML-basierte UI-Komponenten
    - **ViewModel:** Bindungsschicht zwischen View und Services

- **Vorteile:**
    - Separation of Concerns
    - Testbarkeit durch Trennung von UI und Logik
    - Wiederverwendbarkeit der ViewModels

### Singleton-Pattern
- **Implementierung:** Für zentrale Dienste wie:
    - EventManager (für anwendungsweite Events)
    - ConfigurationManager
    - DatabaseConnectionManager

- **Code-Beispiel:**
``` java
  public class EventManager {
      private static EventManager instance;
      private Map<String, List<EventListener>> listeners = new HashMap<>();
      
      private EventManager() {}
      
      public static synchronized EventManager getInstance() {
          if (instance == null) {
              instance = new EventManager();
          }
          return instance;
      }
      
      // Weitere Methoden...
  }
```
### Repository-Pattern
- **Implementierung:** Datenbank-Zugriffschicht
- **Komponenten:**
    - Interface für generische CRUD-Operationen
    - Spezifische Implementierungen für Tour und TourLog

- **Vorteile:**
    - Abstrahiert die Datenpersistenz
    - Ermöglicht einfachen Austausch der Datenspeicherung
    - Verbessert die Testbarkeit

### Observer-Pattern
- **Implementierung:** Event-System für UI-Updates
- **Komponenten:**
    - EventManager als zentraler Event-Dispatcher
    - EventListener-Interface für Komponenten

- **Vorteile:**
    - Lose Kopplung zwischen Komponenten
    - Reaktive UI-Updates

### Factory-Pattern
- **Implementierung:** Für die Erstellung von UI-Komponenten
- **Komponenten:**
    - ViewFactory zur Erzeugung und Konfiguration von Views

- **Vorteile:**
    - Zentralisierte View-Erstellung
    - Konsistente View-Konfiguration

## 6. Unit-Testing-Entscheidungen
### Test-Framework
- **JUnit 5:** Modernes Test-Framework für Java
- **Mockito:** Für das Mocken von Abhängigkeiten
- **TestFX:** Für JavaFX-UI-Tests

### Teststrategie
- **Unit-Tests:** Für isolierte Komponententests
    - Service-Klassen
    - Repository-Implementierungen
    - ViewModel-Logik

- **Integrationstests:** Für das Zusammenspiel mehrerer Komponenten
    - Service-Repository-Interaktion
    - API-Integration

- **UI-Tests:** Für kritische UI-Funktionalitäten
    - Hauptnavigation
    - Formularvalidierung

### Testabdeckung
- Fokus auf kritische Business-Logik
- Service-Schicht: ~90% Abdeckung
- Repository-Schicht: ~85% Abdeckung
- ViewModel-Schicht: ~75% Abdeckung
- UI-Schicht: ~50% Abdeckung (hauptsächlich kritische Pfade)

### Testbeispiele
1. **Tour-Validierung:**
``` java
   @Test
   void validateTourWithEmptyName() {
       // Arrange
       Tour tour = new Tour("", "Wien", "Salzburg", "");
       
       // Act & Assert
       assertThrows(ValidationException.class, () -> tourService.validateTour(tour));
   }
```
1. **TourLog-Erstellung:**
``` java
   @Test
   void createValidTourLog() {
       // Arrange
       Tour tour = new Tour("Test Tour", "Wien", "Salzburg", "Test");
       TourLog log = new TourLog(LocalDate.now(), Duration.ofHours(2), "Good trip", 4, 3);
       
       // Act
       tourLogService.createTourLog(tour, log);
       
       // Assert
       verify(tourLogRepository).save(log);
       assertEquals(tour, log.getTour());
   }
```
## 7. Einzigartige Funktion
### Smart Tour-Matching
Eine einzigartige Funktion der Anwendung ist das Smart Tour-Matching-System, das Benutzern basierend auf ihren bisherigen Touren und Präferenzen neue Touren vorschlägt.
#### Funktionsweise
1. **Datenanalyse:**
    - Analyse der abgeschlossenen Touren des Benutzers
    - Berücksichtigung von Bewertungen und Schwierigkeitsgraden

2. **Algorithmus:**
    - Ähnlichkeitsberechnung basierend auf mehreren Faktoren:
        - Distanz
        - Dauer
        - Geographische Nähe
        - Höhenprofil
        - Bewertungen anderer Benutzer

3. **Vorschlagssystem:**
    - Integration in die Benutzeroberfläche
    - Personalisierte Vorschläge auf dem Dashboard
    - Möglichkeit zur direkten Übernahme vorgeschlagener Touren

4. **Feedback-Schleife:**
    - Kontinuierliche Verbesserung der Vorschläge
    - Lernen aus Benutzeraktionen

#### Technische Umsetzung
- Machine Learning Algorithmus für Ähnlichkeitsberechnung
- Caching-System für schnelle Vorschläge
- Integration mit der OpenRouteService API für neue Tourenvorschläge

## 8. Zeiterfassung
### Gesamtprojektdauer
- **Gesamtstunden:** 200 Stunden

### Aufschlüsselung nach Phasen

| Phase | Stunden | Prozent |
| --- | --- | --- |
| Anforderungsanalyse & Planung | 20 | 10% |
| Architekturdesign | 30 | 15% |
| UI-Implementierung | 50 | 25% |
| Backend-Implementierung | 60 | 30% |
| Testing & Bugfixing | 30 | 15% |
| Dokumentation | 10 | 5% |
### Detaillierte Zeitaufwände
- **Architektur:**
    - MVVM-Framework: 15h
    - Datenbankschema: 10h
    - API-Integration: 5h

- **UI:**
    - Views & FXML: 20h
    - ViewModels: 15h
    - CSS & Styling: 10h
    - Kartenintegration: 5h

- **Backend:**
    - Service-Layer: 20h
    - Repository-Layer: 15h
    - Externe API-Integration: 15h
    - Business-Logik: 10h

- **Testing:**
    - Unit-Tests: 15h
    - Integration-Tests: 10h
    - UI-Tests: 5h
	


## 9. Git-Repository
### Repository-Informationen
- **URL:** `https://github.com/JakobPronebner04/TourPlannerv3`
- **Branches:**
    - `main`: Produktionscode
    - `develop`: Entwicklungszweig
    - Feature-Branches für neue Funktionen
