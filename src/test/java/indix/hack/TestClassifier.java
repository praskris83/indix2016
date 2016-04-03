package indix.hack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestClassifier {

	Map<String, String> outMap = new HashMap<String, String>();

	@Autowired
	private TSVParser parser;

	@Autowired
	private ClassifyHelper helper;

	@Test
	public void testLoad() throws IOException {
		helper.trainModel(0, 609, Application.trainingFile);
		helper.classify(Application.testFile, 0, 609);

		try {
			Thread.sleep(20 * 60 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void createOutput() {
		Iterator<File> it = FileUtils.iterateFiles(new File(Application.outFolder), new String[] { "tsv" }, false);
		while (it.hasNext()) {
			File f = it.next();
			// System.out.println(f.getAbsolutePath());
			System.out.println(f.getName());
			// System.out.println(f.getPath());
			List<String[]> rows = parser.parse(f.getAbsolutePath());
			for (String[] row : rows) {
				if (row.length > 2) {
					String brand = row[0];
					String title = row[1];
					String ctgr = row[2];

					outMap.put(ctgr + "::" + title, brand);
				} else {
					System.out.println("Error on FIle Line");
				}
			}
		}
		try {
			List<String> lines = FileUtils.readLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\classification_blind_set_corrected.tsv"));
			List<String> lines1 = FileUtils.readLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\output2.txt"));
			
			System.out.println("dwdww" + lines.size());
			for (int i = 0 ; i < lines1.size(); i ++) {
				if(lines1.get(i).equals("0")){
					String line = lines.get(i);
					String[] inputs = StringUtils.split(line, "\t");
					if (inputs.length != 2) {
						System.out.println("Failure");
					} else {
						String title = inputs[0];
						String catgr = inputs[1];
						String result = outMap.get(catgr + "::" + title);
						if (result == null) {
							result = "NOT_FOUND";
							System.out.println("Not found input -- " + title + "	" + catgr);
						}else{
							lines.set(i, result);
						}
					}
				}
//				String[] inputs = StringUtils.split(line, "\t");
//				String result = "NOT_FOUND";
//				if (inputs.length != 2) {
//					System.out.println("Failure");
//				} else {
//					String title = inputs[0];
//					String catgr = inputs[1];
//					result = outMap.get(catgr + "::" + title);
//					if (result == null) {
//						result = "NOT_FOUND";
//						System.out.println("Not found input -- " + title + "	" + catgr);
//					}
//				}
//				writeOutputFile(result);
			}
			
			FileUtils.writeLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\output5.txt"), lines1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// List<String[]> inputs = parser.parse(Application.testFile);

		// List<String[]> inputs1 =
		// parser.parse("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\1.tsv");
		// List<String[]> inputs2 =
		// parser.parse("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\2.tsv");
		// List<String[]> inputs3 =
		// parser.parse("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\3.tsv");
		// List<String[]> inputs4 =
		// parser.parse("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\4.tsv");

		// System.out.println("" + (inputs1.size() + inputs2.size() +
		// inputs3.size() + inputs4.size()));

		// for (String[] input : inputs) {
		// if (input.length > 1) {

		// } else {
		// System.out.println("Error on Input FIle Line" + input);
		// }

	}

	private void writeOutputFile(String result) {
		try {
			FileUtils.write(new File(Application.outFolder + "\\finaloutput_final.txt"), result + "\n", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void fill() {
		List<String> correctLines;
		try {
			correctLines = FileUtils.readLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\mani.txt"));
			List<String> actual = FileUtils.readLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\output1.txt"));
//			List<String> input = FileUtils.readLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\classification_blind_set_corrected.tsv"));
			
//					new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\output\\finaloutput_final.txt"));

			for (int i = 0; i < correctLines.size(); i++) {
				if (actual.get(i).startsWith("NOT_FOUND")) {
					actual.set(i, correctLines.get(i));
				}
			}
			
			FileUtils.writeLines(new File("D:\\Prasad\\Personal\\Hackathon\\Indix\\Dataset\\output2.txt"), actual);
			
//			Map<String, Long> ctMap = new HashMap<>();
//			for(int i = 0 ; i < actual.size() ; i ++){
//				if(actual.get(i).startsWith("NOT")){
//					String key = input.get(i).split("\t")[1];
//					if(ctMap.get(key) == null){
//						ctMap.put(key, 0l);
//					}else{
//						long count = ctMap.get(key);
//						ctMap.put(key, count+1);
//					}
//				}
//			}
//			for(String key : ctMap.keySet()){
//				System.out.println(key + "\t" + ctMap.get(key));
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
