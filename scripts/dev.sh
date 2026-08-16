#!/usr/bin/env zsh
# ============================================================
# dev.sh — AnverraGlobal dev environment manager
#
# Usage:
#   ./scripts/dev.sh start [backend|frontend|all]  (default: all)
#   ./scripts/dev.sh stop  [backend|frontend|all]  (default: all)
#   ./scripts/dev.sh restart [backend|frontend|all] (default: all)
#   ./scripts/dev.sh status
#   ./scripts/dev.sh logs [backend|frontend]
# ============================================================

set -euo pipefail

SCRIPT_DIR="${0:A:h}"
ROOT_DIR="${SCRIPT_DIR:h}"

BACKEND_DIR="${ROOT_DIR}/backend"
FRONTEND_DIR="${ROOT_DIR}/frontend"

BACKEND_PID_FILE="${ROOT_DIR}/.backend.pid"
FRONTEND_PID_FILE="${ROOT_DIR}/.frontend.pid"
LOGS_UI_PID_FILE="${ROOT_DIR}/.logs_ui.pid"

BACKEND_LOG="${ROOT_DIR}/.backend.log"
FRONTEND_LOG="${ROOT_DIR}/.frontend.log"
LOGS_UI_LOG="${ROOT_DIR}/.logs_ui.log"

BACKEND_PORT=8080
FRONTEND_PORT=5173
LOGS_UI_PORT=9999

# ── Colour helpers ──────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
RESET='\033[0m'

info()    { print -P "%F{cyan}[dev]%f $*" }
success() { print -P "%F{green}[dev]%f $*" }
warn()    { print -P "%F{yellow}[dev]%f $*" }
error()   { print -P "%F{red}[dev] ERROR:%f $*" >&2 }

# ── PID helpers ─────────────────────────────────────────────
_is_running() {
  local pid_file="$1"
  [[ -f "$pid_file" ]] && kill -0 "$(cat "$pid_file")" 2>/dev/null
}

_read_pid() {
  local pid_file="$1"
  [[ -f "$pid_file" ]] && cat "$pid_file" || echo "—"
}

# ── Backend ─────────────────────────────────────────────────
start_backend() {
  if _is_running "$BACKEND_PID_FILE"; then
    warn "Backend already running (PID $(_read_pid "$BACKEND_PID_FILE"))"
    return
  fi

  info "Starting Spring Boot backend on :${BACKEND_PORT} …"
  (
    cd "$BACKEND_DIR"
    ./mvnw -q spring-boot:run \
      -Dspring-boot.run.jvmArguments="-Dserver.port=${BACKEND_PORT}" \
      > "$BACKEND_LOG" 2>&1 &
    echo $! > "$BACKEND_PID_FILE"
  )
  success "Backend starting → PID $(_read_pid "$BACKEND_PID_FILE")  |  log: ${BACKEND_LOG}"
}

stop_backend() {
  if ! _is_running "$BACKEND_PID_FILE"; then
    warn "Backend is not running"
    rm -f "$BACKEND_PID_FILE"
    return
  fi

  local pid
  pid=$(cat "$BACKEND_PID_FILE")
  info "Stopping backend (PID ${pid}) …"

  # Send SIGTERM; give Spring Boot up to 15 s to shut down gracefully
  kill "$pid" 2>/dev/null || true
  local i=0
  while kill -0 "$pid" 2>/dev/null && (( i < 15 )); do
    sleep 1
    (( i++ ))
  done

  if kill -0 "$pid" 2>/dev/null; then
    warn "Graceful shutdown timed out — sending SIGKILL"
    kill -9 "$pid" 2>/dev/null || true
  fi

  rm -f "$BACKEND_PID_FILE"
  success "Backend stopped"
}

# ── Frontend ────────────────────────────────────────────────
start_frontend() {
  if _is_running "$FRONTEND_PID_FILE"; then
    warn "Frontend already running (PID $(_read_pid "$FRONTEND_PID_FILE"))"
    return
  fi

  if [[ ! -f "${FRONTEND_DIR}/.env" ]]; then
    warn ".env not found in frontend/ — copy .env.example first:"
    warn "  cp frontend/.env.example frontend/.env  (then fill in Auth0 values)"
  fi

  info "Starting Vite dev server on :${FRONTEND_PORT} …"
  (
    cd "$FRONTEND_DIR"
    npm run dev -- --port "${FRONTEND_PORT}" --host \
      > "$FRONTEND_LOG" 2>&1 &
    echo $! > "$FRONTEND_PID_FILE"
  )
  success "Frontend starting → PID $(_read_pid "$FRONTEND_PID_FILE")  |  log: ${FRONTEND_LOG}"
}

stop_frontend() {
  if ! _is_running "$FRONTEND_PID_FILE"; then
    warn "Frontend is not running"
    rm -f "$FRONTEND_PID_FILE"
    return
  fi

  local pid
  pid=$(cat "$FRONTEND_PID_FILE")
  info "Stopping frontend (PID ${pid}) …"
  kill "$pid" 2>/dev/null || true
  sleep 1
  kill -9 "$pid" 2>/dev/null || true
  rm -f "$FRONTEND_PID_FILE"
  success "Frontend stopped"
}

