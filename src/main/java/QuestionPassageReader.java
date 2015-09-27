import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * The Collection Reader that parse the input files
 * 
 * @author zhuyund
 *
 */
public class QuestionPassageReader extends CollectionReader_ImplBase {

  private ArrayList<File> mFiles; // files in the inputDir

  private int mCurrentIndex; // current file index

  public static final String PARAM_INPUTDIR = "InputDir";

  @Override
  public void initialize() throws ResourceInitializationException {
    File directory = new File(((String) getConfigParameterValue(PARAM_INPUTDIR)).trim());
    mCurrentIndex = 0;

    // if input directory does not exist or is not a directory, throw exception
    if (!directory.exists() || !directory.isDirectory()) {
      throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
              new Object[] { PARAM_INPUTDIR, this.getMetaData().getName(), directory.getPath() });
    }

    // get list of files in the specified directory
    mFiles = new ArrayList<File>();
    addFilesFromDir(directory);
  }

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    System.out.println("lalalalala");
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    // open input stream to file
    File file = (File) mFiles.get(mCurrentIndex);

    mCurrentIndex += 1;
    String text = FileUtils.file2String(file);
    // put document into CAS
    jcas.setDocumentText(text);
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(mCurrentIndex, mFiles.size(), Progress.ENTITIES) };
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    return this.mCurrentIndex < mFiles.size();
  }

  /**
   * This method adds files in the directory passed in as a parameter to mFiles.
   * 
   * @param dir
   */
  private void addFilesFromDir(File dir) {
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (!files[i].isDirectory()) {
        mFiles.add(files[i]);
      }
    }
  }

}
