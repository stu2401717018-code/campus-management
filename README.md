# Campus Management System

Система за управление на кампус с множество сървиси, контейнеризирана с Docker и оркестрирана с Docker Compose.

Проектът е изграден на базата на следните задания:
- [Изпитно задание: Контейнеризация и оркестрация](./Assignment%20-%20Containerization%20and%20Orchestration.md)
- [Изпитно задание: Сървърни приложения и Интеграция на бази данни](./Assignment%20-%20Server%20Applications%20and%20Database%20Integration.md)

## Документация

- [Информация относно контейнеризацията и оркестрацията](./Containerization%20and%20orchestration.md).

## Проект

Това е Spring Boot приложение, разделено на два основни сървиса:
- **app-public** - публично API за управление на студенти, курсове, катедри, професори и т.н.
- **app-internal** - вътрешен сървис за обработка на данни, който работи само в защитена мрежа

Всичко е контейнеризирано с Docker и може да се стартира с една команда.

## Архитектура

### Диаграма на потока на заявките
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP :8080
       ▼
┌─────────────────────────────────┐
│  app-public (Public Network)    │
│  Port: 8080 (published)         │
│  - Campus Management API        │
│  - Calls app-internal           │
└──────┬──────────────────────────┘
       │ Internal Network
       │ http://app-internal:8081
       ▼
┌─────────────────────────────────┐
│  app-internal (Internal Network)│
│  Port: 8081 (not published)     │
│  - Data Processing Service      │
│  - Logs to volume               │
└─────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────┐
│  PostgreSQL (Internal Network)  │
│  Port: 5432 (not published)     │
└─────────────────────────────────┘
```

**Поток на заявките:**
1. Клиент изпраща HTTP заявка към `http://localhost:8080/api/reports/process`
2. `app-public` получава заявката през публичната мрежа
3. `app-public` извиква `app-internal` през вътрешната мрежа: `http://app-internal:8081/api/partial`
4. `app-internal` обработва данните и записва логове в volume-а
5. `app-internal` връща резултат на `app-public`
6. `app-public` комбинира резултата и го връща на клиента

### Сървиси

- **app-public**: Публично REST API за управление на кампус (students, courses, departments, professors, enrollments, clubs)
- **app-internal**: Вътрешен сървис за нормализация и валидиране на данни
- **postgres**: PostgreSQL база данни

### Мрежи

- **public**: Достъпна от хоста, само app-public е свързано към нея. Позволява на клиентите да достъпват публичното API.
- **internal**: Вътрешна мрежа за комуникация между сървисите (app-public, app-internal, postgres). Изолирана мрежа, недостъпна от хоста. Позволява на сървисите да комуникират помежду си безопасно.

### Портове

- **8080:8080** (app-public): Публикуван порт, достъпен от хоста. Клиентите използват този порт за достъп до API-то.
- **8081** (app-internal): НЕ е публикуван порт. Доступен само в вътрешната мрежа. Използва се от app-public за извикване на вътрешния сървис.
- **5432** (postgres): НЕ е публикуван порт. Доступен само в вътрешната мрежа. Използва се от app-public за достъп до базата данни.

### Volumes

- **postgres_data**: Именуван volume за запазване на данните на PostgreSQL. Монтиран в `/var/lib/postgresql/data` в контейнера. Данните се запазват между рестарти на контейнера.
- **app_internal_logs**: Именуван volume за запазване на логове от app-internal сървиса. Монтиран в `/var/log/app` в контейнера. Логовете се персистират и могат да се проверят след рестарт.

## Инструкции за пускане

`.env` файл е включен в проекта.

### Създаване на базовия image

Първо трябва да създадете базовия Docker image, който се използва от двата сървиса:

```bash
docker build -t campus/base:1.0 ./base
```

Ако сте на Apple Silicon Mac (M1/M2/M3/M4), използвайте:

```bash
docker build --platform linux/amd64 -t campus/base:1.0 ./base
```

### Създаване и старт на средата

След като базовият образ е създаден, стартирайте всички сървиси:

```bash
docker-compose up --build -d
```

