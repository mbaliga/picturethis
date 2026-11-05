# PictureThis execution plan

This plan implements SPEC.md. Work in dependency order. Each task ends with code, relevant tests, an updated capability/status table, and a short validation report. Estimates are intentionally omitted until hardware and model spikes establish throughput; completion gates are authoritative. Do not silently substitute a UI mock for an implemented capability.

## Milestone 0 Prototype handoff

T00 Inspect the supplied lab on an Android phone. Exercise import, camera thumbnail, themes, drag, mask, undo, save/restore, and export. Record Android and WebView versions, screen dimensions, and observed bugs. P0 has no AI backend or native manual camera controls. Preserve this lab as an interaction reference rather than extending its single HTML file into the production architecture.

Exit: installable debug APK, source/build instructions, known limits, and owner feedback on controls. Physical-device sign-off is still required.

## Milestone 1 Capture and durable projects

T01 Hardware spike. Choose baseline phones with the owner; export camera capabilities per lens. Check manual exposure/focus, RAW, capture latency, available stream combinations, and vendor extensions. Deliver a capability matrix and an architecture decision on CameraX versus dedicated Camera2 sequences. Depends on T00.

T02 Native shell. Create Kotlin/Compose app with module boundaries from SPEC section 10, navigation, theme tokens, accessibility labels, permissions, error states, and native preview. Match interaction concepts, not the WebView implementation. Depends on T01.

T03 Project domain. Implement schema, command reducer, revision IDs, undo/redo, source hashing, source-space mask coordinates, and serialization. Add migration fixtures and malformed-input tests. Independent of camera spike after interface agreement.

T04 Durable storage. Implement atomic asset writes, transactional project index, job journal, recovery scan, local project deletion, and portable archive import/export. Inject failures before and after rename/database commit. Depends on T03.

T05 Camera baseline. Add JPEG/optional RAW, orientation, lens choice, autofocus, metering, manual controls gated by capabilities, exposure/WB locks, histogram, peaking, and level. Test permission denial, camera disconnect, interruption, and full storage. Depends on T01, T02, T04.

Exit: A01-A03, A17-A20 as applicable; original files unchanged; no fake controls; successful real-device captures from each supported test lens.

## Milestone 2 Local scene manipulation

T06 Protection compositor. Implement source-pixel masks, brush/erase, exact interiors, outer feather, baseline selection, conflict representation, and final composite. Test after colour conversion, crop, resize, undo/reload, and export. Depends on T03-T04.

T07 Analysis spike. Benchmark licensed depth/segmentation candidates on consented fixtures. Define confidence thresholds and relative-depth semantics. Deliver measured latency/memory and representative failures. No provider selection on popularity alone. Depends on T03.

T08 Geometry correction. Implement object IDs, mask refinement, ground-plane adjustment, pole endpoints, light direction/elevation controls, and coordinate transforms. Unknown scale stays unknown. Depends on T07.

T09 Local relighting. Render directional/point/area light approximations, shadows, visible moon/sun disc, stars, multiple sources, and protection conflicts. Keep estimate labels visible. Unit-test direction math and compare calibrated scene outputs. Depends on T06, T08.

T10 Virtual optics. Build depth-aware blur with foreground occlusion, highlight PSF, mask presets, focus plane, aperture, anamorphic control, and inserted-light integration. Test thin structures and depth discontinuities. Depends on T06-T08.

T11 Interaction scheduler. Add reduced-resolution previews, cancellation, latest-revision delivery, frame coalescing, thermal degradation, and performance instrumentation. Depends on T09-T10.

Exit: A04-A09 and interactive performance gates on a named baseline; correction tools work when estimation fails. No claim of pixel-exact physical reconstruction.

## Milestone 3 Model augmentation

T12 Provider evaluation. Obtain owner decisions on provider, budget, hosting, retention, licensing, and credentials. Benchmark masked editing, identity, text preservation, prompt adherence, latency, and cost. Deliver an evaluation report with rejected alternatives. Depends on T07-T10.

T13 Gateway. Implement server-side secrets, authentication, signed asset upload, request size/schema validation, rate limits, idempotency, cost ceiling, cancellation, retries, and deletion policy. Use fake providers for contract tests. Depends on T12.

T14 GUI instruction compiler. Compile structured scene state into bounded provider inputs; preserve coordinates, mask precedence, units, and allowed effects. Treat image text as data. Add deterministic compiler tests for conflicting settings and absent capabilities. Depends on T03, T12.

T15 Refinement workflow. Add explicit upload disclosure, estimate/confirm, pending/failed/cancelled/succeeded states, immutable input revision, alternate result gallery, original comparison, and final protected composite. Stale results never replace active edits. Depends on T06, T13-T14.

Exit: A07-A12 plus fixed-dataset quality review. Owner accepts actual model economics and failures. Offline capture remains functional.

## Milestone 4 Advanced capture

T16 Sequence engine. Implement checkpointed finite-state capture with per-frame metadata, stop/pause/recovery, supported-parameter validation, and storage forecasting. Depends on T04-T05.

T17 Guided mosaic. Add overlap overlay, coverage, matching quality, retake, locked settings, registration, seams, exposure compensation, tiled export, and sources export. Depends on T16.

T18 Photosphere and fisheye. Add spherical targets, loop closure, coverage completion, equirectangular metadata, zenith/nadir handling, and projection controls. Depends on T17.

T19 Focus bracketing and stacking. Implement diopter scheduling, settling, lens metadata validation, alignment, sharpness maps, motion flags, and manual correction. Depends on T16 and T10 for shared analysis utilities only.

T20 Long-duration accumulation. Add average/additive/lighten/registered modes, incremental stack persistence, foreground service lifecycle, storage/thermal controls, and interruption recovery. Run 30-minute, 2-hour, and 6-hour device tests with recorded environmental conditions. Depends on T16.

T21 Composition aids. Add objective, dismissible nudges; evaluate false positives and user distraction. Subjective suggestions remain optional. Depends on T05 and T07.

Exit: A13-A16 plus mode-specific output quality review. Publish actual supported device/mode combinations and recovery limitations.

## Milestone 5 Release and portability

T22 Release hardening. Add production signing outside source control, dependency/license inventory, privacy disclosures, accessibility audit, device matrix, benchmark report, crash monitoring policy, migration test, and rollback build. Depends on accepted prior milestones for the intended release scope.

T23 Linux mobile spike. Select device/OS, inspect camera and GPU access, establish packaging, port schema/processing behind adapters, and prove capture/import/edit/export. Deliver a go/no-go report before a full port.

T24 iOS spike. Validate AVFoundation controls, model execution, image pipelines, entitlement and distribution constraints. Use the same project and operation semantics. Deliver a separate go/no-go report.

## Codex execution instruction

Read docs/SPEC.md, docs/EXECUTION_PLAN.md, docs/PROTOTYPE.md, and repository instructions. Start with the next incomplete task. Inspect the code and capability report before choosing APIs. Implement and test the task fully, update docs/STATUS.md with evidence and remaining limitations, and create a reviewable change. Preserve source photographs and exact protection semantics. Never add model secrets or silently enable billable uploads. Do not mark camera, GPU, or thermal behaviour verified without a real-device result. Ask for missing hardware/provider/budget decisions only when the next dependent step actually needs them; continue independent domain and fixture work meanwhile.
