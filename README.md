# Plus qu'Agenda Front

Application web Java EE/JSF (PrimeFaces) pour la gestion de réservation de créneaux horaires.

## Présentation

Ce projet constitue la partie front-end d'une application de gestion de créneaux, réalisée en Java EE avec JSF et PrimeFaces.  
L'accent a été mis sur l'intégration avec le backend via des appels API REST pour toutes les opérations principales :  
- Authentification et gestion des utilisateurs
- Création, réservation et affichage des créneaux
- Administration des comptes
- Modification du profil et du mot de passe

## Points forts

- **Appels API REST** : Toutes les fonctionnalités (connexion, inscription, réservation, etc.) sont réalisées via des requêtes HTTP vers le backend, en utilisant `jakarta.ws.rs.client.Client` et la sérialisation JSON (Jackson).
- **Gestion du JWT** : Après authentification, le token JWT est récupéré et utilisé pour sécuriser les échanges avec le backend.
- **Séparation claire Front/Back** : Le front ne contient aucune logique métier, tout passe par les API exposées par le backend.
- **Interface moderne** : Utilisation de PrimeFaces pour une expérience utilisateur agréable.

## Technologies

- Java 11
- Jakarta EE (CDI, JSF)
- PrimeFaces 12
- REST Client Jakarta
- Jackson (JSON)
- JWT (io.jsonwebtoken)
- Maven

## Structure du projet

- `src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/` : Beans JSF gérant les appels API (ex : [`LoginBean`](src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/LoginBean.java), [`CreneauxBean`](src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/CreneauxBean.java))
- `src/main/webapp/faces/` : Pages JSF/XHTML
- `src/main/webapp/resources/css/` : Feuilles de style CSS
- `pom.xml` : Dépendances Maven

## Exemples d'appels API

- Authentification utilisateur :  
  [`LoginBean.login()`](src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/LoginBean.java)
- Réservation d'un créneau :  
  [`CreneauxBean.reserveCreneau`](src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/CreneauxBean.java)
- Récupération des créneaux disponibles :  
  [`CreneauxBean.loadAllCreneauxAvailable`](src/main/java/fr/sd/reservcreneaux/reservcreneauxfront/Bean/CreneauxBean.java)

