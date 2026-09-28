<link rel="stylesheet" href="../form.css">
<style>
    
</style>
<div id="form-faculty" class="form-div">
    <div class="top">
        <h2>New Entry</h2>
    </div>
    <form  action="../../CRUD/Faculty/insertFaculty.php" method="post">
        
        
        <!---->
        <input style="display: none;" type="text" name="month" id="month">
        <input style="display: none;" type="text" name="day" id="day">
        <input style="display: none;" type="text" name="year" id="year">
        <!---->
        <div class="left-side">
            <div class="form-group">
                <label for="id-num">Faculty</label>
                <input required type="text" name="id-num" class="" placeholder="ID No.">
            </div>
            <div class="form-group">
                <label for="id-num">Last Name</label>
                <input required type="text" name="last-name" class="" placeholder="Last Name">
            </div>
            <div class="form-group">
                <label for="first-name">First Name</label>
                <input required type="text" name="first-name" class="" placeholder="First Name">
            </div>
            <div class="form-group">
                <label for="middle-name">Middle Name</label>
                <input required type="text" name="middle-name" class="" placeholder="Middle Name">
            </div>
            
            <div class="form-group" id="submission-group">
                <input id="submit-form" type="submit" name="submit-data" class="submit-btn" placeholder="">
                <button type="button" id="close-form" class="submit-btn" onclick="closeAddForm()">Cancel</button>
            </div>
        </div><!--==================================================END OF LEFT SIDE=================================-->
        <div class="right-side">
            <div class="form-group">
                <label for="gender">Gender</label>
                <select required name="gender" id="gender">
                    <option selected value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>
            <div class="form-group">
                <label for="rank">Rank</label>
                <select required name="rank" id="rank">
                    <option selected value="Temporary">Temporary</option>
                    <option value="Part-Time">Part-Time</option>
                    <option value="Permanent">Permanent</option>
                </select>
                <!--<input type="text" name="rank" class="" placeholder="Rank">-->
            </div>
            <div class="form-group">
                <label for="faculty-status">Faculty Status</label>
                <select required name="faculty-status" id="faculty-status">
                    <option selected value="Adjunct Faculty">Adjunct Faculty</option>
                </select>
            </div>
            <div class="form-group">
                <label for="department">Department</label>
                <input required type="text" name="department" class="" placeholder="Department">
            </div>
        </div>
        
    </form>
    
</div>
<script>
    
    /*const daySelect = document.getElementById('day-select');
    const dayInput = document.getElementById('days-of-week');
    const daySelDropDown = document.getElementById('day-select-dropdown');
    const daySelGroup = document.getElementById('day-select-group');
    const confirmDaySel = document.getElementById('confirm-day-select');
    const submit = document.getElementById('submit-formt');

    const daySelectionArray = [];
    changedInput = false;
    dayInput.readOnly = true;
    dayInput.style.display =false;
    daySelGroup.addEventListener('focusin',()=>
    {
        daySelect.style.display = 'flex';
    });
    confirmDaySel.addEventListener('click',()=>
    {
        dayInput.value = daySelectionArray;
        daySelect.style.display = 'none';
    });
    
    daySelDropDown.childNodes.forEach(elem => 
    {
        elem.childNodes.forEach(el => 
        {
            el.addEventListener('change', (e)=>{
                console.log(e.target.value);
                
                if(e.target.checked)
                {
                    daySelectionArray.push(e.target.value);
                    
                }else{
                    daySelectionArray.splice(daySelectionArray.indexOf(e.target.value),1);
                }console.log(daySelectionArray);
                
            });
                
        });
        
    });
    $(".readonly").on('keydown paste focus mousedown', function(e){
        if(e.keyCode != 9) // ignore tab
            e.preventDefault();
    });
    /*submit.addEventListener('click',(e)=>{
        const readonly = document.getElementsByClassName('readOnly');
        e.preventDefault();

    });*/
    
</script>
