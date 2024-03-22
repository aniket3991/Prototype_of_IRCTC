/**
 * 
 */

function formFieldValidation() {
	var firstName = document.getElementById("firstName");
	var lastName = document.getElementById("lastName");
	var phone = document.getElementById("phone");
	var email = document.getElementById("email");
	var password = document.getElementById("password");
	var confirmPassword = document.getElementById("confirmPassword");
	var userAdminAccount = document.getElementById("userAdminAccount");


	removeAllError(firstName, lastName, phone, email, password, confirmPassword, userAdminAccount);

	if (firstName.value.trim() === "") {
		showError(firstName, "*First Name should not be blank");
	}

	else if (lastName.value.trim() === "") {
		showError(lastName, "*Last Name should not be blank");
	}


	else if (phone.value.trim() === "" || !validatePhoneNumber(phone.value)) {
		showError(phone, "*Invalid Contact");
	}


	else if (email.value.trim() === "" || !validateEmail(email.value)) {
		showError(email, "*Invalid email");
	}


	else if (password.value.trim() === "") {
		showError(password, "*password should not be blank");
	}


	else if (!isPasswordMatch()) {
		showError(confirmPassword, "*password and confirm password should be same");
	}
	else if (!isAccountTypeSelected()) {
		showError(userAdminAccount, "*Kindly select Account Type");
	}
};

document.getElementById("registrationForm").addEventListener("submit", function(event) {
	formFieldValidation();
});

function showError(input, errMessage) {
	const parentElement = input.parentElement;
	const small = parentElement.querySelector('small');
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

function removeAllError(firstName, lastName, phone, email, password, confirmPassword, userAdminAccount) {
	removeError(firstName);
	removeError(lastName);
	removeError(phone);
	removeError(email);
	removeError(password);
	removeError(confirmPassword);
	removeError(userAdminAccount);
};

function validatePhoneNumber(phoneNumber) {
	var phonePattern = /^\d{10}$/;
	return phonePattern.test(phoneNumber);
};

function validateEmail(email) {
	var validRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
	return email.match(validRegex);
}

function isPasswordMatch() {
	var password = document.getElementById("password").value;
	var confirmPassword = document.getElementById("confirmPassword").value;
	return password === confirmPassword;
};

function isAccountTypeSelected() {
	return document.getElementById("user").checked || document.getElementById("admin").checked;
};