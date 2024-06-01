package com.photogallery.photogallery.Controller;

import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogallery.photogallery.Constant.ERole;
import com.photogallery.photogallery.Entity.Role;
import com.photogallery.photogallery.Entity.User;
import com.photogallery.photogallery.Repository.RoleRepository;
import com.photogallery.photogallery.Repository.UserRepository;
import com.photogallery.photogallery.Request.LoginRequest;
import com.photogallery.photogallery.Request.PasswordRequest;
import com.photogallery.photogallery.Request.SignupRequest;
import com.photogallery.photogallery.Response.JwtResponse;
import com.photogallery.photogallery.Response.MessageResponse;
import com.photogallery.photogallery.Security.Services.UserDetailsImpl;
import com.photogallery.photogallery.Security.jwt.JwtUtils;
import com.photogallery.photogallery.Response.ApiResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println("Inside Login");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println(signUpRequest);
		if (signUpRequest.getPasscode().equals("Goku@9199731275")) {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity.ok(new MessageResponse("Username is already taken!"));
			}

			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity.ok(new MessageResponse("Email is already in use!"));
			}

			// Create new user's account
			User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
					encoder.encode(signUpRequest.getPassword()));

			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "mod":
						Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);

						break;
					case "partner":
						Role partnerRole = roleRepository.findByName(ERole.ROLE_PARTNER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(partnerRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
				});
			}

			user.setRoles(roles);
			userRepository.save(user);

			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
		} else {
			return ResponseEntity.ok(new MessageResponse("Admin Pass Code Not Match!"));
		}

	}

	// change password by user not for administrator
	@PutMapping("/changePass/forgetpass")
	public ApiResponse<?> ChangePassword(@Valid @RequestBody PasswordRequest passwordrequest) {
		System.out.println(passwordrequest);
		User existinguser = userRepository.findByUsername(passwordrequest.getUsername()).get();
		if (passwordrequest.getPasscode().equals("Goku@9199731275")) {
			System.out.println("inside");
			if (passwordrequest.getNewpassword().equals(passwordrequest.getConformpassword())) {
				existinguser.setPassword(encoder.encode(passwordrequest.getNewpassword()));
				userRepository.save(existinguser);
				return ApiResponse.success("Password Changed SuccessFully");
			}
			return ApiResponse.failure("Password Not Match Check New Password");
		} else {
			return ApiResponse.failure("Admin pass code not matched");
		}

	}
}
