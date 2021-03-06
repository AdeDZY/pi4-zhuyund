import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.util.XMLInputSource;

public class Main {

  /**
   * This method is the main program and entry point of your system. It runs a Collection Processing
   * Engine (CPE).
   * 
   * @param args inputDir outputDir method (Boolean or Cosine). Third arg is optional. Defualt is "Boolean"
   */
  public static void main(String[] args) throws Exception {
    // ### A guideline for implementing this method ###
    // This code gives you a template for a CPE. Still, you need to configure each individual
    // component.

    String inputDir = args[0];
    String outputDir = args[1];
    String method = "Boolean"; // method set default to boolean
    if(args.length > 2 && args[2].equals("Cosine"))
      method = args[2];

    // Instantiate CPE.
    CpeDescription cpeDesc = UIMAFramework.getXMLParser()
            .parseCpeDescription(new XMLInputSource("src/main/resources/descriptors/cpeDescriptor.xml"));
    CollectionProcessingEngine mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);

    // Configure your collection reader with the given input directory. The code below assumes that
    // the collection reader has a parameter 'InputDir' to specify the input directory.
    CollectionReader cr = (CollectionReader) mCPE.getCollectionReader();
    cr.setConfigParameterValue("InputDir", inputDir);
    cr.reconfigure();

    // Configure your CAS consumer with the given output directory. The code below assumes that the
    // CAS consumer has a parameter 'OutputDir' to specify the output directory. The code below
    // assumes that the CAS Consumer can be accessed at index 1 from the array of CasProcessors[]
    // mCPE.getCasProcessors().
    AnalysisEngine cc = (AnalysisEngine) mCPE.getCasProcessors()[1];
    cc.setConfigParameterValue("OutputDir", outputDir);
    cc.reconfigure();
    
    // AAE
    AnalysisEngine aae = (AnalysisEngine) mCPE.getCasProcessors()[0];
    aae.setConfigParameterValue("METHOD", method);
    aae.reconfigure();

    // Create and register a Status Callback Listener.
    mCPE.addStatusCallbackListener(new StatusCallbackListenerImpl());

    // Run the CPE.
    mCPE.process();
  }

}
