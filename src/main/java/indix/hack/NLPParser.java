package indix.hack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;

@Service
public class NLPParser {

	public static Parser parser = null;

	public void getNounPhrases(Parse p, List<String> verbPhrases) {
		 if (p.getType().equals("NN") || p.getType().equals("NNS") ||
		 p.getType().equals("NNP")
		 || p.getType().equals("NNPS")) {
			 verbPhrases.add(p.getCoveredText());
		 }

//		 if (p.getType().equals("JJ") || p.getType().equals("JJR") ||
//		 p.getType().equals("JJS")) {
//		 adjectivePhrases.add(p.getCoveredText());
//		 }
		
//		if (p.getType().equals("VB") || p.getType().equals("VBP") || p.getType().equals("VBG")
//				|| p.getType().equals("VBD") || p.getType().equals("VBN")) {
//			verbPhrases.add(p.getCoveredText());
//		}

		for (Parse child : p.getChildren()) {
			getNounPhrases(child, verbPhrases);
		}

		// return nounPhrases;
	}

	public void parserAction(List<String> nounPhrases, String line) throws Exception {
		init();
		Parse topParses[] = ParserTool.parseLine(line, parser, 1);
		for (Parse p : topParses) {
			// p.show();
			getNounPhrases(p, nounPhrases);
		}
	}

	private Parser init() throws FileNotFoundException, IOException, InvalidFormatException {
		if (parser == null) {
			InputStream is = new FileInputStream(new File(
					"D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\tsvparser\\src\\main\\resources\\en-parser-chunking.bin"));
			ParserModel model = new ParserModel(is);
			parser = ParserFactory.create(model);
		}
		return parser;
	}
}
