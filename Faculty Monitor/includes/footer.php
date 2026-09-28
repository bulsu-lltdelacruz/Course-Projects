    </div>
    
  </body>
  <style>
    
  </style>
  <script>
  const home = document.getElementById('home');
  const facultyList = document.getElementById('faculty-list');
  const attendance = document.getElementById('attendance');
  const schedule = document.getElementById('schedule');
  const user = document.getElementById('user');
  home.className = '';
  facultyList.className = '';
  schedule.className = '';
  attendance.className = ''
  user.className = '';
  function goToPage(location)
  {
    window.location.href = location;
  }
  

  home.addEventListener('click', ()=>goToPage('../Home/home.php'));
  facultyList.addEventListener('click', ()=>goToPage('../Faculty/facultyList.php'));
  attendance.addEventListener('click', ()=>goToPage('../Attendance/attendancePage.php'));
  schedule.addEventListener('click', ()=>goToPage('../Schedule/schedule.php'));
  user.addEventListener('click', ()=>goToPage('../User/user.php'));
</script>
    </html>