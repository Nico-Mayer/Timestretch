import Modal from "./modal.js";

const backdrop = document.querySelector("#modal-backdrop");
const modalP = document.querySelector("#modal-container-project");
const modalT = document.querySelector("#modal-container-task");
const modalC = document.querySelector("#modal-container-confirm");
const modalS = document.querySelector("#modal-container-project-settings");
const addProjectBtn = document.getElementById("add-project-btn");
const addTaskBtn = document.getElementById("add-task-btn");

const deleteBtnArray = document.querySelectorAll(".project-delete-btn");
const settingsBtnArray = document.querySelectorAll(".project-settings-btn");
const projectModal = new Modal(
    "project",
    backdrop,
    modalP,
    addProjectBtn,
    "/addProject"
);


if (deleteBtnArray !== null){
    deleteBtnArray.forEach(function(e){
        e.addEventListener("click", function(){
            let deleteModal = new Modal("", backdrop, modalC, e, e.id)
            deleteModal.set();
        })
    })
}
if (settingsBtnArray !== null){
    settingsBtnArray.forEach(function(e){
        e.addEventListener("click", function(){
            let settingsModal = new Modal("settings", backdrop, modalS, e, e.id)
            settingsModal.set();
        })
    })
}
if(addProjectBtn !== null){
    projectModal.set();
}
if(addTaskBtn !== null){
    const projectID = window.location.href.substr(29); // Verbesserung suchen
    console.log(projectID);
    const taskModal = new Modal("task", backdrop,modalT,addTaskBtn, "/addNewTask"+projectID);
    taskModal.set();
}


