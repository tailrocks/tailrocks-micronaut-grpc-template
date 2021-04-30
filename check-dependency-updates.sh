#!/usr/bin/env bash
ABSOLUTE_PATH=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd "${ABSOLUTE_PATH}" || exit

set -e

./gradlew dependencyUpdates -Drevision=release --stacktrace --info "${GRADLE_EXTRA_ARGS}"
