package clef.api;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlgorithmController {

	@RequestMapping( value = "/algorithms", method = RequestMethod.GET )
	public List<String> listAll() {
		return null;
	}
}
