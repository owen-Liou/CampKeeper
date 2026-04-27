# CampKeeper

Multi-module Spring Boot project.

## Modules

- `camp-base`: Core logic and shared services.
- `camp-config`: Configuration center and security-related properties.
- `camp-app`: Application startup module.

## Package naming

`groupId` contains a hyphen (`com.github.owenliou`), which is not valid in Java package names.
This project uses `com.github.owen_liou.campkeeper` for Java packages.

## Quick start

```powershell
mvn clean test
mvn -pl camp-app spring-boot:run
```

