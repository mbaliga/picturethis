# PictureThis product and engineering specification

Version 1.0 • 5 November 2026 • Repository mbaliga/picturethis

## 1 Product brief

PictureThis is an Android-first camera and editable photographic scene. It combines dependable manual capture, guided computational photography, and visual controls for AI augmentation. Users work with photographic concepts and direct manipulation: place a moon, move it to change a pole’s shadow, shift focus so highlights become bokeh, choose the aperture mask, and protect details that must survive every edit. Structured scene state drives the renderer and model instructions. A prompt is an implementation mechanism, not the primary user interface.

The first audience is photographers and visually curious creators who want intentional control beyond a stock camera. The central job is to capture a real scene and explore a controlled interpretation while retaining the original, understanding generated changes, and preserving selected content. The product must remain useful offline as a camera and conventional editor.

Android ships first. Linux mobile, particularly Ubuntu Touch, is a later hardware and camera-adapter project. iOS follows only if supported controls, engineering effort, and user demand justify it. Shared processing and project formats are priorities; identical platform implementations are not a prerequisite.

Success means a person can complete the moon-and-shadow, focus-and-bokeh, and protected-subject workflows without writing a prompt. Best-in-class is an acceptance standard for responsiveness, control precision, reliability, image quality, and recovery; it is not a claim that the prototype exceeds existing products.

## 2 Scope and delivery boundaries

P0 is the supplied offline interaction prototype. It includes an illustrated reference scene, photo import, system-camera thumbnail capture on Android, draggable sun or moon, star overlays, time-of-day tint, virtual focus approximation, four bokeh shapes, painted exact protection, undo, local project save and restore, PNG export, and OLED and skeuomorphic themes. It has no connected AI model. Shadow movement uses known demo geometry; imported photographs have no inferred geometry or relighting. Imported images are resized to at most 1000 pixels on their longest side. This is an interaction study, not the production camera engine.

P1 is a reliable native Android camera plus project editor. P2 adds depth and object estimation, geometry correction, interactive virtual optics and lighting. P3 adds constrained generative refinement with an explicit provider and cost boundary. P4 adds guided mosaics, photospheres, bracketing, focus stacking, long-duration accumulation, and advanced fisheye projection. P5 evaluates Linux mobile and iOS adapters. Detailed task dependencies appear in the execution plan.

All original requested features remain in scope. Sequencing reflects dependencies, not deletion. Real-time generative photorealism on every supported phone is a research goal; the release contract is a responsive local approximate preview plus asynchronous refinement. Do not display mock capabilities as working camera controls.

No account is required for local capture or editing. Cloud generation is optional. Do not introduce a social feed, marketplace, subscription flow, or cloud photo library in the first product release. Monetization and licensing remain owner decisions.

## 3 Primary journeys

Capture and develop: launch into a usable viewfinder, inspect supported lenses, focus and meter, choose a mode, capture, inspect the result, then enter scene editing without losing the original. Switching to virtual settings must not silently overwrite physical capture settings. Return to the camera with the last supported physical settings preserved.

Moon and pole: capture or import, analyze depth and surfaces, confirm or correct the ground plane and pole endpoints, add a moon, drag its direction/elevation, and see approximate lighting and shadows respond. Refine after release or explicit tap. Preserve framing and pole identity. The visual moon disc and its light contribution can be linked or adjusted independently. Stars are a sky layer; they do not automatically brighten the ground.

Focus and bokeh: select a subject or depth, move the virtual focus plane, adjust aperture and mask shape, and inspect occlusion boundaries. Existing and inserted lights use the same virtual optics. Warn when sharpening requires synthesis. Keep foreground edges from bleeding into background blur. Show before/after and allow undo.

Protected subject: tap an object to propose a mask, refine with brush/eraser, select exact pixels or identity preservation, and set allowed effects for identity mode. A lock persists through all later operations. The UI explains conflicts without silently weakening protection.

