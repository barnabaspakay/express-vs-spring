# Step 3: Thymeleaf pages

This is the same Notes app as step 2, now with HTML pages built on the
server.

| URL | What you get | Served by |
|---|---|---|
| http://localhost:8080/notes | the list page + a "new note" form | `NotePageController` (`@Controller`) |
| http://localhost:8080/notes/1 | one note + a delete button | `NotePageController` |
| http://localhost:8080/api/notes | the same data as JSON | `NoteController` (`@RestController`) |

In this step the JSON API moved to `/api/notes`, like AskMate's
`api/question`. If you use `requests.http`, change `/notes` to
`/api/notes` in the requests. `/notes` now returns pages.

It runs **in memory**, so you don't need a database. To use Postgres,
run `schema.sql` (it adds a `created` column) and switch
`Configuration` to `NoteDaoJdbc`.

## The big idea

```
Browser page  ──►  NotePageController (@Controller)      ─┐
                   returns "notes" + Model → HTML         ├──►  NoteService ──► NoteDAO
.http/Postman ──►  NoteController (@RestController)      ─┘
                   returns List<Note> → JSON
```

- `@RestController`: what you return **is** the response (JSON).
- `@Controller`: a returned `String` is a **template name**. Spring finds
  `src/main/resources/templates/<name>.html`, fills it with the data you
  put into `Model`, and sends HTML.
- Both controllers call the **same service**. Neither one has logic in it.

## Thymeleaf cheat sheet

| You want to… | Write | Notes |
|---|---|---|
| Send data to the page | `model.addAttribute("notes", list);` | in the controller method |
| Show a value | `<span th:text="${note.text}">placeholder</span>` | replaces the placeholder and escapes HTML |
| Loop | `<li th:each="note : ${notes}">…</li>` | repeats the element it's on |
| Show only if… | `th:if="${notes.isEmpty()}"` / `th:unless="…"` | |
| Link with an id | `th:href="@{/notes/{id}(id=${note.id})}"` | never glue URLs together by hand |
| Format a date | `${#temporals.format(note.created, 'yyyy-MM-dd HH:mm')}` | for `LocalDateTime` |
| Join text | `th:text="'Note #' + ${note.id}"` | |
| Form target | `<form th:action="@{/notes}" method="post">` | |
| Read a form field | `@RequestParam String text` | matches `<input name="text">` |
| Read many fields | `@ModelAttribute NewQuestionDTO question` | each record field matches an input `name` |
| Redirect after POST | `return "redirect:/notes/" + note.id();` | stops F5 from re-sending the form |
| Link a CSS file | `th:href="@{/css/style.css}"` | the file lives in `static/css/` |
| Put a data attribute on a button | `th:data-id="${note.id}"` | for JavaScript, see below |
| Reuse a header | `th:replace="~{fragments :: header}"` | see `fragments.html` |
| Show login state | `th:if="${session.userId != null}"` | after `session.setAttribute("userId", …)` |

## Which AskMate task needs what

| AskMate task | Thymeleaf you'll use |
|---|---|
| 1. All questions | `th:each`, `th:text`, `#temporals.format`, `@{…}` links. The answer count comes from **SQL** into the DTO, not from the template. |
| 2. Question detail | `@PathVariable` in the page controller, `th:each` over the answers |
| 3. Create a question | `th:action` form, `@ModelAttribute` or `@RequestParam`, `redirect:/question/{id}` |
| 4. Create an answer | a form on the detail page, posting to a URL with the question id in it |
| 5–6. Delete | `<form method="post">` to e.g. `/question/{id}/delete` → `redirect:`, or `fetch` with `DELETE` (below) |
| 7–9. Register / login / logout | forms + `HttpSession` in the page controller, `th:if` for the logged-in parts |
| 12, 14, 15. Accept / edit | the same as delete: a POST form, or `fetch` with `PATCH` / `PUT` |
| 13. Search | `<form method="get">` + `@RequestParam`, then the same list as Task 1 |

The REST endpoints in the task list (`api/question/...`, `api/answer/...`)
still have to exist. Your page controller and your REST controller share
the services.

## HTML forms only know GET and POST

So a page uses `POST /question/{id}/delete` instead of `DELETE`. If you
want to call the real REST endpoint instead, use a few lines of JavaScript.

```html
<button class="delete-btn" th:data-id="${question.id}">Delete</button>
<script th:src="@{/js/delete.js}"></script>
```

```js
// static/js/delete.js
document.querySelectorAll(".delete-btn").forEach((button) => {
    button.addEventListener("click", async () => {
        const response = await fetch(`/api/question/${button.dataset.id}`, { method: "DELETE" });
        if (response.ok) {
            window.location.href = "/questions";
        }
    });
});
```

## When it goes wrong

| You see | Why | Fix |
|---|---|---|
| The page shows the plain word `notes` | the controller is a `@RestController` | use `@Controller` for pages |
| 500, `TemplateInputException: Error resolving template [noets]` | the returned name doesn't match a file in `templates/` | check spelling and folder |
| The app won't start: `Ambiguous mapping` | two methods own the same method + path (e.g. your page and `ExampleController` on `/`) | change one path, or delete `ExampleController` |
| `POST /api/question` gives 404 | the mapping is `@PostMapping("/")`, which means `/api/question/` with a slash | use `@PostMapping` with no argument |
| 500, error mentions `SpelEvaluationException` | a value in `${…}` is `null`, or a field name is wrong | check what you put in `Model`; use `${question?.title}` for values that may be null |
| Template change doesn't appear | IntelliJ hasn't rebuilt it | Build (Ctrl+F9) or restart |
| 400 after submitting a form | an `<input name="…">` doesn't match the `@RequestParam` name | make the names the same |

## Tasks

1. Open `/notes`, add a few notes, delete one. Watch DevTools → Network:
   which requests are POST, which are 302, which are GET?
2. Find every `th:` attribute in `notes.html`. For each one, say what
   Java code it is like.
3. Add an "edit" feature: a form on the detail page that changes the text.
   Go through the page controller, service and DAO.
4. In AskMate: make a `/questions` page that lists every question with
   its title and date. Each title links to its detail page.
