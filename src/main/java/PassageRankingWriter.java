import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import type.Passage;
import type.TokennizedPassage;

/**
 * This CAS Consumer serves as a writer to generate your output. This is just template code, so you
 * need to implement actual code.
 */
public class PassageRankingWriter extends CasConsumer_ImplBase {

  private final String outputFileName = "passageRanking.txt";

  private File mOutputDir;

  public static final String PARAM_OUTPUTDIR = "OutputDir";

  @Override
  public void initialize() throws ResourceInitializationException {
    mOutputDir = new File((String) getConfigParameterValue(PARAM_OUTPUTDIR));
    if (!mOutputDir.exists()) {
      mOutputDir.mkdirs();
    }
  }

  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // read all passages
    ArrayList<Passage> passages = new ArrayList<>();
    FSIndex passageIndex = jcas.getAnnotationIndex(TokennizedPassage.type);
    Iterator passageIter = passageIndex.iterator();
    while (passageIter.hasNext()) {
      passages.add((TokennizedPassage) passageIter.next());
    }

    // sort passages
    Collections.sort(passages, new PassageComparator());
    
    // evaluate P@N
    System.out.println(this.getPrecision(passages));
    
    // write to file
    this.writeToOutputFile(jcas, passages, this.outputFileName);

  }

  /***
   * Write the ranked list to file
   * @param jcas
   * @param sortedPassages
   * @param fileName
   */
  private void writeToOutputFile(JCas jcas, ArrayList<Passage> sortedPassages, String fileName) {
    // write to file
    File outFile = new File(this.mOutputDir, fileName);
    BufferedWriter writer;
    try {
      writer = new BufferedWriter(new FileWriter(outFile));
      for (Passage passage : sortedPassages) {
        writer.write(passage.getQuestion().getId() + " " + passage.getSourceDocId() + " "
                + passage.getScore() + " " + passage.getText() + "\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /***
   * Evaluate the macro-average Precision@N
   * @param sortedPassages
   * @return macro-average Precision@N
   */
  private Double getPrecision(ArrayList<Passage> sortedPassages){
    int i = 0, j;
    double ap = 0.0;
    int nQuestions = 0;
    while(i < sortedPassages.size()){
      String qid = sortedPassages.get(i).getQuestion().getId();
      int n = 0, r = 0;
      for(j = i;j < sortedPassages.size() && sortedPassages.get(j).getQuestion().getId().equals(qid); j++){
        if(sortedPassages.get(j).getLabel())
          n += 1;
      }
      for(int t = i;t < i + n; t++){
        if(sortedPassages.get(t).getLabel())
          r += 1;
      }
      if(n > 0)
        ap += r/(double)n;
      nQuestions += 1;
      i = j;
     }
    return ap/nQuestions;
  } 

}
