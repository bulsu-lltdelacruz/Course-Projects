<style>
    :root{
      --bg:#f7f9fc; --card:#ffffff; --accent:#6a5af9; --muted:#6b7280; --today:#fde68a;
    }
    *{box-sizing:border-box}
    .container{background-color: white;margin:0 auto;display: flex;align-items: flex-start;justify-content: center;}
    .card{width: 100%;;background-color:transparent;border-radius:16px; margin: 20px auto 20px auto; overflow: auto;}
    body{background-color: white;}

    header{display:flex;align-items:center;justify-content:space-between;margin-bottom:24px}
    .title{font-size:26px;font-weight:700}
    .controls{display:flex;gap:12px;align-items:center}
    button{appearance:none;border:0;background:none;padding:12px 18px;border-radius:10px;font-weight:600;cursor:pointer;font-size:18px}
    button:disabled{opacity:.35;cursor:not-allowed}
    .btn-primary{background:var(--accent);color:white; font-size: 14px;padding: 5px;font-weight: 400;}

    .month{border-radius:14px;padding:0px;box-shadow:0 3px 12px rgba(2,6,23,0.06)}
    .month h3{color: var(--card);background-color: var(--accent);margin:0;font-size:22px;text-align:center;padding:12px;border-top-right-radius: 8px;border-top-left-radius: 8px;}
    .weekdays, .days{display:grid;grid-template-columns:repeat(7,1fr);gap:6px}
    .weekday{font-size:16px;text-align:center;color:var(--muted);padding:8px 0}
    .day{min-height:110px;border-radius:8px;display:flex;flex-direction: column;align-items:flex-start;justify-content:start;padding:8px;text-align: left;font-size:10px;cursor:pointer;transition:all .2s ease}
    .day.empty{opacity:0;cursor:default}
    .day.today{background:var(--today);}
    .day:hover {background:var(--accent);color:#fff;}
    .day:hover .count-p{color:#fff;}
    /**************************/
    .month{
      box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
      margin-inline: 7.5%;
    }
    .change-view{
      display: flex;
      flex-direction: row-reverse;
      height: 28px;
      margin-bottom: 15px;
      margin-inline: 7.5%;
    }
    header{
      margin-inline: 7.5%;
    }
    .change-view button:not(#add-new){
      background-color: transparent;
      border-radius: 0px;
      font-size: 13px;
      padding: 5px;
      border: 2px solid var(--muted);
      font-weight: 400;
    }
    #monthly{
      background-color: var(--accent);
      color: var(--card);
    }
    .card{
      box-shadow:0 3px 12px rgba(2,6,23,0.06)
    }
    #add-new{
      margin-inline-end: 1.5%;
    }
  </style>
<div id="monthly-view" class="card">
    <header>
    <div class="title">2025 — Interactive Calendar</div>
    <div class="controls">
        <button type="button" id="prev">◀ Prev</button>
        <div id="currentLabel" style="min-width:200px;text-align:center;font-weight:700;font-size:20px">January 2025</div>
        <button type="button" id="next" class="btn-primary">Next ▶</button>
    </div>
    </header>
  <div class="change-view">
    <button id="weekly">Weekly</button>
    <button id="monthly" style="background-color: var(--accent);
      color: var(--card);">Monthly</button>
    <button id="table">Table</button>
    <button id="add-new" class="btn-primary" onclick="openAddForm()">Add New</button>
  </div>
    <main>
    <div id="calendar"></div>
    </main>
</div>
<script>
  const table = document.getElementById('table');
  const monthly = document.getElementById('monthly');
  const weekly = document.getElementById('weekly');
  table.addEventListener('click', ()=>{
    window.location.replace('scheduleTable.php');
  });
  weekly.addEventListener('click', ()=>{
    window.location.replace('weeklySchedulePage.php');
  });
  function closeDim(){
    document.getElementById('dim').style.display = 'none';
  }
</script>
  