# Express vs Spring: Notes API

The same tiny Notes API written twice, in two steps, plus a third step
that adds server-rendered pages to the Spring version. Run both servers and
compare them.

| Step | Folder | Data lives in |
|---|---|---|
| 1 | `1-in-memory/` | a list in memory (gone on restart) |
| 2 | `2-database/` | PostgreSQL, through a DAO layer |
| 3 | `3-thymeleaf/` | memory by default, or PostgreSQL. Spring only, adds HTML pages: see [`3-thymeleaf/README.md`](3-thymeleaf/README.md) |

Steps 1 and 2 each have an `express-notes/` and a `spring-notes/` folder.
Step 3 has only `spring-notes/`.

| | Express | Spring Boot |
|---|---|---|
| Start | `npm install` then `npm start` | open `spring-notes` in IntelliJ and run `NotesApplication`, or `./mvnw spring-boot:run` |
| URL | http://localhost:3000/notes | http://localhost:8080/notes |

## Endpoints (same in both steps)

| Method | Path | Does |
|---|---|---|
| GET | `/notes` | list all notes |
| GET | `/notes/{id}` | one note, or 404 |
| POST | `/notes` | create a note from `{"text": "..."}`, returns 201 |
| DELETE | `/notes/{id}` | delete a note, 204 or 404 |

## Trying it

Open `requests.http` in IntelliJ and click the green arrow next to a
request. Change `@host` at the top to switch between the two servers.

## Step 1 tasks

1. Run every request against **both** servers.
2. For each line in the Express code, find the line in the Spring code
   that does the same job.
3. The Spring version has two pieces Express doesn't: `NoteDAO` +
   `NoteDaoMemory`, and `Configuration`. What does each one do? Who
   creates `NoteDaoMemory`? (Step 2 shows why they're there.)
4. Run break-it requests 1–5 against both and write down the status codes.
5. Add `PUT /notes/{id}` to the Spring version, through the controller,
   service and DAO.

## Step 2: the database

### Set up the database

Create a database called `notes` and run `2-database/schema.sql` in it
(from pgAdmin, DataGrip, IntelliJ's database tool, or `psql`).

Both servers connect to `localhost:5432/notes` as `postgres` / `postgres`
by default. If yours is different, set environment variables before
starting the server:

| | Express | Spring (IntelliJ: Run → Edit Configurations → Environment variables) |
|---|---|---|
| URL | `DATABASE_URL=postgresql://user:pass@localhost:5432/notes` | `DATABASE_URL=jdbc:postgresql://localhost:5432/notes` |
| User / password | part of the URL | `DATABASE_USERNAME`, `DATABASE_PASSWORD` |

Run both servers at the same time: they share one database, so a note
created through one shows up in the other.

### Step 2 tasks

1. Follow one request from the controller down to the SQL in both
   versions. Which layers exist in both? Which file is new?
2. Compare `1-in-memory/spring-notes` with `2-database/spring-notes`.
   Which Java files are different? Which ones are exactly the same?
3. Count the `await`s in the Express version. How many are in the Spring
   version? Why is the Spring code allowed to just wait?
4. Run break-it request 6 against both. What happened to each server?
5. **Swap the DAO.** In `2-database/spring-notes`, make `Configuration`
   return a `NoteDaoMemory` instead of a `NoteDaoJdbc`. Restart and run the
   requests. Where are the notes now? Which files did you have to change?
6. Add `PUT /notes/{id}` through every layer: controller, service, DAO, SQL.
