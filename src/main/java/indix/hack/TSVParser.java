package indix.hack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.conversions.Conversions;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

@Service
public class TSVParser {

	public List<String[]> parse(String file){

		TsvParserSettings settings = new TsvParserSettings();
		settings.setMaxColumns(800);
		settings.getFormat().setLineSeparator("\n");
		// creates a TSV parser
		TsvParser parser = new TsvParser(settings);
		// parses all rows in one go.
		List<String[]> allRows = new ArrayList<String[]>();
		allRows = parser.parseAll(new File(file));
		System.out.println(allRows.size());
		
		try {
			allRows = parser.parseAll(FileUtils.openInputStream(new File(file)));
			System.out.println(allRows.size());
			
			allRows = parser.parseAll(new File(file),"UTF-8");
			System.out.println(allRows.size());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// all rows parsed from your input will be sent to this processor
		ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
		    @Override
		    public void rowProcessed(Object[] row, ParsingContext context) {
		        //here is the row. Let's just print it.
		        System.out.println(Arrays.toString(row));
		    }
		};
		// the ObjectRowProcessor supports conversions from String to whatever you need:
		// converts values in columns 2 and 5 to BigDecimal
		rowProcessor.convertIndexes(Conversions.toBigDecimal()).set(2, 5);

		// converts the values in columns "Description" and "Model". Applies trim and to lowercase to the values in these columns.
		rowProcessor.convertFields(Conversions.trim(), Conversions.toLowerCase()).set("Description", "Model");

		//configures to use the RowProcessor
		settings.setRowProcessor(rowProcessor);

		//parses everything. All rows will be pumped into your RowProcessor.
		try {
			allRows = parser.parseAll(FileUtils.openInputStream(new File(file)));
			System.out.println(allRows.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allRows;
	
	}
}
