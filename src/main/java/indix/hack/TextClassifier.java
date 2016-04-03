package indix.hack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;

@Service
public class TextClassifier {

	static int success = 0;
	static int error = 0;

	private final List<String> exclude = Arrays.asList(new String[] { "the", "to", "and", "is", "gb", "ram", "with",
			"for", "size", "#", "&", "*", "+", "mb", "\"", ",", "tb", "was" });

	@Autowired
	private NLPParser nlp;
	
	@Async
	public Classifier<String, String> trainModel(List<String[]> data, Classifier<String, String> bayes) {
		int i = 0;
		for (final String[] rows : data) {
			// Train Validation Mode
			if (rows != null && rows.length == 3) {
				String brand = rows[1];
				//				String[] title = rows[0].toLowerCase().split("\\s");
				List<String> titleLst = new LinkedList<String>();
				try {
					nlp.parserAction(titleLst, rows[0].toLowerCase());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					String[] title = rows[0].toLowerCase().split("\\s");
					titleLst.addAll(Arrays.asList(title));
				}
				titleLst.add(rows[2]);
//				titleLst.addAll(Arrays.asList(title));
				titleLst.removeAll(exclude);
				bayes.learn(brand, titleLst);
				i++;
			}
		}
		System.out.println("Trinaed Model Count -- " + i);
		return bayes;
	}

	public String classify(final Classifier<String, String> bayes, String[] rows) {
		// System.out.println("1" + new Date());
		String[] unknownText = null;
		if (rows.length > 2) {
			unknownText = (rows[2] + " " + rows[0]).toLowerCase().split("\\s");
		} else {
			unknownText = (rows[1] + " " + rows[0]).toLowerCase().split("\\s");
		}
		Classification<String, String> classify = bayes.classify(Arrays.asList(unknownText));
		if (classify != null) {
			String prediction = classify.getCategory();
			if (rows.length > 2) {
				boolean match = rows[1].equalsIgnoreCase(prediction);
				if (match) {
					System.out.println("Matched" + rows[1] + "==" + prediction);
					success++;
				} else {
					System.out.println("Not Matched" + rows[1] + "!=" + prediction);
					error++;
				}
			}
			return prediction;
		} else {
			System.out.println("Unable to clasify == " + unknownText[0]);
			error++;
		}

		return "Not Found";
	}

	public void classify(Classifier<String, String> model, List<String[]> data, String file) {
		if(!CollectionUtils.isEmpty(data)){
		for (String[] rows : data) {
			String out = classify(model, rows);
			out = out + "\t" + rows[0] + "\t" + rows[1] + "\n";
			
			try {
				FileUtils.write(new File(file), out, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("File Write Completed for " + file);
		}
	}
}