Guided composite: choose rectangular mosaic or sphere, lock exposure and white balance where possible, follow overlap and coverage guidance, retake failed frames, preview the assembled result, then export while preserving sources. Suspend safely when interrupted.

## 4 Information architecture and interface

Primary destinations are Capture, Scene, Projects, and Settings. Capture has lens selection, mode rail, shutter, last image, and an expandable manual-control deck. Scene has the image, object selection, tool deck, undo/redo, comparison, refinement status, and export. Projects shows thumbnails, source counts, processing state, modified date, and storage footprint.

Scene tool groups are Light, Optics, Atmosphere, Objects, Protect, and Finish. Selecting an object exposes contextual properties. A recipe inspector is optional for advanced users; it shows structured settings and the generated instruction without making prompt writing necessary.

OLED uses true black surfaces, restrained green accents, crisp numerals, thin outlines, and minimal framing. Skeuomorphic uses warm charcoal, metal-like dials, engraved scales, restrained texture, and tactile feedback. Themes share capabilities, control order, labels, units, selected state, and gestures. Physical camera controls and virtual controls have distinct labels in both themes. A skeuomorphic dial must also provide a numeric or slider alternative.

Minimum touch targets are 48 dp in production. Support TalkBack labels, adjustable text, reduced motion, high contrast, left-handed shutter placement, landscape layouts, and controls outside system-bar cutouts. Do not rely on colour alone to communicate locks, warnings, or processing states. At 200 percent text size, controls scroll rather than overlap. Provide accessible numeric position controls for lights and masks as alternatives to dragging.

Gestures: one-finger drag moves the selected element; two fingers pan/zoom the canvas; a dedicated protection tool paints. Avoid gesture ambiguity through an active-tool indicator. Long press temporarily compares original and result; a persistent comparison toggle supports accessibility. Undo groups one continuous drag into one transaction. Theme changes do not affect exported pixels.

## 5 Physical capture requirements

CAP01 Query capabilities per physical/logical camera: focal lengths, RAW, exposure range, ISO range, focus support, minimum focus distance, stabilization, flash, supported formats and stream combinations. Persist a versioned capability report keyed by device, OS, and camera ID. Hide unavailable options with an explanation in capability details.

CAP02 Provide exposure compensation, shutter and ISO when manual sensor control is supported, white balance controls where available, autofocus and manual focus, AE/AF locks, lens switching, timer, grid, level, histogram, highlight warnings, and focus peaking. Never promise manual aperture on fixed-aperture hardware. Lens switching may reset unsupported controls; disclose that before applying.

CAP03 JPEG capture is baseline; optional RAW plus JPEG when supported. Preserve metadata and sensor orientation. Handle rotation, front-camera mirroring, colour profile, and crop explicitly. Offer original capture export without augmentation.

CAP04 Request camera permission at first use. Photo import works when permission is denied. Camera-in-use, disconnected camera, unavailable lens, full storage, thermal pressure, and failed captures receive actionable messages. Release the camera on lifecycle transitions and do not restart a capture sequence without user intent.

CAP05 Third-party access may not reproduce a vendor camera’s proprietary processing. Benchmark against source captures and relevant stock-camera modes per test device; record differences rather than claiming equivalence. Use vendor extensions only when they do not break manual or sequence requirements.

## 6 Scene model and coordinate contracts

A Project has schemaVersion, projectId, timestamps, immutable SourceAssets, capture metadata, SceneRevision, edit history, masks, generated assets, render jobs, and exports. Each source has SHA-256, dimensions, EXIF orientation, colour space, MIME type, capture timestamp, and local URI. Never use an original file as a scratch buffer.

SceneRevision contains camera intrinsics when known, depth with confidence, surface normals, camera pose or an explicit unknown flag, ground plane, objects, lights, sky, virtual optics, global rendering, and protection records. Store estimates with source and confidence. Do not label relative monocular depth as metres without calibration.

