/**
 * 
 */

document.getElementById("irctcForm").addEventListener("submit", function(event) {
	var source = document.getElementById("sourceStation");
	var destination = document.getElementById("destinationStation");
	var dateInput = document.getElementById("travelDate");
	removeError(source);
	removeError(destination);
	if (source.value === "") {
		showError(source, "*please enter source")
	} else if (destination.value === "") {
		showError(destination, "*please enter destination")
	} else if (!validateDate(dateInput)) {
		showError(dateInput, "*please enter valid date")
	}
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

function validateDate(input) {
	var enteredDate = input.valueAsDate;
	return enteredDate || !isNaN(enteredDate.getTime());
};