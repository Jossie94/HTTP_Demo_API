package tec.psl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeReq {
	
	private AnalyzeResult result;
	private int persId;
	
	public AnalyzeReq(String endpoint) {
		
		final String regex = "^\\/person(\\/(\\d+))?$";
        
        
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(endpoint);
        
        if (matcher.find()) {
            // Hvis jeg er her er der fundet et match
        	if(matcher.group(2) != null) {
        		result = AnalyzeResult.personIDMatc;
        		persId = Integer.parseInt(matcher.group(2));
        		return;
        	}
        	// Hvis jeg er her der fundet et match uden id
        	result = AnalyzeResult.personMatch;
            return;
        }
        // hvis jeg er her der ikke fundet et match
        result = AnalyzeResult.noMatch;
	}

	public AnalyzeResult getResult() {
		return result;
	}

	public int getPersId() {
		return persId;
	}
	
	
	

}