Image coordinates use normalized x/y in the orientation-corrected uncropped source: x right, y down, origin at top left. Masks are defined in source pixel space and transformed through crop/resize using an explicit matrix. Scene coordinates use a documented right-handed frame: x right, y up, camera looks along negative z; lengths are metres only after calibration. The adapter must explicitly convert camera API coordinates.

Objects have stable IDs, label, mask asset ID, depth interval, transform, visibility, confidence, and protection links. Lights have ID, type (directional, point, area, emissive), pose/direction, intensity with declared units or normalized artistic scale, colour temperature or RGB, source size, and visible-disc properties. Sun/moon defaults are directional light plus a visible sky element, not a nearby point light.

VirtualOptics contains focusDistance or relativeFocusDepth, virtual focal length, aperture f-number, sensor/crop assumption, aperture-mask asset or preset, blade count, rotation, anamorphic ratio, and distortion. Do not present physically derived units when underlying depth/calibration is unknown; use relative controls and label the approximation.

All mutation commands include commandId, projectId, baseRevision, operation type, payload, and timestamp. A command validates before commit, creates a new revision, and is undoable. Idempotency prevents duplicate imports or cloud jobs. Unknown future schema versions open read-only with export access.

## 7 Protection semantics

Exact protection means equality to a selected baseline image in the canonical working pixel representation at protected interior pixels. Baseline defaults to the original oriented source; allow explicit freeze-current-result later. Exact locks prohibit relighting, blur, tone changes, object displacement, synthesis, and crop removal of those pixels. A crop conflict requires the user to resolve it.

Enforce exact locks after generation, optics, colour processing, and compositing. Let M equal one in the protected interior. The output is M times baseline plus one minus M times candidate. Antialiased mask boundaries may blend only outside the exact interior. Validate pixel equality before export in lossless working space. JPEG compression and colour-space conversion can change decoded values; offer PNG for exact lossless delivery and explain this distinction.

Identity preservation retains subject identity, shape, text, placement, and selected attributes while allowing named effects such as lighting or focus. It is a model constraint, not a pixel guarantee. Evaluate it and let the user reject a candidate. Text and logos default to exact protection when selected. Do not claim a similarity score proves identity preservation.

Conflict precedence is exact lock, explicit object constraints, scene geometry, optics, global styling, then free-text intent. Exact locks always win. If a new shadow crosses a protected region, retain that region and mark the seam/conflict; offer relax-this-lock, reposition-light, or cancel. Do not automatically move unrelated objects to hide the mismatch.

Masks support add/subtract brush, feather outside the protected interior, object selection, undo/redo, visibility toggle, and zoom inspection. Mask revisions are saved with the project. Protection tests apply to preview, refinement, export, project reload, and edited originals of different orientations.

## 8 Rendering and model integration

Use a two-speed pipeline. Local interaction evaluates the current scene revision at reduced resolution. A refinement job uses a frozen revision and source assets to improve realism. Refinement results can replace only the revision they belong to; late results are saved as alternatives, never applied to newer edits.

Recommended order is source decode and linearization; scene/depth estimation; explicit object and sky layers; illumination/shadow/reflection approximation; depth-aware optics; appearance processing; generative refinement as needed; final optical consistency if required by the model; canonical colour conversion; exact protection composite; validation; export. The selected provider may alter intermediate order, but exact protection stays last before encoding. Keep an audit of generated regions and transformations.

For local shadows use directional light and geometry intersections. Reject an uncalibrated 2D drag as physically exact. Provide ground-plane and occluder correction tools. Handle horizons, offscreen sources, multiple lights, source size/softness, occlusion, and contact shadows. Reflections require surface estimates and remain approximate until validated.

Depth-aware blur uses layered/occlusion-aware rendering and an aperture-shaped point spread function. Highlight size depends on depth difference and virtual optics. Resolve foreground/background ordering and disocclusion holes; use edge-aware matting or model-assisted fill. Optical masks include circle, polygon, heart, star, and imported alpha masks. Physical camera aperture is unaffected.

