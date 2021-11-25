import Modal from "./modal.js";

const backdrop = document.querySelector("#modal-backdrop");
const modalP = document.querySelector("#modal-container-project");
const modalT = document.querySelector("#modal-container-task");
const modalC = document.querySelector("#modal-container-confirm");

const addProjectBtn = document.getElementById("add-project-btn");
const addTaskBtn = document.getElementById("add-task-btn");
const deleteBtnArray = document.querySelectorAll(".project-delete-btn");

const projectModal = new Modal(
    "project",
    backdrop,
    modalP,
    addProjectBtn,
    "/addProject"
);
const taskModal = new Modal("task", backdrop,modalT,addTaskBtn, "/addNewTask");

if (deleteBtnArray !== null){
    deleteBtnArray.forEach(function(e){
        e.addEventListener("click", function(){
            let deleteModal = new Modal("", backdrop, modalC, e, "/deleteProject" + e.id)
            deleteModal.set();
        })
    })
}
if(addProjectBtn !== null){
    projectModal.set();
}
if(addTaskBtn !== null){
    taskModal.set();
}


