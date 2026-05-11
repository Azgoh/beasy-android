# Beasy Android App

Android application built with Java + XML for managing professional services.
Users can register, log in, browse professional profiles, and book appointments.

## Tech Stack

- Java
- XML (Views)
- Room (local SQLite database)
- BCrypt (password hashing)
- ViewBinding
- Material Design components

## Project Structure

```
app/
├── data/
│   ├── entity/     → Room table definitions (UserEntity)
│   ├── dao/        → Database queries (UserDao)
│   └── AppDatabase.java  → Room database singleton
├── ui/             → Activities (MainActivity, RegisterActivity, LoginActivity)
├── res/
│   ├── layout/     → XML layouts
│   ├── drawable/   → Shapes, button styles
│   └── values/     → Colors, strings
```

## Features

- [x] User registration (saved to local Room database)
- [x] User login (BCrypt password verification)
- [ ] Home screen with professional profiles
- [ ] Appointment booking

## How to Run

1. Clone the repo
2. Open in Android Studio
3. Click **Run ▶**

No backend required — all data is stored locally using Room.

## Resetting the Database

```bash
adb shell pm clear com.example.beasy
```

Or simply uninstall and reinstall the app.