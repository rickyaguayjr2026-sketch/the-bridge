# The Bridge — Vision Spec

This is the consolidated reference for what The Bridge is and how it's architected. It exists so future contracts/layers can cite "the vision spec" instead of dangling references to content that lives only in a chat history. Sections marked **OPEN** are real, unresolved gaps — do not treat them as settled, and do not let a future contract reference them as if they were.

## What this is
A native Android app (Kotlin + Jetpack Compose) — a Christian devotional and spiritual-discipline companion, at a Glorify/Hallow production quality bar. Began as an earlier prototype called "The House"; The Bridge is that same product, finished, under a new name. Not a PWA — that framing was explicitly rejected.

## The differentiator
Not content-library size — **depth of relationship with the user over time.** WordForge reads the user's actual event history, identifies weak areas/strongholds specific to that individual, and curates responses accordingly. That learning is made visible back to the user as an offline, saved "bird's eye view" of their walk — which strongholds have been slain, which are still active fronts. **OPEN:** the data model and visual form of this bird's-eye view isn't designed yet.

## Core architecture (locked)
- **Mode 1 "The Word"** — deterministic, pre-approved scripture/content, no AI, offline, locked progressions.
- **Mode 2 "The Conversation" — SUPERSEDED 2026-07-09.** No in-app AI conversation pipeline is built. Replaced by "Ask Further": when pre-approved content doesn't satisfy a user's need, the app populates an editable template (scripture refs, category, context) and hands off via the Android share sheet to whatever external LLM app the user already has. See the Layer 5 contract for full detail.
- **WordForge** — a classifier/filter, never generative. Matches input against a category, returns a fixed/curated response. Response format: *validation sentence → "Have you brought this to the Lord yet?" → short speakable prayer.* Expanded vision: tracks habits/patterns over time from the event log, surfaces weak areas, identifies strongholds — still classify/score-only, never generates new spiritual content.
- **Local Room DB, SQLCipher-encrypted.** Device-local is the source of truth; any future sync is sync-only, never source of truth.
- **Covenant Intro** — cinematic consent mechanism. Must explicitly state: this app learns you / lives on your device / you control it / can be wiped anytime. **Built** (see Current Build Status).
- **Sticky Note Ministries** — async batch sync only, roughly weekly, no real-time backend.
- **Three onboarding modes:** Follower (new/searching, gentle "milk" tone), Caregiver (isolated/caring-for-others, soft/bridging tone), Abide (seasoned, direct "meat/iron" tone). **Built** (see Current Build Status).
- **Local church integration** — extends a local church's influence to a user regardless of their geography or point in their walk. **Substantially designed as of 2026-07-09** (Layer 5 addendum): two cases — physical/local church (unlocks a small-group anonymous sharing circle) vs. distant/media ministry (triggers a one-time, user-initiated, anonymous email outreach to the church's pastoral contact — no OAuth, no AI verification, explicit preview-and-confirm before sending, delivered via a stateless transactional email service so the message carries no user-identifying address and no reply path). Still open: how per-church contact emails get sourced/verified, and whether an outreach threshold exists.

## Biblical Avatars system
Each avatar is a biblical figure who guides onboarding and appears contextually in rooms giving in-character encouragement.
- Deterministic/pre-scripted — no live AI generation. Same speech format as WordForge (validation → "have you brought this to the Lord yet" → prayer).
- **Final roster (2026-07-09):** Women — Mary (mother of Jesus, always disambiguated from Magdalene), Rahab, Ruth, Mary Magdalene. Men — David, Paul, Peter, Moses. (Samaritan Woman and Esther, from an earlier draft, are permanently out.)
- **Built:** all 8 have real portrait art (Grok Imagine-generated, clean white backgrounds, consistent style), wired into Avatar Selection (grid + tap-to-highlight + Confirm — a swipe-carousel redesign was proposed then permanently dropped) and the Avatar Walkthrough (Sticky Note Ministries stub → Worship Center stub → closing beat → Home).
- User picks a primary avatar at onboarding; switching in Settings is planned but not built.
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
Multiple incompatible taxonomies have surfaced across old source material (Rest/Peace/Forgiveness/Joy/Night Watch/Stillness; Identity/Fear; Marriage/Anger/Guilt/Fear) and must be reconciled with Ricky, not guessed at. Real gate progressions require pastoral/Scripture research (Romans road, Ephesians 6, Battlefield of the Mind, strongholds literature) that Ricky provides. **Lead (2026-07-09):** Pastor Steve Cowan's book *Strongholds: Walking in Freedom* directly influenced this app — likely a primary source for this taxonomy, not yet formally confirmed as such.

