# Mulungushi Registrar - MVVM layout

Android (Java/XML Views) app + Node.js/Express/MySQL backend for the ICT361 lab-group registration brief.
The existing app code was reorganised into MVVM packages; nothing in the screens' behaviour was changed.

## Project layout

```
MulungushiRegistrar/
├── app/                                   Android app  (package zm.ac.mulungushi.registrar)
│   └── src/main/java/zm/ac/mulungushi/registrar/
│       ├── ui/                            VIEW      - screens only draw state and forward taps
│       │   ├── auth/        LoginActivity, RegisterActivity, ForgotPasswordActivity, SplashActivity
│       │   ├── student/     StudentHomeActivity, MyGroupActivity, GroupChangeActivity, EditDetailsActivity
│       │   ├── lecturer/    LecturerDashboardActivity, LecturerRosterActivity, RequestsActivity, EditStudentSheet
│       │   ├── sync/        SyncActivity
│       │   └── common/      BottomNav, Feedback, Notifier, StudentAdapter, SignOutSheet, ConnectivityBanner, ...
│       ├── viewmodel/                     VIEWMODEL - LoginViewModel (state that survives rotation)
│       ├── repository/                    REPOSITORY- StudentRepository, AuthService (interface), Auth (picks the implementation)
│       ├── data/
│       │   ├── model/       Student                     plain model used by the UI
│       │   ├── local/       AppDatabase, dao/, entity/  LOCAL STORAGE (Room)
│       │   ├── remote/      ApiService, RetrofitClient, TokenStore, ApiConfig, dto/   REMOTE (Retrofit/OkHttp)
│       │   ├── sync/        SyncScheduler, SyncWorker   WorkManager offline queue -> server
│       │   └── demo/        DemoAuthService, DemoAccounts   in-app stand-ins used until the server is connected
│       └── util/            CrashLog, validation/ (LoginValidator, StudentValidator, ContactValidator)
├── backend/                               BACKEND - Node.js + Express + MySQL (see backend/README.md)
│   ├── database/schema.sql                tables, keys, versions, deletion markers, operation receipts
│   ├── scripts/                           init-db.js, seed.js (fictitious data only)
│   ├── src/                               app.js, server.js, routes/, services/, middleware/, validation.js
│   └── test/                              node:test checks
└── gradle/, build.gradle.kts, ...
```

Data flow: `Activity (ui) -> ViewModel -> Repository -> local (Room) and/or remote (Retrofit -> Node.js -> MySQL)`.
The app never talks to MySQL directly.

## What is wired and what is not (please read)

| Part | Status |
|---|---|
| ui / viewmodel / repository / data.model / util | The existing app, moved into packages. Same behaviour as before. |
| `data/local` (Room), `data/remote` (Retrofit), `data/sync` (WorkManager) | **New, ready to use, but not yet called by the screens.** `StudentRepository` is still the in-memory version with the demo seed, exactly as in your upload. Next step: make `StudentRepository` read/write `StudentDao` and queue `PendingOperationEntity` rows, and call `ApiService` for the online path. |
| `backend/` | **New, complete API.** Syntax-checked and 13 tests pass (validation + route guards). The MySQL transaction paths (group capacity, receipts) have **not** been run against a real MySQL server yet - run them on your machine (see backend/README.md). |
| Other screens | Only `LoginViewModel` existed as a ViewModel. The other activities still call `StudentRepository` directly. Moving each into its own ViewModel is a follow-up step. |

The Android code could not be compiled here (no Android SDK). The package/import structure was checked by script and the
non-UI layers (viewmodel, repository, data.model, data.demo, util) were compiled against stubs. Please do
**Build > Clean Project, then Rebuild** in Android Studio; if a Gradle sync complains about a dependency version,
change it in `gradle/libs.versions.toml`.

## Run the Android app
1. Open the `MulungushiRegistrar` folder in Android Studio. It will create `local.properties` itself.
2. Sync Gradle, then Run. The demo accounts still work with no server (lecturer `b.nyirenda@mulungushi.edu.zm` / `Lecturer#2026`, student any 9-digit number).

## Run the backend
```
cd backend
cp .env.example .env        # fill in DB password and JWT_SECRET
npm install
npm run db:init             # creates the database and tables (MySQL 8)
npm run seed                # one lecturer + fictitious students and claim codes
npm start
npm test
```
For the emulator the app reaches your computer at `10.0.2.2` (set in `data/remote/ApiConfig.java`). Use HTTPS for the hand-in.

## Notes on what changed from your upload
- Used the `MulungushiRegistrar.zip` sources as the base; the `ict361_home_roster_screens-ui-pass2.zip` is an older, smaller copy of the same screens (e.g. RegisterActivity 120 lines vs 487), so nothing was taken from it.
- Left out: the duplicate `app/app/...` source tree, the stray `app/src/main/java/java/...SplashActivity.java` (wrong package `java.zm...`), the stray `res/res/layout/activity_student_home.xml`, and `local.properties`.
- Manifest: activity names now use the new packages (`.ui.auth.LoginActivity`, ...) and the INTERNET permission was added.
- Classes and members that other packages now use were made `public` (they were package-private before).
- Unit tests moved next to the classes they test (`util/validation`).
