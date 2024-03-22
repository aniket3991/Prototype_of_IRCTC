package anudip.project.irctc.service.ServiceImp;

import anudip.project.irctc.entity.User;
import anudip.project.irctc.entity.UserVerification;
import anudip.project.irctc.model.Login;
import anudip.project.irctc.repository.UserRepository;
import anudip.project.irctc.repository.UserVerificationRepository;
import anudip.project.irctc.service.UserService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserVerificationRepository userVerificationRepository;

	@Override
	public User saveUser(User user) {
		user.setPassword(passwordEncryption(user.getPassword()));

		if (user.getUserId() != 0)
			return updateUser(user);
		return userRepository.save(user);
	}

	@Override
	public void saveUserAndSentOtp(User user) {
		User savedUser = saveUser(user);
		int otp = 0;
		if (user.getRole().equalsIgnoreCase("user")) {
			otp = generateOTP();
			userVerificationRepository.deleteAllByEmail(user.getEmail());
			userVerificationRepository.save(new UserVerification(user.getEmail(), otp, new Date()));
		}

		try {
			sentVerificationMail(user.getEmail(), user.getFirstName(), otp, user.getRole());
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	@Override
	public boolean verifyUser(UserVerification toVerify) {
		boolean isVerified = false;
		UserVerification storedVerification = userVerificationRepository.findByEmail(toVerify.getEmail());
		if (storedVerification.getOtp() == toVerify.getOtp()) {

			User user = userRepository.findByEmail(toVerify.getEmail());
			user.setStatus(1);
			updateUser(user);
			userVerificationRepository.deleteById(storedVerification.getValidateId());
			isVerified = true;
		}
		return isVerified;
	}

	@Override
	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User updateUser(User user) {
		User existedUser = userRepository.findByEmail(user.getEmail());
		existedUser.setFirstName(user.getFirstName());
		existedUser.setLastName(user.getLastName());
		existedUser.setEmail(user.getEmail());
		existedUser.setContact(user.getContact());
		existedUser.setPassword(passwordEncryption(user.getPassword()));
		existedUser.setStatus(user.getStatus());
		existedUser.setRole(user.getRole());

		return userRepository.save(existedUser);
	}

	@Override
	public void deleteUser(int userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void sentVerificationMail(String toEmail, String userName, int otp, String role) throws MessagingException {
		SimpleMailMessage message = new SimpleMailMessage();

		String subject = "User Verification - IRCTC";

		String body = getMailBody(userName, otp, role);

		message.setFrom("r007.5y573m@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		mailSender.send(message);
	}

	@Override
	public boolean userAuthentication(Login login) {
		User user = userRepository.findByEmail(login.getEmail());

		if (user == null)
			return false;

		return checkPassword(login.getPassword(), user.getPassword());
	}

	private int generateOTP() {
		String otp = "";

		while (otp.length() != 4) {

			otp = String.valueOf(Math.abs((LocalTime.now().getNano()
					* (LocalTime.now().getNano() % 9753)) % 9999));
		}
		return Integer.parseInt(otp);
	}

	private String getMailBody(String userName, int otp, String role) {

		String forUser = "Dear " + userName + ",\n\n" + "Thank you for your registration in IRCTC. \n\n"
				+ "Kindly use code: " + otp + ".\n" + "For account verification\n\n" + "Thanks & Regards\n\n"
				+ "IRCTC(Demo)\n" + "Indian Railway";

		String forAdmin = "Dear " + userName + ",\n\n" + "Welcome to the IRCTC. \n\n"
				+ "Kindly send following details on the same mail for admin access verification\n" + "name - \n"
				+ "address - \n" + "IRCTC mail id - \n" + "Employee Id - \n\n" + "Thanks & Regards\n\n"
				+ "IRCTC(Demo)\n" + "Indian Railway";

		return role.equalsIgnoreCase("user") ? forUser : forAdmin;
	}

	private String passwordEncryption(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}

	private boolean checkPassword(String password, String hashPassword) {
		return BCrypt.checkpw(password, hashPassword);
	}
}