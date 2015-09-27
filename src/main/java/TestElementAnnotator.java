import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;

import type.Passage;
import type.Question;
import type.TokennizedPassage;
import type.TokennizedQuestion;
import type.TokennizedQuestion_Type;

/**
 * Annotates questions and passages
 * @author zhuyund
 *
 */
public class TestElementAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // get document text
    String docText = aJCas.getDocumentText();
    
    String[] lines = docText.split("\n");
    
    int tmp, tmp2;
    HashMap<String, Question> questions = new HashMap<String, Question>();
    HashMap<String, ArrayList<Passage> > questionPassages = new HashMap<String, ArrayList<Passage> >();
    for(int i = 0; i<lines.length; i++){
      String line = lines[i];
      tmp = line.indexOf(' ');
      String id = line.substring(0, tmp);
      tmp2 = line.indexOf(' ', tmp + 1);
      String type = line.substring(tmp + 1, tmp2);
      
      // question
      if(type.equals("QUESTION")){
        TokennizedQuestion question = new TokennizedQuestion(aJCas);
        question.setId(id);
        question.setSentence(line.substring(tmp2 + 1));
        
        questions.put(id, question);
        questionPassages.put(id, new ArrayList<Passage>());
      }
      
      // passage
      else{
        TokennizedPassage passage = new TokennizedPassage(aJCas);
        passage.setSourceDocId(type);
        tmp = line.indexOf(' ', tmp2 + 1);
        boolean label = (Integer.parseInt(line.substring(tmp2 + 1, tmp)) > 0);
        passage.setLabel(label);
        String text = line.substring(tmp + 1);
        passage.setText(text);
        
        // add to question
        questionPassages.get(id).add(passage);
        
        // add question
        passage.setQuestion(questions.get(id));
        passage.addToIndexes();
        }
      }
    
    // put passage list into Question
     for(String id : questions.keySet()){
       FSList fsList = FSListFactory.createFSList(aJCas, questionPassages.get(id));
       questions.get(id).setPassages(fsList);
       questions.get(id).addToIndexes();
    }

  }
}
