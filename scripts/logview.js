#!/usr/bin/env node
/**
 * AnverraGlobal — Dev Log Viewer
 *
 * A Dozzle-style web UI for the local dev log files produced by dev.sh.
 * Uses only Node.js built-in modules (http, fs, path, url).
 *
 * Usage:
 *   node scripts/logview.js [port]         (default port: 9999)
 *   ./scripts/dev.sh logs-ui               (convenience wrapper)
 *
 * Opens:  http://localhost:9999
 */

'use strict';

const http = require('http');
const fs   = require('fs');
const path = require('path');
const { URL } = require('url');

const ROOT     = path.resolve(__dirname, '..');
const PORT     = parseInt(process.argv[2] || process.env.LOG_PORT || '9999', 10);

const LOG_FILES = {
  backend:  path.join(ROOT, '.backend.log'),
  frontend: path.join(ROOT, '.frontend.log'),
};

// ── SSE helpers ──────────────────────────────────────────────────────────────

/**
 * Tail a file from the end and stream new lines as SSE events.
 * Sends up to HISTORY_LINES of existing content on connect, then live lines.
 */
const HISTORY_LINES = 300;

function tailFile(filePath, res) {
  // SSE headers
  res.writeHead(200, {
    'Content-Type':  'text/event-stream',
    'Cache-Control': 'no-cache',
    'Connection':    'keep-alive',
    'X-Accel-Buffering': 'no',
  });

  function sendLine(line) {
    // Escape newlines so the SSE frame stays intact
    const payload = JSON.stringify(line);
    res.write(`data: ${payload}\n\n`);
  }

  // ── Send historical tail ──
  let history = [];
  if (fs.existsSync(filePath)) {
    try {
      const raw = fs.readFileSync(filePath, 'utf8');
      history = raw.split('\n').filter(Boolean).slice(-HISTORY_LINES);
    } catch { /* file locked / empty — ok */ }
  }
  for (const line of history) sendLine(line);

  // ── Watch for new lines ──
  let fileSize = fs.existsSync(filePath) ? fs.statSync(filePath).size : 0;

  const watcher = fs.watch(path.dirname(filePath), { persistent: false }, (event, filename) => {
    if (!filename || !filePath.endsWith(filename)) return;
    try {
      const stat    = fs.statSync(filePath);
      const newSize = stat.size;
      if (newSize <= fileSize) { fileSize = newSize; return; }

      const stream = fs.createReadStream(filePath, {
        start:    fileSize,
        end:      newSize - 1,
        encoding: 'utf8',
      });
      fileSize = newSize;
      let buf = '';
      stream.on('data', chunk => { buf += chunk; });
      stream.on('end', () => {
        const lines = buf.split('\n').filter(Boolean);
        for (const line of lines) sendLine(line);
      });
    } catch { /* file disappeared — ok */ }
  });

  // Heartbeat to keep proxies alive
  const heartbeat = setInterval(() => res.write(': heartbeat\n\n'), 15_000);

  res.on('close', () => {
    clearInterval(heartbeat);
    watcher.close();
  });
}

// ── HTML UI ──────────────────────────────────────────────────────────────────

