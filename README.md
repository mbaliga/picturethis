# PictureThis

Android-first camera and interactive photographic scene editor: manual capture, guided multi-image photography, GUI-driven augmentation, virtual optics, movable lights, and persistent protection.

- [Product and engineering specification](docs/SPEC.md)
- [Codex execution plan](docs/EXECUTION_PLAN.md)
- [Prototype instructions and honest limits](docs/PROTOTYPE.md)
- [Implementation status](docs/STATUS.md)

The supplied Android app is **PictureThis Lab**, an offline interaction prototype. It imports images, simulates scene controls and optics, supports exact painted protection, and exports previews. It does not contain an AI model or production camera pipeline. Both OLED and skeuomorphic themes are included.

Build with Java 17 and Gradle 8.11.1: `gradle --no-daemon :app:assembleDebug :app:lintDebug`. GitHub Actions also builds the APK. Source begins in `app/src/main/assets/index.html` and `app/src/main/java/com/picturethis/prototype/MainActivity.java`.

Production architecture and acceptance criteria are in the specification. Keep the prototype as an interaction reference; build the native camera and scene engine through the execution plan.
