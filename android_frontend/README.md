# Ocean Pro Calculator (Android)

A modern, minimal calculator implementing basic arithmetic with the "Ocean Professional" theme.

## Features
- Display shows history (expression) and current value
- Numeric keypad (0-9, .), operators (+ − × ÷), equals (=), clear (C), and backspace (⌫)
- Sequential evaluation (left-to-right) for simple calculator behavior
- Decimal input with prevention of multiple dots per number segment
- Division-by-zero protection showing "Error" until cleared
- Clean Ocean Professional theme: primary #2563EB, accent #F59E0B, error #EF4444

## Build and Run
- Build: `./gradlew build`
- Install on device/emulator: `:app:installDebug`
- Launch the app named "Ocean Pro Calculator"

## Extending
- Core calculation logic is in `app/src/main/kotlin/org/example/app/Calculator.kt`.
  - PUBLIC_INTERFACE methods: `inputDigit`, `inputDot`, `applyOp`, `equals`, `clear`, `backspace`.
  - UI uses `MainActivity` to wire buttons to the calculator state and update TextViews.
- To add more operations (e.g., percentage), add to `Calculator.compute` and wire a new button in XML + click handler in `MainActivity`.

## Theming
- Colors are defined in `res/values/colors.xml`.
- Button and surface styles are defined via shape drawables in `res/drawable/`.
- Layout uses rounded corners and subtle strokes to create a modern look consistent with the style guide.