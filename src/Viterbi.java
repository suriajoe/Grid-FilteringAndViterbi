
public class Viterbi 
{
	private static String[] states = {"H","H","T","N","N","N","N","B","H"};
	private static String[] observations = {"N","N","H","H"};
	private static double[] start_probability = { 0.125, 0.125, 0.125, 0.125, 
												  0.125, 0.125, 0.125, 0.125};
	private static double[][] transition_probability = { { 0.2, 0.2, 0.6 },
			{ 0.4, 0.1, 0.5 }, { 0.1, 0.8, 0.1 } };
	private static double[][] emission_probability = { { 0.01, 0.02, 0.02 },
			{ 0.8, 0.01, 0.5 }, { 0.19, 0.97, 0.48 } };
	
	//default predictions with no pre-existing data
	public void defaultPrediction()
	{
	}
}
