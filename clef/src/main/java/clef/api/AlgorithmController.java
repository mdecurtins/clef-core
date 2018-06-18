package clef.api;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import clef.common.ClefException;
import clef.mir.AlgorithmEnvironmentService;
import clef.mir.AlgorithmEnvironmentServiceImpl;

@RestController
public class AlgorithmController {

	private AlgorithmEnvironmentService aes;
	
	public AlgorithmController() {
		try {
			aes  = AlgorithmEnvironmentServiceImpl.getInstance();
		} catch ( ClefException | IOException e ) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping( value = "/algorithms", method = RequestMethod.GET )
	public List<String> listAll() {
		Map<String, Object> retval = new HashMap<String, Object>();
		
		return null;
	}
}
