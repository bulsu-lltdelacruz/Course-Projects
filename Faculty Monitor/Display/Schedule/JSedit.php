<script>
    class Edit
    {
        constructor(){

        }
        updateEdit(token){
            let done =false;
            document.getElementById('form-schedule-edit').style.display = 'block';
            dim.style.display = 'block';
            facultySelEdit.innerHTML = '';
            Object.keys(facultyRecords).forEach(key => {
                var middleName = '';
                middleName = facultyRecords[key]['middle-name'];
                var fullName = facultyRecords[key]['first-name'] +' '+  middleName.substring(0,1)+'. '+ facultyRecords[key]['last-name'];
                const option = document.createElement('option');
                option.innerHTML = fullName;
                option.value = key;
                
                facultySelEdit.appendChild(option)
                //console.log(JSON.stringify(scheduleRecords[token]));
            });
            this.updateFields(token);
        }

        updateFields(token){
            const faculty = document.getElementById('name-edit');
            const section = document.getElementById('section-edit');
            const subject = document.getElementById('subject-edit');
            const room = document.getElementById('room-edit');
            const classMode = document.getElementById('class-mode-edit');
            const link = document.getElementById('link-edit');
            const daysOfWeek = document.getElementById('days-of-week-edit');

            const monthFrom = document.getElementById('month-from-edit');
            const monthTo = document.getElementById('month-to-edit');
            const schedStart = document.getElementById('schedule-start-edit');
            const schedEnd = document.getElementById('schedule-end-edit');

            for(let i=0;i<faculty.childElementCount;i++){
                if(scheduleRecords[token]['facultyID'] == faculty[i].value)
                    faculty[i].selected = true;
            }
            section.value = scheduleRecords[token]['section'];
            subject.value = scheduleRecords[token]['subject'];
            room.value = scheduleRecords[token]['room'];
            classMode.value = scheduleRecords[token]['classMode'];
            link.value = scheduleRecords[token]['link'];
            daysOfWeek.value = scheduleRecords[token]['daysOfWeek'];

            monthFrom.value = scheduleRecords[token]['monthFrom'];
            monthTo.value = scheduleRecords[token]['monthTo'];

            for(let i=0;i<schedStart.childElementCount;i++){
                if(scheduleRecords[token]['scheduleStart'] == schedStart[i].value)
                    schedStart[i].selected = true;
            }
            for(let i=0;i<schedEnd.childElementCount;i++){
                if(scheduleRecords[token]['scheduleEnd'] == schedEnd[i].value)
                    schedEnd[i].selected = true;
            }
            document.getElementById('token').value = token;
        }
    }
</script>