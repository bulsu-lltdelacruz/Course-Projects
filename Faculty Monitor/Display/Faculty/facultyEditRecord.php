<link rel="stylesheet" href="../form.css">
<style>
    #close-form-edit{
        margin-left: 15px;
        background-color: transparent;
        color: black;
    }
    #submission-group-edit{
        display: flex;
        flex-direction: row;
        margin-block-start: 20px;
        margin-block-end: 20px;
    }
    #submit-form-edit, #close-form-edit{
        font-size: 15px;
        padding: 10px;
        height: 40px;
        font-weight: 400;
    }
</style>
<?php $currentToken = '';?>
<div id="form-faculty-edit" class="form-div">
    <div class="top">
        <h2>Edit Entry</h2>
    </div>
    <form  action="../../CRUD/Faculty/updateFaculty.php" method="post">
        <!---->
        <input style="display: none;" type="text" name="month" id="month">
        <input style="display: none;" type="text" name="day" id="day">
        <input style="display: none;" type="text" name="year" id="year">
        <input style="display: none;" type="text" name="token" id="token">
        <!---->
        <div class="left-side">
            <div class="form-group">
                <label for="id-num">Faculty</label>
                <input required type="text" name="id-num" id="id-num-edit" class="" placeholder="ID No.">
            </div>
            <div class="form-group">
                <label for="id-num">Last Name</label>
                <input required type="text" name="last-name" id="last-name-edit" class="" placeholder="Last Name">
            </div>
            <div class="form-group">
                <label for="first-name">First Name</label>
                <input required type="text" name="first-name" id="first-name-edit" class="" placeholder="First Name">
            </div>
            <div class="form-group">
                <label for="middle-name">Middle Name</label>
                <input required type="text" name="middle-name" id="middle-name-edit" class="" placeholder="Middle Name">
            </div>
            
            <div class="form-group" id="submission-group-edit">
                <input id="submit-form-edit" type="submit" name="submit-data" class="submit-btn" placeholder="">
                <button type="button" id="close-form-edit" class="submit-btn" onclick="closeEditForm()">Cancel</button>
            </div>
        </div><!--==================================================END OF LEFT SIDE=================================-->
        <div class="right-side">
            <div class="form-group">
                <label for="gender-edit">Gender</label>
                <select required name="gender" id="gender-edit">
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                </select>
            </div>
            <div class="form-group">
                <label for="rank">Rank</label>
                <select required name="rank" id="rank-edit">
                    <option value="Temporary">Temporary</option>
                    <option value="Part-Time">Part-Time</option>
                    <option value="Permanent">Permanent</option>
                </select>
                <!--<input type="text" name="rank" class="" placeholder="Rank">-->
            </div>
            <div class="form-group">
                <label for="faculty-status">Faculty Status</label>
                <select required name="faculty-status" id="faculty-status-edit">
                    <option selected value="Adjunct Faculty">Adjunct Faculty</option>
                </select>
            </div>
            <div class="form-group">
                <label for="department">Department</label>
                <input required type="text" name="department" id="department-edit" class="" placeholder="Department">
            </div>
        </div>
        
    </form>
    
</div>
<script>
</script>
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
