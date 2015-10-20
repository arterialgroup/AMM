package com.arterialgroup.arterialedu.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.repository.MeetingRepository;

/**
 * This has the responsibility for managing meeting data, so files associated to meetings etc..
 * Can also serve as the management service for the Admin interface for meeting dates etc...
 * also marking if the meeting has ended etc.
 * 
 * This is an extension to the current MeetingResource which is just an entity level management for CRUD 
 * operations
 * @author bradleyr
 *
 */
@Service
@Transactional
public class MeetingService {

	@Inject
	private MeetingRepository meetingRepository;
	
	@Autowired
	private ServletContext context;

	//TODO store a path against the meeting object	
	//on meeting creation append the meetingname as a new folder for segregation

	public Boolean addResourceToMeeting(String name, byte[] fileContents, Long meetingId)
	{
		Boolean created = false;
		
		Meeting meeting = meetingRepository.findOne(meetingId);
		
		String path = meeting.getResourcePath();
		
		//find the webapp root
		String serverRootPath = context.getRealPath("/");
		
		//Default permissions allow this folder to be created on the fly, look into using mkdir() 
		//wehn meetings are saved in the first place
		//append the meeting resouce folder, should have parent such as resources/<blah>
		File newResource = new File(serverRootPath + "/" + path, name);
		
		try {
		
			//Upload file to server path...
			FileUtils.writeByteArrayToFile(newResource, fileContents);
			created = true;
			
		} catch (FileNotFoundException e) {
			//TODO handle exceptions
			
		} catch (IOException e) {
			//TODO handle exceptions
		}
		
		return created;
	}
	
	public List<URL> getResourcesForMeeting(Long meetingId)
	{
		List<URL> urls = null;
		
		Meeting meeting = meetingRepository.findOne(meetingId);
		
		String path = meeting.getResourcePath();
		
		//for each file on path, create a URL instance for a link and pass back to client
		String serverRootPath = context.getRealPath("/");
		
		File meetingDir = new File(serverRootPath + "/" + path);
		
		if(meetingDir.exists() && meetingDir.isDirectory()){
			try {
				//TODO a lot of collection transformation, consider refactoring....
				List<File> files = Arrays.asList(meetingDir.listFiles()).stream().filter((f) -> f.isFile()).collect(Collectors.toList());
				
				URL[] urlArray = FileUtils.toURLs(files.toArray(new File[files.size()]));
				
				urls = Arrays.asList(urlArray);
				
			} catch (IOException e) {
				//TODO handle exceptions
			}
		}
		
		return urls;
	}

}
