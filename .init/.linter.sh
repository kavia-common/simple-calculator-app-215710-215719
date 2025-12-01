#!/bin/bash
cd /home/kavia/workspace/code-generation/simple-calculator-app-215710-215719/android_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