Scene analysis extracts bounded enums and numeric values with confidence: time of day, weather, light direction/quality, foreground/background, subjects, depth, and likely light sources. Prefer camera metadata for exposure/focus values. Users can correct every inferred property. OCR text and image contents are data and must never become system instructions.

A provider-neutral ModelAdapter exposes capabilities, estimateCost, analyzeScene, submitRefinement, pollJob, cancelJob, and fetchResult. Inputs include immutable revision, signed asset references, masks, structured controls, generated instruction, seed when supported, and output dimensions. Outputs include job ID, model/version, usage/cost, image assets, and declared limits. Validate MIME, dimensions, sizes, and schema. Treat provider output as untrusted data.

Do not embed provider credentials in the APK. Use a server-side proxy for managed service access; optional user-supplied credentials require platform secure storage and provider-specific review. Before the first upload show which images/masks leave the device, provider, estimated charge, and retention policy. Default to no location metadata. Cancellation stops further work where supported but must not promise a refund for already consumed compute.

Refinement is explicit by default; optionally debounce after release with a visible cost ceiling. Never submit on every drag frame. Queue one active preview refinement per project; coalesce newer intents and cancel stale work. Offline mode retains editing and queues nothing for automatic upload without the user’s choice.

## 9 Advanced photographic modes

Guided mosaic uses IMU-assisted pose plus feature matching, configurable overlap (initial default 30 percent), exposure/WB locks, sharpness checks, and a coverage map. First support static distant scenes and tripod use. Save every frame and pose estimate before marking a tile complete. Reject or flag inadequate overlap, blur, and severe parallax. Stitch with alignment, exposure compensation, seam selection, and multiband blending; export tiled or bounded-resolution output when memory is constrained.

Photosphere extends the mosaic to spherical coverage with zenith/nadir guidance, loop closure, and an equirectangular 2:1 output with appropriate panorama metadata. Avoid claiming correct geometry for close objects without adequate parallax handling. Display missing regions rather than inventing them without a separate fill action.

Fisheye offers a single-frame projection effect and, later, a wider-field projection from a stitched source. A single-frame filter cannot recover an uncaptured field of view. Parameterize projection, field of view, centre, and crop; label missing regions.

Focus bracketing queries variable-focus and manual-control support, lets users choose near/far bounds and frame count, spaces samples initially in diopters, waits for reported lens settling where available, and records requested and actual capture metadata. Reject fixed-focus cameras with a clear reason. Provide originals even if stacking fails.

Focus stacking aligns scale changes caused by focus breathing, builds focus maps, merges sharp regions, and exposes motion/edge artifacts for retouching. Test macro scenes, fine hair, transparent subjects, moving leaves, and low texture. Keep rejected frames available.

Long exposure means a sequence of individually supported exposures, not an unsupported hours-long sensor exposure. Modes are average accumulation, additive accumulation with headroom, lighten blend for trails, and registered astronomical stacking. Each has different alignment and moving-subject behaviour. Explain gaps between exposures and saturation limits.

Long sessions show elapsed time, frame count, estimated remaining storage, battery/thermal status, and a rolling result. Persist frames and a journal incrementally; checkpoint stacks. Offer pause/stop, storage limits, and external-power guidance. Android foreground camera service rules and OEM behaviour must be tested; do not promise uninterrupted capture through arbitrary backgrounding, lock screen, reboot, or thermal shutdown. Resume from saved work after interruption, with user confirmation before camera restart.

Composition nudges are optional and dismissible: horizon, clipped highlights, excessive shake for selected shutter, insufficient overlap, and focus confidence first. Subjective framing suggestions are opt-in and should offer alternatives rather than pronounce a composition wrong.

## 10 Production architecture

Use Kotlin and Jetpack Compose for Android production UI. CameraX is the baseline lifecycle and preview adapter; validate Camera2 interop for manual controls, then use a dedicated Camera2 session adapter where sequence determinism requires it. Do not run competing camera owners. The P0 Java/WebView shell is disposable scaffolding for interaction validation.

