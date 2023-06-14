#! /bin/bash
set -euo pipefail
echo "This is awesome, VAR1=${VAR1:-undefined}"
env
