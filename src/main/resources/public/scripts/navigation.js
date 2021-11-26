const url = location.href;
let projectLinks = document.querySelectorAll("[data-link='navlink']");
projectLinks.forEach(function(e){
    console.log("Href = " +e.href);
    if(e.href === url){
        e.classList.add("active");
    }
})
