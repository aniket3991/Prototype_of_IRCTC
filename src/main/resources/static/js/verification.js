/**
 * 
 */

 document.getElementById("verifyForm").addEventListener("submit", function(event) {
	var otp = document.getElementById("verificationCode");
	
	removeError(otp);
	
	if (otp.value === "" || invalidOtp(otp.value)) {
		showError(otp, "*Invalid OTP, must be 4 digits only")
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

function invalidOtp(otp) {
	if (otp.length !== 4)
		return true;

	for (let i = 0; i < 4; i++) {
		if (!(trainNo.charAt(i) <= '9' && trainNo.charAt(i) >= '0')) {
			return true;
		}
	}
	return false;
};