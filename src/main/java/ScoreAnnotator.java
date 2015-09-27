import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import type.TokennizedPassage;
import type.TokennizedQuestion;

/**
 * Assign score to each passage
 * @author zhuyund
 *
 */
public class ScoreAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex passageIndex = aJCas.getAnnotationIndex(TokennizedPassage.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(TokennizedQuestion.type);
    
    // read passages and put into a hahsmap according to their question id.
    HashMap<String, ArrayList<TokennizedPassage> > questionPassages = new HashMap<>();
    Iterator passageIter = passageIndex.iterator();
    while(passageIter.hasNext()){
      TokennizedPassage passage = (TokennizedPassage)passageIter.next();
      String qid = passage.getQuestion().getId();
      ArrayList<TokennizedPassage> passages = questionPassages.get(qid);
      if(passages == null){
        passages = new ArrayList<TokennizedPassage>();
        questionPassages.put(qid, passages);
      }
      passages.add(passage);
    }
    
    // for each question, evaluate score of its passages
    // cosine(question, passage) using bag-of-words model
    Iterator questionIter = questionIndex.iterator();
    while (questionIter.hasNext()) {
      TokennizedQuestion question = (TokennizedQuestion)questionIter.next();      
      String qid = question.getId();
      // the bag-of-words model of the question
      HashMap<String, Integer> questionVector = new HashMap<>();
      Collection<String> tokens = FSListFactory.createCollection(question.getTokens(), null);
      for(String token: tokens){
        if(questionVector.get(token) == null)
          questionVector.put(token, 1);
        else
          questionVector.put(token, questionVector.get(token) + 1);
      }
      // the length of the question-vector
      Double lenQuestion = this.getVectorLength(questionVector);
      
      // passages
      ArrayList<TokennizedPassage> passages = questionPassages.get(qid);
      for(TokennizedPassage passage: passages){
        // the bag-of-words model of the passage
        HashMap<String, Integer> passageVector = new HashMap<>();
        Collection<String> passageTokens = FSListFactory.createCollection(passage.getTokens(), null);
        for(String token: passageTokens){
          if(passageVector.get(token) == null)
            passageVector.put(token, 1);
          else
            passageVector.put(token, passageVector.get(token) + 1);
        }
        // the length of the passage-vector
        Double lenPassage = this.getVectorLength(passageVector);

        // cosine score
        Double score = this.innerProduct(questionVector, passageVector) / (lenQuestion * lenPassage);
        passage.setScore(score);
      }
    }
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
  
  
  /***
   * Computes the inner product of 2 vectors
   * @param docVec1
   * @param docVec2
   * @return the inner product
   */
  private Double innerProduct(HashMap<String, Integer> docVec1, HashMap<String, Integer> docVec2){
    Double prod = 0.0;
    for(String token: docVec1.keySet()){
      Integer freq2 = docVec2.get(token);
      if( freq2 == null)
        continue;
      Integer freq1 = docVec1.get(token);
      prod += freq1 * freq2;
    }
     return prod;
  }

}
