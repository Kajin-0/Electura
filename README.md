# Electura

Electura is a native Android prototype for practicing AC circuit analysis, complex arithmetic, and engineering problem solving. It uses Kotlin and Jetpack Compose.

## Current status

**Prototype, not yet a functioning problem solver.** The application includes a designed exercise screen and a small hard-coded problem bank. The exercise screen is not wired to that bank; its Submit Solution control does not grade answers, and its navigation/statistics/guide destinations are not implemented.

No model service, API key, or remote inference is required to run the prototype.

## Development

1. Open this repository in Android Studio.
2. Allow Gradle to sync and install the Android SDK requested by the project.
3. Select the `app` run configuration and launch a debug build on an emulator or device.

Debug builds use the standard Android debug signing configuration. Release signing remains separately configured in `app/build.gradle.kts` and requires `KEYSTORE_PATH` (or a keystore at `my-upload-key.jks`), `STORE_PASSWORD`, and `KEY_PASSWORD`, with key alias `upload`.

The repository does not currently include the Gradle Wrapper scripts. Android Studio with a compatible Gradle installation is required until these are added.

## Proposed architecture

- **Problem definitions:** units, circuit topology, source waveform, initial conditions, unknown quantity, acceptable representations, and tolerances.
- **Deterministic analysis:** complex impedance, nodal/mesh equations, phasors, transfer functions, resonance, and transient-state calculation.
- **Answer verification:** check numerical, dimensional, phasor-convention, and algebraic equivalence against independently calculated results.
- **Guided pedagogy:** incremental hints linked to the actual solution steps, with no fabricated intermediate steps.
- **Visual explanations:** circuit schematics, phasor plots, Bode plots, power triangles, and time-domain response.
- **Progress analytics:** topic mastery and error categories, backed by persistent history.

**Engineering note:** The current static RLC example (`R = 10 Ω`, `L = 0.5 H`, `C = 0.02 F`, `E(t) = 100 sin(10t)`) is at resonance, giving steady-state current `i_ss(t) = 10 sin(10t) A`. The full transient response requires initial-condition data. Clarify that distinction before offering the question as a graded problem.

## Project identity

Android application ID: `com.electura.circuits`. This replaces the initial project-generator application ID; it is a **different Android app identity**, so installed builds using the old ID will not be upgraded in place.