Modules are app-ui, camera-api, camera-android, project-domain, project-storage, scene-analysis, renderer-api, renderer-local, model-api, model-gateway, capture-sequences, export, and test-fixtures. Domain and schema code must not depend on Android camera classes. Introduce a portable C++ processing library only after a measured hotspot or cross-platform need; expose a small C ABI for Android JNI and future Swift/Qt adapters.

Use immutable project revisions, a database for indexes/jobs, and app-private files for sources/masks/renders. Writes use temporary files, fsync where appropriate, checksums, and atomic rename. Database transactions reference only completed files. Startup reconciliation marks incomplete jobs and offers recovery. Deleting a project removes its local sources only if they are project-owned copies; never delete a user’s external original.

Rendering operates off the UI thread with bounded buffers, cancellation, and backpressure. Use preview pyramids and tile full-resolution operations. Keep colour and alpha conventions explicit at module boundaries. Avoid several simultaneous full-resolution floating-point copies on low-memory devices.

Project archive format is a ZIP containing manifest.json, sources, masks, scene assets, history, and optional renders, with SHA-256 per asset. Validate paths against traversal, maximum expanded bytes, dimensions, and supported schema. The production schema is versioned and migrated with fixtures. Prototype localStorage format is not this portable archive and needs explicit migration or a new project import.

## 11 Reliability and quality targets

Targets below are proposed release gates, not measured prototype performance. Select and record a baseline midrange Android device before performance sign-off. Test a second vendor, a low-memory device, and a device with limited manual controls.

Target camera ready within 1.5 seconds warm and 3 seconds cold on the baseline. Target local edit feedback under 100 ms at the 95th percentile and at least 30 fps during continuous light/focus drag at a 720p preview. Record input-to-frame latency, not just draw-loop frequency. Degrade preview resolution before dropping user input.

The initial memory budget is 256 MB incremental over idle on the baseline for a 12 MP edit; revise only with measured evidence. Full-resolution processing must be cancellable, show progress, and recover from low memory. No loss of completed source frames is acceptable in injected-crash tests after a successful save acknowledgment.

Refinement latency and cost depend on the selected provider. Benchmark candidates on a fixed consented dataset before setting a public SLA. Report median and tail latency, edit adherence, subject preservation, artifact rate, and cost per accepted result. Reject a provider that cannot support required masks or acceptable constraints; do not hide this behind an optimistic roadmap.

Image-quality evaluation includes skin, text, hair, reflective surfaces, transparent objects, low light, repetitive texture, motion, multiple depth layers, and sky boundaries. Compare deterministic outputs with reference fixtures and generated outputs with repeatable rubrics plus human inspection. Exact-protection interior must have zero pixel difference in canonical output.

## 12 Security privacy and distribution

P0 requests no internet permission and sends no photos to a server. It loads packaged UI content through a fixed intercepted origin and blocks navigation. File import uses the system picker; output uses the system document saver. Its camera shortcut receives a low-resolution thumbnail from the installed camera application. No capture quality claims apply to that path.

Production uses least-privilege permissions and system media APIs. Location tagging is off unless selected. Logs omit image content, prompts containing personal data, asset URLs, and credentials. Crash reports are opt-in and scrubbed. Delete remote jobs/assets according to the chosen provider policy and explain limits honestly.

Use an application-owned production signing key stored outside the repository, with recovery procedures. P0 is debug-signed for testing, not Play Store distribution. Rebuilding on a clean CI runner may use a different debug certificate and require uninstalling the previous test build, which removes local projects. Never publish a production key or model token in Actions logs or artifacts.

Production release requires dependency/license inventory, permission review, privacy disclosure matching actual providers, reproducible build instructions, crash-free smoke testing, and a rollback path. Repository visibility is public; do not commit personal sample photographs or private credentials.

## 13 Test and acceptance catalogue

