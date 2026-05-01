# Beasy Android App 

Android application built with Java + XML that communicates with a [Spring Boot backend](https://github.com/Azgoh/Practice-API) using Retrofit.
The app currently supports user registration with backend integration.

# Tech Stack

- Java
- XML (Views)
- Retrofit (HTTP client)
- OkHttp (logging)
- Gson (data handling)
- Material Design components

# Project Structure

```
app/ 
├── api/        → Retrofit setup (ApiClient, ApiService)
├── model/      → Request/Response models
├── ui/         → Activities (MainActivity, RegisterActivity)
├── res/        → XML layouts, colors, drawables
```

# Backend Communication

The app uses Retrofit to communicate with the backend.

## Base URL (Development)

` http://192.168.100.39:8080/ `
> Replace with you local machine IP when running on a real device.

# How to Run

## 1. Start backend
Make sure Spring Boot is running on:
`http://localhost:8080`

## 2. Update base URL

Set your PC IP in: `ApiClient.java`

## 3. Run Android app
   - Connect device or emulator
   - Press Run ▶