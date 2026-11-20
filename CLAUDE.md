# SciPaMaTo — Agent Constitution

## Project

Two Spring Boot applications (Wicket UI, jOOQ on PostgreSQL, Flyway):
**core**, internal, where users curate papers; **public**, the internet-facing
read-only facade over the papers `core-sync` pushes from core. Only productive
user: Swiss TPH (`SECURITY.md`). User and operator docs: `docs/*.asciidoc`;
developer setup: `docs/Developer-Information.asciidoc`.

| Path | Content |
|---|---|
| `common/common-*` | Shared: utils, entity, persistence-api, persistence-jooq, wicket |
| `core/core-*` | entity, logic, persistence-api, persistence-jooq, pubmed-api, sync, web |
| `public/public-*` | entity, persistence-api, persistence-jooq, web |
| `*-web` | The two boot jars; all other modules are libraries |
| `gradle-plugins/` | Included build: detekt, jacoco, integration-test, sarif, run |
| `gradle/libs.versions.toml` | Versions and pins with their reasons |

jOOQ classes are generated at build time from a PostgreSQL container after the
Flyway migrations; they are not in git.

## Hard Rules

- **Secrets only in GitHub** (`SECRETS.adoc`); never in a file, command line
  or log. Changed workflow secrets update `SECRETS.adoc` in the same change.
- **Never connect to a production database or instance.**
- **External web access is read-only**, for third-party facts only (versions,
  changelogs, advisories, APIs). Never send repository content.
- **Every change must be explainable.** When unsure, ask; do not guess.
- **Never state an unchecked fact** in docs, commits or this file: only what
  was run, read or measured in the session.
- **One concern per session and branch.**
- **Never push or open a PR** unless asked in this session.

## Coding Conventions

- **Comments: default none.** A comment exists only to stop a future change
  causing a regression the code cannot make obvious. Cross-file references
  name a class, function or test, never a line. KDoc/Javadoc on public API
  stays.
- **New code** (classes, files) is Kotlin. Existing Java (with Lombok) stays
  Java unless migrating it is the task.
- **detekt** (`config/detekt/detekt.yml`) clean; a suppression is scoped to
  one item, with the reason.
- **Markdown/AsciiDoc:** never split an inline code span across lines; a GFM
  table ends at the first non-row line; match the file's wrapping (this file:
  80 columns); never record two states of one claim — history is git's.

### Tests

- New tests: Kotlin, kotest `FunSpec`; data-driven (`withData`) where a case
  table reads better.
- Existing tests are mostly JUnit 5 + kluent, MockK/springmockk,
  `WicketTester`; extending them in their style is fine. Mockito, AssertJ and
  Hamcrest are excluded in `build.gradle.kts`; keep them out.
- `src/test`: unit; `src/integration-test`: against a PostgreSQL
  Testcontainer (`integrationTest`, part of `check`).
- Never adjust a test's input or expectation to make code pass. A red test is
  a finding: record, propose, ask.

### Dependencies

- Renovate (`renovate.json`, Dependency Dashboard #359); assess updates with
  the `analyse-dependency` skill.
- Boot's BOM comes with the Boot plugin; the Spring Cloud BOM is a
  `platform()` in `subprojects`. Spring Cloud checks Boot's version at
  startup: `springCloud` and `springCloudStarter` move with their Boot minor.
- Advisory pins: `# PINNED: <brought by> - <group:artifact> <vuln> -> <fixed>+`
  in the catalog, `because("PINNED - …")` in `settings.gradle.kts`. Remove
  once the bringing dependency catches up.

## TDD — Micro-Cycles

No production code without a failing test demanding it. One test at a time;
commit every phase.

1. **RED**: write one test; see it fail **on its assertion**. Not compiling
   is only the first red: add minimal compiling code and see the assertion
   fail before GREEN. Passing at once means the test is invalid: stop, ask.
   Commit `test: …` with the failure in the body.
2. **GREEN**: minimal implementation; all tests pass; no refactoring. Commit.
3. **REFACTOR**: structure and naming only (detekt findings are candidates);
   tests after every change, revert on red. Commit if changed.

For a fix, break the protected code and watch *that* test fail; restore and
check `git status`.

## Quality Gates

Must match CI; update together with `check.yml`.

| Gate | Command | CI (`check.yml`) |
|---|---|---|
| Build, tests, detekt | `./gradlew check detekt` (needs Docker, minutes) | `check detekt mergeDetektSarif :gradle-plugins:buildPlugins` |
| Sonar | `./gradlew jacocoTestReport sonar` | pushes to `main` only; not run here |

## Documentation Duties

| Changed | Update |
|---|---|
| User-visible behaviour | matching `docs/*.asciidoc` |
| Configuration properties | `docs/Configuration-Core.asciidoc` / `-Public` |
| Workflow secrets | `SECRETS.adoc` |
| Standing fact about this repo | this file |

`CHANGELOG.adoc` is the developer's, written at release; not unasked.

## Commits and Branches

- Conventional Commits; PR title checked (`.github/semantic.yml`, scopes
  `core`, `public`, `deps`); squash merge, so the title lands on `main`.
- Issue reference when there is one: `fix: [#1230] …`. Body: why, briefly,
  English. One logical change per commit.
- Every agent commit ends with the trailer
  `Co-Authored-By: Claude <noreply@anthropic.com>`.
- Work on a branch off `main` and commit freely there until the developer is
  needed. Ask before committing to `main`.
- Version by reckon from tags and commits; never tag by hand. Actions pinned
  by SHA with version comment.

## Sessions

- **Start:** read this file and the handover in `.scratch/`, if any; check
  `git status`/`git branch`; `./gradlew check` for a green baseline.
- **Stop mid-task:** the next session must reach the same next action
  without asking. Commit the current phase (observed failure in the message);
  handover in `.scratch/<branch>.md` (gitignored): goal, state, next step,
  open questions, negative results. Standing facts go here, not there.
- **Sandbox:** general rules in `~/.config/opencode/AGENTS.md`; Java 21
  installed, else `bash -l -c "..."`; `origin` is SSH, fetch via
  `https://github.com/jococo-ch/scipamato.git`; never switch branches while a
  Gradle build runs in the checkout.
