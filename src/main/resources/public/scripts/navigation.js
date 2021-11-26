const url = window.location.href;
const arr = url.split('/');

console.log(arr[3]);
if(arr[3].length === 0){
    const home = document.querySelector("a.general-nav-links[href='/']");
    home.classList.add("active");
}else{
    let projectLinks = document.querySelectorAll("a[data-link='navlink']");
    projectLinks.forEach(function(e){
        if(e.getAttribute("href") === "/project" + arr[3].split("t")[1]){
            e.classList.add("active");
        }
    })

}