const HTML = /* html */ `<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>AnverraGlobal — Dev Logs</title>
<style>
  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :root {
    --bg:           #0d1117;
    --surface:      #161b22;
    --surface2:     #1c2128;
    --border:       #30363d;
    --text:         #e6edf3;
    --muted:        #7d8590;
    --accent:       #58a6ff;
    --backend-hue:  #3fb950;
    --frontend-hue: #d2a8ff;

    --level-error:  #f85149;
    --level-warn:   #e3b341;
    --level-info:   #58a6ff;
    --level-debug:  #7d8590;
    --level-trace:  #56d364;

    --font-mono: 'JetBrains Mono', 'Fira Code', 'Cascadia Code', ui-monospace, monospace;
  }

  html, body { height: 100%; }

  body {
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
    background: var(--bg);
    color: var(--text);
    display: flex;
    flex-direction: column;
    height: 100dvh;
    overflow: hidden;
  }

  /* ── Topbar ── */
  .topbar {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 20px;
    background: var(--surface);
    border-bottom: 1px solid var(--border);
    flex-shrink: 0;
    user-select: none;
  }
  .topbar-logo {
    font-weight: 700;
    font-size: 15px;
    letter-spacing: -.3px;
    color: var(--accent);
  }
  .topbar-logo span { color: var(--muted); font-weight: 400; }
  .topbar-spacer { flex: 1; }
  .topbar-badge {
    font-size: 11px;
    padding: 2px 8px;
    border-radius: 20px;
    border: 1px solid var(--border);
    color: var(--muted);
  }
  .topbar-badge.live { border-color: #56d364; color: #56d364; }

  .btn {
    cursor: pointer;
    border: 1px solid var(--border);
    background: var(--surface2);
    color: var(--text);
    font-size: 12px;
    padding: 4px 10px;
    border-radius: 6px;
    transition: background .15s, border-color .15s;
  }
  .btn:hover { background: var(--border); border-color: var(--muted); }
  .btn-danger:hover { border-color: var(--level-error); color: var(--level-error); }

  /* ── Search bar ── */
  .searchbar {
    padding: 8px 20px;
    background: var(--surface);
    border-bottom: 1px solid var(--border);
    flex-shrink: 0;
    display: flex;
    gap: 10px;
    align-items: center;
  }
  .search-input {
    flex: 1;
    background: var(--bg);
    border: 1px solid var(--border);
    border-radius: 6px;
    color: var(--text);
    font-size: 13px;
    padding: 5px 10px;
    outline: none;
    transition: border-color .15s;
  }
  .search-input:focus { border-color: var(--accent); }
  .search-input::placeholder { color: var(--muted); }
  .filter-label { font-size: 12px; color: var(--muted); white-space: nowrap; }

  /* ── Log columns ── */
  .columns {
    display: grid;
    grid-template-columns: 1fr 1fr;
    flex: 1;
    min-height: 0;
    gap: 1px;
    background: var(--border);
  }

  .log-panel {
    display: flex;
    flex-direction: column;
    background: var(--bg);
    min-height: 0;
  }

  .panel-header {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    background: var(--surface2);
    border-bottom: 1px solid var(--border);
    flex-shrink: 0;
  }
  .panel-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--muted); }
  .panel-dot.backend  { background: var(--backend-hue); }
  .panel-dot.frontend { background: var(--frontend-hue); }
  .panel-title { font-size: 13px; font-weight: 600; }
  .panel-title.backend  { color: var(--backend-hue); }
  .panel-title.frontend { color: var(--frontend-hue); }
  .panel-url { font-size: 11px; color: var(--muted); margin-left: 4px; }
  .panel-spacer { flex: 1; }
  .panel-count { font-size: 11px; color: var(--muted); }

  .log-output {
    flex: 1;
    overflow-y: auto;
    padding: 8px 0;
    font-family: var(--font-mono);
    font-size: 12px;
    line-height: 1.6;
    scroll-behavior: smooth;
  }
  .log-output::-webkit-scrollbar { width: 6px; }
  .log-output::-webkit-scrollbar-track { background: transparent; }
  .log-output::-webkit-scrollbar-thumb { background: var(--border); border-radius: 3px; }

  .log-line {
    display: flex;
    gap: 10px;
    padding: 1px 16px;
    white-space: pre-wrap;
    word-break: break-all;
    transition: background .05s;
  }
  .log-line:hover { background: var(--surface2); }
  .log-line.hidden { display: none; }

  .line-ts  { color: var(--muted); flex-shrink: 0; font-size: 10px; padding-top: 2px; }
  .line-msg { flex: 1; }

  /* Level colouring */
  .log-line[data-level="error"] .line-msg { color: var(--level-error); }
  .log-line[data-level="warn"]  .line-msg { color: var(--level-warn); }
  .log-line[data-level="info"]  .line-msg { color: var(--level-info); }
  .log-line[data-level="debug"] .line-msg { color: var(--level-debug); }
  .log-line[data-level="trace"] .line-msg { color: var(--level-trace); }

  /* Empty state */
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    gap: 8px;
    color: var(--muted);
    font-size: 13px;
    pointer-events: none;
    user-select: none;
  }
  .empty-icon { font-size: 32px; opacity: .4; }

  /* Statusbar */
  .statusbar {
    padding: 4px 20px;
    background: var(--surface);
    border-top: 1px solid var(--border);
    flex-shrink: 0;
    font-size: 11px;
    color: var(--muted);
    display: flex;
    gap: 20px;
  }
  .statusbar strong { color: var(--text); }

  /* Autoscroll toggle */
  .autoscroll-btn { padding: 2px 8px; }
  .autoscroll-btn.active { border-color: #56d364; color: #56d364; }
</style>
</head>
<body>

<!-- Topbar -->
<nav class="topbar">
  <div class="topbar-logo">Anverra<span>Global</span> — Dev Logs</div>
  <div class="topbar-spacer"></div>
  <span class="topbar-badge live" id="live-badge">● LIVE</span>
  <button class="btn btn-danger" onclick="clearAll()">Clear all</button>
</nav>

<!-- Search -->
<div class="searchbar">
  <span class="filter-label">Filter</span>
  <input class="search-input" id="search" type="text" placeholder="Filter lines across both panels… (regex supported)" />
  <button class="btn" onclick="document.getElementById('search').value=''; applyFilter()">✕ Clear</button>
  <button class="btn autoscroll-btn active" id="autoscroll-btn" onclick="toggleAutoscroll()">↓ Auto-scroll</button>
</div>

<!-- Log columns -->
<div class="columns">

  <!-- Backend -->
  <div class="log-panel" id="panel-backend">
    <div class="panel-header">
      <span class="panel-dot backend"></span>
      <span class="panel-title backend">Backend</span>
      <span class="panel-url">:8080  ·  Spring Boot</span>
      <span class="panel-spacer"></span>
      <span class="panel-count" id="count-backend">0 lines</span>
      <button class="btn" style="margin-left:8px" onclick="clearPanel('backend')">Clear</button>
    </div>
    <div class="log-output" id="output-backend">
      <div class="empty-state" id="empty-backend">
        <span class="empty-icon">⬡</span>
        <span>Waiting for backend logs…</span>
        <span style="font-size:11px">Start with: ./scripts/dev.sh start backend</span>
      </div>
    </div>
  </div>

  <!-- Frontend -->
  <div class="log-panel" id="panel-frontend">
    <div class="panel-header">
      <span class="panel-dot frontend"></span>
      <span class="panel-title frontend">Frontend</span>
      <span class="panel-url">:5173  ·  Vite</span>
      <span class="panel-spacer"></span>
      <span class="panel-count" id="count-frontend">0 lines</span>
      <button class="btn" style="margin-left:8px" onclick="clearPanel('frontend')">Clear</button>
    </div>
    <div class="log-output" id="output-frontend">
      <div class="empty-state" id="empty-frontend">
        <span class="empty-icon">⬡</span>
        <span>Waiting for frontend logs…</span>
        <span style="font-size:11px">Start with: ./scripts/dev.sh start frontend</span>
      </div>
    </div>
  </div>

</div>

<!-- Status bar -->
<footer class="statusbar">
  <span>Backend: <strong id="stat-backend">connecting…</strong></span>
  <span>Frontend: <strong id="stat-frontend">connecting…</strong></span>
  <span id="stat-filter" style="display:none">Filter: <strong id="stat-filter-text"></strong></span>
  <span style="margin-left:auto">Log viewer · port <strong>${PORT}</strong></span>
</footer>

<script>
const counts     = { backend: 0, frontend: 0 };
const sources    = ['backend', 'frontend'];
let autoscroll   = true;
let filterRegex  = null;

// ── Parse log level from a line ──────────────────────────────
function parseLevel(line) {
  const u = line.toUpperCase();
  if (u.includes('ERROR') || u.includes('SEVERE'))               return 'error';
  if (u.includes('WARN'))                                         return 'warn';
  if (u.includes(' INFO') || u.includes('[INFO]'))                return 'info';
  if (u.includes('DEBUG'))                                        return 'debug';
  if (u.includes('TRACE'))                                        return 'trace';
  return 'default';
}

// ── Append a log line to a panel ────────────────────────────
function appendLine(source, rawLine) {
  const output = document.getElementById('output-' + source);
  const empty  = document.getElementById('empty-' + source);

  if (empty) empty.remove();

  const level = parseLevel(rawLine);
  const now   = new Date().toLocaleTimeString('en', { hour12: false });

  const row = document.createElement('div');
  row.className  = 'log-line';
  row.dataset.level = level;
  row.dataset.raw   = rawLine.toLowerCase();

  const ts  = document.createElement('span');
  ts.className  = 'line-ts';
  ts.textContent = now;

  const msg = document.createElement('span');
  msg.className  = 'line-msg';
  msg.textContent = rawLine;

  row.appendChild(ts);
  row.appendChild(msg);

  // Apply current filter
  if (filterRegex && !filterRegex.test(rawLine)) {
    row.classList.add('hidden');
  }

  output.appendChild(row);
  counts[source]++;
  document.getElementById('count-' + source).textContent = counts[source] + ' lines';

  if (autoscroll) output.scrollTop = output.scrollHeight;
}

// ── SSE connections ──────────────────────────────────────────
function connectSource(source) {
  const stat = document.getElementById('stat-' + source);
  const es   = new EventSource('/stream/' + source);

  es.onopen = () => {
    stat.textContent = 'connected';
    stat.style.color = '#56d364';
  };

  es.onmessage = (e) => {
    try {
      const line = JSON.parse(e.data);
      if (line) appendLine(source, line);
    } catch { appendLine(source, e.data); }
  };

  es.onerror = () => {
    stat.textContent = 'reconnecting…';
    stat.style.color = '#e3b341';
  };
}

sources.forEach(connectSource);

// ── Filter ───────────────────────────────────────────────────
function applyFilter() {
  const val  = document.getElementById('search').value.trim();
  const stat = document.getElementById('stat-filter');
  const stxt = document.getElementById('stat-filter-text');

  filterRegex = val ? new RegExp(val, 'i') : null;

  document.querySelectorAll('.log-line').forEach(row => {
    if (!filterRegex || filterRegex.test(row.dataset.raw || '')) {
      row.classList.remove('hidden');
    } else {
      row.classList.add('hidden');
    }
  });

  if (val) {
    stat.style.display = '';
    stxt.textContent = val;
  } else {
    stat.style.display = 'none';
  }
}

document.getElementById('search').addEventListener('input', applyFilter);

// ── Clear ────────────────────────────────────────────────────
function clearPanel(source) {
  const output = document.getElementById('output-' + source);
  output.innerHTML = '';
  counts[source] = 0;
  document.getElementById('count-' + source).textContent = '0 lines';
}

function clearAll() {
  sources.forEach(clearPanel);
}

// ── Auto-scroll toggle ───────────────────────────────────────
function toggleAutoscroll() {
  autoscroll = !autoscroll;
  const btn = document.getElementById('autoscroll-btn');
  btn.classList.toggle('active', autoscroll);
}

// Disable autoscroll when user manually scrolls up
sources.forEach(source => {
  const output = document.getElementById('output-' + source);
  output.addEventListener('scroll', () => {
    const atBottom = output.scrollHeight - output.scrollTop - output.clientHeight < 40;
    if (!atBottom && autoscroll) {
      autoscroll = false;
      document.getElementById('autoscroll-btn').classList.remove('active');
    }
  });
});
</script>
</body>
</html>`;

