/**
 * 
 */

document.getElementById("searchTrain").addEventListener(
	"submit",
	function(event) {

		var trainNo = document.getElementById("trainNo");

		removeError(trainNo);

		if (trainNo.value.trim() === "" || !checkTrainNo(trainNo.value))
			showError(trainNo, "Invalid train number It must has 5 digits");
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

function checkTrainNo(trainNo) {
	if (trainNo.length !== 5)
		return false;

	for (let i = 0; i < 5; i++) {
		if (!(trainNo.charAt(i) <= '9' && trainNo.charAt(i) >= '0')) {
			return false;
		}
	}
	return true;
};