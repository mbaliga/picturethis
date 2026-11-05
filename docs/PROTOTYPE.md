# Using PictureThis Lab

Install the debug APK on Android 8 or newer. Android may ask you to allow installation from the app opening the APK. This is a test build, not a store release. A clean CI rebuild may have a new debug signing certificate; uninstalling an older build can erase its locally saved project.

1. Open the illustrated scene. Drag the moon across the sky and observe the pole shadow.
2. Use Scene to select dusk/day/night, sun/moon/none, intensity, or stars.
3. Open Optics. Move focus toward Near, open the virtual aperture, and select Heart or Star. The moon and demo highlights change shape.
4. Open Protect. Paint over part of the scene. It returns to the original pixels. Change the other controls and verify the locked area stays unchanged. Erase or clear protection to release it.
5. Import a JPEG or PNG. The photo receives local tint, blur, and overlays. The app does not infer geometry or generate realistic shadows on imported photos.
6. Camera opens your installed camera app and receives its thumbnail. Full-resolution native capture is a later milestone.
7. Compare original/edited, undo, save a project locally, restore it, or export a PNG through Android’s document picker.
8. Switch OLED and Skeuomorphic at the top. Both expose identical controls.

The same HTML file can be opened on a desktop browser for review. Camera is Android-only. Use an up-to-date Android System WebView. If a photo format is unsupported, convert it to JPEG or PNG.

## Limits

No AI provider, semantic object selection, depth estimation, identity-preserving generation, true physical camera controls, guided capture, focus stack, or long exposure is implemented in P0. Those are specified and sequenced in the handoff. Imported previews are limited to 1000 pixels on the long side. The star field and light disc are overlays. Photo focus is a global blur approximation. Only the illustrated scene has a simulated shadow, based on fixed geometry. A painted exact mask is a real final-composite operation, not a prompt promise.

One saved project is stored in local browser storage. Undo retains up to 15 snapshots. Storage can fill; the app reports failure and still permits PNG export. The saved project is not yet portable. Resetting app data removes it. Export excludes the blue protection overlay and viewfinder labels.

## Build

Java 17, Gradle 8.11.1, Android SDK platform 35, Android Gradle Plugin 8.9.2. From repository root run `gradle --no-daemon :app:assembleDebug :app:lintDebug`. The APK is at `app/build/outputs/apk/debug/app-debug.apk`. The GitHub Actions workflow produces the same artifact. No Gradle wrapper is committed in this prototype; use the pinned Gradle version above.

## Validation

Browser interaction test exercised both themes, bokeh controls, mask painting, save/restore, and phone-width layout. Fully covered protected pixels were compared against baseline. APK build and Android lint are separate CI checks. Device camera handoff, document picker, lifecycle, and actual phone GPU behaviour still require physical-device testing.