// ── HTTP server ──────────────────────────────────────────────────────────────

const server = http.createServer((req, res) => {
  const { pathname } = new URL(req.url, `http://localhost:${PORT}`);

  // SSE stream endpoints
  if (pathname.startsWith('/stream/')) {
    const source = pathname.slice('/stream/'.length);
    if (!LOG_FILES[source]) {
      res.writeHead(404); res.end('Unknown source');
      return;
    }
    tailFile(LOG_FILES[source], res);
    return;
  }

  // Root → serve embedded HTML
  if (pathname === '/' || pathname === '/index.html') {
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    res.end(HTML);
    return;
  }

  res.writeHead(404); res.end('Not found');
});

server.listen(PORT, '0.0.0.0', () => {
  const url = `http://localhost:${PORT}`;
  console.log(`\n  AnverraGlobal — Dev Log Viewer`);
  console.log(`  ──────────────────────────────`);
  console.log(`  Open: \x1b[36m${url}\x1b[0m`);
  console.log(`  Watching:`);
  for (const [name, file] of Object.entries(LOG_FILES)) {
    console.log(`    ${name.padEnd(10)} ${file}`);
  }
  console.log(`\n  Press Ctrl+C to stop\n`);
});

server.on('error', (err) => {
  if (err.code === 'EADDRINUSE') {
    console.error(`\n  Port ${PORT} is already in use.`);
    console.error(`  Try: node scripts/logview.js <another-port>\n`);
  } else {
    throw err;
  }
  process.exit(1);
});
