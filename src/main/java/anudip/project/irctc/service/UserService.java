package anudip.project.irctc.service;

import anudip.project.irctc.entity.UserVerification;
import anudip.project.irctc.model.Login;
import jakarta.mail.MessagingException;

import java.util.List;

import anudip.project.irctc.entity.User;

public interface UserService {

	User saveUser(User user);

	void saveUserAndSentOtp(User user);

	boolean verifyUser(UserVerification verification);

	List<User> getAllUser();

	User getUserByEmail(String email);

	User updateUser(User user);

	void deleteUser(int userId);

	void sentVerificationMail(String toEmail, String userName, int otp, String role) throws MessagingException;

	boolean userAuthentication(Login login);

}
