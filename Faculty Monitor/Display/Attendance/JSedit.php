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
            document.getElementById('token').value = stuff['attendanceKey'];
        }

        updateAdd(stuff){
            let done =false;
            document.getElementById('form-add').style.display = 'block';
            if(stuff['isUpdate'])
            {
                document.getElementById('form').action = '../../CRUD/Attendance/attendanceUpdate.php';
                document.getElementById('photo').required = false;
            }
            dim.style.display = 'block';
            facultySel.innerHTML = '';
            Object.keys(facultyRecords).forEach(key => {
                var middleName = '';
                middleName = facultyRecords[key]['middle-name'];
                var fullName = facultyRecords[key]['first-name'] +' '+  middleName.substring(0,1)+'. '+ facultyRecords[key]['last-name'];
                const option = document.createElement('option');
                option.innerHTML = fullName;
                option.value = key;
                
                facultySel.appendChild(option);
            });
            this.updateAddFields(stuff);
        }

        updateAddFields(stuff){
            var token = stuff['scheduleKey'];

            document.getElementById('token-attendance').value = stuff['attendanceKey'];
            document.getElementById('token').value = token;
            //console.log(document.getElementById('token').value);
            
            const faculty = document.getElementById('name');
            const section = document.getElementById('section');
            const subject = document.getElementById('subject');
            const room = document.getElementById('room');
            const classMode = document.getElementById('class-mode');
            const daysOfWeek = document.getElementById('days-of-week');

            const date = document.getElementById('date');
            const schedStart = document.getElementById('schedule-start');
            const schedEnd = document.getElementById('schedule-end');

            const link = document.getElementById('link');
            const attendance = document.getElementById('attendance-status');
            const dressCode = document.getElementById('dress-code');
            const remarks = document.getElementById('remarks');

            /*for(let i=0;i<faculty.childElementCount;i++){
                if(scheduleRecords[token]['facultyID'] == faculty[i].value)
                    faculty[i].selected = true;
            }*/
            var key = scheduleRecords[token]['facultyID'];
            var middleName = '';
            middleName = facultyRecords[key]['middle-name'];
            var fullName = facultyRecords[key]['first-name'] +' '+  middleName.substring(0,1)+'. '+ facultyRecords[key]['last-name'];

            
            faculty.value = fullName;
            section.value = scheduleRecords[token]['section'];
            subject.value = scheduleRecords[token]['subject'];
            room.value = scheduleRecords[token]['room'];
            classMode.value = scheduleRecords[token]['classMode'];
            link.value = scheduleRecords[token]['link'];
            daysOfWeek.value = scheduleRecords[token]['daysOfWeek'];
            date.value = stuff['date'];
            schedStart.value = scheduleRecords[token]['scheduleStart'];
            schedEnd.value = scheduleRecords[token]['scheduleEnd'];
            if(stuff['isUpdate'])
            {
                link.value = attendanceRecords[stuff['attendanceKey']]['link'];
                
                for(const option of attendance)
                {
                    if(option.value == attendanceRecords[stuff['attendanceKey']]['attendance'])
                        option.selected = true;
                }
               for(const option of dressCode)
                {
                    if(option.value == attendanceRecords[stuff['attendanceKey']]['dressCode'])
                        option.selected = true;
                }
                remarks.value = attendanceRecords[stuff['attendanceKey']]['remarks'];
            }
        }
    }
</script>