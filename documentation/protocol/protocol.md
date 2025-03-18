# Tourplanner Protokoll

## 1. Projektüberblick

Das Projekt **Tourplanner** wurde als Desktop-Anwendung in Java unter Verwendung von JavaFX realisiert. Ziel war es, eine Anwendung zu entwickeln, in der Benutzer Touren (z. B. Fahrrad-, Wander-, Lauf- oder Urlaubstouren) erstellen, verwalten und ihre zugehörigen Tourlogs (Erfolgsberichte, Statistiken) einpflegen können. Dabei wurden folgende Hauptaspekte umgesetzt:

- **GUI:** Implementierung eines modernen, übersichtlichen Benutzerinterfaces mit FXML und JavaFX.
- **Architektur:** Anwendung des MVVM-Patterns, um die Benutzeroberfläche von der Logik zu trennen.
- **Layered Architecture:** Trennung in UI-, Business- und Data-Access-Layer.
- **Datenpersistenz:** Nutzung eines OR-Mappers zur Speicherung von Tour- und Log-Daten in einer PostgreSQL-Datenbank.
- **Externe Services:** Integration externer APIs (OpenRouteservice.org für Distanz-/Zeitberechnungen, Leaflet für Kartenanzeige).
- **Testing:** Implementierung von Unit-Tests (JUnit) zur Absicherung kritischer Codeabschnitte.

## 2. Technische Schritte und Entscheidungen

### Technologien & Frameworks
- **Programmiersprache:** Java
- **GUI-Framework:** JavaFX (unter Nutzung von FXML)
- **Architekturmuster:** MVVM (Model-View-ViewModel)
- **Persistenz:** PostgreSQL in Verbindung mit einem OR-Mapper
- **Logging:** log4j zur Fehler- und Ereignisprotokollierung
- **Testframework:** JUnit für Unit-Tests

### Design-Entscheidungen
- **MVVM Pattern:** Erlaubt eine klare Trennung zwischen UI und Businesslogik. Die ViewModels wurden als Bindeglied zwischen den Views und den Services implementiert.
- **Singletons:** Für zentrale Komponenten wie `ViewFactory`, `WindowManager` und `StateRepository` wurde das Singleton-Muster gewählt, um konsistente Zustände über die gesamte Anwendung hinweg zu gewährleisten.
- **Layered Architecture:** Um die Wartbarkeit und Erweiterbarkeit zu erhöhen, wurde eine strikte Trennung in UI-, Business- und Datenzugriffsschichten vorgenommen.

### Herausforderungen & Lösungen
- **Datenbindung:** Die Implementierung der bidirektionalen Bindung zwischen den UI-Elementen (TextFields, TableViews) und den ViewModel-Eigenschaften erwies sich als kritisch. Dies wurde durch umfangreiche Tests und Anpassungen in den ViewModels gelöst.
- **Fehlerhafte Eingaben:** Um eine robuste Validierung zu gewährleisten, wurden in den Services Validierungsmethoden (z. B. `hasNullProperties`) implementiert, die ungültige Eingaben abfangen.
- **Integration externer APIs:** Die Einbindung der OpenRouteservice.org API zur Berechnung von Tourdaten erforderte ein sorgfältiges Handling von REST-Requests und JSON-Parsing, was durch externe Bibliotheken unterstützt wurde.

## 3. UML-Diagramme & Wireframes

- **UML-Diagramme:** Zur Dokumentation der Anwendung wurden u.a. ein Klassendiagramm (zur Übersicht der Architektur und Beziehungen zwischen den Modulen) sowie ein Sequenzdiagramm (für den Ablauf der Full-Text-Suche) erstellt.
- **Wireframes:** Das UI wurde durch einfache Wireframes skizziert, die den Aufbau der Suchzeile, Listenansichten für Tours und Tourlogs, einen Kartenplatzhalter sowie Aktionsbuttons (Add, Edit, Delete) visualisieren.

## 4. Unit Tests

Es wurden mindestens 20 Unit-Tests mit JUnit implementiert, um sicherzustellen, dass:
- Kritische Geschäftslogik in den Services korrekt funktioniert.
- Die Validierung von Benutzereingaben fehlerfrei abläuft.
- Die Datenbindung in den ViewModels wie erwartet reagiert.

Die Auswahl der Tests erfolgte basierend auf den Kernfunktionen der Anwendung, um sicherzustellen, dass bei Änderungen keine Regressionen auftreten.

## 5. Zeitaufwand

Die Entwicklung des Projekts erstreckte sich über mehrere Wochen. Der gesamte Zeitaufwand umfasste:
- **Planung und Konzept:** ca. 10%
- **Implementierung:** ca. 50%
- **Testen und Optimierung:** ca. 30%
- **Dokumentation und Präsentationsvorbereitung:** ca. 10%

## 6. Zusammenfassung

Das Tourplanner-Projekt stellt eine umfassende Anwendung zur Tourenverwaltung dar, die den Anforderungen der Aufgabenstellung entspricht. Durch den Einsatz des MVVM-Patterns, einer klaren Layer-Architektur und der Integration moderner Technologien konnte eine wartbare und erweiterbare Lösung realisiert werden. Die sorgfältige Planung und Durchführung der Unit-Tests sorgt zudem für eine hohe Zuverlässigkeit der Anwendung.
