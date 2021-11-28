const url = location.href;
let projectLinks = document.querySelectorAll("[data-link='navlink']");
projectLinks.forEach(function(e){
    if(e.href === url){
        e.parentElement.classList.add("active");
    }
})
