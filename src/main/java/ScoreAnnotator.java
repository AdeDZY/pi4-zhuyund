import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import type.TokennizedPassage;
import type.TokennizedQuestion;

/**
 * Assign score to each passage
 * 2 methods: Boolean or Cosine
 * Take param "METHOD"
 * @author zhuyund
 *
 */
public class ScoreAnnotator extends JCasAnnotator_ImplBase {

  public final String PARAM_METHOD = "METHOD";
  private String method;

  @Override
  public void initialize(UimaContext aContext)
      throws ResourceInitializationException {
    super.initialize(aContext);
    // Get config. parameter value
    this.method = (String)aContext.getConfigParameterValue(this.PARAM_METHOD);
  }
    
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    FSIndex passageIndex = aJCas.getAnnotationIndex(TokennizedPassage.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(TokennizedQuestion.type);
    
    // 2 Scoring Classes
    BooleanMatchScoring boolScoring = new BooleanMatchScoring();
    CosineSimScoring cosScoring = new CosineSimScoring();
    
    // read passages and put into a hahsmap according to their question id.
    HashMap<String, ArrayList<TokennizedPassage> > questionPassages = new HashMap<String, ArrayList<TokennizedPassage> >();
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
      
      // passages
      ArrayList<TokennizedPassage> passages = questionPassages.get(qid);
      for(TokennizedPassage passage: passages){
        // cosine score
        Double score = 0.0;
        // assign score with the specified method
        if(this.method.equals("Boolean"))
          score = boolScoring.evaluateScore(question, passage);
        else
          score = cosScoring.evaluateScore(question, passage);
        passage.setScore(score);
      }
    }
  }

}
