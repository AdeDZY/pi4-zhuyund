import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.StringList;

import type.TokennizedPassage;
import type.TokennizedQuestion;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;

/***
 * Tokennize question and passages Write tokennized data into Passage and Questions
 * 
 * @author zhuyund
 *
 */
public class TokenAnnotator extends JCasAnnotator_ImplBase {

  private HashSet<String> stopWords; // Stop words

  private final String stopFilePath = "src/main/resources/models/stoplist.dft";

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // read stop words
    this.stopWords = this.readStopWords(this.stopFilePath);

    // get annotation indexes
    FSIndex passageIndex = aJCas.getAnnotationIndex(TokennizedPassage.type);
    FSIndex questionIndex = aJCas.getAnnotationIndex(TokennizedQuestion.type);
    String docText = aJCas.getDocumentText();

    TokenizerFactory<Word> factory = PTBTokenizerFactory.newTokenizerFactory();

    // tokenize the question
    Iterator questionIter = questionIndex.iterator();
    while (questionIter.hasNext()) {
      TokennizedQuestion question = (TokennizedQuestion) questionIter.next();
      String sentence = question.getSentence();
      Tokenizer<Word> tokenizer = factory.getTokenizer(new StringReader(sentence));
      List<Word> tokens = tokenizer.tokenize();
      ArrayList<String> strTokens = new ArrayList<String>();
      for (Word token : tokens) {
        strTokens.add(token.toString().toLowerCase());
      }
      ArrayList<String> cleanedTokens = this.removeStopWords(strTokens);
      StringList strList = FSListFactory.createStringList(aJCas, cleanedTokens);
      question.setTokens(strList);
    }

    // tokenize answers
    Iterator passageIter = passageIndex.iterator();
    while (passageIter.hasNext()) {
      TokennizedPassage passage = (TokennizedPassage) passageIter.next();
      // clean the passage's text
      String text = this.cleanText(passage.getText());
      //tokenize
      Tokenizer<Word> tokenizer = factory.getTokenizer(new StringReader(text));
      List<Word> tokens = tokenizer.tokenize();
      ArrayList<String> strTokens = new ArrayList<String>();
      for (Word token : tokens) {
        strTokens.add(token.toString().toLowerCase());
      }
      ArrayList<String> cleanedTokens = this.removeStopWords(strTokens);
      StringList strList = FSListFactory.createStringList(aJCas, cleanedTokens);
      passage.setTokens(strList);
    }
  }

  
  /***
   * Read stop words from file into a hashset
   * 
   * @param filePath
   * @return hashset<String>
   */
  private HashSet<String> readStopWords(String filePath) {
    HashSet<String> stopWords = new HashSet<>();
    File stopFile = new File(filePath);
    try {
      BufferedReader reader = new BufferedReader(new FileReader(stopFile));
      String line = null;
      while ((line = reader.readLine()) != null) {
        stopWords.add(line.trim());
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stopWords;
  }

  
  /***
   * removes non-alphanumeric tokens and stop words
   * 
   * @param tokens
   * @return ArrayList<String>
   */
  private ArrayList<String> removeStopWords(ArrayList<String> tokens) {
    ArrayList<String> result = new ArrayList<>();
    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
    for (String token : tokens) {
      if (this.stopWords.contains(token))
        continue;
      if (p.matcher(token).find())
        continue;
      result.add(token);
    }
    return result;
  }
  
  /***
   * Cleans the passage. Removes html tags
   * @param rawText
   * @return cleaned text
   */
  private String cleanText(String rawText){
    String text = rawText.replaceAll("<([^<>]*)>", "");
    return text;
  }
}
