/**
 * 
 */
document.getElementById("loginForm").addEventListener("submit",
	function(event) {
		var email = document.getElementById("email");
		var password = document.getElementById("password");

		removeError(email);
		removeError(password);

		if (email.value.trim() === "" || !validateEmail(email.value))
			showError(email, "Invalid email");

		else if (password.value.trim() === "")
			showError(password, "password can not be blank");
	});

function showError(input, errMessage) {
	const formControl = input.parentElement;
	const small = formControl.querySelector('small');
	small.innerHTML = errMessage;
	input.classList.add("is-invalid");
	event.preventDefault();
};

function removeError(input) {
	const formControl = input.parentElement;
	const small = formControl.querySelector('small');
	small.innerHTML = "";
	input.classList.remove("is-invalid");
};

function validateEmail(email) {
	var validRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	return email.match(validRegex);
};