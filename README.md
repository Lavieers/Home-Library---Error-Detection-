# PROJEKT: Home Library – System Zarządzania Biblioteką

## Opis Projektu

**Home Library** to aplikacja desktopowa stworzona w technologii **JavaFX**, przeznaczona do zarządzania prywatnymi zbiorami książek.

Głównym celem projektu było opracowanie stabilnego i funkcjonalnego narzędzia. Program został użyty przede wszystkim do **testowania prawidłowości kodu, wykrywania błędów** oraz weryfikacji funkcjonalności na poziomie modelu danych i interfejsu użytkownika.

Aplikacja umożliwia cyfrowe katalogowanie książek, śledzenie ich liczby oraz generowanie podstawowych statystyk.

##Kluczowe Funkcjonalności

* **Zarządzanie Kolekcją:** Dodawanie, przeglądanie i usuwanie książek (tytuł, autor, rok wydania, kategoria, unikalne ID).
* **Interfejs Użytkownika:**
    * **Widok Główny:** Pulpit nawigacyjny z dostępem do Biblioteki, dodawania/usuwania książek oraz statystyk.     * **Motywy:** Przełączanie między jasnym a ciemnym motywem.
    * **Ekran Powitalny:** Prezentacja wizualna przy uruchomieniu. * **Trwałość Danych:** Automatyczny zapis i wczytywanie biblioteki z lokalnego pliku CSV.
* **Eksport i Logowanie:** Eksport listy książek do pliku TXT oraz rejestrowanie operacji w historii zmian (`history.log`).
* **Statystyki:** Moduł prezentujący podsumowania kolekcji (np. średni rok wydania, najczęściej występujący autor).

## Aspekt Testowy Projektu

W ramach projektu położono duży nacisk na sprawdzenie stabilności kodu i integralności danych. Zaimplementowano kompleksowy zestaw testów:

* **Testy Jednostkowe (JUnit 5):** Dla modelu danych (`BookTest`) i kluczowych operacji I/O (`MainTest`).
* **Testy Integracyjne (TestFX):** Dla weryfikacji funkcjonalności GUI, w tym filtrowania w oknie `LibraryWindow` oraz poprawności okien dialogowych usuwania i dodawania książek.

##  Autorzy

Projekt został stworzony przez zespół:

* **Kinga Łopata**
* **Amelia Kucharz**
* **Piotr Kula**