A01 Launch offline, deny camera permission, and import a photo successfully. A02 Capture JPEG on every baseline device and verify orientation/metadata. A03 Unsupported manual focus is unavailable with a reason, while ordinary capture still works.

A04 Drag moon across a calibrated reference scene and verify the pole shadow points away from the light and changes with elevation. On unknown geometry display an approximate state and offer correction. A05 Changing focus alters both original and inserted highlights consistently. A06 Every aperture-mask preset changes out-of-focus point-source shape without changing protected pixels.

A07 Paint an exact mask, apply light/focus/time/weather edits, save/reload, refine, and export PNG; all interior pixels equal baseline in the canonical representation. A08 A shadow-lock conflict preserves the lock and surfaces a resolution choice. A09 Identity mode never claims exactness and preserves an accessible original comparison.

A10 Cancel a model request, edit again, then receive the old result; it must not replace the new revision. A11 Repeated submission with the same idempotency key creates no duplicate billable request where the provider contract supports it. A12 Upload requires informed user action and credentials are absent from the APK.

A13 Kill the process after each sequence checkpoint and recover all acknowledged frames. A14 Storage exhaustion stops safely without deleting prior captures. A15 Heat, battery, permission revocation, camera disconnect, and background restrictions generate recoverable states. A16 Mosaic overlap failures are visible and source images remain exportable.

A17 Both themes expose the same commands and switching themes preserves scene state. A18 TalkBack and large-text flows cover capture, import, light adjustment, protection, and export. A19 Malformed project archives, oversized images, invalid masks, and unknown schema versions fail safely. A20 Original files remain byte-identical after all workflows.

Test levels are pure unit tests for schema, commands, math, and mask composition; golden image tests for deterministic rendering; instrumentation for camera/storage/lifecycle; device soak tests for long sessions; and fixed-dataset model evaluations. Emulator success does not establish real camera support or sustained thermal performance.

## 14 Decisions risks and unresolved inputs

The initial Android target phone is not yet specified. P0 supports Android 8 or newer by manifest; production minimum API and device matrix should be decided in the capability spike. Recheck current store target API requirements before a store release; the prototype target SDK is a build choice, not a compliance promise.

No generation provider, hosting budget, model license, or production signing identity has been selected. These are explicit gates before P3 deployment. Implement the provider boundary and offline mock fixtures first, then benchmark real candidates with authorized credentials and cost limits.

The hardest risks are single-image geometry ambiguity, temporally stable previews, protection seams, subject preservation, depth-edge artifacts, phone camera fragmentation, and multi-hour thermal reliability. Mitigations are correction tools, local deterministic previews, strict protection priority, separate final refinement, device probes, and checkpointed sequences. Do not equate plausible generated geometry with measured geometry.

Linux mobile is a separate feasibility spike on a named device and OS build. Inspect its actual camera stack, permissions, available controls, packaging, GPU path, and memory limits before committing to parity. iOS requires an equivalent AVFoundation capability spike and evaluation of model/privacy/distribution choices. Neither port should hold Android delivery hostage.

## 15 Sources and technical references

Android capture controls and capability contracts: https://developer.android.com/reference/android/hardware/camera2/CaptureRequest and https://developer.android.com/reference/android/hardware/camera2/CameraMetadata

CameraX architecture and interop: https://developer.android.com/media/camera/camerax/architecture

Foreground camera service constraints: https://developer.android.com/develop/background-work/services/fgs/service-types

Android Gradle compatibility: https://developer.android.com/build/releases/about-agp and https://docs.gradle.org/8.11.1/release-notes.html

Reference interactions, not an assertion of overall product ranking: https://luxoptics.zendesk.com/hc/en-us/articles/360051909451-Manual-Focus and https://www.blackmagicdesign.com/uk/products/blackmagiccamera/techspecs

Conceptual inspiration: https://bjoernkarmann.dk/project/paragraphica . PictureThis begins with captured imagery and editable scene state; Paragraphica’s context-to-image concept is an influence, not an identical implementation.
