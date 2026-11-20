# Assessing one dependency update

Read by `SKILL.md` for every update. Mark every claim **read** (changelog,
advisory, code, log), **measured** (a command run here) or **inferred**.
Where a question cannot be answered from what is reachable, say so.

## What we expose

Two Spring Boot jars. **public** faces the internet with anonymous access;
**core** is used internally behind a login (Spring Security). Both use
Wicket, jOOQ and PostgreSQL. `core-pubmed-api` calls the PubMed API through
OpenFeign and parses its XML (JAXB); `core-web` generates PDFs with
JasperReports; `core-sync` runs Spring Batch jobs from core into public. A
CVE in a web-, security-, XML- or PDF-path weighs more for public than for
core.

## What to establish

1. **Where it lands.** Diff
   `./gradlew -q :<module>:dependencies --configuration runtimeClasspath`
   between `origin/main` and the branch, at least for `core-web` and
   `public-web`; compile and testRuntime classpaths for the modules
   concerned. Only a coordinate that moves in a `*-web` runtime classpath
   ships. A Gradle plugin, the wrapper or an action ships nothing.
2. **Changelog.** The PR body first, then the release pages it links. For a
   Boot update, also the managed versions: compare the `<*.version>`
   properties of `spring-boot-dependencies-<v>.pom` on Maven Central.
3. **Fixed CVEs.** From the changelog and from `api.osv.dev` for both
   versions, for every coordinate that moves in point 1.
4. **Per CVE, are we affected.** Name the vulnerable function, feature or
   configuration and search our code and `application*.properties` for it.
   Affected, not affected, or cannot tell, with the evidence.
5. **Fixed bugs that concern us.** Search the code, the properties and
   `git log` for the dependency and the symptom. A workaround the fix makes
   obsolete (comments with an issue link, e.g. spring-boot#6907 in the
   `application.properties` of the persistence modules) is a proposal.
6. **New features** that replace something we built or make something easy.
7. **Incompatibilities.** The couplings `SKILL.md` names; anything moving
   beside the bump in point 1. For Spring Cloud, the accepted Boot versions
   from a failing run's `CompatibilityNotMetException`, or from the train's
   release notes.
8. **CVE pins this update makes redundant.** For each `# PINNED:` in
   `libs.versions.toml` and `because("PINNED - …")` in
   `settings.gradle.kts`: does the update now bring a version at or past
   every advisory's fixed version (check them all on `api.osv.dev`, not only
   the one named)? Then the pin can go. Measure it: remove the pin on a
   scratch state and diff the classpaths. A proposal, never a commit made
   unasked.
9. **Regression risk.** Patch, minor or major; how much of our code relies on
   it; whether a test exercises that path (Wicket pages via `WicketTester`,
   persistence via the integration tests, PubMed via
   `core-pubmed-api:integrationTest`).
10. **Supply chain.** Age of the release (`renovate.json` sets no
    `minimumReleaseAge`), a changed publisher or group, new coordinates in
    point 1, a new Gradle plugin. For an action, whether the version comment
    matches the pinned SHA. Say where the signal is weak.
11. **What CI needs.** An aligned coupled entry, a code adaptation, or a
    second update that belongs to the same step (e.g. a Boot minor with its
    Spring Cloud train). Read the PR's CI run first.
12. **Recommendation.** Merge, merge after the commit made here, merge
    together with another PR, hold, or close, with the reason.

## The batch: deployment

1. **What ships**: the coordinates that move in the runtime classpath of
   `core-web` and `public-web` (point 1).
2. **How**: merge to `main`, release (tag; version from reckon), then the
   jars are replaced on the server (`docs/Operations.asciidoc` →
   Deployment). Steps are named, never run.
3. **How soon**: now only for an advisory that point 4 found affecting us,
   with public before core; otherwise with the next release.
4. **Confidence**: high, medium or low, resting on the read, measured and
   inferred marks, and what would raise it.

## Report template

One per update, ready for the PR, in its own fenced block with four
backticks.

```markdown
## Assessment: <dependency> <old> → <new>

### Recommendation

<merge / merge after fix commit / merge with #n / hold / close>: <one sentence>

### Lands in

<core-web / public-web / build / test / CI>

### Changelog

<link>; <what changed, in two or three lines>

### CVEs fixed

<id: affected / not affected / cannot tell, and why> or none

### Bugs fixed that concern us

<bug: workaround to remove / exposure> or none

### Features of use

<feature: what it replaces or enables> or none

### Incompatibilities

<what> or none found

### CVE pins

<pin: still needed / removable, and why> or none touched

### Regression risk

<low / medium / high>: <why>

### Supply chain

<signals, and how weak they are>

### CI

<state of the PR's run; commit made on the branch, if any>

### Not verified

<what this assessment could not check>
```

Once per request, after the last update:

```markdown
## Deployment: <the updates of this batch>

### Recommendation

<release now / with the next release / nothing to deploy>: <one sentence>

### What ships

| Update | core-web | public-web | Evidence |
|---|---|---|---|
| <dependency> <old> → <new> | <yes / no> | <yes / no> | <measured / read / inferred: how> |

### Why

<the urgency, and what else the next release would carry>

### Confidence

<high / medium / low>: <what it rests on, and what would raise it>
```
