# Weather Dashboard Backend

API REST Spring Boot pour un dashboard météo, avec recherche de villes, météo en temps réel et gestion des villes favorites par utilisateur.

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Stack technique](#stack-technique)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Configuration](#configuration)
- [Lancer le projet](#lancer-le-projet)
- [Documentation API](#documentation-api)
- [Endpoints principaux](#endpoints-principaux)
- [Tests](#tests)
- [Bonnes pratiques sécurité](#bonnes-pratiques-sécurité)
- [Roadmap d'amélioration](#roadmap-damélioration)

## Fonctionnalités

- Recherche de villes pour autocomplétion.
- Récupération de la météo actuelle par ville.
- Gestion CRUD des villes favorites par utilisateur.
- Récupération de la météo d'une ville favorite.
- Validation des entrées et gestion centralisée des erreurs.
- Documentation OpenAPI/Swagger intégrée.

## Stack technique

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- H2 Database (runtime)
- Spring Validation
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Maven
- Docker / Docker Compose

## Architecture

Structure principale du projet :

```text
src/main/java/com/vacheronalyssa/weatherdashboardback
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
└── service
```

Organisation par couches : contrôleurs REST, logique métier (services), accès aux données (repository), mapping DTO et gestion d'erreurs.

## Prérequis

- JDK 21
- Maven 3.9+
- Docker (optionnel)

## Configuration

Le projet utilise les variables d'environnement pour les secrets.

Variable requise :

- `OPENWEATHER_API_KEY` : clé API OpenWeather

Pour obtenir cette clé :

1. Crée un compte sur OpenWeather : https://home.openweathermap.org/users/sign_up
2. Connecte-toi à ton espace OpenWeather.
3. Va dans la section API keys.
4. Génère une nouvelle clé API si besoin.
5. Copie cette clé dans la variable d'environnement `OPENWEATHER_API_KEY`.
6. Attends éventuellement quelques minutes si la clé vient d'être créée, le temps qu'elle soit active.

Configuration actuelle :

- Fichier applicatif : `src/main/resources/application.yaml`
- CORS configuré pour :
	- `https://weather-dashboard-front-ten.vercel.app`
	- `http://localhost:4200`

Exemple Linux/macOS :

```bash
export OPENWEATHER_API_KEY="votre_cle_api"
```

Exemple PowerShell :

```powershell
$env:OPENWEATHER_API_KEY="votre_cle_api"
```

## Lancer le projet

### 1. En local (Maven)

```bash
mvn clean spring-boot:run
```

L'application est disponible sur : `http://localhost:8080`

### 2. Avec Docker Compose

```bash
OPENWEATHER_API_KEY="votre_cle_api" docker compose up --build
```

## Documentation API

- Swagger UI : `http://localhost:8080/swagger-ui`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`

## Endpoints principaux

### Météo

- `GET /api/weather/current?city=Paris`
- `GET /api/weather/favorite/{userId}/{favoriteCityId}`
- `GET /api/users/{userId}/favorite-cities/{favoriteCityId}/weather`

### Recherche de villes

- `GET /api/search-cities?q=Paris&limit=5`

### Villes favorites

- `GET /api/users/{userId}/favorite-cities`
- `GET /api/users/{userId}/favorite-cities/{favoriteCityId}`
- `POST /api/users/{userId}/favorite-cities`
- `PUT /api/users/{userId}/favorite-cities/{favoriteCityId}`
- `DELETE /api/users/{userId}/favorite-cities/{favoriteCityId}`

Exemple de payload pour création/modification de ville favorite :

```json
{
	"nomVille": "Paris"
}
```

## Tests

Exécuter tous les tests :

```bash
mvn test
```

## Bonnes pratiques sécurité

- Ne jamais committer de clés API, tokens, secrets ou fichiers `.env` réels.
- Conserver les secrets uniquement via variables d'environnement.
- Vérifier que `target/`, logs et artefacts de build restent ignorés par Git.

## Roadmap d'amélioration

- Ajouter un pipeline CI (build + tests) avec badge dans le README.
- Ajouter des tests d'intégration API (MockMvc / Testcontainers).
- Ajouter un fichier `LICENSE` et un `CONTRIBUTING.md`.
- Ajouter un endpoint de healthcheck dédié (`/actuator/health`).

---

Projet réalisé dans le cadre d'un portfolio backend Java/Spring orienté bonnes pratiques API REST et qualité de code.

