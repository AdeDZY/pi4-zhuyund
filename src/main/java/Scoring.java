import java.util.HashMap;
import java.util.HashSet;

import type.TokennizedPassage;
import type.TokennizedQuestion;

/***
 * Template class for bag-of-words-based scoring
 * Defines the skeleton of scoring method with the template method and abstract functions  
 * Lets subclasses redefine certain steps 
 * @author zhuyund
 *
 */
public abstract class Scoring {
  /***
   * get the vetor representation for a question
   * 
   * @param question
   * @return HashMap<String, Integer>. Key is token
   */
  protected abstract HashMap<String, Integer> getQuestionVec(TokennizedQuestion question);
  
  /***
   * get the vetor representation for a passage
   * 
   * @param passage
   * @return HashMap<String, Integer>. Key is token
   */
  protected abstract HashMap<String, Integer> getPassageVec(TokennizedPassage passage);
  
  /***
   * Evaluate score based on vectors.
   * @param qVec
   * @param pVec
   * @return
   */
  protected abstract Double evaluate(HashMap<String, Integer> qVec, HashMap<String, Integer> pVec);
  
  /***
   * Template method for scoring
   * The logic is to first compute the vectors of the question and the passage
   * Then compute the score based on the vectors
   * @param question
   * @param passage
   * @return the score
   */
  public final Double evaluateScore(TokennizedQuestion question, TokennizedPassage passage) {
    HashMap<String, Integer> qVec = this.getQuestionVec(question);
    HashMap<String, Integer> pVec = this.getPassageVec(passage);
    return this.evaluate(qVec, pVec);
  }
}
