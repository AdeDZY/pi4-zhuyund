import java.util.Collection;
import java.util.HashMap;

import type.TokennizedPassage;
import type.TokennizedQuestion;

/***
 * Scoring with Cosine Similarity
 * @author zhuyund
 *
 */
public class CosineSimScoring extends Scoring {

  @Override
  protected HashMap<String, Integer> getQuestionVec(TokennizedQuestion question) {
    HashMap<String, Integer> questionVector = new HashMap<>();
    Collection<String> tokens = FSListFactory.createCollection(question.getTokens(), null);
    for (String token : tokens) {
      if (questionVector.get(token) == null)
        questionVector.put(token, 1);
      else
        questionVector.put(token, questionVector.get(token) + 1);
    }
    return questionVector;
  }

  @Override
  protected HashMap<String, Integer> getPassageVec(TokennizedPassage passage) {
    HashMap<String, Integer> passageVector = new HashMap<>();
    Collection<String> passageTokens = FSListFactory.createCollection(passage.getTokens(), null);
    for (String token : passageTokens) {
      if (passageVector.get(token) == null)
        passageVector.put(token, 1);
      else
        passageVector.put(token, passageVector.get(token) + 1);
    }
    return passageVector;
  }

  @Override
  protected Double evaluate(HashMap<String, Integer> qVec, HashMap<String, Integer> pVec) {
    Double prod = 0.0;
    for(String token: qVec.keySet()){
      Integer freq2 = pVec.get(token);
      if( freq2 == null)
        continue;
      Integer freq1 = qVec.get(token);
      prod += freq1 * freq2;
    }
    Double lenQuestion = this.getVectorLength(qVec);
    Double lenPassage = this.getVectorLength(pVec);
    return prod / (lenQuestion * lenPassage);
  }

  /***
   * Computes the lenght of a bag-of-words vector
   * @param docVec
   * @return the length
   */
  private Double getVectorLength(HashMap<String, Integer> docVec){
    Double len = 0.0;
    for(Integer freq: docVec.values()){
      len += freq * freq;
    }
    return Math.sqrt(len);
  }
}
