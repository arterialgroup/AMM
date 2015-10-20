package com.arterialgroup.arterialedu.service;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.domain.Authority;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.Moderator;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.repository.AuthorityRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.ModeratorRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.service.util.RandomUtil;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

@Service
@Transactional
public class ModeratorService {

	@Inject
	private AuthorityRepository authorityRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private ModeratorRepository moderatorRepository;
	
	@Inject 
	private MeetingRepository meetingRepository;
	
	/**
	 * This could benefit from an abstract/generic class for MeetingUser also
	 * But the actual logic here is different, as we don't sign up moderators to 
	 * modules, they just get access to the meeting
	 * @param newUserDTO
	 * @param meetingId
	 * @return
	 */
	public Boolean addModerator(UserDTO newUserDTO, Long meetingId) {

		User newUser = new User();
		Authority authority = authorityRepository.findOne("ROLE_MOD"); //TODO create this role.... in the SPRING SECURITY CONFIG
		Set<Authority> authorities = new HashSet<>();
		newUser.setLogin(newUserDTO.getLogin());
		newUser.setPassword(newUserDTO.getPassword());
		newUser.setFirstName(newUserDTO.getFirstName());
		newUser.setLastName(newUserDTO.getLastName());
		newUser.setEmail(newUserDTO.getEmail().toLowerCase());
		newUser.setLangKey(newUserDTO.getLangKey());
		newUser.setActivated(true);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.saveAndFlush(newUser);

		Meeting meeting = meetingRepository.findOne(meetingId);
		
		Moderator moderator = new Moderator();
		moderator.setUser(newUser);
		moderator.setMeeting(meeting);
		moderator = moderatorRepository.saveAndFlush(moderator);

		return (moderator != null && moderator.getId() != null);
	}
	
}