# ── Status ───────────────────────────────────────────────────
cmd_status() {
  print ""
  print "${BOLD}  AnverraGlobal — dev service status${RESET}"
  print "  ──────────────────────────────────"

  if _is_running "$BACKEND_PID_FILE"; then
    print "  ${GREEN}●${RESET} Backend   running  PID=$(_read_pid "$BACKEND_PID_FILE")  http://localhost:${BACKEND_PORT}"
  else
    print "  ${RED}○${RESET} Backend   stopped"
  fi

  if _is_running "$FRONTEND_PID_FILE"; then
    print "  ${GREEN}●${RESET} Frontend  running  PID=$(_read_pid "$FRONTEND_PID_FILE")  http://localhost:${FRONTEND_PORT}"
  else
    print "  ${RED}○${RESET} Frontend  stopped"
  fi

  if _is_running "$LOGS_UI_PID_FILE"; then
    print "  ${GREEN}●${RESET} Logs UI   running  PID=$(_read_pid "$LOGS_UI_PID_FILE")  http://localhost:${LOGS_UI_PORT}"
  else
    print "  ${RED}○${RESET} Logs UI   stopped"
  fi

  print ""
}

# ── Log UI ──────────────────────────────────────────────────
start_logs_ui() {
  if _is_running "$LOGS_UI_PID_FILE"; then
    warn "Logs UI already running (PID $(_read_pid "$LOGS_UI_PID_FILE"))"
    return
  fi

  info "Starting log viewer UI on port ${LOGS_UI_PORT} …"
  node "${SCRIPT_DIR}/logview.js" "${LOGS_UI_PORT}" > "$LOGS_UI_LOG" 2>&1 &
  echo $! > "$LOGS_UI_PID_FILE"
  success "Logs UI starting → PID $(_read_pid "$LOGS_UI_PID_FILE")  |  log: ${LOGS_UI_LOG}"
}

stop_logs_ui() {
  if ! _is_running "$LOGS_UI_PID_FILE"; then
    warn "Logs UI is not running"
    rm -f "$LOGS_UI_PID_FILE"
    return
  fi

  local pid
  pid=$(cat "$LOGS_UI_PID_FILE")
  info "Stopping logs UI (PID ${pid}) …"
  kill "$pid" 2>/dev/null || true
  sleep 1
  kill -9 "$pid" 2>/dev/null || true
  rm -f "$LOGS_UI_PID_FILE"
  success "Logs UI stopped"
}

cmd_logs_ui() {
  info "Logs UI should now be running in the background. Use 'dev.sh start logs-ui' or 'dev.sh start all' to start it."
  info "Access it at http://localhost:${LOGS_UI_PORT}"
}

cmd_logs() {
  local target="${1:-all}"
  case "$target" in
    backend)  tail -f "$BACKEND_LOG" ;;
    frontend) tail -f "$FRONTEND_LOG" ;;
    all)
      # Interleave both log streams with a prefix
      tail -f "$BACKEND_LOG"  --pid $$ 2>/dev/null | sed 's/^/[backend]  /' &
      tail -f "$FRONTEND_LOG" --pid $$ 2>/dev/null | sed 's/^/[frontend] /'
      wait
      ;;
    *) error "Unknown log target: ${target}" ; exit 1 ;;
  esac
}

# ── Dispatch ─────────────────────────────────────────────────
cmd_start() {
  local target="${1:-all}"
  case "$target" in
    backend)  start_backend ;;
    frontend) start_frontend ;;
    logs-ui)  start_logs_ui ;;
    all)      start_backend ; start_frontend ; start_logs_ui ;;
    *) error "Unknown target: ${target}. Use backend | frontend | logs-ui | all" ; exit 1 ;;
  esac
}

cmd_stop() {
  local target="${1:-all}"
  case "$target" in
    backend)  stop_backend ;;
    frontend) stop_frontend ;;
    logs-ui)  stop_logs_ui ;;
    all)      stop_frontend ; stop_backend ; stop_logs_ui ;;
    *) error "Unknown target: ${target}. Use backend | frontend | logs-ui | all" ; exit 1 ;;
  esac
}

cmd_restart() {
  local target="${1:-all}"
  cmd_stop  "$target"
  sleep 1
  cmd_start "$target"
}

# ── Entry point ──────────────────────────────────────────────
main() {
  local cmd="${1:-help}"
  shift || true

  case "$cmd" in
    start)    cmd_start   "${1:-all}" ;;
    stop)     cmd_stop    "${1:-all}" ;;
    restart)  cmd_restart "${1:-all}" ;;
    status)   cmd_status ;;
    logs)     cmd_logs    "${1:-all}" ;;
    logs-ui)  cmd_logs_ui "${1:-9999}" ;;
    help|--help|-h)
      cat <<'HELP'

Usage: ./scripts/dev.sh <command> [target]

Commands:
  start   [backend|frontend|all]   Start service(s)          (default: all)
  stop    [backend|frontend|all]   Stop service(s)           (default: all)
  restart [backend|frontend|all]   Stop then start service(s) (default: all)
  status                           Show running/stopped state
  logs    [backend|frontend|all]   Tail log file(s) in terminal (default: all)
  logs-ui [port]                   Open Dozzle-style web log viewer (default port: 9999)

Examples:
  ./scripts/dev.sh start              # start everything
  ./scripts/dev.sh start backend      # start only Spring Boot
  ./scripts/dev.sh restart frontend   # bounce Vite
  ./scripts/dev.sh stop               # stop everything
  ./scripts/dev.sh status
  ./scripts/dev.sh logs backend       # tail backend log in terminal
  ./scripts/dev.sh logs-ui            # open http://localhost:9999 log viewer
  ./scripts/dev.sh logs-ui 8888       # custom port

HELP
      ;;
    *)
      error "Unknown command: ${cmd}"
      main help
      exit 1
      ;;
  esac
}

main "$@"
