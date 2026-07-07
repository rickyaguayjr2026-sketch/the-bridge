# The Bridge — Vision Spec

This is the consolidated reference for what The Bridge is and how it's architected. It exists so future contracts/layers can cite "the vision spec" instead of dangling references to content that lives only in a chat history. Sections marked **OPEN** are real, unresolved gaps — do not treat them as settled, and do not let a future contract reference them as if they were.

## What this is
A native Android app (Kotlin + Jetpack Compose) — a Christian devotional and spiritual-discipline companion, at a Glorify/Hallow production quality bar. Began as an earlier prototype called "The House"; The Bridge is that same product, finished, under a new name. Not a PWA — that framing was explicitly rejected.

## The differentiator
Not content-library size — **depth of relationship with the user over time.** WordForge reads the user's actual event history, identifies weak areas/strongholds specific to that individual, and curates responses accordingly. That learning is made visible back to the user as an offline, saved "bird's eye view" of their walk — which strongholds have been slain, which are still active fronts. **OPEN:** the data model and visual form of this bird's-eye view isn't designed yet.

## Core architecture (locked)
- **Mode 1 "The Word"** — deterministic, pre-approved scripture/content, no AI, offline, locked progressions.
- **Mode 2 "The Conversation"** — sandboxed, explicit disclaimer on entry, human approval required before anything writes back to the engine, never touches Mode 1.
- **WordForge** — a classifier/filter, never generative. Matches input against a category, returns a fixed/curated response. Response format: *validation sentence → "Have you brought this to the Lord yet?" → short speakable prayer.* Expanded vision: tracks habits/patterns over time from the event log, surfaces weak areas, identifies strongholds — still classify/score-only, never generates new spiritual content.
- **Local Room DB, SQLCipher-encrypted.** Device-local is the source of truth; any future sync is sync-only, never source of truth.
- **Covenant Intro** — cinematic consent mechanism. Must explicitly state: this app learns you / lives on your device / you control it / can be wiped anytime. **Built** (see Current Build Status).
- **Sticky Note Ministries** — async batch sync only, roughly weekly, no real-time backend.
- **Three onboarding modes:** Follower (new/searching, gentle "milk" tone), Caregiver (isolated/caring-for-others, soft/bridging tone), Abide (seasoned, direct "meat/iron" tone). **Built** (see Current Build Status).
- **Local church integration** — extends a local church's influence to a user regardless of their geography or point in their walk. **OPEN:** not designed yet — needs its own spec (how a church is represented, what it can push to users, sync model, privacy/consent). Biggest undesigned piece in the whole product.

## Biblical Avatars system
Each avatar is a biblical figure who guides onboarding and appears contextually in rooms giving in-character encouragement.
- Deterministic/pre-scripted — no live AI generation. Same speech format as WordForge (validation → "have you brought this to the Lord yet" → prayer).
- **Roster (gender-balanced 4/4):** Women — Mary, Samaritan Woman, Esther, Mary Magdalene. Men — David, Paul, Peter, Moses.
- Art direction: "Bible Project animation style, clean hand-drawn 2D illustration, warm earthy color palette... soft lighting, emotional and reverent mood." Each avatar has a symbolic element (Mary: dove/lily; Samaritan Woman: water jar; David: harp/sling; Paul: scroll/broken chains; Peter: keys/fishing net; Moses: staff/burning bush; Esther and Mary Magdalene still need art prompts written).
- User picks/is assigned a primary avatar; can switch anytime in Settings.
- **OPEN:** is avatar choice independent of onboarding mode, or does each mode map to a subset of avatars? Not resolved — don't assume either answer.

## Settings screen
Will be extensive, not a simple list: live Bible Project-style artwork, instructional guidance led by whichever avatar the user chose. Confirmed contents so far: rewatch Covenant Intro (real feature — good enough to show other people the app, not just a dev convenience), avatar switching. Not fully spec'd beyond that.

## Feature surface (validated content/UX reference, not yet built)
- **Sanctuary / Armor of God** — Ephesians 6 armor checklist ritual with affirmations.
- **Meditation Room** — Hallow/Abide-style: guided meditation, Lectio Divina, sleep stories, journaling.
- **Worship Center** — Glorify-style: worship audio, daily devotional.
- **My Walk / Stones of Remembrance** — a testimony/answered-prayer journal, distinct from the raw WordForge event log and from the bird's-eye-view differentiator feature above.

## Visual design system
Deep charcoal `#0A0500`, warm gold `#D4AF37`, fire red/orange accents, "Bible Project" hand-drawn illustration style. Already in use in the built Covenant Intro and Mode Selector screens.

## Gate progressions / stronghold taxonomy — OPEN, explicitly not to be invented by AI
Multiple incompatible taxonomies have surfaced across old source material (Rest/Peace/Forgiveness/Joy/Night Watch/Stillness; Identity/Fear; Marriage/Anger/Guilt/Fear) and must be reconciled with Ricky, not guessed at. Real gate progressions require pastoral/Scripture research (Romans road, Ephesians 6, Battlefield of the Mind, strongholds literature) that Ricky provides.

## Pastoral layer — OPEN, tentative
Shepherd Mode (anonymous aggregate congregation dashboard for a pastor) and Shepherd of the Shepherd Mode (pastor's private AI-governed workspace) — from one early source only, not corroborated elsewhere. May be the mechanism behind local church integration. Needs confirmation it's still wanted before any design work happens on it.

## Explicitly deferred (parked, don't build unless opened)
Sharing architecture (user sharing WordForge data with pastor/mentor/family), Firebase schema detail, monetization specifics, MetaMode as a formal skill file.

## Current build status (as of this writing)
- **Layer 1 — Covenant Intro:** built and CI-verified. Real cinematic video (Grok Imagine-generated) with muxed licensed audio, explicit tap-to-accept consent (not a passive timer), persisted to an encrypted local profile.
- **Layer 2 — Mode Selector:** built and CI-verified. Tap-to-highlight + Confirm pattern, re-entrant (built to be reusable from Settings for mode-switching later, not a one-time gate), original screen copy for Follower/Caregiver/Abide.
- **Layer 3 — in contract-check, not yet built.** Adds a Home/Porch Intro screen (between Covenant Intro and Mode Selector) and an Avatar Selection screen (after Mode Selector). Two things are blocking that contract from moving to implementation:
  1. The actual narration copy and visual direction for the Home/Porch Intro screen — referenced as already given, not actually supplied yet.
  2. Whether to introduce real `androidx.navigation.compose` (proper back-stack) or extend the existing `AppScreen` enum + `when`-block pattern used for Layers 1–2.
- Screens/features not yet touched: Sanctuary, Meditation Room, Worship Center, My Walk, Sticky Note Ministries, Settings, local church integration, WordForge's learning/bird's-eye-view logic, the porch-to-house transition (flagged as its own future layer).
