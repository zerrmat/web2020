# StockExchange
0) Projekt jest dostepny na GitHub pod adresem: https://github.com/zerrmat/web2020
Archiwum zip (kopia projektu z GitHub) jest dostępne pod adresem: 
"Minimalne" archiwum zip z wygenerowanym plikiem WAR oraz schematem bazy danych jest dostępne pod adresem:

1) Do uruchomienia projektu z pliku WAR są potrzebne:
    • Java Runtime Edition w wersji ośmej bądź nowsza.
    • Kontener serwletów - projekt testowany na Tomcat 8, procedura uruchamiania aplikacji dotyczy właśnie tego kontenera.
    • PostgreSQL
Aplikacja oczekuje, że Postgre będzie dostępny pod portem 5432 a Tomcat będzie działał na porcie 8080.
Na bazie danych należy wykonać polecenia z pliku schema.sql (katalog główny projektu) tworzące odpowiednie tabele i relacje.
Plik WAR trzeba umiescić w katalogu <katalog-domowy-tomcata>/webapps, następnie uruchomić Tomcata. Jeżeli wszystko przebiegło pomyślnie to Tomcat wygeneruje katalog o identycznej nazwie co plik WAR oraz aplikacja będzie dostępna pod adresem localhost:8080/stockexchange.

Do uruchomienia projektu poprzez kompilację kodu źródłowego i wygenerowanie archiwum WAR jest dodatkowo potrzebne node.js.
Procedura generowania pliku WAR:
    1. W terminalu trzeba przejść do katalogu stockexchange/stockexchange-app i wykonać polecenie npm run build. 
    2. Jeśli krok 1. wykonał się pomyślnie to pliki z katalogu stockexchange/stockexchange-app/build należy skopiować do katalogu stockexchange/src/main/webapp.
    3. Następnie w terminalu przejść do katalogu stockexchange i wywołać polecenie gradlew build.
    4. Jeśli krok 3. wykonał się pomyślnie, to w katalogu stockexchange/build/libs czeka wygenerowany WAR. Należy ręcznie zmienić jego nazwę na "stockexchange.war".
    5. Aplikacja oczekuje, że Postgre będzie dostępny pod portem 5432 a Tomcat będzie działał na porcie 8080. Plik WAR trzeba umiescić w katalogu <katalog-domowy-tomcata>/webapps, następnie uruchomić Tomcata. Jeżeli wszystko przebiegło pomyślnie to Tomcat wygeneruje katalog o identycznej nazwie co plik WAR oraz aplikacja będzie dostępna pod adresem localhost:8080/stockexchange

Istotne - do zewnętrznego źródła danych można wykonać 1000 zapytań miesięcznie - każda większa operacja to zazwyczaj jedno możliwe zapytanie (zero jeśli dane idą z pamięci podręcznej, więcej jeśli tysiące obiektów). Po wykorzystaniu limitu jedyne dane będą serwowane z kopii w bazie danych.
Czasami zapytania trwają długo i trzeba czekać na odświeżenie widoku - kwestia dużych ilość danych przesyłanych w tle.

2) Aplikacja składa się z dwóch głównych elementów - frontend jest napisany w React (create-react-app), Javascript, HTML5. Stylowanie elementów odbywa się w sumie za pomocą czystego CSS, wbrew rozszerzeniu pliku (.scss) oraz zależności w postaci Bootstrapa - ostatecznie skorzystałem z tego co było mi znane. Frontend wysyła zapytania lokalnie do drugiej części aplikacji - backendu napisanego w Javie ze wsparciem Spring Framework (Spring Boot). Lombok wyręczył mnie z pisania nudnego kodu "boilerplate" - gettery, settery, konstruktory. Testy jednostkowe zostały napisane ze wsparciem JUnit5, AssertJ, Mockito. Podczas rozwijania aplikacji do uruchamiania aplikacji springowej używałem Tomcata, do uruchamiania modułu Reacta używałem npm. Jako baza danych zostało użyte PostgreSQL. Do kontaktu pomiędzy bazą danych a backendem wspomogłem się technologią do mapowania relacyjnego Hibernate. Komunikacja między backendem a frontendem oraz backendem i zewnętrznym źródłem danych odbywa się za pomocą obiektów JSON.
Do zarządzania zależnościami od stronu backendu został użyty Gradle, od strony frontendu node.js. Jako system kontroli wersji został użyty Git.

3) 500 linii kodu osiągnąłem w początkowej fazie pisania części Javowej, ta ilość została przekroczona kilkukrotnie.
React słynie z dużych możliwośći w kwestii tworzenia niezależnych "klocków" - komponentów, intensywnie korzystałem z tych ułatwień do separacji poszczególnych typów danych / obiektów. Starałem się też stosować do schematów działania zalecanych dla projektów w React - obiekty stanu, obiekty props (przekazywanie danych między komponentami), nadpisanie metod cyklu życia komponentu (render, componentDidMount i tak dalej), własne zdarzenia, JSX, renderowanie warunkowe. Ostylowanie komponentów pojawia się w dwóch wersjach - odrębny plik i style inline, oba mają zalety i wady w Reactcie ale szczerze mówiąc jeszcze muszę się oswoić z tym kiedy które stosować - jest to moja pierwsza apka pisana w Reactcie.
Z Javascriptu użyłem kilku obiektów typu Promise i kilku lambd (map, forEach), mechanizmu importów; oprócz tego było potrzebne kilka operacji na DOM.
W części backendowej starałem się pisać kod w Javie zgodnie z zasadami programowania obiektowego - jeden obiekt, jedna odpowiedzialność, pojawiły się wzorce obiektowe; Spring zapewnia mechanizm wstrzykiwania zależności i multum adnotacji przyspieszających tworzenia aplikacji webowych; jest też podział obiektów na te dostosowane do bazy danych jak i do przesyłania w aplikacji, serwisy i kontrolery. Nie jestem zadowolony z niektórych fragmentów ale mogłoby się to skończyć niekończącą się refaktoryzacją. Testy jednostkowe pozwalały uniknąć regresji - próbowałem stosować się do zasady "najpierw testy" lecz trudno z tym było gdyż funkcjonalności były tworzone "eksperymentalnie", długo krystalizowało się jak właściwie najlepiej byłoby napisać dany kawałek kodu.

Data persistence objawia się w tym projekcie w postaci cache'owania danych pobieranych z zewnątrz. Wynika to z dwóch rzeczy: ograniczona dozwolona liczba zapytań do zewnętrznego źródła danych w okresie czasu, dane zmieniają się w jasno określonych odstępach czasowych. Kopia danych z bazy pozwala na zminimalizowanie zapytań na zewnątrz i przyspieszeniu aplikacji.

Przesyłanie danych odbywa się na dwóch płaszczyznach. Pierwsza - na zewnątrz, aplikacja kontaktuje się z API strony marketstack.com - apka wystosowuje proste żądania GET do marketstack a ten zwraca dane w formacie JSON.
Druga - wewnątrz aplikacji. Backend pilnuje unikalności i aktualności danych w bazie danych. Apka w tym przypadku operuję na obiektach odwzorowujących 1:1 reprezentacje danych z bazy. Frontend w odpowiedzi na działania użytkownika zleca backendowi dostarczenie odpowiednich danych - na tym drugim spoczywa odpowiedzialność za ich aktualność i przystępny format. Backend wystawia dane w postaci JSON, zbudowane na podstawie tego, co podaje baza danych.
Warte zauważenia jest iż aplikacja przerzuca duże ilości danych, potrafią one przytkać frontend, zdarzają się pliki JSON z kilkoma tysiącami obiektów.
