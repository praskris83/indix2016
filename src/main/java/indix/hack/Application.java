/**
 * 
 */
package indix.hack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author user
 *
 */
@SpringBootApplication
@EnableAsync
public class Application {

	static String trainingFile = "D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\classification_train.tsv";
	
	static String testFile = "D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\classification_blind_set_corrected.tsv";
	
	static String outFile = "D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\";
	
	static String outFolder = "D:\\Prasad\\Personal\\Hackathon\\Indix\\workspace\\product\\src\\main\\resources\\output";
	
//	static String trainingFile = "/home/pkrishnan/classification_train.tsv";
//	
//	static String testFile = "/home/pkrishnan/classification_blind_set_corrected.tsv";
//	
//	static String outFile = "/home/pkrishnan/output.txt";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
