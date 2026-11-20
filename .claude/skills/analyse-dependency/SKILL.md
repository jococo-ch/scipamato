---
name: analyse-dependency
description: Assesses and lands dependency updates in the scipamato repo - open Renovate pull requests, updates without a PR (e.g. a patch Renovate skipped), and Dependabot security alerts - across the Gradle version catalog, the Gradle wrapper and the GitHub Actions. Reads changelogs, checks fixed CVEs and bugs against our code, checks the Spring Boot / Spring Cloud coupling, commits what a Renovate branch needs to pass CI, and reports one assessment per update for the pull request and one deployment recommendation per batch.
disable-model-invocation: true
argument-hint: [nothing for open Renovate PRs, a PR number, a dependency and version, or alerts followed by pasted alerts]
allowed-tools: Read Grep Glob WebFetch Bash(git:*) Bash(gh:*) Bash(./gradlew:*) Bash(curl:*) Bash(jq:*) Bash(mktemp:*)
---

Task: $ARGUMENTS

This skill is an index and a procedure. The working rules live in
`CLAUDE.md` (Hard Rules, Dependencies, Commit Style); read them there. What
to assess per update is in [assessment.md](assessment.md).

## Entry points

| `$ARGUMENTS` | Source | Commits |
|---|---|---|
| empty, or a PR number | open Renovate pull requests | on the PR's `renovate/*` branch, if CI needs it |
| a dependency and version | an update without a PR | on a new branch off `main`, after asking |
| `alerts` and pasted alerts | Dependabot security alerts | none; the fix is a proposal |

One update per assessment. A grouped PR (`spring boot`, `spring cloud`,
`feign monorepo`) is assessed per dependency it moves, with one CI section.
Whatever the entry point, end with one deployment recommendation for the
batch.

## Where versions are pinned

| What | Where | Held by |
|---|---|---|
| Libraries and plugins | `gradle/libs.versions.toml` | the catalog; `# PINNED:` comments give the reason |
| Spring Boot BOM | the Boot plugin's version (`springBoot`) | `io.spring.dependency-management` in every subproject |
| Spring Cloud BOM | `springCloud`, imported as `platform()` in `subprojects` | Spring Cloud's compatibility check at startup |
| Buildscript classpath | `settings.gradle.kts` (`strictly`, `because("PINNED - …")`) | the jOOQ Gradle plugin's classpath only |
| Gradle | `gradle/wrapper/gradle-wrapper.properties` | `wrapper-validation` in `check.yml` |
| GitHub Actions | `.github/workflows/check.yml` | the commit SHA after `@`, version as comment |

Couplings:

- **Spring Boot ↔ Spring Cloud.** A Spring Cloud train supports specific
  Boot minors, and `CompatibilityVerifierAutoConfiguration` fails every
  Spring context on a mismatch, with `CompatibilityNotMetException` naming
  the accepted Boot versions. `springCloud` and `springCloudStarter` move
  with the Boot minor; a Boot minor and its train are one step.
- **Spring Boot ↔ Spring Boot Admin, wicket-spring-boot-starter, springmockk.**
  Check their supported Boot version before a Boot minor.
- **Wicket, wicketstuff, wicket-bootstrap** move together; check the Spring
  version Wicket is built against (`wicket-parent` pom).
- **feign (`openfeign`) ↔ `spring-cloud-starter-openfeign`.** Our feign
  version is above the starter's; the Spring Cloud platform must not pull it
  down.
- **Buildscript pins** in `settings.gradle.kts` act on the jOOQ plugin, not on
  the applications; the app's version of the same library comes from Boot.

## Renovate pull requests

1. Fetch over HTTPS; `origin` is an SSH remote:
   `git fetch https://github.com/jococo-ch/scipamato.git '+refs/heads/renovate/*:refs/remotes/origin/renovate/*' '+refs/heads/main:refs/remotes/origin/main'`
2. List them: `gh pr list --repo jococo-ch/scipamato --state open`, keeping
   the heads that start with `renovate/`.
3. Per PR, read:
   - the body (release notes): `gh pr view <n> --repo jococo-ch/scipamato --json title,body`;
   - CI: `gh pr checks <n> --repo jococo-ch/scipamato`, and for a failure
     `gh run view <id> --repo jococo-ch/scipamato --log-failed`. Read the
     root cause (`Caused by`) before calling it the update's fault; a red
     run on an old base may be fixed on `main`;
   - the diff: `git diff origin/main...origin/<head>`.
4. Check out the head and merge `origin/main` as it stands:
   `git checkout -b <head> origin/<head> && git merge origin/main`.
   A conflict is a finding; abort and report.
5. Assess along [assessment.md](assessment.md).
6. If the branch needs a commit to pass CI, make it (next section).
7. Report per [assessment.md](assessment.md)'s template.
8. After the last PR, the deployment recommendation for the batch.

## Committing on a Renovate branch

- Only on the local branch of the PR's own head from step 4.
- Only the merge of `origin/main` and what CI needs for this bump: aligning
  a coupled entry in `libs.versions.toml`, or adapting code to an API the
  bump changed, with every existing test and its input unchanged.
- Read `git diff` before committing, and diff the classpaths (assessment,
  point 1): a coordinate moving that the bump did not intend is a finding.
- Message: `chore(deps): <what> for <dependency> <version>`, ending with the
  attribution trailer from CLAUDE.md.
- Never push; the developer does. Say in the report that Renovate stops
  updating a branch someone else has committed to.

## Checks

Free, always, on the merged branch: `./gradlew check detekt --continue`
(needs Docker; several minutes). Count the results from
`*/build/test-results/**/TEST-*.xml`. Sonar runs on `main` only.

Do not switch branches in the checkout while a build runs; use a
`git worktree` under `/tmp` for a second state and remove it afterwards.

## Network and downloads

- Read-only, and only facts about third-party code: changelogs, advisories,
  release metadata, published artifacts. Never send repository content.
- Reachable: `github.com`, `api.github.com`, `repo.maven.apache.org`,
  `api.osv.dev`. A blocked host: report host and port, do not work around it.
- A downloaded jar or pom goes into its own new directory from `mktemp -d`,
  outside the repository. Inspect it (`unzip -l`, `javap`, reading the pom);
  never run it.

## Stop and ask before

- A change beyond the update's own: moving a BOM import, removing a pin, a
  Renovate config change, a refactoring the update invites.
- A commit that is not on a `renovate/*` branch.
- Changing a PR's title.

## Never

- Push, merge, or open a PR.
- Put a secret, key or token into any file or command line.
- Loosen a pin, exclude a test or disable the compatibility verifier
  (`spring.cloud.compatibility-verifier.enabled=false`) to make a bump pass.
- Edit `CHANGELOG.adoc` unasked.

## Advisories

There is no advisory check in CI. The sources are:

- Dependabot alerts, fed by GitHub's automatic Gradle dependency submission:
  `gh api repos/jococo-ch/scipamato/dependabot/alerts`.
- `api.osv.dev` per coordinate and version:
  `POST https://api.osv.dev/v1/query` with
  `{"package":{"ecosystem":"Maven","name":"<group>:<artifact>"},"version":"<v>"}`.