Тази команда:
- Изгражда Docker образите за app-public и app-internal
- Създава мрежите (public и internal)
- Създава volumes (postgres_data и app_internal_logs)
- Стартира PostgreSQL базата данни
- Стартира app-internal сървиса (само вътрешна мрежа)
- Стартира app-public сървиса (достъпен на порт 8080)

Проверка на статуса:

```bash
docker-compose ps
```

Всички сървиси трябва да показват статус "Up" и "healthy".

### Мащабируемост (пример с 2 реплики на публичния сървис)

За да демонстрирате мащабиране с множество инстанции на app-public:

1. Спрете текущите сървиси:
    ```bash
    docker-compose down
    ```

2. Стартирайте с 2 инстанции на app-public:
    ```bash
    docker-compose up --scale app-public=2 -d
    ```

3. Проверете статуса:
    ```bash
    docker-compose ps
    ```

Трябва да видите 2 инстанции на app-public (например `campus-management-app-public-1` и `campus-management-app-public-2`). И двете инстанции могат да обработват заявки и ще извикват app-internal през вътрешната мрежа.

### Проверка на персистирани логове

Логовете на app-internal се запазват в именувания volume `app_internal_logs`. За да ги проверите:

#### Преглед на логовете в контейнера:

```bash
docker-compose exec app-internal cat /var/log/app/campus-processor.log
```

#### Проверка на volume-а директно:

```bash
docker volume inspect campus-management_app_internal_logs
```

#### Преглед на всички логове:

```bash
docker-compose logs app-internal
```

#### Тест за персистентност:

1. Създайте заявка, която генерира логове:
    ```bash
    curl -X POST http://localhost:8080/api/reports/process \
      -H "Content-Type: application/json" \
      -d '{"input": "test data"}'
    ```

2. Проверете логовете:
    ```bash
    docker-compose exec app-internal cat /var/log/app/campus-processor.log
    ```

3. Рестартирайте app-internal:
    ```bash
    docker-compose restart app-internal
    ```

4. Проверете логовете отново - те трябва да са все още там:
    ```bash
    docker-compose exec app-internal cat /var/log/app/campus-processor.log
    ```

### Спиране и почистване

#### Спиране на сървисите:

```bash
docker-compose down
```

Тази команда спира и премахва контейнерите, но запазва volumes и мрежите.

#### Спиране и премахване на volumes (ВНИМАНИЕ: изтрива данните):

```bash
docker-compose down -v
```

Тази команда изтрива всички volumes, включително:
- `postgres_data` - данните от базата данни
- `app_internal_logs` - логовете на app-internal

#### Премахване на всички образи:

```bash
docker-compose down --rmi all
```

Тази команда премахва всички Docker образи, създадени от docker-compose.

#### Пълно почистване (контейнери, volumes, мрежи, образи):

```bash
docker-compose down -v --rmi all
```

## Тестване на API-то

### Базов URL

`http://localhost:8080`

### Основни endpoints

#### Students
- `GET /api/students` - Всички студенти
- `GET /api/students/{id}` - Студент по ID
- `POST /api/students` - Създаване на студент
- `PUT /api/students/{id}` - Актуализация на студент
- `DELETE /api/students/{id}` - Изтриване на студент
- `POST /api/students/search` - Динамично търсене

#### Courses
- `GET /api/courses` - Всички курсове
- `GET /api/courses/{id}` - Курс по ID
- `POST /api/courses` - Създаване на курс
- `PUT /api/courses/{id}` - Актуализация на курс
- `DELETE /api/courses/{id}` - Изтриване на курс

#### Departments
- `GET /api/departments` - Всички катедри
- `GET /api/departments/{id}` - Катедра по ID
- `POST /api/departments` - Създаване на катедра
- `PUT /api/departments/{id}` - Актуализация на катедра
- `DELETE /api/departments/{id}` - Изтриване на катедра

#### PRofessors
- `GET /api/professors` - Всички професори
- `GET /api/professors/{id}` - Професор по ID
- `POST /api/professors` - Създаване на професор
- `PUT /api/professors/{id}` - Актуализация на професор
- `DELETE /api/professors/{id}` - Изтриване на професор

