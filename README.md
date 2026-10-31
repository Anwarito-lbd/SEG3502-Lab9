# SEG3502 — Laboratoire 9 — Sécurité (Spring Security)

Étudiants : Anwar (300254189), Manasse (300268472)

API REST de conversion de température sécurisée avec **Spring Security** :
authentification **In Memory** (utilisateurs `user1/pass1` et `user2/pass2`, rôle USER)
et **HTTP Basic** exigée pour toutes les requêtes (`anyRequest().authenticated()`).

Fichier principal : `src/main/kotlin/seg3x02/tempconverterapi/security/SecurityConfig.kt`.

## Installer et exécuter (Java 21)

```bash
git clone https://github.com/Anwarito-lbd/SEG3502-Lab9.git
cd SEG3502-Lab9
./gradlew bootRun          # Windows : gradlew.bat bootRun
```

## Tester

```bash
curl -i http://localhost:8080/temperature-converter/celsius-fahrenheit/50                  # 401
curl -u user1:pass1 http://localhost:8080/temperature-converter/celsius-fahrenheit/50       # 122.0
curl -u user2:pass2 http://localhost:8080/temperature-converter/fahrenheit-celsius/122      # 50.0
curl -i -u user1:wrong http://localhost:8080/temperature-converter/celsius-fahrenheit/50    # 401
./gradlew test             # 4 tests MockMvc
```

Swagger UI : http://localhost:8080/swagger-ui.html (identifiants demandés par le navigateur).
