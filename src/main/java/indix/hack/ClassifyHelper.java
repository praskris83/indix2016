/**
 * 
 */
package indix.hack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import indix.hack.entity.Category;
import indix.hack.entity.CategoryRepo;

/**
 * @author user
 *
 */
@Service
public class ClassifyHelper {

	ExecutorService executorService = Executors.newFixedThreadPool(100);
	
	@Autowired
	private CategoryRepo ctRepo;

	@Autowired
	private TSVParser parser;

	private Map<String, Classifier<String, String>> modelMap = new HashMap<String, Classifier<String, String>>();

	private Map<String, List<String[]>> ctDataMap = new HashMap<String, List<String[]>>();

	private Map<String, List<String[]>> ctTestMap = new HashMap<String, List<String[]>>();

	public void trainModel(long start, long end, String trainData) throws IOException {
		List<Category> catgs = ctRepo.findByIdBetween(start, end);
//		List<Category> catgs = ctRepo.findOne(counter);
		List<String> catIds = new ArrayList<String>();

		List<String> allData = FileUtils.readLines(new File(trainData));
		for (String inp : allData) {
			String[] data = StringUtils.split(inp,"\t");
			if (data.length > 1) {
				String catg = data[2];
				if (!ctDataMap.containsKey(catg)) {
					List<String[]> newData = new ArrayList<String[]>();
					ctDataMap.put(catg, newData);
				}
				ctDataMap.get(catg).add(data);
			}
		}

		for (final Category ct : catgs) {
			catIds.add(ct.getName());
			Classifier<String, String> model = modelMap.get(ct.getName());

			if (model == null) {
				model = new BayesClassifier<String, String>();
				modelMap.put(ct.getName(), model);
			}
			model = modelMap.get(ct.getName());
//			executorService.execute(new Runnable() {
//			    public void run() {
			    	new TextClassifier().trainModel(ctDataMap.get(ct.getName()), model);
//			    }
//			});
		}
	}

	public void classify(String trainData, long start, long end) throws IOException {
		List<String> allData = FileUtils.readLines(new File(trainData));
//		List<String[]> allData = parser.parse(trainData);
		List<Category> catgs = ctRepo.findByIdBetween(start, end);

		for (String inp : allData) {
			String[] data = StringUtils.split(inp,"\t");
			String catg = data[1];
			if (data.length == 3) {
				catg = data[2];
			}
			if (!ctTestMap.containsKey(catg)) {
				List<String[]> newData = new ArrayList<String[]>();
				ctTestMap.put(catg, newData);
			}
			ctTestMap.get(catg).add(data);
		}
		for (Category ct : catgs) {
			clasify(ct);
		}
	}

	private void clasify(final Category ct) {
		System.out.println("Clasify of " + ct.getName());
		executorService.execute(new Runnable() {
		    public void run() {
				new TextClassifier().classify(modelMap.get(ct.getName()), ctTestMap.get(ct.getName()), Application.outFile + ct.getName()+"_n1" + ".tsv");
		    }
		});
		System.out.println("Clasify ENd of " + ct.getName());
	}
}
