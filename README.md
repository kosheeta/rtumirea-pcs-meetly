# Запуск Meetly

Нужны JDK 25 или 26 и запущенный Docker Desktop. Все команды выполняйте в корне проекта.

**1. Настройте подключение к базе.** Создайте файл `.env` в корне проекта (или проверьте существующий):

```dotenv
DB_URL=jdbc:postgresql://localhost:5432/meetly
DB_USER=postgres
DB_PASSWORD=postgres
```

**2. Запустите базу данных:**

```shell
docker compose up -d
```

Дождитесь запуска базы данных.

**3. При первом запуске создайте таблицы:**

```shell
docker compose cp src/main/resources/db/migration/initial.sql postgres:/tmp/initial.sql
docker compose exec -T postgres psql -U postgres -d meetly --single-transaction -f /tmp/initial.sql
```

Повторно этот шаг не выполняйте: скрипт удаляет существующие данные.

По желанию после создания таблиц заполните базу тестовыми данными:

```shell
docker compose cp src/main/resources/db/migration/seed.sql postgres:/tmp/seed.sql
docker compose exec -T postgres psql -U postgres -d meetly --single-transaction -f /tmp/seed.sql
```

`seed.sql` заменяет существующие данные тестовыми.

**4. Запустите программу.**

macOS / Linux:

```shell
./gradlew -q --console=plain run
```

Windows PowerShell:

```powershell
.\gradlew.bat -q --console=plain run
```

В консоли появится меню. Для выхода выберите `0`.

После работы остановите базу:

```shell
docker compose stop postgres
```

При следующем запуске выполните только шаги 2 и 4.