## Pastoral layer — CONFIRMED wanted, deliberately last in build order
Shepherd Mode (anonymous aggregate congregation dashboard for a pastor) is confirmed real as of 2026-07-09 — no longer tentative. It needs its own separate auth process (pastor-level access, distinct from regular users) and is bundled together with Local Church Integration as one combined effort — deliberately one of the last major pieces to build. Shepherd of the Shepherd Mode (pastor's private AI-governed workspace) has not been re-confirmed alongside this — treat as still unconfirmed until raised again.

## Explicitly deferred (parked, don't build unless opened)
Sharing architecture (user sharing WordForge data with pastor/mentor/family), Firebase schema detail, monetization specifics, MetaMode as a formal skill file.

## Current build status (updated 2026-07-09)

**✅ Locked AND built (in code, CI-verified, Ricky-approved on-device):**
- Layer 1 — Covenant Intro: real cinematic video (Grok Imagine) + muxed licensed audio, explicit tap-to-accept consent, persisted to encrypted local profile.
- Layer 2 — Mode Selector: tap-to-highlight + Confirm, re-entrant for future Settings mode-switching, original copy for Follower/Caregiver/Abide.
- Layer 3 — Home/Porch Intro + Avatar Selection: real navigation on `androidx.navigation.compose`, real porch-scene art, all 8 real avatar portraits (swipe-carousel redesign proposed then permanently dropped).
- Layer 4 — Avatar Walkthrough: 3 stub-screen stops (Sticky Note Ministries, Worship Center, closing beat → Home), locked in-character narration per avatar, text-only (Piper TTS deferred, see ledger).

**✅ Locked, ❌ not built yet (spec finished, no code exists):**
- Sync architecture (bank/wallet model, WordForge grocery-list pattern)
- Content doctrine boundary (7-source approved list, Category A/B classifier)
- Follower Mode 30-day Bible plan
- Growth-detection instrumentation (mechanism-only, thresholds deferred on purpose)
- Stones of Remembrance
- 12-category Psalms/Proverbs content library (structure locked, verse data not loaded; whether it stays fixed at 12 or grows toward more categories is still open — don't assume either)
- "Ask Further" share-sheet handoff (supersedes the old in-app Mode 2 AI chat vision)
- Sticky Notes Path A (in-app corkboard) + Path B (WhatsApp export)
- Local church integration: physical-church small groups + distant-ministry anonymous email outreach

**🔄 Locked, actively being built now (re-sequenced ahead of everything above per Ricky's 2026-07-09 correction):**
- A real (non-stub) first entry screen that captures genuine timestamped interaction data, so growth-detection thresholds get calibrated from real usage rather than invented numbers. Spec incoming from Ricky directly; standing by as of this writing.

**❌ NOT locked — open design gaps, not ready to build:**
- WordForge's bird's-eye-view of the user's walk (differentiator feature) — data model/visual form undesigned
- Gate progressions / stronghold taxonomy (Steve Cowan's *Strongholds* is a lead, not confirmed as the answer)
- Settings screen (only rewatch-intro + avatar-switching confirmed so far)
- Avatar-to-mode mapping (independent vs. mode-linked subset)
- Three sync sub-questions: cloud retention, church-selection UX, push timing
- Ask Further's "rough summary" step: pure template vs. small AI-assist
- Church contact-email sourcing/verification method, and outreach threshold

**⏸️ Confirmed real, deliberately parked (sequencing, not doubt):**
- Shepherd Mode + bundled local-church admin side — last major build
- Caregiver Mode, Abide Mode — after Follower Mode ships and is approved on-device
- Sharing architecture, Firebase schema, monetization, MetaMode

**Screens/features literally untouched in code:** Sanctuary, Meditation Room, real Worship Center/Sticky Note Ministries (currently stubs), My Walk, Settings, the porch-to-house transition into a real Home shell.

## App Size & Data Cost Ledger

Purpose: track what each feature actually costs — in bundled APK size, and separately, in runtime network data usage — so scope decisions are made against real numbers, not guesses.

**Current baseline:** ~38MB (through Layer 4, before Piper TTS or any Layer 5 additions).

**Bundled size adds (permanent, part of the install):**
| Feature | Est. size add | Status |
|---|---|---|
| 8 avatar portraits (already wired) | 2.6MB confirmed (`du -sh` on the actual files) | Built |
| Piper TTS (2 voices + runtime) | ~150-180MB estimated, unverified | Deferred — fast-follow after Layer 4 |

**Data-cost, not size-cost (online-only features, don't touch offline core):**
| Feature | Cost type | Notes |
|---|---|---|
| YouTube deep-link / curated content | User's mobile/wifi data, not APK size | Requires connection — explicitly an online-only bonus, not part of offline-first core. Does not affect install size. |

**Rule going forward:** any new feature contract that adds bundled assets (audio, video, art, models) must include an honest size estimate before implementation, verified where possible, flagged as unverified where not — same discipline already applied to the Piper research. Any feature requiring a live network connection must be explicitly marked as **online-only, non-core**, since it sits outside the offline-first guarantee.