#### Enrollments
- `GET /api/enrollments` - Всички записи
- `GET /api/enrollments/{id}` - Запис по ID
- `POST /api/enrollments` - Създаване на запис
- `PUT /api/enrollments/{id}` - Актуализация на запис
- `DELETE /api/enrollments/{id}` - Изтриване на запис

#### Clubs
- `GET /api/clubs` - Всички клубове
- `GET /api/clubs/{id}` - Клуб по ID
- `POST /api/clubs` - Създаване на клуб
- `PUT /api/clubs/{id}` - Актуализация на клуб
- `DELETE /api/clubs/{id}` - Изтриване на клуб

#### Reports (извиква app-internal)
- `POST /api/reports/process` - Обработка на данни през вътрешния сървис

## Валидация че Приложение 2 (app-internal) не е публично достъпно

### Тест 1: Опит за достъп от хоста

app-internal сървисът НЕ трябва да е достъпен директно от хоста:

```bash
curl http://localhost:8081/api/health
```

**Очакван резултат:** Грешка за свързване (connection refused или connection timeout), което потвърждава че app-internal няма публикуван порт и е достъпен само в Docker вътрешната мрежа.

### Тест 2: Проверка на портовете

Проверете кои портове са публикувани:

```bash
docker-compose ps
```

В колоната "Ports" трябва да видите само `0.0.0.0:8080->8080/tcp` за app-public. app-internal НЕ трябва да има публикуван порт.

### Тест 3: Проверка чрез app-public

Въпреки че app-internal не е достъпен от хоста, app-public може успешно да го извика през вътрешната мрежа:

```bash
curl -X POST http://localhost:8080/api/reports/process \
  -H "Content-Type: application/json" \
  -d '{"input": "test data"}'
```

**Очакван резултат:** Успешен отговор с обработени данни, което потвърждава че комуникацията между app-public и app-internal работи правилно през вътрешната мрежа.

### Тест 4: Проверка на мрежите

Проверете кои сървиси са в коя мрежа:

```bash
docker network inspect campus-management_internal
docker network inspect campus-management_public
```

**Очакван резултат:**
- `internal` мрежата трябва да съдържа: app-public, app-internal, postgres
- `public` мрежата трябва да съдържа само: app-public

## Тестови примери

### Пример 1: Създаване на student

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Chackie",
    "lastName": "Jan",
    "email": "chakie.jan@veryhotmail.com",
    "studentNumber": "STU123",
    "dateOfBirth": "2000-01-15",
    "enrollmentDate": "2023-09-01",
    "address": {
      "street": "Main Street",
      "city": "Yew Nork",
      "state": "Cool State",
      "postalCode": "4610",
      "country": "Italy"
    }
  }'
```

### Пример 2: Динамично търсене на students

```bash
curl -X POST http://localhost:8080/api/students/search \
  -H "Content-Type: application/json" \
  -d '{
    "studentName": "John",
    "city": "Plovdiv",
    "enrollmentYear": 2023
  }'
```

### Пример 3: Обработка на report (извиква app-internal)

```bash
curl -X POST http://localhost:8080/api/reports/process \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Example data",
    "metadata": {
      "source": "api",
      "type": "report"
    }
  }'
```

**Очакван отговор:**
```json
{
  "result": "Example data",
  "status": "SUCCESS",
  "timestamp": "2024-01-24T10:30:00",
  "processedCount": 3
}
```

Този endpoint демонстрира комуникацията между app-public и app-internal през вътрешната мрежа.

### Пример 4: Проверка на health endpoints

```bash
# Health check на app-public
curl http://localhost:8080/actuator/health

