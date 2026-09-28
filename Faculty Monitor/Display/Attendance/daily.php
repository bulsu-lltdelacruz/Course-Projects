<?php
include '../../includes/dbConfig.php';

$ref = 'Schedule';
$database = new Database();
$records = $database->getRecords($ref);
$facultyRecords = $database->getRecords('Faculty');
$attendanceRecords = $database->getRecords('Attendance');

include '../../includes/header.php';
?>
<script>
var scheduleRecords = <?php echo json_encode($records); ?>;
var facultyRecords = <?php echo json_encode($facultyRecords); ?>;
var attendanceRecords = <?php echo json_encode($attendanceRecords); ?>;
</script>

<style>
.daily-wrap { width: 80%; margin: 0 auto 40px; }
.daily-header { display:flex; align-items:center; justify-content:space-between; margin: 8px 0 12px 0; }
.daily-controls { display:flex; gap:10px; align-items:center; }
.daily-title { font-size:22px; font-weight:700; }
.daily-nav button { cursor:pointer; padding:6px 10px; border-radius:6px; border:1px solid #ddd; background:#fff; }
.daily-container { position:relative; border-left:2px solid #e6e6e6; height:900px; overflow:auto; background:#fff; }
.time-gutter { position:absolute; left:-90px; top:0; width:80px; text-align:right; padding-right:10px; font-size:13px; color:#6b7280; }
.time-label { position:absolute; left:-90px; width:80px; height:60px; line-height:60px; }
.hour-line { position: absolute; left: 0; right: 0; height: 0; border-top: 1px dashed #f1f1f1; }
.block { position:absolute; border-radius:8px; padding:6px 8px; color:#fff; box-shadow:0 4px 10px rgba(0,0,0,0.08); overflow:hidden; cursor:pointer; }
.block .small { font-size:12px; opacity:0.95; }
.legend { margin:8px 0 12px 0; display:flex; gap:12px; font-size:13px; align-items:center; }
.legend .box { width:12px; height:12px; border-radius:3px; display:inline-block; vertical-align:middle; margin-right:6px; }
.container, .daily-container{overflow: hidden;}
.change-view{
      display: flex;
      flex-direction: row-reverse;
      margin-bottom: 15px;
      margin-inline: 7.5%;
      height: 28px;
    }
    .change-view button:not(#add-new){
      background-color: transparent;
      border-radius: 0px;
      font-size: 13px;
      padding: 5px;
      border: 1px solid var(--muted);
      color: black;
      font-weight: 400;
    }

/* ✅ NEW: Daily in-container date header */
.daily-date-header {
  position: sticky;
  top: 0;
  width: 100%;
  background: var(--accent);
  color: var(--card);
  text-align: center;
  font-weight: 600;
  padding: 6px 0;
  z-index: 5;
  border-bottom: 1px solid #ddd;
  font-size: 20px;
}
</style>

<div class="container">
  <div class="daily-wrap">
    <div class="daily-header">
      <div class="daily-title">Daily Attendance</div>
      <div class="daily-controls">
        <div class="daily-nav">
          <button id="prevDay">◀ Prev</button>
          <span id="currentLabel" style="margin:0 12px; font-weight:600;"></span>
          <button id="nextDay">Next ▶</button>
        </div>
      </div>
    </div>

  <div class="change-view">
    <button id="daily"style="background-color: var(--accent);
      color: var(--card);">Daily</button>
    <button id="weekly">Weekly</button>
    <button id="monthly" >Monthly</button>
    <button id="table">Table</button>
  </div>
    <div class="legend">
      <span><span class="box" style="background:#4FBE87"></span> Present/Excuse</span>
      <span><span class="box" style="background:#ef4444"></span> Absent</span>
      <span><span class="box" style="background:#FE5E7B"></span> Late</span>
      <span><span class="box" style="background:#6b7280"></span> No record</span>
    </div>

    <div style="position:relative; margin-left:90px;">
      <div class="time-gutter" id="timeGutter"></div>
      <div class="daily-container" id="dailyContainer" aria-live="polite">
        <!-- ✅ The date header will be dynamically updated here -->
        <div id="dailyDateHeader" class="daily-date-header"></div>
      </div>
    </div>
  </div>
</div>

<script>
const dailyContainer = document.getElementById('dailyContainer');
const timeGutter = document.getElementById('timeGutter');
const currentLabel = document.getElementById('currentLabel');
const dailyDateHeader = document.getElementById('dailyDateHeader');

const START_HOUR = 6;
const END_HOUR = 21;
const TOTAL_MINUTES = (END_HOUR - START_HOUR) * 60;
const CONTAINER_HEIGHT_PX = 900;
const PX_PER_MIN = CONTAINER_HEIGHT_PX / TOTAL_MINUTES;

function getTimeString(sched, keys) {
  for (let k of keys) if (sched[k]) return sched[k];
  return null;
}

function normalizeTimeString(ts) {
  if (!ts) return null;
  ts = ts.trim().toUpperCase();
  const match = ts.match(/^(\d{1,2}):(\d{2})\s*(AM|PM)?$/i);
  if (!match) return null;

  let hh = parseInt(match[1], 10);
  let mm = parseInt(match[2], 10);
  const period = match[3];

  if (period === 'PM' && hh < 12) hh += 12;
  if (period === 'AM' && hh === 12) hh = 0;

  hh = Math.max(0, Math.min(23, hh));
  mm = Math.max(0, Math.min(59, mm));

  return `${String(hh).padStart(2,'0')}:${String(mm).padStart(2,'0')}`;
}

function timeToMinutes(ts) {
  const norm = normalizeTimeString(ts);
  if (!norm) return null;
  const [h, m] = norm.split(':').map(n => parseInt(n,10));
  return h * 60 + m;
}

function findAttendanceStatus(scheduleKey, dateISO) {
  if (!attendanceRecords) return null;
  for (const aKey in attendanceRecords) {
    const a = attendanceRecords[aKey];
    if (a['scheduleID'] == scheduleKey && a['date'] == dateISO) {
      return (a['attendance'] || '').toString().toUpperCase();
    }
  }
  return null;
}

function renderTimeGutter() {
  timeGutter.innerHTML = '';
  for (let h = START_HOUR; h <= END_HOUR; h++) {
    const y = (h - START_HOUR) * 60 * PX_PER_MIN;
    const label = document.createElement('div');
    label.className = 'time-label';
    label.style.top = `${y}px`;
    label.textContent = `${h <= 12 ? h : h - 12}${h < 12 ? 'AM' : 'PM'}`;
    timeGutter.appendChild(label);
  }

  dailyContainer.querySelectorAll('.hour-line').forEach(e => e.remove());
  for (let h = START_HOUR; h <= END_HOUR; h++) {
    const y = (h - START_HOUR) * 60 * PX_PER_MIN;
    const line = document.createElement('div');
    line.className = 'hour-line';
    line.style.top = `${y}px`;
    dailyContainer.appendChild(line);
  }
}

function renderDay(dateObj) {
  dailyContainer.querySelectorAll('.block').forEach(e => e.remove());
  renderTimeGutter();

  const yyyy = dateObj.getFullYear();
  const mm = String(dateObj.getMonth() + 1).padStart(2,'0');
  const dd = String(dateObj.getDate()).padStart(2,'0');
  const dateISO = `${yyyy}-${mm}-${dd}`;
  const dayName = dateObj.toLocaleString('en-US', { weekday: 'long' });
  const dateLabel = `${dayName}, ${MONTH_NAME(dateObj.getMonth())} ${dateObj.getDate()}, ${yyyy}`;

  // ✅ Update both top label and in-container header
  currentLabel.textContent = dateLabel;
  dailyDateHeader.textContent = dateLabel;

  const matches = [];
  if (scheduleRecords) {
    for (const key in scheduleRecords) {
      const s = scheduleRecords[key];
      if (!s) continue;
      let mf = s['monthFrom']?.toString().slice(-2);
      let mt = s['monthTo']?.toString().slice(-2);
      let monthFromIdx = parseInt(mf,10) - 1;
      let monthToIdx = parseInt(mt,10) - 1;
      const currentMonth = dateObj.getMonth();
      if (currentMonth < monthFromIdx || currentMonth > monthToIdx) continue;

      const dow = s['daysOfWeek'] || '';
      if (!dow.includes(dayName)) continue;

      const startStr = getTimeString(s, ['schedule-start','scheduleStart','timeStart','time-start','time_start']);
      const endStr   = getTimeString(s, ['schedule-end','scheduleEnd','timeEnd','time-end','time_end']);
      const startMin = timeToMinutes(startStr);
      const endMin = timeToMinutes(endStr);
      if (startMin === null || endMin === null || endMin <= startMin) continue;

      matches.push({ key, sched: s, startMin, endMin, startStr, endStr });
    }
  }

  if (matches.length === 0) return;
  const total = matches.length;
  const widthPercent = 100 / total;

  matches.forEach((m, idx) => {
    const topPx = (m.startMin - START_HOUR * 60) * PX_PER_MIN + 30; // offset for header
    const heightPx = (m.endMin - m.startMin) * PX_PER_MIN;

    const block = document.createElement('div');
    block.className = 'block';
    block.style.top = `${topPx}px`;
    block.style.left = `${idx * widthPercent}%`;
    block.style.width = `calc(${widthPercent}% - 8px)`;
    block.style.height = `${Math.max(20, heightPx)}px`;

    const status = findAttendanceStatus(m.key, dateISO);
    let bg = '#6a5af9';
    if (status === 'ABSENT') bg = '#D83B4F';
    else if (status === 'LATE') bg = '#FE5E7B';
    else if (status === 'PRESENT' || status === 'EXCUSE') bg = '#4FBE87';
    else bg = '#6b7280';
    block.style.backgroundColor = bg;

    let facultyName = '';
    try {
      const f = facultyRecords[m.sched['facultyID']];
      if (f) facultyName = `${f['first-name']} ${f['last-name']}`;
    } catch {}

    const subj = m.sched['subject'] || '';
    const room = m.sched['room'] || '';
    block.innerHTML = `<div style="font-weight:700;">${facultyName || subj || 'Class'}</div>
                       <div class="small">${m.startStr} - ${m.endStr}${room ? ' • '+room : ''}</div>
                       <div style="font-size:11px;">${status ? status : 'No Record'}</div>`;
    dailyContainer.appendChild(block);
  });
}

function MONTH_NAME(i){
  return ['January','February','March','April','May','June','July','August','September','October','November','December'][i];
}

let currentDate = new Date();
document.getElementById('prevDay').addEventListener('click', () => { currentDate.setDate(currentDate.getDate()-1); renderDay(currentDate); });
document.getElementById('nextDay').addEventListener('click', () => { currentDate.setDate(currentDate.getDate()+1); renderDay(currentDate); });

renderDay(currentDate);

</script>

<?php include '../../includes/footer.php'; ?>
<script>attendance.className = 'active';</script>
