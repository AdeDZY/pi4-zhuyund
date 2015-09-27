import java.util.Collection;
import java.util.HashMap;

import type.TokennizedPassage;
import type.TokennizedQuestion;

/***
 * Scoring with Boolean Match Methods
 * @author zhuyund
 *
 */
public class BooleanMatchScoring extends Scoring {

  @Override
  protected HashMap<String, Integer> getQuestionVec(TokennizedQuestion question) {
    HashMap<String, Integer> questionVector = new HashMap<String, Integer>();
    Collection<String> tokens = FSListFactory.createCollection(question.getTokens(), null);
    for (String token : tokens) {
      questionVector.put(token, 1);  // 0-1 vector
    }
    return questionVector;
  }

  @Override
  protected HashMap<String, Integer> getPassageVec(TokennizedPassage passage) {
    HashMap<String, Integer> passageVector = new HashMap<String, Integer>();
    Collection<String> passageTokens = FSListFactory.createCollection(passage.getTokens(), null);
    for (String token : passageTokens) {
      passageVector.put(token, 1); // 0-1 vector
    }
    return passageVector;
  }

  @Override
  protected Double evaluate(HashMap<String, Integer> qVec, HashMap<String, Integer> pVec) {
    Double prod = 0.0;
    for(String token: qVec.keySet()){
      if(pVec.containsKey(token))
        prod += 1; // overlap
    }
    return prod;
  }

}
