# Store

A small Spring Boot application I built to **learn the fundamentals of Dependency Injection and Spring Data JPA**. It models an online store (users, addresses, profiles, tags, categories, products, wishlists) on top of MySQL, and the service class contains small, runnable examples of the main JPA features I practiced.

> This is a learning project, not a production application. There is no REST API yet; the demos run from `main()` and print to the console.

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java 26 |
| Framework | Spring Boot 4.1.1 (Web, Data JPA, JDBC, DevTools) |
| Database | MySQL |
| Migrations | Flyway (`flyway-core`, `flyway-mysql`) |
| Boilerplate reduction | Lombok |
| Build tool | Maven (wrapper included) |

## What I practiced

### Dependency Injection

- **Constructor injection** with `@Service` + Lombok `@AllArgsConstructor`; `UserServices` receives all six dependencies (repositories and `EntityManager`) as `final` fields, with no `@Autowired` needed.
- **Stereotype annotations** (`@Service`, `@Repository`) so Spring discovers and wires beans through component scanning.
- **Retrieving a bean manually** from the `ApplicationContext` in `StoreApplication.main()`.
- **Injecting `EntityManager`** with `@PersistenceContext` in a custom repository implementation.

### Spring Data JPA

- **Entity mapping** with `@Entity`, `@Table`, `@Column`, `@GeneratedValue`.
- **Relationships**
  - One-to-many / many-to-one: `User` ↔ `Address`, `Category` ↔ `Product`
  - One-to-one with shared primary key (`@MapsId`): `User` ↔ `Profile`
  - Many-to-many: `User` ↔ `Tag` (`user_tags`) and `User` ↔ `Product` (`wishlist`)
  - Cascade types, `orphanRemoval`, and helper methods that keep both sides of a bidirectional relationship in sync (`addAddress`, `removerAddress`, `addTag`)
- **Fetching**: lazy vs. eager loading, `@EntityGraph` to avoid N+1 queries
- **Entity lifecycle states**: transient, persistent, detached (`showEntityStates`)
- **Derived query methods**: `findByName`, `findByNameContaining`, `findByNameStartingWith`, and more
- **`@Query` (JPQL)** including aggregate queries and `@Modifying` bulk updates
- **Projections**: interface-based (`ProductSummary`, `UserSummary`) and class-based DTO (`ProductSummaryDTO`)
- **Query by Example** with `ExampleMatcher`
- **Criteria API** in a custom repository fragment (`ProductCriteriaRepositoryImpl`)
- **Specifications** for composable, dynamic filters (`ProductSpec`)
- **Pagination and sorting** with `PageRequest` and `Sort`
- **Stored procedure** call via `@Procedure` (`findProductsByPrice`)
- **Transactions** with `@Transactional`
- **Database versioning** with Flyway migrations

## Project structure

```
store/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/learnjavawithcrocsa/store/
    │   │   ├── StoreApplication.java
    │   │   ├── entities/        Address, Category, Product, Profile, Tag, User
    │   │   ├── dtos/            ProductSummary, ProductSummaryDTO, UserSummary
    │   │   ├── repositories/    Spring Data repositories
    │   │   │   └── specifications/   ProductSpec
    │   │   └── services/        UserServices (demo methods)
    │   └── resources/
    │       ├── application.yaml
    │       ├── db/migration/    V1 … V7 Flyway scripts
    │       └── static/index.html
    └── test/
        └── StoreApplicationTests.java
```

## Database schema

Created by the Flyway migrations in `src/main/resources/db/migration/`:

| Migration | Purpose |
| --- | --- |
| `V1__initial_migration.sql` | `user`, `addresses`, `profiles` |
| `V2__add_tags_tables.sql` | `tags`, `user_tags` |
| `V3__add_categories_and_products_tables.sql` | `categories`, `products` |
| `V4__.sql` | adds `products.description` |
| `V5__add_wishlist.sql` | `wishlist` join table |
| `V6__enable_cascade_on_wishlist.sql` | `ON DELETE CASCADE` for wishlist → products |
| `V7__find_products_procedure.sql` | stored procedure `findProductsByPrice` |

```
user 1──* addresses
user 1──1 profiles          (shared primary key)
user *──* tags              (user_tags)
user *──* products          (wishlist)
categories 1──* products
```

## Getting started

### Prerequisites

- JDK 26
- MySQL running on `localhost:3306`
- Maven (or just use the included `./mvnw`)

### 1. Configure the database

Edit `src/main/resources/application.yaml` with your own MySQL credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store?createDatabaseIfNotExist=true
    username: <your-mysql-user>
    password: <your-mysql-password>
```

The `store` database is created automatically if it does not exist, and Flyway applies all migrations on startup.

If you use the Flyway Maven plugin directly (`./mvnw flyway:migrate`), update the connection settings in the `flyway-maven-plugin` block of `pom.xml` as well.

### 2. Run the application

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

### 3. Run the tests

```bash
./mvnw test
```

`StoreApplicationTests` is the default context-load test, so it needs the MySQL database to be reachable.

## Trying the demos

`StoreApplication.main()` currently fetches the `UserServices` bean and calls:

```java
services.fetchProductsBySpecification("Books");
```

To try a different example, replace that line with another method from `UserServices`:

| Method | Demonstrates |
| --- | --- |
| `showEntityStates()` | transient vs. persistent entities |
| `showRelatedEntities()` | one-to-one navigation (lazy loading) |
| `persistRelated()` / `deleteRelated()` | cascade and orphan removal |
| `manageProducts()` | delete by id |
| `updateProductPrices()` | bulk `@Modifying` update |
| `fetchProducts()` | Query by Example |
| `fetchProductsByCriteria()` | Criteria API |
| `fetchProductsBySpecification(category)` | Specifications |
| `fetchPaginatedProducts(page, size)` | pagination |
| `fetchSortedProducts()` | multi-field sorting |
| `fetchUsers()` | `@EntityGraph` fetching |
| `printLoyaltyProfiles()` | interface projection with JPQL |

**Note:** the migrations create the tables but do not insert any data. Several demos look up specific rows (for example, a `Books` category, or user/product IDs 2, 3 and 4), so insert some sample rows first or adjust the IDs in the code.

## Known limitations / next steps

- Database credentials are currently hard-coded in `application.yaml` and `pom.xml`; move them to environment variables or a profile-specific config before sharing the repo.
- Demos are triggered by editing `main()`; a REST controller layer would be a natural next step.
- Add real unit and integration tests beyond the context-load test.
- Add seed data (for example, a Flyway `afterMigrate` script or a `data.sql`) so every demo works out of the box.
