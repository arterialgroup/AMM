package com.arterialgroup.arterialedu.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.service.util.PDFGeneratorUtil;
import com.itextpdf.text.DocumentException;

/**
 * A service layer for business logic surrounding the generation of educational
 * certificates
 * 
 * @author bradleyr
 * 
 */
@Service
@Transactional
public class CertificateService {

	@Inject
	private AttendeeRepository attendeeRepository;

	public Boolean generateCertificate(Long attendeeId) {

		Boolean generated = true;

		Attendee attendee = attendeeRepository.findOne(attendeeId);

		if (attendee != null) {
			// Load PDF template for meeting ID from attendee record

			// fill out with attendee data
			Map<String, String> certificateData = new HashMap<String, String>();
			certificateData.put("CERT_FIELD_NAME", attendee.getUser()
					.getFirstName() + " " + attendee.getUser().getLastName());
			// TODO specify these fields in a configuration file.

			// TODO...
			// Add meeting PDF cert template path as attribute on meeting object
			// add cert path generated to the attendee object
			// define locations on web-server to host/save files
			// save off to remote location
			// /resources/meetingX/certificates/xxxx.pdf
			/*
			 * try { PDFGeneratorUtil .createPDF("path-of-template",
			 * certificateData,
			 * "output-path-on-server-that-can-be-linked-to-the-attendee-record-object..."
			 * ); } catch (IOException ex) { generated = false; } catch
			 * (DocumentException docEx) { generated = false; }
			 * 
			 * }
			 */

		}
		return generated;
	}
}
