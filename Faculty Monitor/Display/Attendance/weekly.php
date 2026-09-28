<style>
  :root {
    --bg: #f7f9fc;
    --card: #ffffff;
    --accent: #6a5af9;
    --muted: #6b7280;
    --today: #fde68a;
  }

    .container { 
      display: flex; 
      justify-content: center; 
    }
  .card {
    width: 100%;
    background: transparent;
    border-radius: 16px;
    margin: 20px auto;
    overflow: auto;
  }
  header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24px;
    padding: 10px 20px;
  }
  .title { font-size: 26px; font-weight: 700; }
  .controls { display: flex; gap: 12px; align-items: center;justify-content: right;width: 50%;}
  button {
    appearance: none;
    border: 0;
    background: none;
    padding: 10px 16px;
    border-radius: 10px;
    font-weight: 600;
    cursor: pointer;
    font-size: 16px;
  }
  button:disabled { opacity: .35; cursor: not-allowed; }
  .btn-primary { background: var(--accent); color: white; }

  .week {
    border-radius: 14px;
    background: linear-gradient(180deg, #fff, #fbfdff);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    margin-inline: 7.5%;
    margin-bottom: 15px;
  }
  header{
    padding-inline: 7.5%;
  }
  .week h3 {
    color: var(--card);
    background-color: var(--accent);
    margin: 0;
    font-size: 22px;
    text-align: center;
    padding: 12px;
    border-top-right-radius: 8px;
    border-top-left-radius: 8px;
  }
  .weekdays, .days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 0px;
  }
  .weekday {
    font-size: 16px;
    text-align: center;
    color: var(--muted);
    padding: 8px 0;
    background-color: var(--accent);
    color: var(--card);
    border-top: 1px solid var(--muted);
  }
  .day {
    min-height: 110px;
    border-radius: 0px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    padding: 8px;
    text-align: center;
    font-size: 14px;
    cursor: pointer;
    transition: all .2s ease;
    background: #fff;
  }
  .day.today { background: var(--today); }
  .day:hover { background: var(--accent); color: #fff;}
  .day:hover .count-p{color:#fff;}
  #currentLabel{width: 57%;}
  
  /**************************/
    .change-view{
      display: flex;
      flex-direction: row-reverse;
      margin-bottom: 15px;
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
    #weekly{
      background-color: var(--accent);
      color: var(--card);
    }
    #add-new{
      margin-inline-end: 1.5%;
    }
    .btn-primary{background:var(--accent);color:white; font-size: 16px;padding: 9px,12px,12px,9px;font-weight: 400;}
</style>
<div class="card">
  <header>
    <div id="title" class="title"></div>
    <div class="controls">
      <button type="button" id="prev">◀ Prev</button>
      <div id="currentLabel" style="min-width:200px;text-align:center;font-weight:700;font-size:20px">Week 1 — Jan 1–7</div>
      <button type="button" id="next" class="btn-primary">Next ▶</button>
    </div>
  </header>
  <div class="change-view">
        <button id="weekly" style="background-color: var(--accent);
      color: var(--card);">Weekly</button>
        <button id="monthly">Monthly</button>
        <button id="table">Table</button>
    </div>

  <main>
    <div id="calendar"></div>
  </main>
</div>


<script>
  const monthly = document.getElementById('monthly');
  const weekly = document.getElementById('weekly');
  table.addEventListener('click', ()=>{
    window.location.replace('attendanceTable.php');
  });
  monthly.addEventListener('click', ()=>{
    window.location.replace('attendancePage.php');
  });
  function closeDim(){
    document.getElementById('dim').style.display = 'none';
  }
</script>