# Health check на app-internal (от контейнера)
docker-compose exec app-internal curl http://localhost:8081/api/health
```

## Docker образи

### Използвани images

- **campus/base:1.0**: Базов образ с Java 17 и общи инструменти (curl, bash, wget). Използва се като основа за app-public и app-internal.
- **campus/app-public:1.0**: Публично API за управление на кампус. Базира се на `campus/base:1.0`.
- **campus/app-internal:1.0**: Вътрешен сървис за обработка на данни. Базира се на `campus/base:1.0`.
- **postgres:15-alpine**: Официален PostgreSQL образ за базата данни.

## ARG и ENV параметри

### Базов образ (base/Dockerfile)

- **`ARG JAVA_VERSION=17`**: Build аргумент за версията на Java. Използва се по време на build процеса за да се избере правилната версия на Java runtime. Не е достъпно в runtime.

### app-public (app-public/Dockerfile)

**ARG параметри (build-time):**
- **`ARG JAVA_VERSION=17`**: Build аргумент, подаден от docker-compose. Използва се за избор на Java версия при build.

**ENV параметри (runtime):**
- **`ENV SERVER_PORT=8080`**: Конфигурация на порт за runtime. Spring Boot използва тази променлива за да стартира сървиса на правилния порт.
- **`ENV SPRING_PROFILES_ACTIVE=docker`**: Spring профил. Активира docker-специфични конфигурации.

### app-internal (app-internal/Dockerfile)

**ARG параметри (build-time):**
- **`ARG JAVA_VERSION=17`**: Build аргумент, подаден от docker-compose. Използва се за избор на Java версия при build.
- **`ARG MAVEN_VERSION=3.9.6`**: Версия на Maven за build процеса.

**ENV параметри (runtime):**
- **`ENV SERVER_PORT=8081`**: Конфигурация на порт за runtime. Spring Boot използва тази променлива за да стартира сървиса на правилния порт.
- **`ENV SPRING_PROFILES_ACTIVE=docker`**: Spring профил. Активира docker-специфични конфигурации.
- **`ENV LOG_PATH=/var/log/app`**: Път към директорията за логове. Използва се от приложението за да знае къде да записва логовете.

### Разлика между ARG и ENV

- **ARG**: Използва се по време на build процеса. Не е достъпно в runtime. Полезно за параметризиране на build процеса (версии, флагове).
- **ENV**: Използва се в runtime. Доступно е в контейнера след стартиране. Полезно за конфигурация на приложението по време на изпълнение.

## Health Checks

Всички сървиси включват health checks за мониторинг и управление на зависимостите:

- **postgres**: `pg_isready` команда - проверява дали PostgreSQL е готов да приема заявки
- **app-public**: `GET /actuator/health` - Spring Boot Actuator health endpoint
- **app-internal**: `GET /api/health` - кастомен health endpoint

Сървисите изчакват зависимостите да станат healthy преди стартиране (чрез `depends_on` с `condition: service_healthy`). Това гарантира че:
- app-public изчаква postgres да е готов
- app-public изчаква app-internal да е готов
- Всички сървиси стартират в правилния ред

## Message Broker / Redis

В текущата имплементация не се използва message broker или Redis. Проектът демонстрира директна HTTP комуникация между сървисите през вътрешната мрежа.

За бъдещи разширения може да се добави:
- **RabbitMQ** или **Kafka** за асинхронна обработка на задачи
- **Redis** за кеширане на междинни резултати

Ако бъдат добавени, демонстрацията би включвала:
- Създаване на задачи в опашката от app-public
- Обработка на задачи от app-internal
- Кеширане на резултати в Redis за по-бърз достъп

## Структура на проекта

```
campus-management/
├── base/
│   └── Dockerfile              # Базов образ
├── app-public/
│   ├── Dockerfile             # Dockerfile на публичния сървис
│   ├── pom.xml
│   └── src/                    # Spring Boot приложение
├── app-internal/
│   ├── Dockerfile              # Dockerfile на вътрешния сървис
│   ├── pom.xml
│   └── src/                    # Spring Boot приложение
├── docker-compose.yml          # Оркестрация на сървисите
├── .env                        # Променливи на средата (създайте го)
└── README.md                   # Този файл
```

## Полезни команди

```bash
# Стартиране на всичко
docker-compose up --build

# Стартиране на заден план
docker-compose up --build -d

# Спиране
docker-compose down

# Преглед на статуса
docker-compose ps

# Преглед на логовете
docker-compose logs -f

# Мащабиране на app-public до 2 инстанции
docker-compose up --scale app-public=2

# Преизграждане на конкретен сървис
docker-compose build app-public

# Изпълнение на команда в контейнер
docker-compose exec app-public sh
```
