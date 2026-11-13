# Proof of Work — SEG3502 Lab 9

## 1. Automated Test & Build Verification

### Command Executed
```powershell
.\gradlew.bat test --rerun-tasks --console=plain
```

### Observed Output Summary
- **Exit Code:** `0` (`BUILD SUCCESSFUL in 3m 18s`, `5 actionable tasks: 5 executed`)
- **Toolchain:** OpenJDK `21.0.12.1+1-LTS`, Gradle `8.8`, Kotlin `1.9.24`, Spring Boot `3.3.1`
- **Gradle Tasks Executed:** `:checkKotlinGradlePluginConfigurationErrors`, `:processResources`, `:compileKotlin`, `:classes`, `:compileTestKotlin`, `:test`

```text
> Task :checkKotlinGradlePluginConfigurationErrors
> Task :processResources
> Task :processTestResources NO-SOURCE
> Task :compileKotlin
> Task :compileJava NO-SOURCE
> Task :classes
> Task :compileTestKotlin
> Task :compileTestJava NO-SOURCE
> Task :testClasses UP-TO-DATE
> Task :test

BUILD SUCCESSFUL in 3m 18s
5 actionable tasks: 5 executed
```

### JUnit Test Suite Results
Source: `build/test-results/test/TEST-seg3x02.tempconverterapi.TempConverterApiApplicationTests.xml`  
Summary: **`tests="4" skipped="0" failures="0" errors="0"`**

| Test Case (`TempConverterApiApplicationTests`) | Behavior Verified | Observed Status |
|---|---|---|
| `sansIdentifiantsRefuse()` | `GET /temperature-converter/celsius-fahrenheit/50` without credentials → `401 Unauthorized` | PASS |
| `user1Accepte()` | `GET /temperature-converter/celsius-fahrenheit/50` with Basic Auth `user1` → `200 OK`, body `"122.0"` | PASS |
| `user2Accepte()` | `GET /temperature-converter/fahrenheit-celsius/122` with Basic Auth `user2` → `200 OK`, body `"50.0"` | PASS |
| `mauvaisMotDePasseRefuse()` | `GET /temperature-converter/celsius-fahrenheit/50` with wrong password → `401 Unauthorized` | PASS |

---

## 2. PII & Privacy Audit

| Artifact | Finding | Remediation Applied on Branch |
|---|---|---|
| `README.md` | Contained 9-digit student ID numbers | Redacted personal student IDs; retained clean course and first-name attribution |
| `RAPPORT_LAB9.pdf` | Page 1 contained 9-digit student ID numbers | Removed `RAPPORT_LAB9.pdf` from branch (`git rm`) |
| `docs/screenshots/*.png` | Inspected 6 captured screenshots | Clean — no student IDs or personal secrets present |
| `src/**/*.kt`, `build.gradle.kts`, `application.properties` | Scanned all text files for `300\d{6}` and credentials | Clean — only intentional academic in-memory demo users (`user1`/`pass1`, `user2`/`pass2`) |

> **Git History Advisory:** Because the baseline `main` commit (`f86065a`) predates this branch, the removed PDF and original `README.md` remain reachable in commit history until history is rewritten/squashed prior to making the repository public.

---

## 3. Visual & Structural Assets Verified

- `docs/images/security-architecture.svg` — Validated XML structure via Python `xml.etree.ElementTree.parse`; embedded in `README.md`.
- `docs/screenshots/` — Retained verified runtime captures (`lab9_noauth.png`, `lab9_user1.png`, `lab9_user2.png`, `lab9_wrong.png`, `lab9_badinput.png`, `lab9_swagger.png`) and linked in `README.md`.
