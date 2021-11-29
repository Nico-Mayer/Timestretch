import Modal from "./modal.js";

const backdrop = document.querySelector("#modal-backdrop");
const modalP = document.querySelector("#modal-container-project");
const modalT = document.querySelector("#modal-container-task");
const modalC = document.querySelector("#modal-container-confirm");
const modalS = document.querySelector("#modal-container-project-settings");

const addTaskBtn = document.getElementById("add-task-btn");
const deleteBtnArray = document.querySelectorAll(".project-delete-btn");
const settingsBtnArray = document.querySelectorAll(".project-settings-btn");

const addProjectBtnArray = document.querySelectorAll(".addProjectBtn");


if(addProjectBtnArray != null){
    addProjectBtnArray.forEach(function (e){
        let projectModalArray = (new Modal(
            "project",
            backdrop,
            modalP,
            e,
            "/addProject"
        ));
            projectModalArray.set();
    })
}


if (deleteBtnArray !== null){
    deleteBtnArray.forEach(function(e){
        e.addEventListener("click", function(){
            event.stopPropagation();
            let deleteModal = new Modal("", backdrop, modalC, e, e.id)
            deleteModal.set();
        })
    })
}

if (settingsBtnArray !== null){
    settingsBtnArray.forEach(function(e){
        e.addEventListener("click", function(){
            event.stopPropagation();
            let settingsModal = new Modal("settings", backdrop, modalS, e, e.id)
            settingsModal.set();
        })
    })
}

if(addTaskBtn !== null){
    const projectID = window.location.href.substr(29); // Verbesserung suchen
    //console.log(projectID);
    const taskModal = new Modal("task", backdrop,modalT,addTaskBtn, "/addNewTask"+projectID);
    taskModal.set();
}

// Content Switcher for Single Page use
const path = window.location.pathname;
let homeContentView = document.getElementById("home-content-view");
let projectContetView = document.getElementById("project-content-view")
if(path == "/"){
    projectContetView.style.display = "none";
}else{
    homeContentView.style.display = "none";
}


const collapseBtn = document.getElementById("done-container-collapse-btn");
const doneSidebar = document.getElementById("done-sidebar")
collapseBtn.addEventListener('click', function(){
        doneSidebar.classList.toggle("collapsed");
        collapseBtn.classList.toggle("btn-collapsed-mode");
})