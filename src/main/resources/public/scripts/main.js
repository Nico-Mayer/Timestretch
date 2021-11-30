import Modal from "./modal.js";

const backdrop = document.querySelector("#modal-backdrop");
const modalP = document.querySelector("#modal-container-project");
const modalT = document.querySelector("#modal-container-task");
const modalC = document.querySelector("#modal-container-confirm");
const modalS = document.querySelector("#modal-container-project-settings");

// Content Switcher for Single Page use
const path = window.location.pathname;
let homeContentView = document.getElementById("home-content-view");
let projectContetView = document.getElementById("project-content-view")

if(path === "/"){
    projectContetView.style.display = "none";
    document.getElementById("addBtn").classList.add("addProjectBtn")
}else{
    homeContentView.style.display = "none";
    document.getElementById("addBtn").classList.add("add-task-btn")
}
// Button Handler Area
// For Tasks
const addTaskBtnArray = document.querySelectorAll(".add-task-btn");
const deleteBtnArrayTask = document.querySelectorAll(".task-btn.delete-btn")
const doneBtnArrayTask = document.querySelectorAll(".task-btn.done-btn");

if(addTaskBtnArray !== null){
    const projectID = window.location.href.substr(29); // Verbesserung suchen
    addTaskBtnArray.forEach(function (e){
        const taskModal = new Modal("task", backdrop,modalT,e, "/addNewTask" + projectID);
        taskModal.set();
    });
}
if(deleteBtnArrayTask !== null){
    deleteBtnArrayTask.forEach(function (e){
        let deleteModal = new Modal("", backdrop, modalC, e, e.id)
        deleteModal.set();
    })
}
if(doneBtnArrayTask !== null){
    doneBtnArrayTask.forEach(function (e){
        e.addEventListener('click',function (){
            window.location.href = e.id;
        })
    })
}

// For Projects
const deleteBtnArrayProject = document.querySelectorAll(".project-delete-btn");
const settingsBtnArrayProject = document.querySelectorAll(".project-settings-btn");
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


if (deleteBtnArrayProject !== null){
    deleteBtnArrayProject.forEach(function(e){
        e.addEventListener("mouseover", function(){
            event.stopPropagation();
            let deleteModal = new Modal("", backdrop, modalC, e, e.id)
            deleteModal.set();
        })
    })
}

if (settingsBtnArrayProject !== null){
    settingsBtnArrayProject.forEach(function(e){
        e.addEventListener("mouseover", function(){
            event.stopPropagation();
            let settingsModal = new Modal("settings", backdrop, modalS, e, e.id)
            settingsModal.set();
        })
    })
}


// Sidebar Collapse
const collapseBtn = document.getElementById("done-container-collapse-btn");
const doneSidebar = document.getElementById("done-sidebar")
collapseBtn.addEventListener('click', function(){
        doneSidebar.classList.toggle("collapsed");
        collapseBtn.classList.toggle("btn-collapsed-mode");
})