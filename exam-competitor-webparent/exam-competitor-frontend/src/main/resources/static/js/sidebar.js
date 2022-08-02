
/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */
function openNav() {
	document.getElementById("mySidebar").style.width = "170px";
	document.getElementById("main").style.marginLeft = "170px";
	// 		  document.getElementById("profie-icon").setAttribute("onclick","closeNav()");
}

/* Set the width of the sidebar to 0 and the left margin of the page content to 0 */
function closeNav() {
	document.getElementById("mySidebar").style.width = "0";
	document.getElementById("main").style.marginLeft = "0";
	// 		  document.getElementById("profie-icon").setAttribute("onclick","openNav()");
}
