let logOutPopUp;
let navBar;
let prevNavbarTop;
let accAlt;
start();

function start()
{
    accAlt = document.querySelector('.subLogInNav');
    accAlt.style.display = 'none';
    logOutPopUp = document.getElementById('logoutPopUp');
    navBar = document.getElementById('navBar');

    if(navBar.getBoundingClientRect().top == 0)
        {
            accAlt.style.display = 'inline';
            console.log("gagoo");
            prevNavbarTop = 0;
        }


    document.getElementById('logOutBtn').onclick = 
    ()=>{window.location = '/Profile/index.html'};
    document.addEventListener('scroll', ()=>{

        if(navBar.getBoundingClientRect().top == 0)
        {
            accAlt.style.display = 'inline';
            console.log("gagoo");
            prevNavbarTop = 0;
        }
        if(navBar.getBoundingClientRect().top > 0)
            {
                accAlt.style.display = 'none';
                console.log("gagoo");
                prevNavbarTop = 0;
            }

    });
}
function popUpLogOut()
{
    let bg = document.getElementById('darkenScreen');
    bg.style.opacity = '0.5';
    bg.style.display = "block";
    logOutPopUp.style.display = "block";
}
function unPopUpLogOut()
{
    let bg = document.getElementById('darkenScreen');
    bg.style.opacity = '0';
    bg.style.display = "none";
    logOutPopUp.style.display = "none";
}