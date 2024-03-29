package protocol;

import server.PrefixParser;
import server.ServerConsumable;
import server.ServerRecommendation;
import server.ServerUser;
import client.ClientRecommendation;

public class CreateRecommendationMessage implements ProtocolMessage {

	public static final String PREFIX = CreateRecommendationMessage.class.getSimpleName();
	public static final String SUCCESS_PREFIX = "succ";
	public static final String FAILURE_PREFIX = "fail";
	
	private ClientRecommendation recommendation;
	
	public CreateRecommendationMessage(ClientRecommendation r)
	{
		setRecommendation(r);
	}
	
	public CreateRecommendationMessage( ) {}
	
	@Override
	public String generateMessage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(PREFIX);
		sb.append(PrefixParser.DELIMITER);
		sb.append(getRecommendation().getUser().getUserId());
		sb.append(PrefixParser.DELIMITER);
		sb.append(getRecommendation().getConsumable().getConsumableId());
		sb.append(PrefixParser.DELIMITER);
		sb.append(getRecommendation().getRevisedRating());

		return sb.toString();
	}

	@Override
	public String generateResponse(String message) {
		String parts[] = message.split(PrefixParser.DELIMITER);
		
		ServerUser user = new ServerUser(Integer.parseInt(parts[1]));
		ServerConsumable consumable = new ServerConsumable(Integer.parseInt(parts[2]));
		
		ServerRecommendation recommendation = new ServerRecommendation(user, consumable, Integer.parseInt(parts[3]));
		
		//NRF: Doing this AFTER the recommendation is created so that we can see a stack trace on the server side as well as the client side.
		if(Integer.parseInt(parts[1]) == 0 || Integer.parseInt(parts[2]) == 0)
		{
			return FAILURE_PREFIX;
		}
		
		return SUCCESS_PREFIX + PrefixParser.DELIMITER + new Integer(recommendation.getInitialRating()).toString();
	}

	@Override
	public void processResponse(String response) throws ProtocolException {
		String[] parts = response.split(PrefixParser.DELIMITER);
		
		if(response.equals(FAILURE_PREFIX))
		{
			throw new ProtocolException("Server could not parse request.");
		}
		
		getRecommendation().setInitialRatingWithoutCommit(Integer.parseInt(parts[1]));
	}

	public void setRecommendation(ClientRecommendation recommendation) {
		this.recommendation = recommendation;
	}

	public ClientRecommendation getRecommendation() {
		return recommendation;
	}

}
